package com.sakuragame.eternal.justattribute.core;

import com.sakuragame.eternal.justattribute.JustAttribute;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AttributeManager {

    private final JustAttribute plugin;

    public final static String ORDINARY_DISPLAY_NODE = "display.ordinary";
    public final static String POTENCY_DISPLAY_NODE = "display.potency";

    public final static List<UUID> loading = new ArrayList<>();

    public AttributeManager(JustAttribute plugin) {
        this.plugin = plugin;
        this.start();
    }

    private void start() {
        Bukkit.getScheduler().runTaskLater(JustAttribute.getInstance(), () -> Bukkit.getOnlinePlayers().forEach(player -> {
            UUID uuid = player.getUniqueId();

            JustAttribute.getRoleManager().getPlayerState(uuid).restore();
        }), 20);
    }
}
