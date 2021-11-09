package com.sakuragame.eternal.justattribute.file.sub;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public class ConfigFile {
    private static YamlConfiguration config;

    public static String prefix;

    public static String none_potency;

    public static String attribute_format;
    public static String bound_format;
    public static String unbound_format;
    public static String realm_format;
    public static String type_format;

    public static void init() {
        config = JustAttribute.getInstance().getFileManager().getConfig();

        prefix = getString("prefix");

        none_potency = getString("none-potency");

        attribute_format = getString("format.attribute");
        bound_format = getString("format.bound");
        unbound_format = getString("format.unbound");
        type_format = getString("format.type");
    }

    private static String getString(String path) {
        return MegumiUtil.onReplace(config.getString(path));
    }

    private static List<String> getStringList(String path) {
        return MegumiUtil.onReplace(config.getStringList(path));
    }
}
