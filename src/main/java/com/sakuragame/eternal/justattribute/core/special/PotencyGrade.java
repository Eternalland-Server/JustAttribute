package com.sakuragame.eternal.justattribute.core.special;

import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.api.ItemStream;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTagData;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
public enum PotencyGrade {

    NONE(-1, "ㅐ", ""),
    B(0, "ㅑ", "B"),
    A(1, "ㅒ", "A"),
    S(2, "ㅓ", "S"),
    SS(3, "ㅔ", "SS"),
    SSS(4, "ㅏ", "SSS");

    private final int id;
    private final String symbol;
    private final String name;

    public final static String NBT_TAG = "justattribute.grade";
    private final static String POTENCY_DESC = "⌚";

    PotencyGrade(int id, String symbol, String name) {
        this.id = id;
        this.symbol = symbol;
        this.name = name;
    }

    public String formatting() {
        return ConfigFile.format.potency
                .replace("<symbol>", getSymbol())
                .replace("<desc>", POTENCY_DESC);
    }

    public static PotencyGrade match(int id) {
        for (PotencyGrade grade : values()) {
            if (grade.getId() == id) {
                return grade;
            }
        }

        return null;
    }

    public static PotencyGrade getGrade(ItemStack item) {
        ItemStream itemStream = ZaphkielAPI.INSTANCE.read(item);
        if (itemStream.isVanilla()) return null;

        ItemTag itemTag = itemStream.getZaphkielData();
        return getGrade(itemTag);
    }

    public static PotencyGrade getGrade(ItemTag tag) {
        ItemTagData data = tag.getDeep(NBT_TAG);
        if (data == null) return null;

        return match(data.asInt());
    }
}
