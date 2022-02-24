package com.sakuragame.eternal.justattribute.listener.hook;

import com.mengcraft.playersql.event.PlayerDataProcessedEvent;
import com.sakuragame.eternal.justattribute.core.RoleManager;
import com.sakuragame.eternal.justattribute.util.Loader;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class StorageListener implements Listener {

    @EventHandler
    public void onDataProcessed(PlayerDataProcessedEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        Loader loader = RoleManager.getLoader(uuid);

        loader.setInventory(true);
        loader.tryExecute();
    }
}
