package com.sakuragame.eternal.justattribute.listener;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.api.event.JARoleAttributeInitEvent;
import com.sakuragame.eternal.justattribute.api.event.JARoleStateInitEvent;
import com.sakuragame.eternal.justattribute.api.event.JAUpdateAttributeEvent;
import com.sakuragame.eternal.justattribute.core.AttributeManager;
import com.sakuragame.eternal.justattribute.core.attribute.VanillaSlot;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleAttribute;
import com.sakuragame.eternal.justattribute.hook.DragonCoreSync;
import com.sakuragame.eternal.justattribute.util.Utils;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import net.sakuragame.eternal.justlevel.api.event.sub.JLPlayerStageChangeEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
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
    public void onAttrInit(JARoleAttributeInitEvent e) {
        Player player = e.getPlayer();
        JustAttribute.getRoleManager().loadStateData(player);
    }

    @EventHandler
    public void onStateInit(JARoleStateInitEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        AttributeManager.loading.remove(uuid);
        JustAttribute.getRoleManager().getPlayerAttribute(uuid).updateRoleAttribute();
    }

    @EventHandler
    public void onUpdate(JAUpdateAttributeEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        if (AttributeManager.loading.contains(uuid)) return;

        JustAttribute.getRoleManager().getPlayerState(uuid).update();
        DragonCoreSync.send(player);
    }

    @EventHandler
    public void onStageChange(JLPlayerStageChangeEvent e) {
        Player player = e.getPlayer();

        RoleAttribute role = JustAttribute.getRoleManager().getPlayerAttribute(player.getUniqueId());
        role.updateStageGrowth();
        role.updateRoleAttribute();
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
        Player player = e.getPlayer();

        JustAttribute.getRoleManager().updateVanillaSlot(player, VanillaSlot.MainHand);
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
}
