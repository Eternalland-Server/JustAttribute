package com.sakuragame.eternal.justattribute.core;

import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import com.sakuragame.eternal.justattribute.core.special.EquipQuality;
import com.sakuragame.eternal.justattribute.file.sub.PetFile;
import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.api.Item;
import ink.ptms.zaphkiel.api.ItemStream;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTagData;
import net.sakuragame.eternal.dragoncore.util.Pair;
import org.apache.commons.lang.text.StrBuilder;
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
    public final static String NBT_NODE_NAME = "pet.name";

    public final static String NBT_NODE_SADDLE = "pet.saddle";
    public final static Map<String, Pair<String, String>> NBT_NODE_EQUIP = new LinkedHashMap<String, Pair<String, String>>() {{
        put("pet_hat_slot", new Pair<>("帽子", "pet.equip.hat"));
        put("pet_clothes_slot", new Pair<>("衣服", "pet.equip.clothes"));
        put("pet_adorn_slot", new Pair<>("配饰", "pet.equip.adorn"));
    }};

    public final static String NBT_NODE_CAPACITY = "pet.capacity";
    public final static String DISPLAY_NODE_INFO = "display.pet-info";
    public final static String DISPLAY_NODE_CAPACITY = "display.pet-capacity";

    public final static int[] stage = {3, 8, 18, 30, 31};

    public static ItemStack setSaddle(Player player, ItemStack egg) {
        ItemStream itemStream = ZaphkielAPI.INSTANCE.read(egg);
        ItemTag tag = itemStream.getZaphkielData();
        tag.putDeep(NBT_NODE_SADDLE, 1);
        return itemStream.rebuildToItemStack(player);
    }

    public static int isUseSaddle(ItemStack egg) {
        ItemStream itemStream = ZaphkielAPI.INSTANCE.read(egg);
        ItemTag tag = itemStream.getZaphkielData();
        return tag.getDeepOrElse(NBT_NODE_SADDLE, new ItemTagData(0)).asInt();
    }

    public static ItemStack setEquip(Player player, String slot, ItemStack egg, ItemStack equip) {
        ItemStream equipStream = ZaphkielAPI.INSTANCE.read(equip);
        String id = equipStream.getZaphkielName();

        ItemStream itemStream = ZaphkielAPI.INSTANCE.read(egg);
        ItemTag tag = itemStream.getZaphkielData();
        tag.putDeep(NBT_NODE_EQUIP.get(slot).getValue(), new StrBuilder(id).reverse().toString());

        return itemStream.rebuildToItemStack(player);
    }

    public static Pair<ItemStack, ItemStack> removeEquip(Player player, String slot, ItemStack egg) {
        ItemStream itemStream = ZaphkielAPI.INSTANCE.read(egg);
        ItemTag tag = itemStream.getZaphkielData();
        ItemTagData data = tag.removeDeep(NBT_NODE_EQUIP.get(slot).getValue());
        if (data == null) return null;
        String equipID = new StrBuilder(data.asString()).reverse().toString();
        Item equip = ZaphkielAPI.INSTANCE.getRegisteredItem().get(equipID);
        return new Pair<>(itemStream.rebuildToItemStack(player), equip.buildItemStack(player));
    }

    public static ItemStack unlockCapacity(Player player, ItemStack egg, int level) {
        ItemStream itemStream = ZaphkielAPI.INSTANCE.read(egg);
        ItemTag tag = itemStream.getZaphkielData();
        EquipQuality quality = EquipQuality.getQuality(tag);
        if (quality == null) return egg;

        for (int i : stage) {
            if (i == 31 && quality.getId() < 5) continue;

            ItemTagData data = tag.getDeep(NBT_NODE_CAPACITY + "." + i);
            if (data != null) continue;
            if (level >= i ) {
                Pair<Attribute, Double> result = PetFile.random(i);
                tag.putDeep(NBT_NODE_CAPACITY + "." + i, result.getKey().getId() + "|" + result.getValue());
            }
        }

        return itemStream.rebuildToItemStack(player);
    }
}
