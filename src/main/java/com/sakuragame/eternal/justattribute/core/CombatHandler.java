package com.sakuragame.eternal.justattribute.core;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.api.event.JARoleAttackEvent;
import com.sakuragame.eternal.justattribute.api.event.vampire.JARoleHealthStealEvent;
import com.sakuragame.eternal.justattribute.api.event.vampire.JARoleHealthStoleEvent;
import com.sakuragame.eternal.justattribute.core.attribute.Identifier;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleAttribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;


public class CombatHandler {

    public static JARoleAttackEvent calculate(RoleAttribute attacker, RoleAttribute sufferer) {
        boolean critical = false;
        double damage = attacker.getTotalDamage();
        double dp = getValue(attacker, Identifier.Defence_Penetration);
        double cc = getValue(attacker, Identifier.Critical_Chance);
        double cd = getValue(attacker, Identifier.Critical_Damage);

        double defence = sufferer.getTotalDefence();
        double di = getValue(sufferer, Identifier.Damage_Immune);

        double lastDamage = damage - (Math.max(defence - dp, 0));

        if (cc >= 1 || cc > Math.random()) {
            lastDamage = lastDamage * cd;
            critical = true;
        }
        
        lastDamage = lastDamage * (1 - di);

        return new JARoleAttackEvent((Player) attacker.getRole(), sufferer.getRole(), lastDamage, critical);
    }

    public static void physicalVampire(Player player, LivingEntity source, double damage) {
        RoleAttribute attribute = JustAttribute.getRoleManager().getPlayerAttribute(player.getUniqueId());

        double damageRate = attribute.getTotalValue(Identifier.Vampire_Damage);
        double versatileRate = attribute.getTotalValue(Identifier.Vampire_Versatile);

        double damageValue = damage * damageRate;
        double versatileValue = damage * versatileRate;

        JARoleHealthStealEvent stealEvent = new JARoleHealthStealEvent(player, source, damageValue + versatileValue);
        if (stealEvent.call()) {
            return;
        }

        double vampire = stealEvent.getVampire();

        JARoleHealthStoleEvent stoleEvent = new JARoleHealthStoleEvent(player, source, vampire);
        stoleEvent.call();

        JustAttribute.getRoleManager().getPlayerState(player.getUniqueId()).addHealth(vampire);
    }

    private static double getValue(RoleAttribute role, Identifier ident) {
        if (role == null) return 0d;
        return JustAttribute.getAttributeManager().getAttribute(ident).calculate(role);
    }
}
