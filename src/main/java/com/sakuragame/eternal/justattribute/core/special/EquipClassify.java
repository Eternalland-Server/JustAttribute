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

    MainHand(0, "武器", true),
    OffHand(1, "副手", true),
    Helmet(11, "头盔", true),
    ChestPlate(12, "盔甲", true),
    Leggings(13, "裤子", true),
    Boots(14, "鞋子", true),
    Glasses(3, "眼镜", true),
    EarDrop(4, "耳坠", true),
    Gloves(5, "手套", true),
    Ring(6, "戒指", true),
    Medal(7, "勋章", true),
    Honor(8, "头衔", true),
    Potion(9, "药水"),
    Pet_Egg(21, "宠物蛋"),
    Pet_Saddle(22, "宠物鞍"),
    Pet_Hat(23, "宠物装备(帽子)", true),
    Pet_Clothes(24, "宠物装备(衣服)", true),
    Pet_Adorn(25, "宠物装备(装饰)", true),
    Skin_Suit(100, "时装(套装)"),
    SKin_MainHand(101, "时装(主武器)"),
    SKin_Offhand(102, "时装(副武器)"),
    Skin_HandWear(103, "时装(头饰)"),
    Skin_Clothes(104, "时装(上衣)"),
    Skin_Pants(105, "时装(裤子)"),
    Skin_Shoes(106, "时装(鞋子)"),
    Skin_Wings(107, "时装(翅膀)");


    private final int id;
    private final String name;
    private final boolean enhance;

    public final static String NBT_NODE = "justattribute.classify";
    public final static String DISPLAY_NODE = "display.classify";

    EquipClassify(int id, String name) {
        this.id = id;
        this.name = name;
        this.enhance = false;
    }

    EquipClassify(int id, String name, boolean enhance) {
        this.id = id;
        this.name = name;
        this.enhance = enhance;
    }

    public String formatting() {
        return ConfigFile.format.classify.replace("<classify>", getName());
    }

    public static EquipClassify match(int id) {
        for (EquipClassify type : values()) {
            if (type.getId() == id) {
                return type;
            }
        }

        return null;
    }

    public static EquipClassify getClassify(ItemStack item) {
        ItemStream itemStream = ZaphkielAPI.INSTANCE.read(item);
        if (itemStream.isVanilla()) return null;

        ItemTag itemTag = itemStream.getZaphkielData();
        return getClassify(itemTag);
    }

    public static EquipClassify getClassify(ItemTag tag) {
        ItemTagData data = tag.getDeep(NBT_NODE);
        if (data == null) return null;

        return match(data.asInt());
    }
}
