package com.sakuragame.eternal.justattribute.core.attribute.stats;

import com.sakuragame.eternal.justattribute.core.codition.EquipType;
import lombok.Getter;

@Getter
public enum VanillaSlot {

    Helmet(5, EquipType.Armor, "helmet"),
    ChestPlate(6, EquipType.Armor, "chestplate"),
    Leggings(7, EquipType.Armor, "leggings"),
    Boots(8, EquipType.Armor, "boots"),
    OffHand(45, EquipType.OffHand, "offhand");

    private final int index;
    private final EquipType type;
    private final String ident;

    VanillaSlot(int index, EquipType type, String ident) {
        this.index = index;
        this.type = type;
        this.ident = ident;
    }

    public static VanillaSlot getSlot(int index) {
        for (VanillaSlot slot : values()) {
            if (slot.getIndex() == index) {
                return slot;
            }
        }

        return null;
    }
}
