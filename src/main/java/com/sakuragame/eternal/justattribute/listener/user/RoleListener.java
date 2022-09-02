package com.sakuragame.eternal.justattribute.listener.user;

import com.sakuragame.eternal.justattribute.api.event.role.RoleAttributeUpdateEvent;
import com.sakuragame.eternal.justattribute.api.event.role.RoleStateUpdateEvent;
import com.sakuragame.eternal.justattribute.core.AttributeHandler;
import com.sakuragame.eternal.justattribute.core.RoleManager;
import com.sakuragame.eternal.justattribute.core.VanillaSlot;
import com.sakuragame.eternal.justattribute.hook.ClientPlaceholder;
import com.sakuragame.eternal.justattribute.util.ItemUtil;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class RoleListener implements Listener {

    @EventHandler
    public void onAttributeUpdate(RoleAttributeUpdateEvent e) {
        Player player = e.getPlayer();
        ClientPlaceholder.sendAttribute(player);
        ClientPlaceholder.sendState(player);
    }

    @EventHandler
    public void onStateUpdate(RoleStateUpdateEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        ClientPlaceholder.sendState(player);
    }

    @EventHandler
    public void onSwapHand(PlayerSwapHandItemsEvent e) {
        e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onHold(PlayerItemHeldEvent e) {
        Player player = e.getPlayer();
        if (RoleManager.isLoading(player.getUniqueId())) return;

        AttributeHandler.updateMainHandSlot(player, e.getNewSlot());
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent e) {
        if (!(e.getEntity() instanceof Player)) return;

        Player player = (Player) e.getEntity();
        if (!MegumiUtil.isEmpty(player.getInventory().getItemInMainHand())) return;

        AttributeHandler.updateVanillaSlot(player, VanillaSlot.MainHand);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (!(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) return;

        ItemStack item = e.getItem();
        if (MegumiUtil.isEmpty(item)) return;
        if (!ItemUtil.isArmor(item.getType())) return;

        VanillaSlot slot = VanillaSlot.getSlot(item.getType());
        if (slot == null) return;
        if (!MegumiUtil.isEmpty(slot.getItem(player))) return;

        AttributeHandler.updateVanillaSlot(player, slot, item);
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent e) {
        Entity entity = e.getEntity();
        if (!(entity instanceof Player)) return;
        if (!e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) return;

        e.setCancelled(true);
    }
}
