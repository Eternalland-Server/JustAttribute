package com.sakuragame.eternal.justattribute.listener;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.api.event.JARoleAttributeInitEvent;
import com.sakuragame.eternal.justattribute.api.event.JARoleStateInitEvent;
import com.sakuragame.eternal.justattribute.api.event.JAUpdateAttributeEvent;
import com.sakuragame.eternal.justattribute.core.attribute.VanillaSlot;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleAttribute;
import com.sakuragame.eternal.justattribute.util.RoleInitSync;
import com.sakuragame.eternal.justattribute.util.Utils;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import net.sakuragame.eternal.dragoncore.api.event.PlayerSlotLoadedEvent;
import net.sakuragame.eternal.justlevel.api.event.sub.JLPlayerInitFinishedEvent;
import net.sakuragame.eternal.justlevel.api.event.sub.JLPlayerStageChangeEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerListener implements Listener {

    private final JustAttribute plugin = JustAttribute.getInstance();
    private final HashMap<UUID, RoleInitSync> sync = new HashMap<>();
    private final List<UUID> backList = new ArrayList<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        RoleInitSync roleInitSync = new RoleInitSync();
        roleInitSync.setInventory(true);
        sync.put(player.getUniqueId(), roleInitSync);

        backList.add(player.getUniqueId());
    }

    @EventHandler
    public void onJLInit(JLPlayerInitFinishedEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        RoleInitSync initSync = sync.get(uuid);
        if (initSync == null) return;

        initSync.setJustLevel(true);

        if (initSync.isFinished()) {
            sync.remove(uuid);
            JustAttribute.getRoleManager().loadAttributeData(player);
        }
    }

    @EventHandler
    public void onSlotLoaded(PlayerSlotLoadedEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        RoleInitSync initSync = sync.get(uuid);
        if (initSync == null) return;

        initSync.setDragonSlot(true);

        if (initSync.isFinished()) {
            sync.remove(uuid);
            JustAttribute.getRoleManager().loadAttributeData(player);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        JustAttribute.getRoleManager().removeAttributeData(uuid);
        JustAttribute.getRoleManager().removeStateData(uuid);
        sync.remove(uuid);
        backList.remove(uuid);
    }

    @EventHandler
    public void onAttrInit(JARoleAttributeInitEvent e) {
        Player player = e.getPlayer();
        JustAttribute.getRoleManager().loadStateData(player);
    }

    @EventHandler
    public void onStateInit(JARoleStateInitEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        JustAttribute.getRoleManager().getPlayerState(uuid).update();
        backList.remove(uuid);
    }

    @EventHandler
    public void onAttrUpdate(JAUpdateAttributeEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        if (backList.contains(uuid)) return;

        JustAttribute.getRoleManager().getPlayerState(uuid).update();
    }

    @EventHandler
    public void onStageChange(JLPlayerStageChangeEvent e) {
        Player player = e.getPlayer();

        RoleAttribute role = JustAttribute.getRoleManager().getPlayerAttribute(player.getUniqueId());
        role.updateStageGrowth();
        role.updateRoleAttribute();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onSwapHand(PlayerSwapHandItemsEvent e) {
        e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onHold(PlayerItemHeldEvent e) {
        Player player = e.getPlayer();
        if (backList.contains(player.getUniqueId())) return;

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
