package com.sakuragame.eternal.justattribute.core.special;

import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.api.ItemStream;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTagData;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
public enum EquipQuality {

    C(0, "C", "§7一无是处"),
    B(1, "B", "§f十分常见"),
    BP(2, "B+", "§2不可多得"),
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
        return ConfigFile.format.quality
                .replace("<name>", getName())
                .replace("<desc>", getDesc());
    }

    public static EquipQuality match(int id) {
        for (EquipQuality equipQuality : values()) {
            if (equipQuality.getId() == id) {
                return equipQuality;
            }
        }

        return null;
    }

    public static EquipQuality getQuality(ItemStack item) {
        ItemStream itemStream = ZaphkielAPI.INSTANCE.read(item);
        if (itemStream.isVanilla()) return null;

        ItemTag itemTag = itemStream.getZaphkielData();
        return getQuality(itemTag);
    }

    public static EquipQuality getQuality(ItemTag tag) {
        ItemTagData data = tag.getDeep(NBT_NODE);
        if (data == null) return null;

        return match(data.asInt());
    }
}
