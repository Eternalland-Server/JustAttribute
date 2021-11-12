package com.sakuragame.eternal.justattribute.core.special;

import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import lombok.Getter;

@Getter
public enum PotencyGrade {

    NONE(-1, "ㅐ", ""),
    B(0, "ㅑ", "B"),
    A(1, "ㅒ", "A"),
    SS(2, "ㅓ", "SS"),
    SSS(3, "ㅔ", "SSS");

    private final int id;
    private final String symbol;
    private final String name;

    public final static String NBT_TAG = "justattribute.potency.grade";
    private final static String POTENCY_DESC = "⌚";

    PotencyGrade(int id, String symbol, String name) {
        this.id = id;
        this.symbol = symbol;
        this.name = name;
    }

    public String formatting() {
        return ConfigFile.potency_format
                .replace("<symbol>", getSymbol())
                .replace("<desc>", POTENCY_DESC);
    }

    public static PotencyGrade getGrade(int id) {
        for (PotencyGrade grade : values()) {
            if (grade.getId() == id) {
                return grade;
            }
        }

        return null;
    }
}
