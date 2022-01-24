package com.sakuragame.eternal.justattribute.core.smithy.factory;

import com.sakuragame.eternal.justattribute.core.soulbound.Action;
import com.sakuragame.eternal.justattribute.core.soulbound.SoulBound;
import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.api.ItemStream;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SealFactory {

    public final static String SCREEN_ID = "seal";

    public final static String PROP_SLOT = "seal_prop";
    public final static String RESULT_SLOT = "seal_result";

    public static ItemStack unlock(Player player, ItemStack item) {
        item.setAmount(1);

        ItemStream itemStream = ZaphkielAPI.INSTANCE.read(item);
        ItemTag itemTag = itemStream.getZaphkielData();

        itemTag.removeDeep(SoulBound.NBT_NODE);
        itemTag.putDeep(SoulBound.NBT_TYPE_NODE, Action.USE.getId());
        itemTag.putDeep(SoulBound.NBT_UUID_NODE, player.getUniqueId().toString());
        itemTag.putDeep(SoulBound.NBT_NAME_NODE, player.getName());

        return itemStream.rebuildToItemStack(player);
    }

    public static ItemStack lock(ItemStack item) {
        item.setAmount(1);

        ItemStream itemStream = ZaphkielAPI.INSTANCE.read(item);
        ItemTag itemTag = itemStream.getZaphkielData();

        itemTag.removeDeep(SoulBound.NBT_NODE);
        itemTag.putDeep(SoulBound.NBT_TYPE_NODE, Action.SEAL.getId());

        return itemStream.rebuildToItemStack(null);
    }
}
