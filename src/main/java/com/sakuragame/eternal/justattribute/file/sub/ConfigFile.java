package com.sakuragame.eternal.justattribute.file.sub;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public class ConfigFile {
    private static YamlConfiguration config;

    public static String prefix;

    public static String format;

    public static void init() {
        config = JustAttribute.getInstance().getFileManager().getConfig();

        prefix = getString("prefix");

        format = getString("format");
    }

    private static String getString(String path) {
        return MegumiUtil.onReplace(config.getString(path));
    }

    private static List<String> getStringList(String path) {
        return MegumiUtil.onReplace(config.getStringList(path));
    }
}
