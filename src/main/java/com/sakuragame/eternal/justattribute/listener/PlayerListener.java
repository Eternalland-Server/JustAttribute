package com.sakuragame.eternal.justattribute.listener;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.RoleManager;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleState;
import com.sakuragame.eternal.justattribute.util.Load;
import net.sakuragame.eternal.dragoncore.api.event.PlayerSlotLoadedEvent;
import net.sakuragame.eternal.justlevel.api.event.PlayerDataLoadEvent;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;

import java.util.UUID;

public class PlayerListener implements Listener {

    private final JustAttribute plugin = JustAttribute.getInstance();

    @EventHandler(priority = EventPriority.HIGH)
    public void onPreLogin(AsyncPlayerPreLoginEvent e) {
        if (e.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            return;
        }
        UUID uuid = e.getUniqueId();
        JustAttribute.getRoleManager().loadStateData(uuid);
        RoleManager.addLoad(uuid);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPreLoginMonitor(AsyncPlayerPreLoginEvent e) {
        if (e.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            JustAttribute.getRoleManager().clearData(e.getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLogin(PlayerLoginEvent e) {
        Player player = e.getPlayer();
        RoleState state = JustAttribute.getRoleManager().getPlayerState(player);

        if (state == null) {
            e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            e.setKickMessage("账户未被正确加载，请重新进入。");
            plugin.getLogger().info("玩家 " + player.getName() + " 账户数据载入失败!");
            return;
        }

        PlayerDataLoadEvent event = new PlayerDataLoadEvent(player);
        event.call();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        player.setHealthScale(20);
        player.setHealthScaled(true);
        player.setFoodLevel(20);

        if (!player.isOp()) return;
        player.setGameMode(GameMode.ADVENTURE);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
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

        Load load = RoleManager.getLoad(uuid);
        if (load == null) return;

        load.setJustLevel(true);
        if (load.isFinished()) {
            JustAttribute.getRoleManager().loadAttributeData(player);
        }
    }

    @EventHandler
    public void onSlotLoaded(PlayerSlotLoadedEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        Load load = RoleManager.getLoad(uuid);
        if (load == null) return;

        load.setDragonSlot(true);
        if (load.isFinished()) {
            JustAttribute.getRoleManager().loadAttributeData(player);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        JustAttribute.getRoleManager().clearData(uuid);
    }

    @EventHandler
    public void onFood(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }
}
