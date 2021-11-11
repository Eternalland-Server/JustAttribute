package com.sakuragame.eternal.justattribute.util;

import com.sakuragame.eternal.justattribute.core.codition.EquipType;
import com.sakuragame.eternal.justattribute.core.codition.Realm;
import com.sakuragame.eternal.justattribute.core.codition.SoulBound;
import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.api.ItemStream;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTagData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.UUID;

public class Utils {

    private final static ZaphkielAPI zap = ZaphkielAPI.INSTANCE;
    private final static DecimalFormat a = new DecimalFormat("#.#");

    public static String formatValue(double value, boolean isPercent) {
        String s = value > 0 ? "+" + a.format(value) : "-" + a.format(value);
        return isPercent ? s + "%" : s;
    }

    public static boolean isArmor(Material material) {
        String name = material.name();
        return name.contains("HELMET") ||
                name.contains("CHESTPLATE") ||
                name.contains("LEGGINGS") ||
                name.contains("BOOTS");
    }

    public static ItemStack binding(Player player, ItemStack item) {
        ItemStream itemStream = zap.read(item);
        if (itemStream.isVanilla()) return item;

        ItemTag itemTag = itemStream.getZaphkielData();

        int actionType = itemTag.getDeepOrElse(SoulBound.NBT_ACTION_NODE, new ItemTagData(-1)).asInt();
        if (actionType == -1 || actionType != SoulBound.Action.USE.getId()) return item;

        itemTag.removeDeep(SoulBound.NBT_ACTION_NODE);
        itemTag.putDeep(SoulBound.NBT_UUID_NODE, player.getUniqueId().toString());
        itemTag.putDeep(SoulBound.NBT_NAME_NODE, player.getName());

        return itemStream.rebuildToItemStack(player);
    }

    public static boolean isUseBind(ItemStack item) {
        ItemStream itemStream = zap.read(item);
        if (itemStream.isVanilla()) return false;

        ItemTag itemTag = itemStream.getZaphkielData();
        int actionType = itemTag.getDeepOrElse(SoulBound.NBT_ACTION_NODE, new ItemTagData(-1)).asInt();

        return actionType == SoulBound.Action.USE.getId();
    }

    public static int getItemRealm(ItemStack item) {
        ItemStream itemStream = zap.read(item);
        if (itemStream.isVanilla()) return -1;

        ItemTag itemTag = itemStream.getZaphkielData();

        return itemTag.getDeepOrElse(Realm.NBT_NODE, new ItemTagData(-1)).asInt();
    }

    public static UUID getItemOwner(ItemStack item) {
        ItemStream itemStream = zap.read(item);
        if (itemStream.isVanilla()) return null;

        ItemTag itemTag = itemStream.getZaphkielData();

        ItemTagData data = itemTag.getDeep(SoulBound.NBT_UUID_NODE);
        if (data == null) return null;

        return UUID.fromString(data.asString());
    }

    public static int getItemType(ItemStack item) {
        ItemStream itemStream = zap.read(item);
        if (itemStream.isVanilla()) return -1;

        ItemTag itemTag = itemStream.getZaphkielData();

        return itemTag.getDeepOrElse(EquipType.NBT_NODE, new ItemTagData(-1)).asInt();
    }
}
