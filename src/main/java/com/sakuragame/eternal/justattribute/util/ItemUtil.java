package com.sakuragame.eternal.justattribute.util;

import org.bukkit.Material;

public class ItemUtil {

    public static boolean isArmor(Material material) {
        String name = material.name();
        return name.endsWith("_HELMET") ||
                name.endsWith("_CHESTPLATE") ||
                name.endsWith("_LEGGINGS") ||
                name.endsWith("_BOOTS");
    }

    public static boolean isShield(Material material) {
        return material == Material.SHIELD;
    }
}
