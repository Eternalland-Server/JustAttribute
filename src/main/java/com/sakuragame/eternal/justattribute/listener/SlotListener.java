package com.sakuragame.eternal.justattribute.listener;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.VanillaSlot;
import com.sakuragame.eternal.justattribute.core.codition.EquipType;
import com.sakuragame.eternal.justattribute.core.codition.Realm;
import com.sakuragame.eternal.justattribute.core.codition.SoulBound;
import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import com.sakuragame.eternal.justattribute.util.Utils;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.api.ItemStream;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTagData;
import net.sakuragame.eternal.dragoncore.api.event.PlayerSlotHandleEvent;
import net.sakuragame.eternal.dragoncore.api.event.PlayerSlotUpdateEvent;
import net.sakuragame.eternal.justlevel.api.JustLevelAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SlotListener implements Listener {

    private final JustAttribute plugin = JustAttribute.getInstance();

    @EventHandler
    public void onVanilla(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;

        Player player = (Player) e.getWhoClicked();
        Inventory gui = e.getClickedInventory();

        if (!(gui.getType() == InventoryType.PLAYER || gui.getType() == InventoryType.CRAFTING)) return;

        int index = e.getRawSlot();

        VanillaSlot slot = VanillaSlot.getSlot(index);
        if (slot == null) return;

        ItemStack handItem = e.getCursor();

        ItemStream itemStream = ZaphkielAPI.INSTANCE.read(handItem);
        if (itemStream.isVanilla()) return;
        ItemTag itemTag = itemStream.getZaphkielData();

        int itemType = itemTag.getDeepOrElse(EquipType.NBT_NODE, new ItemTagData(-1)).asInt();
        if (slot.getType().getId() != itemType) {
            e.setCancelled(true);
            return;
        }

        int realmLimit = itemTag.getDeepOrElse(Realm.NBT_NODE, new ItemTagData(-1)).asInt();
        if (realmLimit != -1 && JustLevelAPI.getRealm(player) < realmLimit) {
            e.setCancelled(true);
        }

        String owner = itemTag.getDeepOrElse(SoulBound.NBT_UUID_NODE, new ItemTagData("")).asString();
        if (!owner.isEmpty() && !player.getUniqueId().toString().equals(owner)) {
            e.setCancelled(true);
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            ItemStack item = player.getInventory().getItem(index);
            if (MegumiUtil.isEmpty(item)) return;

            if (Utils.isUseBind(item)) {
                player.getInventory().setItem(index, Utils.binding(player, item));
            }

            JustAttribute.getRoleManager().updateVanillaSlot(player, slot);
        }, 1);
    }

    @EventHandler
    public void onCustom(PlayerSlotUpdateEvent e) {
        Player player = e.getPlayer();
        String ident = e.getIdentifier();
        ItemStack item = e.getItemStack();

        Integer id = ConfigFile.slotSetting.get(ident);
        if (id == null) return;

        EquipType type = EquipType.getType(id);
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

        ItemStream itemStream = ZaphkielAPI.INSTANCE.read(item);
        if (itemStream.isVanilla()) {
            e.setCancelled(true);
            return;
        }
        ItemTag itemTag = itemStream.getZaphkielData();

        int itemType = itemTag.getDeepOrElse(EquipType.NBT_NODE, new ItemTagData(-1)).asInt();
        if (id != itemType) {
            e.setCancelled(true);
            return;
        }

        int realmLimit = itemTag.getDeepOrElse(Realm.NBT_NODE, new ItemTagData(-1)).asInt();
        if (realmLimit != -1 && JustLevelAPI.getRealm(player) < realmLimit) {
            e.setCancelled(true);
            return;
        }

        String owner = itemTag.getDeepOrElse(SoulBound.NBT_UUID_NODE, new ItemTagData("")).asString();
        if (!owner.isEmpty() && !player.getUniqueId().toString().equals(owner)) {
            e.setCancelled(true);
        }
        else {
            e.setHandItem(Utils.binding(player, item));
        }
    }
}
