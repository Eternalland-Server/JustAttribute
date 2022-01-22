package com.sakuragame.eternal.justattribute.core.smithy;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import com.sakuragame.eternal.justattribute.core.attribute.AttributeEntry;
import com.sakuragame.eternal.justattribute.core.attribute.PotencyLayer;
import com.sakuragame.eternal.justattribute.core.special.PotencyGrade;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.List;

public class IdentifyFactory {

    private static List<PotencyLayer> weaponPotency = new ArrayList<>();
    private static List<PotencyLayer> equipPotency = new ArrayList<>();

    public static void init() {
        loadConfig();
    }

    private static void loadConfig() {
        YamlConfiguration yaml = JustAttribute.getFileManager().getIdentifyConfig();

        ConfigurationSection weapon = yaml.getConfigurationSection("weapon");
        ConfigurationSection equip = yaml.getConfigurationSection("equip");

        weaponPotency = loadPotency(weapon);
        equipPotency = loadPotency(equip);
    }

    private static List<PotencyLayer> loadPotency(ConfigurationSection section) {
        List<PotencyLayer> layers = new ArrayList<>();
        if (section == null) return layers;

        for (String key : section.getKeys(false)) {
            PotencyGrade grade = PotencyGrade.match(Integer.parseInt(key));
            if (grade == null) {
                JustAttribute.getInstance().getLogger().info("锻造配置中出现未知潜能等级: " + key);
                continue;
            }
            double weight = section.getDouble(key + ".weight");
            ConfigurationSection attribute = section.getConfigurationSection(key + ".attribute");
            List<AttributeEntry> entries = loadEntry(attribute);
            layers.add(new PotencyLayer(grade, weight, entries));
        }

        return layers;
    }

    private static List<AttributeEntry> loadEntry(ConfigurationSection section) {
        List<AttributeEntry> entries = new ArrayList<>();
        if (section == null) return entries;

        for (String key : section.getKeys(false)) {
            Attribute attribute = Attribute.match(key);
            if (attribute == null) {
                JustAttribute.getInstance().getLogger().info("锻造配置中出现未知属性: " + key);
                continue;
            }
            String range = section.getString(key);
            entries.add(new AttributeEntry(attribute, range));
        }

        return entries;
    }
}
