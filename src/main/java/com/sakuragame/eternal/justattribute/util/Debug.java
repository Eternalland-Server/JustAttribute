package com.sakuragame.eternal.justattribute.util;

import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import lombok.Getter;
import org.bukkit.Bukkit;

@Getter
public enum Debug {

    CombatCapacity(1),
    Attribute(2),
    Role(3);

    private final int level;

    Debug(int level) {
        this.level = level;
    }

    public static void info(Debug debug, String message) {
        int level = ConfigFile.debug;

        if (debug.getLevel() < level) return;

        Bukkit.getConsoleSender().sendMessage("[JustAttribute - Debug] " + message);
    }

}
