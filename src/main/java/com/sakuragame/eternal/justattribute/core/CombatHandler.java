package com.sakuragame.eternal.justattribute.core;

import com.sakuragame.eternal.justattribute.api.JustAttributeAPI;
import com.sakuragame.eternal.justattribute.api.event.role.RoleVampireEvent;
import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.EntityAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.MobAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleState;
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
        double cd = 1;

        lastDamage = lastDamage * (1 - di);

        if (attacker instanceof MobAttribute) {
            MobAttribute mob = (MobAttribute) attacker;
            lastDamage = Math.max(mob.getMinimumDamage(), lastDamage);
        }

        if (attacker instanceof RoleAttribute) {
            RoleAttribute role = (RoleAttribute) attacker;
            lastDamage = Math.min(role.getDamageUpperLimit(), lastDamage);
        }

        if (cc >= 1 || cc > Math.random()) {
            cd = cd + attacker.getValue(Attribute.Critical_Damage);
        }

        return new Pair<>(lastDamage, cd);
    }

    public static void damageVampire(Player player, LivingEntity source) {
        RoleState state = JustAttributeAPI.getRoleState(player);
        RoleAttribute attribute = JustAttributeAPI.getRoleAttribute(player);

        double rate = attribute.getValue(Attribute.Vampire_Damage);
        if (rate <= 0) return;

        double value = state.getDamageVampire() * rate;

        RoleVampireEvent.Pre preEvent = new RoleVampireEvent.Pre(player, source, value, RoleVampireEvent.Cause.Damage);
        preEvent.call();
        if (preEvent.isCancelled()) return;

        double result = preEvent.getValue();
        JustAttributeAPI.getRoleState(player).addHealth(result);

        RoleVampireEvent.Post postEvent = new RoleVampireEvent.Post(player, source, result, RoleVampireEvent.Cause.Damage);
        postEvent.call();
    }

    public static void powerVampire(Player player, LivingEntity source) {
        RoleState state = JustAttributeAPI.getRoleState(player);
        RoleAttribute attribute = JustAttributeAPI.getRoleAttribute(player);

        double rate = attribute.getValue(Attribute.Vampire_Versatile);
        if (rate <= 0) return;

        double value = state.getPowerVampire() * rate;

        RoleVampireEvent.Pre preEvent = new RoleVampireEvent.Pre(player, source, value, RoleVampireEvent.Cause.Power);
        preEvent.call();
        if (preEvent.isCancelled()) return;

        double result = preEvent.getValue();
        JustAttributeAPI.getRoleState(player).addHealth(result);

        RoleVampireEvent.Post postEvent = new RoleVampireEvent.Post(player, source, result, RoleVampireEvent.Cause.Power);
        postEvent.call();
    }
}
