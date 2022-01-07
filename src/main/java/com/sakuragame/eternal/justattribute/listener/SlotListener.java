package com.sakuragame.eternal.justattribute.listener;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.VanillaSlot;
import com.sakuragame.eternal.justattribute.core.soulbound.Action;
import com.sakuragame.eternal.justattribute.core.soulbound.Owner;
import com.sakuragame.eternal.justattribute.core.special.EquipClassify;
import com.sakuragame.eternal.justattribute.core.soulbound.SoulBound;
import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import com.sakuragame.eternal.justattribute.util.Scheduler;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.api.ItemStream;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTagData;
import net.sakuragame.eternal.dragoncore.api.event.PlayerSlotHandleEvent;
import net.sakuragame.eternal.dragoncore.api.event.PlayerSlotUpdateEvent;
import net.sakuragame.eternal.justmessage.api.MessageAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SlotListener implements Listener {

    private final JustAttribute plugin = JustAttribute.getInstance();

    @EventHandler
    public void onVanilla(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;

        Player player = (Player) e.getWhoClicked();
        Inventory gui = e.getInventory();

        if (!(gui.getType() == InventoryType.PLAYER || gui.getType() == InventoryType.CRAFTING)) return;

        int index = e.getRawSlot();

        if (index == -1) return;

        VanillaSlot slot = player.getInventory().getHeldItemSlot() == e.getSlot() ? VanillaSlot.MainHand : VanillaSlot.getSlot(index);
        if (slot == null) return;

        ItemStack handItem = e.getCursor();
        if (!MegumiUtil.isEmpty(handItem) && slot != VanillaSlot.MainHand) {
            ItemStream itemStream = ZaphkielAPI.INSTANCE.read(handItem);
            if (itemStream.isVanilla()) return;
            ItemTag itemTag = itemStream.getZaphkielData();

            int itemType = itemTag.getDeepOrElse(EquipClassify.NBT_NODE, new ItemTagData(-1)).asInt();
            if (slot.getType().getId() != itemType) {
                MessageAPI.sendActionTip(player, "§a§l该槽位只能放入 §c§l" + slot.getType().getName() + " §a§l类型的装备");
                e.setCancelled(true);
                return;
            }

            String owner = itemTag.getDeepOrElse(SoulBound.NBT_UUID_NODE, new ItemTagData("")).asString();
            if (!owner.isEmpty() && !player.getUniqueId().toString().equals(owner)) {
                MessageAPI.sendActionTip(player, "§c§l你不是这件装备的所有者");
                e.setCancelled(true);
            }
        }

        Scheduler.runLaterAsync(() -> {
            ItemStack item = player.getInventory().getItem(index);

            if (!MegumiUtil.isEmpty(item) && slot != VanillaSlot.MainHand) {
                Action action = SoulBound.getAction(item);
                if (action == null) return;

                if (action == Action.USE || action == Action.USE_LOCK) {
                    player.getInventory().setItem(index, SoulBound.binding(player, item, action));
                }
            }

            JustAttribute.getRoleManager().updateVanillaSlot(player, slot);
        }, 3);
    }

    @EventHandler
    public void onCustom(PlayerSlotUpdateEvent e) {
        Player player = e.getPlayer();
        String ident = e.getIdentifier();
        ItemStack item = e.getItemStack();

        Integer id = ConfigFile.slotSetting.get(ident);
        if (id == null) return;

        EquipClassify type = EquipClassify.getType(id);
        if (type == null) return;

        JustAttribute.getRoleManager().updateCustomSlot(player, ident, type, item);
    }

    @EventHandler
    public void onHandle(PlayerSlotHandleEvent e) {
        Player player = e.getPlayer();
        String ident = e.getIdentifier();
        ItemStack item = e.getHandItem();

        if (MegumiUtil.isEmpty(item)) return;

        Integer id = ConfigFile.slotSetting.get(ident);
        if (id == null) return;

        EquipClassify classify = EquipClassify.getType(id);
        if (classify == null) return;

        ItemStream itemStream = ZaphkielAPI.INSTANCE.read(item);
        if (itemStream.isVanilla()) {
            MessageAPI.sendActionTip(player, "§a§l该槽位不能放入该物品");
            e.setCancelled(true);
            return;
        }
        ItemTag itemTag = itemStream.getZaphkielData();

        int itemType = itemTag.getDeepOrElse(EquipClassify.NBT_NODE, new ItemTagData(-1)).asInt();
        if (classify.getId() != itemType) {
            MessageAPI.sendActionTip(player, "§a§l该槽位只能放入 §c§l" + classify.getName() + " §a§l类型的装备");
            e.setCancelled(true);
            return;
        }

        Owner owner = SoulBound.getOwner(itemTag);
        if (owner != null && !player.getUniqueId().equals(owner.getUUID())) {
            MessageAPI.sendActionTip(player, "§c§l这件装备绑定的是其他人");
            e.setCancelled(true);
        }
        else {
            Action action = SoulBound.getAction(itemTag);
            if (action == null) return;

            if (action == Action.USE || action == Action.USE_LOCK) {
                e.setHandItem(SoulBound.binding(player, item, action));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (MegumiUtil.isEmpty(item)) return;

        Action action = SoulBound.getAction(item);
        if (action == null) return;

        if (action == Action.USE || action == Action.USE_LOCK) {
            player.getInventory().setItemInMainHand(SoulBound.binding(player, item, action));
            JustAttribute.getRoleManager().updateVanillaSlot(player, VanillaSlot.MainHand);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onSwap(PlayerSwapHandItemsEvent e) {
        if (e.isCancelled()) return;

        Player player = e.getPlayer();
        ItemStack item = e.getOffHandItem();

        if (MegumiUtil.isEmpty(item)) return;

        Action action = SoulBound.getAction(item);
        if (action == null) return;

        if (action == Action.USE || action == Action.USE_LOCK) {
            player.getInventory().setItemInMainHand(SoulBound.binding(player, item, action));
            JustAttribute.getRoleManager().updateVanillaSlot(player, VanillaSlot.OffHand);
        }
    }
}
