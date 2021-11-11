package com.sakuragame.eternal.justattribute.listener;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.api.event.JARoleInitFinishedEvent;
import com.sakuragame.eternal.justattribute.api.event.JAUpdateAttributeEvent;
import com.sakuragame.eternal.justattribute.core.attribute.VanillaSlot;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleAttribute;
import net.sakuragame.eternal.justlevel.event.JustPlayerStageChangeEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

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

        JustAttribute.getRoleManager().removeAttributeData(player.getUniqueId());
    }

    @EventHandler
    public void onSwapHand(PlayerSwapHandItemsEvent e) {
        Player player = e.getPlayer();

        JustAttribute.getRoleManager().updateVanillaSlot(player, VanillaSlot.MainHand);
        JustAttribute.getRoleManager().updateVanillaSlot(player, VanillaSlot.OffHand);
    }

    @EventHandler
    public void onRoleInit(JARoleInitFinishedEvent e) {
        Player player = e.getPlayer();
        RoleAttribute role = e.getAttribute();

    }

    @EventHandler
    public void onUpdateAttribute(JAUpdateAttributeEvent e) {
        Player player = e.getPlayer();
        JustAttribute.getRoleManager().getPlayerState(player.getUniqueId()).update();
    }

    @EventHandler
    public void onStageChange(JustPlayerStageChangeEvent e) {
        Player player = e.getPlayer();
        RoleAttribute role = JustAttribute.getRoleManager().getPlayerAttribute(player.getUniqueId());
        role.updateStageGrowth();
        role.updateRoleAttribute();
    }
}
