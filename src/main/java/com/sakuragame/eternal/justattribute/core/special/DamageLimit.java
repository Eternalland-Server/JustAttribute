package com.sakuragame.eternal.justattribute.core.special;

import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.api.ItemStream;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTagData;
import net.sakuragame.eternal.dragoncore.util.Pair;
import org.bukkit.inventory.ItemStack;

public class DamageLimit {

    public final static String DISPLAY_NODE = "display.damage-limit";
    public final static String LIMIT_NBT_NODE = "justattribute.damage-limit";
    public final static String BOOST_NBT_NODE = "justattribute.damage-boost";

    public static String format(int limit, int boost) {
        return "   §a伤害上限: §f" + limit + "&6(+" + boost + ")";
    }

    public static int getDamageLimit(ItemStack item) {
        ItemStream itemStream = ZaphkielAPI.INSTANCE.read(item);
        if (itemStream.isVanilla()) return 0;

        ItemTag itemTag = itemStream.getZaphkielData();
        return getDamageLimit(itemTag);
    }

    public static int getDamageLimit(ItemTag tag) {
        ItemTagData data = tag.getDeep(LIMIT_NBT_NODE);
        if (data == null) return 0;

        return data.asInt();
    }

    public static int getDamageBoost(ItemStack item) {
        ItemStream itemStream = ZaphkielAPI.INSTANCE.read(item);
        if (itemStream.isVanilla()) return 0;

        ItemTag itemTag = itemStream.getZaphkielData();
        return getDamageLimit(itemTag);
    }

    public static int getDamageBoost(ItemTag tag) {
        ItemTagData data = tag.getDeep(BOOST_NBT_NODE);
        if (data == null) return 0;

        return data.asInt();
    }

    public static Pair<Integer, Integer> getDamagePair(ItemStack item) {
        ItemStream itemStream = ZaphkielAPI.INSTANCE.read(item);
        if (itemStream.isVanilla()) return null;

        ItemTag itemTag = itemStream.getZaphkielData();
        return getDamagePair(itemTag);
    }

    public static Pair<Integer, Integer> getDamagePair(ItemTag tag) {
        int first = 0;
        int second = 0;
        ItemTagData limit = tag.getDeep(LIMIT_NBT_NODE);
        ItemTagData boost = tag.getDeep(BOOST_NBT_NODE);
        if (limit != null) first = limit.asInt();
        if (boost != null) second = boost.asInt();

        return new Pair<>(first, second);
    }

    public static int getDamageUpperLimit(ItemStack item) {
        ItemStream itemStream = ZaphkielAPI.INSTANCE.read(item);
        if (itemStream.isVanilla()) return 0;

        ItemTag itemTag = itemStream.getZaphkielData();
        return getDamageLimit(itemTag);
    }

    public static int getDamageUpperLimit(ItemTag tag) {
        int upperLimit = 0;
        ItemTagData limit = tag.getDeep(LIMIT_NBT_NODE);
        ItemTagData boost = tag.getDeep(BOOST_NBT_NODE);
        if (limit != null) upperLimit += limit.asInt();
        if (boost != null) upperLimit += boost.asInt();

        return upperLimit;
    }
}
