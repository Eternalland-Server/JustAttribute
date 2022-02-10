package com.sakuragame.eternal.justattribute.listener.hook;

import com.sakuragame.eternal.justattribute.api.JustAttributeAPI;
import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleAttribute;
import net.sakuragame.eternal.justlevel.api.JustLevelAPI;
import net.sakuragame.eternal.justlevel.api.event.PlayerExpIncreaseEvent;
import net.sakuragame.eternal.justlevel.api.event.PlayerRealmChangeEvent;
import net.sakuragame.eternal.justlevel.api.event.PlayerStageChangeEvent;
import net.sakuragame.eternal.justlevel.util.Utils;
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
        RoleAttribute role = JustAttributeAPI.getRoleAttribute(player);
        role.updateStageGrowth();
        role.updateRoleAttribute();
    }

    @EventHandler
    public void onExpIncrease(PlayerExpIncreaseEvent.Pre e) {
        Player player = e.getPlayer();
        double amount = e.getAmount();
        double rate = JustAttributeAPI.getRoleAttributeValue(player, Attribute.EXP_Addition);

        double value = amount * rate;
        e.addAddition(value);
    }
}
