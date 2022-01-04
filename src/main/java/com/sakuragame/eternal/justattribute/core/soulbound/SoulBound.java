package com.sakuragame.eternal.justattribute.core.soulbound;

import com.sakuragame.eternal.justattribute.api.event.EquipBoundEvent;
import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.api.ItemStream;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTagData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class SoulBound {

    public final static String NBT_ACTION_NODE = "justattribute.soulbound.action";
    public final static String NBT_TYPE_NODE = "justattribute.soulbound.type";
    public final static String NBT_UUID_NODE = "justattribute.soulbound.uuid";
    public final static String NBT_NAME_NODE = "justattribute.soulbound.name";
    public final static String DISPLAY_NODE = "display.soulbound";

    public static Action getAction(ItemStack item) {
        ItemStream itemStream = ZaphkielAPI.INSTANCE.read(item);
        if (itemStream.isVanilla()) return null;

        ItemTag itemTag = itemStream.getZaphkielData();
        return getAction(itemTag);
    }

    public static Action getAction(ItemTag tag) {
        ItemTagData data = tag.getDeep(NBT_ACTION_NODE);
        if (data == null) return null;

        return Action.match(data.asInt());
    }

    public static Owner getOwner(ItemStack item) {
        ItemStream itemStream = ZaphkielAPI.INSTANCE.read(item);
        if (itemStream.isVanilla()) return null;

        ItemTag itemTag = itemStream.getZaphkielData();

        return getOwner(itemTag);
    }

    public static Owner getOwner(ItemTag tag) {
        ItemTagData uuidDate = tag.getDeep(NBT_UUID_NODE);
        ItemTagData nameDate = tag.getDeep(NBT_NAME_NODE);

        if (uuidDate == null) return null;

        return new Owner(UUID.fromString(uuidDate.asString()), nameDate.asString());
    }

    public static ItemStack binding(Player player, ItemStack item, Action action) {
        ItemStream itemStream = ZaphkielAPI.INSTANCE.read(item);
        if (itemStream.isVanilla()) return item;

        ItemTag itemTag = itemStream.getZaphkielData();

        action.getHandler().build(player, itemTag);

        ItemStack rebuild = itemStream.rebuildToItemStack(player);

        EquipBoundEvent event = new EquipBoundEvent(player, rebuild);
        event.call();

        return rebuild;
    }
}
