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

    public static boolean spring;

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

    public static HashMap<String, Integer> slotSetting;

    public static void init() {
        config = JustAttribute.getFileManager().getConfig();

        prefix = getString("prefix");

        debug = config.getInt("debug");
        spring = config.getBoolean("spring", false);

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
