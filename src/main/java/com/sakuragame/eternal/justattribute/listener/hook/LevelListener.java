package com.sakuragame.eternal.justattribute.listener.hook;

import com.sakuragame.eternal.justattribute.api.JustAttributeAPI;
import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleAttribute;
import com.sakuragame.eternal.justattribute.util.Utils;
import net.sakuragame.eternal.justlevel.api.event.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class LevelListener implements Listener {

    @EventHandler
    public void onLevelChange(PlayerLevelChangeEvent e) {
        Player player = e.getPlayer();
        RoleAttribute role = JustAttributeAPI.getRoleAttribute(player);
        role.updateLevelPromote();
        role.update(true);
    }

    @EventHandler
    public void onStageChange(PlayerStageChangeEvent e) {
        Player player = e.getPlayer();
        this.updateChange(player);
    }

    @EventHandler
    public void onRealChange(PlayerRealmChangeEvent e) {
        Player player = e.getPlayer();
        this.updateChange(player);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onStageBreak(PlayerBrokenEvent.Stage e) {
        Player player = e.getPlayer();

        RoleAttribute role = JustAttributeAPI.getRoleAttribute(player);
        role.updateDamageUpperLimit();
        role.update();
        int change = role.getChange();
        e.addMessage((change > 0 ? "&e&l+" : "&c&l-") + Math.abs(change) + "战斗力");
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onRealmBreak(PlayerBrokenEvent.Realm e) {
        Player player = e.getPlayer();

        RoleAttribute role = JustAttributeAPI.getRoleAttribute(player);
        role.updateDamageUpperLimit();
        role.update();
        int change = role.getChange();
        e.addMessage((change > 0 ? "&e&l+" : "&c&l-") + Math.abs(change) + "战斗力");
    }

    private void updateChange(Player player) {
        RoleAttribute role = JustAttributeAPI.getRoleAttribute(player);
        role.updateDamageUpperLimit();
        role.update();
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
