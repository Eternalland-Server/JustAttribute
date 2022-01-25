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

    public static void init() {
        price = JustAttribute.getFileManager().getTransferConfig().getInt("price");
    }

    public static ItemStack machining(Player player, ItemStack equip, ItemStack prop) {
        equip.setAmount(1);

        ItemStream equipStream = ZaphkielAPI.INSTANCE.read(equip);
        ItemStream propStream = ZaphkielAPI.INSTANCE.read(prop);
        ItemTag equipTag = equipStream.getZaphkielData();
        ItemTag propTag = propStream.getZaphkielData();

        equipTag.removeDeep(Attribute.ORDINARY_NBT_NODE);
        equipTag.putDeep(Attribute.ORDINARY_NBT_NODE, propTag.getDeep(Attribute.ORDINARY_NBT_NODE));

        return equipStream.rebuildToItemStack(player);
    }
}
