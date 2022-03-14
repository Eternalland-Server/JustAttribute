package com.sakuragame.eternal.justattribute.core.smithy.factory;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.special.DamageLimit;
import com.sakuragame.eternal.justattribute.util.Utils;
import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.api.ItemStream;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTagData;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class BoostFactory {
    
    public static HashMap<String, Integer> stones;
    
    public final static String SCREEN_ID = "boost";
    public final static String EQUIP_SLOT = "boost_equip";
    public final static String PROP_SLOT = "boost_prop";
    public final static String RESULT_SLOT = "boost_result";

    public static void init() {
        stones = new HashMap<>();
        YamlConfiguration yaml = JustAttribute.getFileManager().getBoostConfig();
        for (String key : yaml.getKeys(false)) {
            int value = yaml.getInt(key);
            stones.put(key, value);
        }
    }

    public static ItemStack machining(Player player, ItemStack equip, ItemStack prop) {
        equip.setAmount(1);

        String propID = Utils.getZapID(prop);
        int value = stones.get(propID);

        ItemStream itemStream = ZaphkielAPI.INSTANCE.read(equip);
        ItemTag data = itemStream.getZaphkielData();

        ItemTagData boost = data.getDeep(DamageLimit.BOOST_NBT_NODE);
        if (boost == null) {
            data.putDeep(DamageLimit.BOOST_NBT_NODE, value);
        }
        else {
            value += boost.asInt();
            data.putDeep(DamageLimit.BOOST_NBT_NODE, value);
        }

        return itemStream.rebuildToItemStack(player);
    }
}
