package com.sakuragame.eternal.justattribute.util;

import java.text.DecimalFormat;

public class Utils {

    private final static DecimalFormat a = new DecimalFormat("#.#");

    public static String formatValue(double value, boolean isPercent) {
        String s = value > 0 ? "+" + a.format(value) : "-" + a.format(value);
        return isPercent ? s + "%" : s;
    }
}
