package com.sakuragame.eternal.justattribute.listener.hook;

import com.sakuragame.eternal.justattribute.core.RoleManager;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleAttribute;
import com.sakuragame.eternal.justattribute.util.Scheduler;
import net.sakuragame.eternal.justlevel.api.event.PlayerRealmChangeEvent;
import net.sakuragame.eternal.justlevel.api.event.PlayerStageChangeEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class LevelListener implements Listener {

    @EventHandler
    public void onStageChange(PlayerStageChangeEvent e) {
        Player player = e.getPlayer();
        updateChange(player);
    }

    @EventHandler
    public void onRealChange(PlayerRealmChangeEvent e) {
        Player player = e.getPlayer();
        updateChange(player);
    }

    private void updateChange(Player player) {
        Scheduler.runAsync(() -> {
            RoleAttribute role = RoleManager.getPlayerAttribute(player.getUniqueId());
            role.updateStageGrowth();
            role.updateRoleAttribute();
        });
    }
}
