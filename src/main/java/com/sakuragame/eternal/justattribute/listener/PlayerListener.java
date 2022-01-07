package com.sakuragame.eternal.justattribute.listener;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.AttributeManager;
import com.sakuragame.eternal.justattribute.util.RoleSync;
import net.sakuragame.eternal.dragoncore.api.event.PlayerSlotLoadedEvent;
import net.sakuragame.eternal.justlevel.api.event.sub.JLPlayerInitFinishedEvent;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        player.setHealthScale(20);
        player.setHealthScaled(true);
        player.setFoodLevel(20);
        if (!player.isOp()) player.setGameMode(GameMode.ADVENTURE);

        RoleSync roleSync = new RoleSync();
        AttributeManager.sync.put(player.getUniqueId(), roleSync);

        AttributeManager.loading.add(player.getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onGameModeChange(PlayerGameModeChangeEvent e) {
        Player player = e.getPlayer();
        if (player.isOp()) return;
        if (e.getNewGameMode() == GameMode.ADVENTURE) return;

        e.setCancelled(true);
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
    public void onHeld(PlayerItemHeldEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        RoleSync initSync = AttributeManager.sync.get(uuid);
        if (initSync == null) return;

        initSync.setHeldEvent(true);
        if (initSync.isFinished()) {
            AttributeManager.sync.remove(uuid);
            JustAttribute.getRoleManager().loadAttributeData(player);
        }
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

    @EventHandler
    public void onFood(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }
}
