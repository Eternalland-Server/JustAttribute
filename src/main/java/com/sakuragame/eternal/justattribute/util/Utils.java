package com.sakuragame.eternal.justattribute.util;

import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleAttribute;
import org.bukkit.Material;

import java.text.DecimalFormat;

public class Utils {

    private final static DecimalFormat a = new DecimalFormat("0.0");

    public static String formatValue(double value, boolean isPercent) {
        value = isPercent ? value * 100 : value;
        String s = value >= 0 ? "+" + a.format(value) : "-" + a.format(value);
        return isPercent ? s + "%" : s;
    }

    public static String format(double value, boolean isPercent) {
        value = isPercent ? value * 100 : value;
        String s = a.format(value);
        return isPercent ? s + "%" : s;
    }

    public static boolean isArmor(Material material) {
        String name = material.name();
        return name.contains("HELMET") ||
                name.contains("CHESTPLATE") ||
                name.contains("LEGGINGS") ||
                name.contains("BOOTS");
    }

    public static String getSource(int i) {
        String s = String.valueOf(i);
        return s
                .replace("0", "❿")
                .replace("1", "❶")
                .replace("2", "❷")
                .replace("3", "❸")
                .replace("4", "❹")
                .replace("5", "❺")
                .replace("6", "❻")
                .replace("7", "❼")
                .replace("8", "❽")
                .replace("9", "❾");
    }
}