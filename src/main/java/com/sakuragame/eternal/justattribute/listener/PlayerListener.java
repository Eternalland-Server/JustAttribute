package com.sakuragame.eternal.justattribute.listener;

import com.sakuragame.eternal.justattribute.JustAttribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerListener implements Listener {

    private final JustAttribute plugin = JustAttribute.getInstance();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        JustAttribute.getRoleManager().loadAttributeData(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        JustAttribute.getRoleManager().removeAttributeData(uuid);
        JustAttribute.getRoleManager().removeStateData(uuid);
    }
}
