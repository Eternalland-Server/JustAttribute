package com.sakuragame.eternal.justattribute.listener;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.RoleManager;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleAttribute;
import com.sakuragame.eternal.justattribute.util.Loader;
import com.sakuragame.eternal.justattribute.util.Scheduler;
import net.sakuragame.eternal.dragoncore.api.event.PlayerSlotLoadedEvent;
import net.sakuragame.eternal.justlevel.api.event.PlayerDataLoadEvent;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPreLogin(AsyncPlayerPreLoginEvent e) {
        UUID uuid = e.getUniqueId();
        JustAttribute.getRoleManager().putAttributeData(uuid, new RoleAttribute(uuid));
        RoleManager.generateLoader(uuid);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onLogin(AsyncPlayerPreLoginEvent e) {
        if (e.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED) return;
        UUID uuid = e.getUniqueId();
        RoleManager.removeLoader(uuid);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        Scheduler.runLaterAsync(() -> JustAttribute.getRoleManager().loadStateData(uuid), 6);

        player.setHealthScale(20);
        player.setHealthScaled(true);
        player.setFoodLevel(20);

        if (!player.isOp()) return;
        player.setGameMode(GameMode.ADVENTURE);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onGameModeChange(PlayerGameModeChangeEvent e) {
        Player player = e.getPlayer();
        if (player.isOp()) return;
        if (e.getNewGameMode() == GameMode.ADVENTURE || e.getNewGameMode() == GameMode.SPECTATOR) return;

        e.setCancelled(true);
    }

    @EventHandler
    public void onLevel(PlayerDataLoadEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        Loader loader = RoleManager.getLoader(uuid);

        loader.setJustLevel(true);
        loader.tryExecute();
    }

    @EventHandler
    public void onSlotLoaded(PlayerSlotLoadedEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        Loader loader = RoleManager.getLoader(uuid);

        loader.setDragonSlot(true);
        loader.tryExecute();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        Scheduler.cancel(uuid);
        Scheduler.runAsync(() -> JustAttribute.getRoleManager().saveData(uuid));
    }

    @EventHandler
    public void onFood(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }
}
