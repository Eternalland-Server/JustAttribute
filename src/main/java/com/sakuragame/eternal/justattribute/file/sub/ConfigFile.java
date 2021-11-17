package com.sakuragame.eternal.justattribute.file.sub;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.Identifier;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.List;

public class ConfigFile {
    private static YamlConfiguration config;

    public static String prefix;

    public static double damage_immune_limit;
    public static String potency_empty;
    public static String soulbound_auto;
    public static String soulbound_use;

    public static String attribute_format;
    public static String bound_format;
    public static String unbound_format;
    public static String realm_format;
    public static String classify_format;
    public static String quality_format;
    public static List<String> combat_format;
    public static String potency_format;

    public static HashMap<Identifier, Integer> combatCapability;

    public static class RoleBase {
        public static double health;
        public static double mana;
        public static double damage;
        public static double defence;
        public static double restoreHP;
        public static double restoreMP;
    }

    public static class RolePromote {
        public static double health;
        public static double mana;
        public static double damage;
        public static double defence;
        public static double restoreHP;
        public static double restoreMP;
    }

    public static HashMap<String, Integer> slotSetting;

    public static void init() {
        config = JustAttribute.getInstance().getFileManager().getConfig();

        prefix = getString("prefix");

        damage_immune_limit = config.getDouble("base.damage-immune-limit");
        potency_empty = getString("base.potency.empty");
        soulbound_auto = getString("base.soulbound.auto");
        soulbound_use = getString("base.soulbound.use");

        attribute_format = getString("format.attribute");
        bound_format = getString("format.bound");
        unbound_format = getString("format.unbound");
        classify_format = getString("format.classify");
        quality_format = getString("format.quality");
        combat_format = getStringList("format.combat");
        potency_format = getString("format.potency");

        RoleBase.health = config.getDouble("role-base.health");
        RoleBase.mana = config.getDouble("role-base.mana");
        RoleBase.damage = config.getDouble("role-base.damage");
        RoleBase.defence = config.getDouble("role-base.defence");
        RoleBase.restoreHP = config.getDouble("role-base.restore-hp");
        RoleBase.restoreMP = config.getDouble("role-base.restore-mp");

        RolePromote.health = config.getDouble("role-promote.health");
        RolePromote.mana = config.getDouble("role-promote.mana");
        RolePromote.damage = config.getDouble("role-promote.damage");
        RolePromote.defence = config.getDouble("role-promote.defence");
        RolePromote.restoreHP = config.getDouble("role-promote.restore-hp");
        RolePromote.restoreMP = config.getDouble("role-promote.restore-mp");

        loadSlotSetting();
        loadCombatCapability();
    }

    private static String getString(String path) {
        return MegumiUtil.onReplace(config.getString(path));
    }

    private static List<String> getStringList(String path) {
        return MegumiUtil.onReplace(config.getStringList(path));
    }

    private static void loadSlotSetting() {
        slotSetting = new HashMap<>();

        ConfigurationSection section = config.getConfigurationSection("slot-setting");
        if (section == null) return;

        for (String s : section.getKeys(false)) {
            int type = section.getInt(s);
            slotSetting.put(s, type);
        }
    }

    private static void loadCombatCapability() {
        combatCapability = new HashMap<>();

        ConfigurationSection section = config.getConfigurationSection("combat-capability");
        if (section == null) return;

        for (String s : section.getKeys(false)) {
            int value = section.getInt(s);
            combatCapability.put(Identifier.get(s), value);
        }
    }
}
