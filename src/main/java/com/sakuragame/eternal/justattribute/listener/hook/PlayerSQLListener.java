package com.sakuragame.eternal.justattribute.listener.hook;

import com.mengcraft.playersql.event.PlayerDataProcessedEvent;
import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.util.RoleSync;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class PlayerSQLListener implements Listener {

    @EventHandler
    public void onDataProcessed(PlayerDataProcessedEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        RoleSync initSync = JustAttribute.getRoleManager().getSync().get(uuid);
        if (initSync == null) return;

        initSync.setInventory(true);
        if (initSync.isFinished()) {
            JustAttribute.getRoleManager().getSync().remove(uuid);
            JustAttribute.getRoleManager().loadAttributeData(player);
        }
    }
}
