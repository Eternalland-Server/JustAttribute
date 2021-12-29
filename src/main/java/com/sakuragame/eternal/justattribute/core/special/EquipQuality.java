package com.sakuragame.eternal.justattribute.core.special;

import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import lombok.Getter;

@Getter
public enum EquipQuality {

    C(0, "C", "§7一无是处"),
    B(1, "B", "§f十分常见"),
    BP(2, "B+", "§4不可多得"),
    A(3, "A", "§a百里挑一"),
    AP(4, "A+", "§3千载难逢"),
    S(5, "S", "§b希世之珍"),
    SP(6, "S+", "§e万众瞩目"),
    SS(7, "SS", "§6王者无敌"),
    SSS(8, "SSS", "§c超凡入圣");

    private final int id;
    private final String name;
    private final String desc;

    public final static String NBT_NODE = "justattribute.quality";
    public final static String DISPLAY_NODE = "display.quality";

    EquipQuality(int id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
    }

    public String formatting() {
        return ConfigFile.quality_format
                .replace("<name>", getName())
                .replace("<desc>", getDesc());
    }

    public static EquipQuality getQuality(int id) {
        for (EquipQuality equipQuality : values()) {
            if (equipQuality.getId() == id) {
                return equipQuality;
            }
        }

        return null;
    }
}
