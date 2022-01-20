package com.sakuragame.eternal.justattribute.core;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleState;
import com.sakuragame.eternal.justattribute.core.special.ItemExpire;
import com.sakuragame.eternal.justattribute.util.RoleSync;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class AttributeManager {

    private final JustAttribute plugin;

    public final static String ORDINARY_DISPLAY_NODE = "display.ordinary";
    public final static String POTENCY_DISPLAY_NODE = "display.potency";

    public final static HashMap<UUID, RoleSync> sync = new HashMap<>();
    public final static List<UUID> loading = new ArrayList<>();

    public AttributeManager(JustAttribute plugin) {
        this.plugin = plugin;
    }

    public void init() {
        this.start();
    }

    private void start() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (!player.isOnline()) continue;

                    if (player.isDead()) continue;

                    UUID uuid = player.getUniqueId();
                    RoleState state = RoleManager.getPlayerState(uuid);

                    if (state == null) continue;

                    state.restore();
                }
            }
        }.runTaskTimer(plugin, 20, 20);
    }
}
