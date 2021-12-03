package com.sakuragame.eternal.justattribute.core.special;

import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.api.ItemStream;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTagData;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
public enum EquipClassify {

    MainHand(0, "武器"),
    OffHand(1, "副手"),
    Armor(2, "防具"),
    Glasses(3, "眼镜"),
    EarDrop(4, "耳坠"),
    Cloak(5, "披风"),
    Ring(6, "戒指"),
    Medal(7, "勋章"),
    Honor(8, "头衔"),
    PetEgg(9, "宠物蛋"),
    PetSaddle(10, "宠物鞍"),
    PetEquip(11, "宠物装备"),
    Skin_Suit(100, "时装(套装)"),
    SKin_Weapon(101, "时装(武器)"),
    Skin_HandWear(102, "时装(头饰)"),
    Skin_Clothes(103, "时装(上衣)"),
    Skin_Pants(104, "时装(裤子)"),
    Skin_Shoes(105, "时装(鞋子)"),
    Skin_Wings(106, "时装(翅膀)");


    private final int id;
    private final String name;

    public final static String NBT_NODE = "justattribute.classify";
    public final static String DISPLAY_NODE = "display.classify";

    EquipClassify(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String formatting() {
        return ConfigFile.classify_format.replace("<classify>", getName());
    }

    public static EquipClassify getType(int id) {
        for (EquipClassify type : values()) {
            if (type.getId() == id) {
                return type;
            }
        }

        return null;
    }

    public static EquipClassify getItemType(ItemStack item) {
        ItemStream itemStream = ZaphkielAPI.INSTANCE.read(item);
        if (itemStream.isVanilla()) return null;

        ItemTag itemTag = itemStream.getZaphkielData();
        int id = itemTag.getDeepOrElse(EquipClassify.NBT_NODE, new ItemTagData(-1)).asInt();

        return getType(id);
    }
}
