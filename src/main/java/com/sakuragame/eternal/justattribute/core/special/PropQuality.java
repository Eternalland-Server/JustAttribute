package com.sakuragame.eternal.justattribute.core.special;

import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.api.ItemStream;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTagData;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
public enum PropQuality {

    Normal(1, "普通"),
    Excellent(2, "优良"),
    Exquisite(3, "精致"),
    Prefect(4, "完美"),
    Epic(5, "史诗"),
    Legend(6, "传说");

    private final int id;
    private final String name;

    public final static String NBT_NODE = "eternal.quality";
    public final static String DISPLAY_NODE = "prop.quality";

    PropQuality(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static PropQuality match(int id) {
        for (PropQuality propQuality : values()) {
            if (propQuality.getId() == id) {
                return propQuality;
            }
        }

        return null;
    }

    public static PropQuality getQuality(ItemStack item) {
        ItemStream itemStream = ZaphkielAPI.INSTANCE.read(item);
        if (itemStream.isVanilla()) return null;

        ItemTag itemTag = itemStream.getZaphkielData();
        return getQuality(itemTag);
    }

    public static PropQuality getQuality(ItemTag tag) {
        ItemTagData data = tag.getDeep(NBT_NODE);
        if (data == null) return null;

        return match(data.asInt());
    }
}
