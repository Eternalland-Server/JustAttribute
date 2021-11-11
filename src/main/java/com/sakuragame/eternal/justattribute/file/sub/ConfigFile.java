package com.sakuragame.eternal.justattribute.file.sub;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.List;

public class ConfigFile {
    private static YamlConfiguration config;

    public static String prefix;

    public static double damage_immune_limit;

    public static String attribute_format;
    public static String bound_format;
    public static String unbound_format;
    public static String realm_format;
    public static String type_format;

    public static String potency_empty;

    public static String soulbound_auto;
    public static String soulbound_use;

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

        attribute_format = getString("format.attribute");
        bound_format = getString("format.bound");
        unbound_format = getString("format.unbound");
        type_format = getString("format.type");

        potency_empty = getString("potency.empty");

        soulbound_auto = getString("soulbound.auto");
        soulbound_use = getString("soulbound.use");

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
}
