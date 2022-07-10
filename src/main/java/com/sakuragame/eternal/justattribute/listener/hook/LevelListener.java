package com.sakuragame.eternal.justattribute.listener.hook;

import com.sakuragame.eternal.justattribute.api.JustAttributeAPI;
import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import com.sakuragame.eternal.justattribute.core.attribute.character.PlayerCharacter;
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
        PlayerCharacter role = JustAttributeAPI.getRoleCharacter(player);
        role.updateLevelAddition();
        role.update();

        int change = role.getCombatLastTimeChange();
        Utils.sendCombatChange(player, change);
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

        PlayerCharacter role = JustAttributeAPI.getRoleCharacter(player);
        role.update();

        int change = role.getCombatLastTimeChange();
        e.addMessage("&a&l战斗力提升➚&8&l[ " + (change > 0 ? "&e&l+" : "&c&l-") + Math.abs(change) + " &8&l]");
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onRealmBreak(PlayerBrokenEvent.Realm e) {
        Player player = e.getPlayer();

        PlayerCharacter role = JustAttributeAPI.getRoleCharacter(player);
        role.update();

        int change = role.getCombatLastTimeChange();
        e.addMessage("&a&l战斗力提升➚&8&l[ " + (change > 0 ? "&e&l+" : "&c&l-") + Math.abs(change) + " &8&l]");
    }

    private void updateChange(Player player) {
        PlayerCharacter role = JustAttributeAPI.getRoleCharacter(player);
        role.update();
    }

    @EventHandler
    public void onExpIncrease(PlayerExpIncreaseEvent.Pre e) {
        Player player = e.getPlayer();
        double amount = e.getAmount();
        double rate = JustAttributeAPI.getRoleCharacter(player).getAttributeValue(Attribute.EXP_Addition);

        double value = amount * rate;
        e.addAddition(value);
    }
}
