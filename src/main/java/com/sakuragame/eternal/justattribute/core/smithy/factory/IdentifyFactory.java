package com.sakuragame.eternal.justattribute.core.smithy.factory;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import com.sakuragame.eternal.justattribute.core.smithy.data.Entry;
import com.sakuragame.eternal.justattribute.core.smithy.data.Group;
import com.sakuragame.eternal.justattribute.core.smithy.data.Level;
import com.sakuragame.eternal.justattribute.core.special.EquipClassify;
import com.sakuragame.eternal.justattribute.core.special.PotencyGrade;
import com.sakuragame.eternal.justattribute.util.Utils;
import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.api.ItemStream;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import net.sakuragame.eternal.dragoncore.util.Pair;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class IdentifyFactory {

    public final static String SCREEN_ID = "identify";

    public final static String EQUIP_SLOT = "identify_equip";
    public final static String PROP_SLOT = "identify_prop";

    private static Group weaponGroup;
    private static Group equipGroup;

    public static void init() {
        loadConfig();
    }

    public static Pair<PotencyGrade, ItemStack> machining(Player player, ItemStack item) {
        return machining(player, item, null);
    }

    public static Pair<PotencyGrade, ItemStack> machining(Player player, ItemStack item, PotencyGrade grade) {
        item.setAmount(1);

        ItemStream itemStream = ZaphkielAPI.INSTANCE.read(item);
        ItemTag itemTag = itemStream.getZaphkielData();

        EquipClassify classify = EquipClassify.getClassify(itemTag);
        if (classify == null) return new Pair<>(PotencyGrade.NONE, item);

        Group group = getGroup(classify);
        Pair<PotencyGrade, Map<Attribute, String>> result = group.getRandom(grade);
        grade = result.getKey();
        Map<Attribute, String> potency = result.getValue();

        itemTag.putDeep(PotencyGrade.NBT_TAG, grade.getId());
        itemTag.removeDeep(Attribute.NBT_NODE_POTENCY);
        for (Attribute key : potency.keySet()) {
            String s = potency.get(key);
            double value = Utils.getRangeValue(s) / 100d;
            itemTag.putDeep(key.getPotencyNode(), value);
        }

        return new Pair<>(grade, itemStream.rebuildToItemStack(player));
    }

    private static Group getGroup(EquipClassify classify) {
        if (classify == EquipClassify.MainHand || classify == EquipClassify.OffHand) {
            return weaponGroup;
        }

        return equipGroup;
    }

    private static void loadConfig() {
        YamlConfiguration yaml = JustAttribute.getFileManager().getSmithyConfig("identify");

        ConfigurationSection weapon = yaml.getConfigurationSection("weapon");
        ConfigurationSection equip = yaml.getConfigurationSection("equip");

        weaponGroup = loadGroup(weapon);
        equipGroup = loadGroup(equip);
    }

    private static Group loadGroup(ConfigurationSection section) {
        if (section == null) return null;

        Entry mini = loadScope(Level.MINI, section.getConfigurationSection("mini"));
        Entry pro = loadScope(Level.PRO, section.getConfigurationSection("pro"));
        Entry ultra = loadScope(Level.ULTRA, section.getConfigurationSection("ultra"));

        return new Group(mini, pro, ultra);
    }

    private static Entry loadScope(Level level, ConfigurationSection section) {
        double weight = section.getDouble("weight");
        Map<Attribute, String> map = loadEntry(section.getConfigurationSection("attribute"));

        return new Entry(level, weight, map);
    }

    private static Map<Attribute, String> loadEntry(ConfigurationSection section) {
        Map<Attribute, String> map = new HashMap<>();
        if (section == null) return map;

        for (String key : section.getKeys(false)) {
            Attribute attribute = Attribute.match(key);
            if (attribute == null) {
                JustAttribute.getInstance().getLogger().info("锻造配置中出现未知属性: " + key);
                continue;
            }
            String range = section.getString(key);
            map.put(attribute, range);
        }

        return map;
    }
}
