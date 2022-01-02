package com.sakuragame.eternal.justattribute.listener.hook;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleAttribute;
import com.sakuragame.eternal.justattribute.util.Scheduler;
import net.sakuragame.eternal.justlevel.api.event.sub.JLPlayerRealmChangeEvent;
import net.sakuragame.eternal.justlevel.api.event.sub.JLPlayerStageChangeEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class LevelListener implements Listener {

    @EventHandler
    public void onStageChange(JLPlayerStageChangeEvent e) {
        Player player = e.getPlayer();
        updateChange(player);
    }

    @EventHandler
    public void onRealChange(JLPlayerRealmChangeEvent e) {
        Player player = e.getPlayer();
        updateChange(player);
    }

    private void updateChange(Player player) {
        Scheduler.runAsync(() -> {
            RoleAttribute role = JustAttribute.getRoleManager().getPlayerAttribute(player.getUniqueId());
            role.updateStageGrowth();
            role.updateRoleAttribute();
        });
    }
}
