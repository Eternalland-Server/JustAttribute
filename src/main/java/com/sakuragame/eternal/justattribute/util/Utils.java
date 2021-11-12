package com.sakuragame.eternal.justattribute.util;

import org.bukkit.Material;

import java.text.DecimalFormat;

public class Utils {

    private final static DecimalFormat a = new DecimalFormat("#.#");

    public static String formatValue(double value, boolean isPercent) {
        value = isPercent ? value * 100 : value;
        String s = value > 0 ? "+" + a.format(value) : "-" + a.format(value);
        return isPercent ? s + "%" : s;
    }

    public static boolean isArmor(Material material) {
        String name = material.name();
        return name.contains("HELMET") ||
                name.contains("CHESTPLATE") ||
                name.contains("LEGGINGS") ||
                name.contains("BOOTS");
    }
}
