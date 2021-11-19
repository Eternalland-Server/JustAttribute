package com.sakuragame.eternal.justattribute.listener;

import com.mengcraft.playersql.event.PlayerDataProcessedEvent;
import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.AttributeManager;
import com.sakuragame.eternal.justattribute.util.RoleInitSync;
import net.sakuragame.eternal.dragoncore.api.event.PlayerSlotLoadedEvent;
import net.sakuragame.eternal.justlevel.api.event.sub.JLPlayerInitFinishedEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.UUID;

public class PlayerListener implements Listener {

    private final HashMap<UUID, RoleInitSync> sync = new HashMap<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        RoleInitSync roleInitSync = new RoleInitSync();
        sync.put(player.getUniqueId(), roleInitSync);

        AttributeManager.loading.add(player.getUniqueId());
    }

    @EventHandler
    public void onJLInit(JLPlayerInitFinishedEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        RoleInitSync initSync = sync.get(uuid);
        if (initSync == null) return;

        initSync.setJustLevel(true);
        if (initSync.isFinished()) {
            sync.remove(uuid);
            JustAttribute.getRoleManager().loadAttributeData(player);
        }
    }

    @EventHandler
    public void onSlotLoaded(PlayerSlotLoadedEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        RoleInitSync initSync = sync.get(uuid);
        if (initSync == null) return;

        initSync.setDragonSlot(true);
        if (initSync.isFinished()) {
            sync.remove(uuid);
            JustAttribute.getRoleManager().loadAttributeData(player);
        }
    }

    @EventHandler
    public void onDataProcessed(PlayerDataProcessedEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        RoleInitSync initSync = sync.get(uuid);
        if (initSync == null) return;

        initSync.setInventory(true);
        if (initSync.isFinished()) {
            sync.remove(uuid);
            JustAttribute.getRoleManager().loadAttributeData(player);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        JustAttribute.getRoleManager().removeAttributeData(uuid);
        JustAttribute.getRoleManager().removeStateData(uuid);
        sync.remove(uuid);
        AttributeManager.loading.remove(uuid);
    }
}
