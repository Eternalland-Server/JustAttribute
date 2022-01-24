package com.sakuragame.eternal.justattribute.file.sub;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.List;

public class ConfigFile {
    private static YamlConfiguration config;

    public static String prefix;

    public static int debug;

    public static double damage_immune_limit;
    public static String potency_empty;

    public static class SoulBound {
        public static String seal;
        public static String auto;
        public static String use;
        public static String prop;
        public static String autoLock;
        public static String useLock;
        public static String propLock;
    }
    
    public static class format {
        public static String attribute;
        public static String bound;
        public static String boundLock;
        public static String classify;
        public static String quality;
        public static List<String> combat;
        public static String potency;
        public static String expire;
    }

    public static HashMap<Attribute, Integer> combatCapability;

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
        config = JustAttribute.getFileManager().getConfig();

        prefix = getString("prefix");

        debug = config.getInt("debug");

        damage_immune_limit = config.getDouble("base.damage-immune-limit");
        potency_empty = getString("base.potency.empty");
        SoulBound.seal = getString("base.soulbound.seal");
        SoulBound.auto = getString("base.soulbound.auto");
        SoulBound.use = getString("base.soulbound.use");
        SoulBound.prop = getString("base.soulbound.prop");
        SoulBound.autoLock = getString("base.soulbound.auto_lock");
        SoulBound.useLock = getString("base.soulbound.use_lock");
        SoulBound.propLock = getString("base.soulbound.prop_lock");

        format.attribute = getString("format.attribute");
        format.bound = getString("format.bound");
        format.boundLock = getString("format.bound_lock");
        format.classify = getString("format.classify");
        format.quality = getString("format.quality");
        format.combat = getStringList("format.combat");
        format.potency = getString("format.potency");
        format.expire = getString("format.expire");

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
            combatCapability.put(Attribute.match(s), value);
        }
    }
}
