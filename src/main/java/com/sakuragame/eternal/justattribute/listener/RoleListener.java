package com.sakuragame.eternal.justattribute.listener;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.api.event.role.RoleAttributeLoadedEvent;
import com.sakuragame.eternal.justattribute.api.event.role.RoleStateLoadedEvent;
import com.sakuragame.eternal.justattribute.api.event.role.RoleAttributeUpdateEvent;
import com.sakuragame.eternal.justattribute.api.event.role.RoleStateUpdateEvent;
import com.sakuragame.eternal.justattribute.core.AttributeManager;
import com.sakuragame.eternal.justattribute.core.attribute.VanillaSlot;
import com.sakuragame.eternal.justattribute.hook.DragonCoreSync;
import com.sakuragame.eternal.justattribute.util.Scheduler;
import com.sakuragame.eternal.justattribute.util.Utils;
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

    private final JustAttribute plugin = JustAttribute.getInstance();

    @EventHandler
    public void onAttributeLoaded(RoleAttributeLoadedEvent e) {
        Player player = e.getPlayer();
        JustAttribute.getRoleManager().loadStateData(player);
    }

    @EventHandler
    public void onStateLoaded(RoleStateLoadedEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        AttributeManager.loading.remove(uuid);

        Scheduler.runAsync(() -> JustAttribute.getRoleManager().getPlayerAttribute(uuid).updateRoleAttribute());
    }

    @EventHandler
    public void onAttributeUpdate(RoleAttributeUpdateEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        if (AttributeManager.loading.contains(uuid)) return;

        JustAttribute.getRoleManager().getPlayerState(uuid).update();
        DragonCoreSync.sendAttribute(player);
    }

    @EventHandler
    public void onStateUpdate(RoleStateUpdateEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        if (AttributeManager.loading.contains(uuid)) return;

        DragonCoreSync.sendState(player);
    }

    @EventHandler
    public void onSwapHand(PlayerSwapHandItemsEvent e) {
        e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onHold(PlayerItemHeldEvent e) {
        Player player = e.getPlayer();
        if (AttributeManager.loading.contains(player.getUniqueId())) return;

        JustAttribute.getRoleManager().updateMainHandSlot(player, e.getNewSlot());
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent e) {
        if (!(e.getEntity() instanceof Player)) return;

        Player player = (Player) e.getEntity();

        JustAttribute.getRoleManager().updateVanillaSlot(player, VanillaSlot.MainHand);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (!(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) return;

        ItemStack item = e.getItem();
        if (MegumiUtil.isEmpty(item)) return;
        if (!Utils.isArmor(item.getType())) return;

        JustAttribute.getRoleManager().updateVanillaSlot(player, VanillaSlot.getSlot(item.getType()));
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent e) {
        Entity entity = e.getEntity();
        if (!(entity instanceof Player)) return;
        if (!e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) return;

        e.setCancelled(true);
    }
}
