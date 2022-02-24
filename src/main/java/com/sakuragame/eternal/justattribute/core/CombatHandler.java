package com.sakuragame.eternal.justattribute.core;

import com.sakuragame.eternal.justattribute.api.JustAttributeAPI;
import com.sakuragame.eternal.justattribute.api.event.role.RoleHealthStealEvent;
import com.sakuragame.eternal.justattribute.api.event.role.RoleHealthStoleEvent;
import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.EntityAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.MobAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleAttribute;
import net.sakuragame.eternal.dragoncore.util.Pair;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;


public class CombatHandler {

    public static Pair<Double, Double> calculate(EntityAttribute attacker, EntityAttribute sufferer) {
        double damage = attacker.getValue(Attribute.Damage);
        double dp = attacker.getValue(Attribute.Defence_Penetration);
        double cc = attacker.getValue(Attribute.Critical_Chance);

        double defence = sufferer.getValue(Attribute.Defence);
        double di = sufferer.getValue(Attribute.Damage_Immune);

        double lastDamage = Math.max(1, damage - (Math.max(defence - dp, 0)));
        double cd = 0;

        lastDamage = lastDamage * (1 - di);

        if (attacker instanceof MobAttribute) {
            MobAttribute mob = (MobAttribute) attacker;
            lastDamage = Math.max(mob.getMinimumDamage(), lastDamage);
        }

        if (cc >= 1 || cc > Math.random()) {
            cd = attacker.getValue(Attribute.Critical_Damage);
        }

        return new Pair<>(lastDamage, cd);
    }

    public static void physicalVampire(Player player, LivingEntity source, double damage) {
        RoleAttribute attribute = JustAttributeAPI.getRoleAttribute(player);

        double damageRate = attribute.getTotalValue(Attribute.Vampire_Damage);
        double versatileRate = attribute.getTotalValue(Attribute.Vampire_Versatile);

        if (damageRate + versatileRate <= 0) return;

        double damageValue = damage * damageRate;
        double versatileValue = damage * versatileRate;

        RoleHealthStealEvent stealEvent = new RoleHealthStealEvent(player, source, damageValue + versatileValue);
        stealEvent.call();
        if (stealEvent.isCancelled()) return;

        double vampire = stealEvent.getVampire();

        JustAttributeAPI.getRoleState(player).addHealth(vampire);

        RoleHealthStoleEvent stoleEvent = new RoleHealthStoleEvent(player, source, vampire);
        stoleEvent.call();
    }

    private static double getValue(RoleAttribute role, Attribute ident) {
        if (ident == Attribute.Damage) return role.getActualDamage();
        if (ident == Attribute.Defence) return role.getActualDefence();

        return ident.calculate(role);
    }
}
