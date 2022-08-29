package com.sakuragame.eternal.justattribute.core.smithy.factory;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.api.ItemStream;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TransferFactory {

    public static int price;

    public final static String SCREEN_ID = "transfer";
    public final static String EQUIP_SLOT = "transfer_equip";
    public final static String PROP_SLOT = "transfer_prop";
    public final static String RESULT_SLOT = "transfer_result";

    public final static String NBT_NODE_EXTENDS = "justattribute.extends";

    public static void init() {
        price = JustAttribute.getFileManager().getSmithyConfig("transfer").getInt("price");
    }

    public static ItemStack machining(Player player, ItemStack accept, ItemStack consume) {
        accept.setAmount(1);

        ItemStream acceptStream = ZaphkielAPI.INSTANCE.read(accept);
        ItemStream consumeStream = ZaphkielAPI.INSTANCE.read(consume);
        ItemTag acceptTag = acceptStream.getZaphkielData();
        ItemTag consumeTag = consumeStream.getZaphkielData();

        acceptTag.removeDeep(Attribute.NBT_NODE_ORDINARY);
        acceptTag.putDeep(Attribute.NBT_NODE_ORDINARY, consumeTag.getDeep(Attribute.NBT_NODE_ORDINARY));
        acceptTag.putDeep(EnhanceFactory.NBT_NODE_ENHANCE, consumeTag.getDeep(EnhanceFactory.NBT_NODE_ENHANCE));
        acceptTag.putDeep(NBT_NODE_EXTENDS, consumeStream.getZaphkielName());

        return acceptStream.rebuildToItemStack(player);
    }
}
