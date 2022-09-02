package com.sakuragame.eternal.justattribute.core;

import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.api.Item;
import ink.ptms.zaphkiel.api.ItemStream;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTagData;
import net.sakuragame.eternal.dragoncore.util.Pair;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;
import java.util.Map;

public class PetHandler {

    public final static int MAX_LEVEL = 31;
    public final static String EGG_SLOT = "pet_egg_slot";
    public final static String SADDLE_SLOT = "pet_saddle_slot";
    public final static String NBT_NODE_LEVEL = "pet.level";
    public final static String NBT_NODE_EXP = "pet.exp";
    public final static String NBT_NODE_INTIMACY = "pet.intimacy";

    public final static String NBT_NODE_SADDLE = "pet.saddle";
    public final static Map<String, Pair<String, String>> NBT_NODE_EQUIP = new LinkedHashMap<String, Pair<String, String>>() {{
        put("pet_hat_slot", new Pair<>("帽子", "pet.equip.hat"));
        put("pet_clothes_slot", new Pair<>("衣服", "pet.equip.clothes"));
        put("pet_adorn_slot", new Pair<>("配饰", "pet.equip.adorn"));
    }};

    public final static String NBT_NODE_CAPACITY = "pet.capacity";
    public final static String DISPLAY_NODE_INFO = "display.pet-info";
    public final static String DISPLAY_NODE_CAPACITY = "display.pet-capacity";

    public final static int[] stage = {1, 8, 18, 31};

    public static ItemStack setSaddle(Player player, ItemStack egg) {
        ItemStream itemStream = ZaphkielAPI.INSTANCE.read(egg);
        ItemTag tag = itemStream.getZaphkielData();
        tag.putDeep(NBT_NODE_SADDLE, 1);
        return itemStream.rebuildToItemStack(player);
    }

    public static ItemStack setEquip(Player player, String slot, ItemStack egg, ItemStack equip) {
        ItemStream equipStream = ZaphkielAPI.INSTANCE.read(equip);
        String id = equipStream.getZaphkielName();

        ItemStream itemStream = ZaphkielAPI.INSTANCE.read(egg);
        ItemTag tag = itemStream.getZaphkielData();
        tag.putDeep(NBT_NODE_EQUIP.get(slot).getValue(), id);

        return itemStream.rebuildToItemStack(player);
    }

    public static Pair<ItemStack, ItemStack> removeEquip(Player player, String slot, ItemStack egg) {
        ItemStream itemStream = ZaphkielAPI.INSTANCE.read(egg);
        ItemTag tag = itemStream.getZaphkielData();
        ItemTagData data = tag.removeDeep(NBT_NODE_EQUIP.get(slot).getValue());
        if (data == null) return null;
        Item equip = ZaphkielAPI.INSTANCE.getRegisteredItem().get(data.asString());
        return new Pair<>(itemStream.rebuildToItemStack(player), equip.buildItemStack(player));
    }
}
