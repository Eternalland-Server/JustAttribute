package com.sakuragame.eternal.justattribute.api;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleState;
import org.bukkit.entity.Player;

import java.util.UUID;

public class JustAttributeAPI {

    public static RoleAttribute getRoleAttribute(Player player) {
        return getRoleAttribute(player.getUniqueId());
    }

    public static RoleState getRoleState(Player player) {
        return getRoleState(player.getUniqueId());
    }

    public static RoleAttribute getRoleAttribute(UUID uuid) {
        return JustAttribute.getRoleManager().getPlayerAttribute(uuid);
    }

    public static RoleState getRoleState(UUID uuid) {
        return JustAttribute.getRoleManager().getPlayerState(uuid);
    }

    public static double getRoleAttributeValue(Player player, Attribute attribute) {
        return getRoleAttributeValue(player.getUniqueId(), attribute);
    }

    public static double getRoleAttributeValue(UUID uuid, Attribute attribute) {
        RoleAttribute role = getRoleAttribute(uuid);
        if (role == null) return 0;

        return role.getTotalValue(attribute);
    }

    public static void addHP(Player player, double value) {
        addHP(player.getUniqueId(), value);
    }

    public static void addMP(Player player, double value) {
        addMP(player.getUniqueId(), value);
    }

    public static void takeHP(Player player, double value) {
        takeHP(player.getUniqueId(), value);
    }

    public static void takeMP(Player player, double value) {
        takeMP(player.getUniqueId(), value);
    }

    public static void addHP(UUID uuid, double value) {
        RoleState state = getRoleState(uuid);
        if (state == null) return;
        state.addHealth(value);
    }

    public static void addMP(UUID uuid, double value) {
        RoleState state = getRoleState(uuid);
        if (state == null) return;
        state.addMana(value);
    }

    public static void takeHP(UUID uuid, double value) {
        RoleState state = getRoleState(uuid);
        if (state == null) return;
        state.takeHealth(value);
    }

    public static void takeMP(UUID uuid, double value) {
        RoleState state = getRoleState(uuid);
        if (state == null) return;
        state.takeMana(value);
    }

    public static boolean consumeHP(Player player, double value) {
        return consumeHP(player.getUniqueId(), value);
    }

    public static boolean consumeMP(Player player, double value) {
        return consumeMP(player.getUniqueId(), value);
    }

    public static boolean consumeHP(UUID uuid, double value) {
        RoleState state = getRoleState(uuid);
        if (state == null) return false;

        return state.consumeMana(value);
    }

    public static boolean consumeMP(UUID uuid, double value) {
        RoleState state = getRoleState(uuid);
        if (state == null) return false;

        return state.consumeMana(value);
    }

}
