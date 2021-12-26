package com.sakuragame.eternal.justattribute.core;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.api.event.JARoleAttackEvent;
import com.sakuragame.eternal.justattribute.api.event.vampire.JARoleHealthStealEvent;
import com.sakuragame.eternal.justattribute.api.event.vampire.JARoleHealthStoleEvent;
import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleAttribute;
import com.sakuragame.eternal.justattribute.hook.DamageModify;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;


public class CombatHandler {

    public static JARoleAttackEvent calculate(RoleAttribute attacker, RoleAttribute sufferer) {
        boolean critical = false;
        double damage = getValue(attacker, Attribute.Damage);
        double dp = getValue(attacker, Attribute.Defence_Penetration);
        double cc = getValue(attacker, Attribute.Critical_Chance);
        double cd = getValue(attacker, Attribute.Critical_Damage);

        double defence = getValue(sufferer, Attribute.Defence);
        double di = getValue(sufferer, Attribute.Damage_Immune);

        double lastDamage = damage - (Math.max(defence - dp, 0));

        if (cc >= 1 || cc > Math.random()) {
            lastDamage = lastDamage * cd;
            critical = true;
        }
        
        lastDamage = lastDamage * (1 - di);

        return new JARoleAttackEvent(attacker.getPlayer(), sufferer.getPlayer(), lastDamage, critical);
    }

    public static JARoleAttackEvent calculate(RoleAttribute attacker, ActiveMob sufferer) {
        MythicMob mob = sufferer.getType();

        boolean critical = false;
        double damage = getValue(attacker, Attribute.Damage);
        double cc = getValue(attacker, Attribute.Critical_Chance);
        double cd = getValue(attacker, Attribute.Critical_Damage);

        if (cc >= 1 || cc > Math.random()) {
            damage = damage * cd;
            critical = true;
        }

        double damageModify = mob.getDamageModifiers().getOrDefault(DamageModify.ATTRIBUTE_ATTACK.name(), 1.0);

        double lastDamage = damage * damageModify;

        return new JARoleAttackEvent(attacker.getPlayer(), (LivingEntity) sufferer.getEntity().getBukkitEntity(), lastDamage, critical);
    }

    public static void physicalVampire(Player player, LivingEntity source, double damage) {
        RoleAttribute attribute = JustAttribute.getRoleManager().getPlayerAttribute(player.getUniqueId());

        double damageRate = attribute.getTotalValue(Attribute.Vampire_Damage);
        double versatileRate = attribute.getTotalValue(Attribute.Vampire_Versatile);

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

    private static double getValue(RoleAttribute role, Attribute ident) {
        if (ident == Attribute.Damage) return role.getTotalDamage();
        if (ident == Attribute.Defence) return role.getTotalDefence();

        return ident.calculate(role);
    }
}
