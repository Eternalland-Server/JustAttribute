package com.sakuragame.eternal.justattribute.core.smithy.factory;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import com.sakuragame.eternal.justattribute.core.special.EquipClassify;
import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.api.Item;
import ink.ptms.zaphkiel.api.ItemStream;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTagData;
import net.sakuragame.eternal.dragoncore.util.Pair;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnhanceFactory {

    public final static String SCREEN_ID = "enhance";

    public final static String EQUIP_SLOT = "enhance_equip";
    public final static String PROP_SLOT = "enhance_prop";
    public final static String DISPLAY_NODE_ENHANCE = "display.enhance";
    public final static String NBT_NODE_ENHANCE = "justattribute.enhance";
    public final static String NBT_NODE_PRE_ENHANCE = "justattribute._enhance_";

    public final static int MAX = 21;
    public final static List<Attribute> ATTRIBUTES = Arrays.asList(
            Attribute.Energy,
            Attribute.Stamina,
            Attribute.Wisdom,
            Attribute.Technique,
            Attribute.Damage,
            Attribute.Defence,
            Attribute.Health,
            Attribute.Mana
    );

    public final static Map<Integer, Double> chance = new HashMap<>();

    public static void init() {
        chance.clear();

        YamlConfiguration config = JustAttribute.getFileManager().getSmithyConfig("enhance");
        ConfigurationSection section = config.getConfigurationSection("chance");

        for (String key : section.getKeys(false)) {
            double value = section.getDouble(key);
            chance.put(Integer.parseInt(key), value);
        }
    }

    public static Pair<Boolean, ItemStack> machining(Player player, ItemStack equip) {
        ItemStream itemStream = ZaphkielAPI.INSTANCE.read(equip);
        Item item = itemStream.getZaphkielItem();
        ItemTag tag = itemStream.getZaphkielData();

        ItemTagData extend = tag.getDeep(TransferFactory.NBT_NODE_EXTENDS);
        if (extend != null) {
            String id = new StringBuilder(extend.asString()).reverse().toString();
            Item exItem = ZaphkielAPI.INSTANCE.getRegisteredItem().get(id);
            if (exItem != null) item = exItem;
        }

        EquipClassify classify = EquipClassify.getClassify(tag);
        if (classify == null) return new Pair<>(false, equip);

        int count = tag.getDeepOrElse(NBT_NODE_ENHANCE, new ItemTagData(0)).asInt();
        if (count == MAX) return new Pair<>(false, equip);

        double ratio = chance.get(count + 1);
        if (ratio < Math.random()) return new Pair<>(false, equip);

        for (Attribute identifier : ATTRIBUTES) {
            double original = item.getData().getDouble(identifier.getOrdinaryNode(), -1);
            if (original == -1) continue;

            double from = original * (classify.getId() <= 5 ? 0.707 : 0.382) / MAX;
            double to = original * (classify.getId() <= 5 ? 1.618 : 0.66) / MAX;

            double random = from + Math.random() * (to - from);
            double current = tag.getDeep(identifier.getOrdinaryNode()).asDouble();

            tag.putDeep(identifier.getOrdinaryNode(), new BigDecimal(current + random).setScale(2, RoundingMode.HALF_UP).doubleValue());
        }

        tag.putDeep(NBT_NODE_ENHANCE, count + 1);

        return new Pair<>(true, itemStream.rebuildToItemStack(player));
    }

    public static double calculate(EquipClassify classify, double original, int level) {
        level = Math.min(level, 21);
        double from = original * (classify.getId() <= 5 ? 0.707 : 0.382) / MAX;
        double to = original * (classify.getId() <= 5 ? 1.618 : 0.66) / MAX;

        double value = original;
        for (int i = 0; i < level; i++) {
            value += from + Math.random() * (to - from);
        }

        return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
