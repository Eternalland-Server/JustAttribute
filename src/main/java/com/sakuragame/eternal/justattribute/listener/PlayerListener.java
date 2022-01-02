package com.sakuragame.eternal.justattribute.listener;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.AttributeManager;
import com.sakuragame.eternal.justattribute.util.RoleSync;
import net.sakuragame.eternal.dragoncore.api.event.PlayerSlotLoadedEvent;
import net.sakuragame.eternal.dragoncore.api.event.YamlSendFinishedEvent;
import net.sakuragame.eternal.justlevel.api.event.sub.JLPlayerInitFinishedEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        player.setHealthScale(20);
        player.setHealthScaled(true);

        RoleSync roleSync = new RoleSync();
        AttributeManager.sync.put(player.getUniqueId(), roleSync);

        AttributeManager.loading.add(player.getUniqueId());
    }

    @EventHandler
    public void onJLInit(JLPlayerInitFinishedEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        RoleSync initSync = AttributeManager.sync.get(uuid);
        if (initSync == null) return;

        initSync.setJustLevel(true);
        if (initSync.isFinished()) {
            AttributeManager.sync.remove(uuid);
            JustAttribute.getRoleManager().loadAttributeData(player);
        }
    }

    @EventHandler
    public void onSlotLoaded(PlayerSlotLoadedEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        RoleSync initSync = AttributeManager.sync.get(uuid);
        if (initSync == null) return;

        initSync.setDragonSlot(true);
        if (initSync.isFinished()) {
            AttributeManager.sync.remove(uuid);
            JustAttribute.getRoleManager().loadAttributeData(player);
        }
    }

    @EventHandler
    public void onDCFinished(YamlSendFinishedEvent e) {
        Player player = e.getPlayer();
        player.setHealthScaled(false);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        JustAttribute.getRoleManager().removeAttributeData(player);
        JustAttribute.getRoleManager().removeStateData(player);
        AttributeManager.sync.remove(uuid);
        AttributeManager.loading.remove(uuid);
    }
}
