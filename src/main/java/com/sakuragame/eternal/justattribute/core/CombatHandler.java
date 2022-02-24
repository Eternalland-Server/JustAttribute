package com.sakuragame.eternal.justattribute.core;

import com.sakuragame.eternal.justattribute.api.JustAttributeAPI;
import com.sakuragame.eternal.justattribute.api.event.role.RoleLaunchAttackEvent;
import com.sakuragame.eternal.justattribute.api.event.role.RoleHealthStealEvent;
import com.sakuragame.eternal.justattribute.api.event.role.RoleHealthStoleEvent;
import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.MobAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleAttribute;
import com.sakuragame.eternal.justattribute.hook.DamageModify;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;


public class CombatHandler {

    public static RoleLaunchAttackEvent calculate(RoleAttribute attacker, RoleAttribute sufferer) {
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

        return new RoleLaunchAttackEvent(attacker.getBukkitPlayer(), sufferer.getBukkitPlayer(), lastDamage, critical, RoleLaunchAttackEvent.Cause.Normal);
    }

    public static RoleLaunchAttackEvent calculate(RoleAttribute attacker, MobAttribute sufferer) {
        boolean critical = false;
        double damage = getValue(attacker, Attribute.Damage);
        double cc = getValue(attacker, Attribute.Critical_Chance);
        double cd = getValue(attacker, Attribute.Critical_Damage);

        double defence = sufferer.getDefence();
        double lastDamage = damage - defence;

        if (cc >= 1 || cc > Math.random()) {
            lastDamage = lastDamage * cd;
            critical = true;
        }

        double damageModify = sufferer.getDamageModifiers().getOrDefault(DamageModify.ATTRIBUTE_ATTACK.name(), 1.0);
        lastDamage = lastDamage * damageModify;

        return new RoleLaunchAttackEvent(attacker.getBukkitPlayer(), sufferer.getEntity(), lastDamage, critical, RoleLaunchAttackEvent.Cause.Normal);
    }

    public static double calculate(MobAttribute attacker, RoleAttribute sufferer) {
        double damage = attacker.getDamage();
        double dp = attacker.getDefencePenetration();
        double cc = attacker.getCriticalChance();
        double cd = attacker.getCriticalDamage();

        double defence = getValue(sufferer, Attribute.Defence);
        double di = getValue(sufferer, Attribute.Damage_Immune);

        double lastDamage = damage - defence * dp;

        if (cc >= 1 || cc > Math.random()) {
            lastDamage = lastDamage * cd;
        }

        lastDamage = Math.max(attacker.getMinimumDamage(), lastDamage * (1 - di));

        return lastDamage;
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
