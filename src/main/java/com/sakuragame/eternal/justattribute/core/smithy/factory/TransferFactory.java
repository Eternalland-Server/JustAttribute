package com.sakuragame.eternal.justattribute.core.smithy.factory;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import com.sakuragame.eternal.justattribute.core.smithy.SmithyManager;
import com.sakuragame.eternal.justattribute.core.special.EquipClassify;
import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.api.ItemStream;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTagData;
import org.apache.commons.lang.text.StrBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TransferFactory {

    public static int price;

    public final static String SCREEN_ID = "transfer";
    public final static String EQUIP_SLOT = "transfer_equip";
    public final static String PROP_SLOT = "transfer_prop";
    public final static String RESULT_SLOT = "transfer_result";
    public final static String DISPLAY_NODE_EXTENDS = "display.extends";
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

        EquipClassify consumeClassify = EquipClassify.getClassify(consumeTag);
        if (consumeClassify == EquipClassify.Unknown) {
            for (Attribute ident : SmithyManager.ATTRIBUTES) {
                ItemTagData a = acceptTag.getDeep(ident.getOrdinaryNode());
                if (a == null) continue;
                acceptTag.putDeep(ident.getOrdinaryNode(), consumeTag.getDeepOrElse(ident.getOrdinaryNode(), new ItemTagData(1)));
            }
        }
        else {
            acceptTag.removeDeep(Attribute.NBT_NODE_ORDINARY);
            acceptTag.putDeep(Attribute.NBT_NODE_ORDINARY, consumeTag.getDeep(Attribute.NBT_NODE_ORDINARY));
        }

        acceptTag.putDeep(EnhanceFactory.NBT_NODE_ENHANCE, consumeTag.getDeepOrElse(EnhanceFactory.NBT_NODE_ENHANCE, new ItemTagData(0)));
        acceptTag.putDeep(NBT_NODE_EXTENDS, new StrBuilder(consumeStream.getZaphkielName()).reverse().toString());

        return acceptStream.rebuildToItemStack(player);
    }
}
