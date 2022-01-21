package com.sakuragame.eternal.justattribute.core;

import com.sakuragame.eternal.justattribute.api.JustAttributeAPI;
import com.sakuragame.eternal.justattribute.core.attribute.AttributeSource;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleAttribute;
import com.sakuragame.eternal.justattribute.core.special.EquipClassify;
import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import net.sakuragame.eternal.dragoncore.api.SlotAPI;
import net.sakuragame.eternal.dragoncore.config.FileManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AttributeHandler {

    public static void loadVanillaSlot(Player player) {
        RoleAttribute role = JustAttributeAPI.getRoleAttribute(player);
        if (role == null) return;

        for (VanillaSlot slot : VanillaSlot.values()) {
            ItemStack item = slot.getItem(player);
            if (MegumiUtil.isEmpty(item)) {
                item = new ItemStack(Material.AIR);
            }
            role.addAttributeSource(slot.getIdent(), new AttributeSource(item));
        }
    }

    public static void loadCustomSlot(Player player) {
        RoleAttribute role = JustAttributeAPI.getRoleAttribute(player);
        if (role == null) return;

        for (String key : ConfigFile.slotSetting.keySet()) {
            if (!FileManager.getSlotSettings().containsKey(key)) continue;

            int id = ConfigFile.slotSetting.get(key);
            EquipClassify classify = EquipClassify.getType(id);
            if (classify == null) continue;

            ItemStack item = SlotAPI.getCacheSlotItem(player, key);

            updateSlot(player, key, item);
        }
    }

    public static void updateVanillaSlot(Player player, VanillaSlot slot) {
        ItemStack item = slot.getItem(player);
        updateSlot(player, slot.getIdent(), item);
    }

    public static void updateVanillaSlot(Player player, VanillaSlot slot, ItemStack item) {
        updateSlot(player, slot.getIdent(), item);
    }

    public static void updateMainHandSlot(Player player, int slot) {
        ItemStack item = player.getInventory().getItem(slot);
        updateSlot(player, VanillaSlot.MainHand.getIdent(), item);
    }

    public static void updateCustomSlot(Player player, String ident, ItemStack item) {
        updateSlot(player, ident, item);
    }

    private static void updateSlot(Player player, String ident, ItemStack item) {
        RoleAttribute role = JustAttributeAPI.getRoleAttribute(player);
        if (role == null) return;

        if (MegumiUtil.isEmpty(item)) {
            item = new ItemStack(Material.AIR);
        }

        role.addAttributeSource(ident, new AttributeSource(item.clone()));
        role.updateRoleAttribute();
    }
}
