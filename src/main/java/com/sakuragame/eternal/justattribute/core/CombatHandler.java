package com.sakuragame.eternal.justattribute.core;

import com.sakuragame.eternal.justattribute.api.JustAttributeAPI;
import com.sakuragame.eternal.justattribute.api.event.role.RoleVampireEvent;
import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import com.sakuragame.eternal.justattribute.core.attribute.character.ICharacter;
import com.sakuragame.eternal.justattribute.core.attribute.character.PlayerCharacter;
import net.sakuragame.eternal.dragoncore.util.Pair;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;


public class CombatHandler {

    public static Pair<Double, Double> calculate(ICharacter attacker, ICharacter sufferer) {
        // attacker attribute
        double damage = attacker.getAttributeValue(Attribute.Damage);
        double dp = attacker.getAttributeValue(Attribute.Defence_Penetration);
        double cc = attacker.getAttributeValue(Attribute.Critical_Chance);

        // sufferer attribute
        double defence = sufferer.getAttributeValue(Attribute.Defence);
        double di = sufferer.getAttributeValue(Attribute.Damage_Immune);

        // calculate damage
        double result = Math.max(1, damage - Math.max(defence - (dp * defence), 0)) * (1 - di);
        // calculate critical damage
        double cd = (cc >= 1 || cc > Math.random()) ? 1 + attacker.getAttributeValue(Attribute.Critical_Damage) : 1;

        // final damage = result * cd
        return new Pair<>(result, cd);
    }

    public static void damageVampire(Player player, LivingEntity source) {
        if (player.isDead()) return;

        PlayerCharacter role = JustAttributeAPI.getRoleCharacter(player);

        double rate = 1 + role.getAttributeValue(Attribute.Vampire_Damage);
        double value = role.getDamageVampire() * rate;

        RoleVampireEvent.Pre preEvent = new RoleVampireEvent.Pre(player, source, value, RoleVampireEvent.Cause.Damage);
        preEvent.call();
        if (preEvent.isCancelled()) return;

        double result = preEvent.getValue();
        role.addHP(result);

        RoleVampireEvent.Post postEvent = new RoleVampireEvent.Post(player, source, result, RoleVampireEvent.Cause.Damage);
        postEvent.call();
    }

    public static void powerVampire(Player player, LivingEntity source) {
        if (player.isDead()) return;

        PlayerCharacter role = JustAttributeAPI.getRoleCharacter(player);

        double rate =  1 + role.getAttributeValue(Attribute.Vampire_Versatile);
        double value = role.getSkillVampire() * rate;

        RoleVampireEvent.Pre preEvent = new RoleVampireEvent.Pre(player, source, value, RoleVampireEvent.Cause.Power);
        preEvent.call();
        if (preEvent.isCancelled()) return;

        double result = preEvent.getValue();
        role.addHP(result);

        RoleVampireEvent.Post postEvent = new RoleVampireEvent.Post(player, source, result, RoleVampireEvent.Cause.Power);
        postEvent.call();
    }
}
