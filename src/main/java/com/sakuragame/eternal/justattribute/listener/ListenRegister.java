package com.sakuragame.eternal.justattribute.listener;

import com.sakuragame.eternal.justattribute.JustAttribute;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public abstract class ListenRegister {

    private final JustAttribute plugin;

    public ListenRegister(JustAttribute plugin) {
        this.plugin = plugin;
    }

    public void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }
}
