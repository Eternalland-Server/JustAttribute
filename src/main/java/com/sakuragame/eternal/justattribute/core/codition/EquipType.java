package com.sakuragame.eternal.justattribute.core.codition;

import lombok.Getter;

@Getter
public enum EquipType {

    MainHand(0, "武器"),
    OffHand(1, "副手"),
    Armor(2, "防具"),
    Glasses(3, "护目镜"),
    EarDrop(4, "耳坠"),
    Cloak(5, "披风"),
    Ring(5, "戒指"),
    Medal(6, "勋章"),
    Honor(7, "头衔"),
    PetEgg(8, "宠物");


    private final int id;
    private final String name;

    public final static String TYPE_NODE = "justattribute.type";
    public final static String DISPLAY_NODE = "type.display";

    EquipType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static EquipType getType(int id) {
        for (EquipType type : values()) {
            if (type.getId() == id) {
                return type;
            }
        }

        return null;
    }
}
