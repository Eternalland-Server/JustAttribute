package com.sakuragame.eternal.justattribute.listener.user;

import com.sakuragame.eternal.justattribute.core.AttributeHandler;
import com.sakuragame.eternal.justattribute.core.PetHandler;
import com.sakuragame.eternal.justattribute.core.VanillaSlot;
import com.sakuragame.eternal.justattribute.core.soulbound.Owner;
import com.sakuragame.eternal.justattribute.core.soulbound.SoulBound;
import com.sakuragame.eternal.justattribute.core.special.EquipClassify;
import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import com.sakuragame.eternal.justattribute.util.ItemUtil;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.api.ItemStream;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import net.sakuragame.eternal.dragoncore.api.SlotAPI;
import net.sakuragame.eternal.dragoncore.api.event.PlayerSlotHandleEvent;
import net.sakuragame.eternal.dragoncore.api.event.PlayerSlotUpdateEvent;
import net.sakuragame.eternal.justmessage.api.MessageAPI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SlotListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onVanilla(InventoryClickEvent e) {
        if (e.isCancelled()) return;
        if (!(e.getWhoClicked() instanceof Player)) return;

        Player player = (Player) e.getWhoClicked();
        Inventory gui = e.getInventory();
        if (!(gui.getType() == InventoryType.PLAYER || gui.getType() == InventoryType.CRAFTING)) return;

        int index = e.getRawSlot();
        if (index == -1) return;

        VanillaSlot slot = player.getInventory().getHeldItemSlot() == e.getSlot() ? VanillaSlot.MainHand : VanillaSlot.getSlot(index);
        ItemStack switchItem = e.getCursor();

        if (slot == null) {
            ItemStack item = e.getCurrentItem();
            if (MegumiUtil.isEmpty(item)) return;
            Material type = item.getType();
            InventoryAction action = e.getAction();
            if (action != InventoryAction.MOVE_TO_OTHER_INVENTORY) return;
            if (!(ItemUtil.isArmor(type) || ItemUtil.isShield(type))) return;

            VanillaSlot itemSlot = VanillaSlot.getSlot(type);
            if (itemSlot == null) return;
            if (!MegumiUtil.isEmpty(itemSlot.getItem(player))) return;

            slot = itemSlot;
            switchItem = item;
        }

        if (!MegumiUtil.isEmpty(switchItem) && slot != VanillaSlot.MainHand) {
            ItemStream itemStream = ZaphkielAPI.INSTANCE.read(switchItem);
            if (itemStream.isVanilla()) return;
            ItemTag itemTag = itemStream.getZaphkielData();

            EquipClassify classify = EquipClassify.getClassify(itemTag);
            if (classify == null || classify != slot.getType()) {
                MessageAPI.sendActionTip(player, "??a??l????????????????????? ??c??l" + slot.getType().getName() + " ??a??l???????????????");
                e.setCancelled(true);
                return;
            }

            if (SoulBound.isSeal(itemTag)) {
                MessageAPI.sendActionTip(player, "&a&l?????????????????????????????????");
                e.setCancelled(true);
                return;
            }

            Owner owner = SoulBound.getOwner(itemTag);
            if (owner != null && !player.getUniqueId().equals(owner.getUUID())) {
                MessageAPI.sendActionTip(player, "??c??l?????????????????????????????????");
                e.setCancelled(true);
            }
        }

        if (MegumiUtil.isEmpty(switchItem)) {
            AttributeHandler.updateVanillaSlot(player, slot, new ItemStack(Material.AIR));
        }
        else {
            AttributeHandler.updateVanillaSlot(player, slot, switchItem);
        }
    }

    @EventHandler
    public void onCustom(PlayerSlotUpdateEvent e) {
        Player player = e.getPlayer();
        String ident = e.getIdentifier();
        ItemStack item = e.getItemStack();

        if (!ConfigFile.slotSetting.containsKey(ident)) return;
        int id = ConfigFile.slotSetting.get(ident);
        if (id > 21 && id < 26) return;

        AttributeHandler.updateCustomSlot(player, ident, item);
    }

    @EventHandler
    public void onSlotHandle(PlayerSlotHandleEvent e) {
        Player player = e.getPlayer();
        String ident = e.getIdentifier();
        ItemStack item = e.getHandItem();

        Integer id = ConfigFile.slotSetting.get(ident);
        if (id == null) return;

        if (MegumiUtil.isEmpty(item)) return;

        EquipClassify slotClassify = EquipClassify.match(id);
        if (slotClassify == null) return;

        ItemStream itemStream = ZaphkielAPI.INSTANCE.read(item);
        if (itemStream.isVanilla()) {
            MessageAPI.sendActionTip(player, "??a??l??????????????????????????????");
            e.setCancelled(true);
            return;
        }
        ItemTag itemTag = itemStream.getZaphkielData();

        EquipClassify classify = EquipClassify.getClassify(itemTag);
        if (classify == null || classify != slotClassify) {
            MessageAPI.sendActionTip(player, "??a??l????????????????????? ??c??l" + slotClassify.getName() + " ??a??l???????????????");
            e.setCancelled(true);
            return;
        }

        if (SoulBound.isSeal(item)) {
            MessageAPI.sendActionTip(player, "&a&l??????????????????????????????????????????");
            e.setCancelled(true);
            return;
        }

        Owner owner = SoulBound.getOwner(itemTag);
        if (owner != null && !player.getUniqueId().equals(owner.getUUID())) {
            MessageAPI.sendActionTip(player, "??c??l?????????????????????????????????");
            e.setCancelled(true);
        }
//        else {
//            Action action = SoulBound.getAction(itemTag);
//            if (action == null) return;
//
//            if (action == Action.USE || action == Action.USE_LOCK) {
//                e.setHandItem(SoulBound.binding(player, itemStream, action));
//            }
//        }

        if (!(id > 21 && id < 26)) return;
        ItemStack petEgg = SlotAPI.getCacheSlotItem(player, PetHandler.EGG_SLOT);
        if (petEgg == null) {
            MessageAPI.sendActionTip(player, "&c&l????????????????????????");
            e.setCancelled(true);
        }
    }

//    @EventHandler(priority = EventPriority.HIGH)
//    public void onInteract(PlayerInteractEvent e) {
//        Player player = e.getPlayer();
//        ItemStack item = player.getInventory().getItemInMainHand();
//
//        if (MegumiUtil.isEmpty(item)) return;
//
//        ItemStream itemStream = ZaphkielAPI.INSTANCE.read(item);
//        if (itemStream.isVanilla()) return;
//
//        Action action = SoulBound.getAction(itemStream.getZaphkielData());
//        if (action == null) return;
//
//        if (action == Action.USE || action == Action.USE_LOCK) {
//            player.getInventory().setItemInMainHand(SoulBound.binding(player, itemStream, action));
//            AttributeHandler.updateVanillaSlot(player, VanillaSlot.MainHand);
//        }
//    }

//    @EventHandler(priority = EventPriority.HIGH)
//    public void onSwap(PlayerSwapHandItemsEvent e) {
//        if (e.isCancelled()) return;
//
//        Player player = e.getPlayer();
//        ItemStack item = e.getOffHandItem();
//
//        if (MegumiUtil.isEmpty(item)) return;
//
//        ItemStream itemStream = ZaphkielAPI.INSTANCE.read(item);
//        if (itemStream.isVanilla()) return;
//
//        Action action = SoulBound.getAction(itemStream.getZaphkielData());
//        if (action == null) return;
//
//        if (action == Action.USE || action == Action.USE_LOCK) {
//            player.getInventory().setItemInMainHand(SoulBound.binding(player, itemStream, action));
//            AttributeHandler.updateVanillaSlot(player, VanillaSlot.OffHand);
//        }
//    }
}
