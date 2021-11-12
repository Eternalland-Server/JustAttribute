package com.sakuragame.eternal.justattribute.core.attribute;

import com.sakuragame.eternal.justattribute.core.special.EquipClassify;
import lombok.Getter;
import org.bukkit.Material;

@Getter
public enum VanillaSlot {

    Helmet(5, EquipClassify.Armor, "helmet"),
    ChestPlate(6, EquipClassify.Armor, "chestplate"),
    Leggings(7, EquipClassify.Armor, "leggings"),
    Boots(8, EquipClassify.Armor, "boots"),
    MainHand(-1, EquipClassify.MainHand, "mainhand"),
    OffHand(45, EquipClassify.OffHand, "offhand");

    private final int index;
    private final EquipClassify type;
    private final String ident;

    VanillaSlot(int index, EquipClassify type, String ident) {
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

    public static VanillaSlot getSlot(Material material) {
        for (VanillaSlot slot : values()) {
            if (material.name().contains(slot.getIdent().toUpperCase())) {
                return slot;
            }
        }

        return null;
    }
}
