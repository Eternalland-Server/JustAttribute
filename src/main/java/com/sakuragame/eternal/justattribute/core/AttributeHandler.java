package com.sakuragame.eternal.justattribute.core;

import com.sakuragame.eternal.justattribute.api.JustAttributeAPI;
import com.sakuragame.eternal.justattribute.core.attribute.AttributeSource;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleAttribute;
import com.sakuragame.eternal.justattribute.core.special.EquipClassify;
import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import net.sakuragame.eternal.dragoncore.api.SlotAPI;
import net.sakuragame.eternal.dragoncore.config.FileManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AttributeHandler {

    public static void loadVanillaSlot(Player player) {
        RoleAttribute role = JustAttributeAPI.getRoleAttribute(player);
        if (role == null) return;

        for (VanillaSlot slot : VanillaSlot.values()) {
            ItemStack item = slot.getItem(player);
            if (MegumiUtil.isEmpty(item)) continue;

            role.putSource(slot.getIdent(), item);
        }
    }

    public static void loadCustomSlot(Player player) {
        RoleAttribute role = JustAttributeAPI.getRoleAttribute(player);
        if (role == null) return;

        for (String key : ConfigFile.slotSetting.keySet()) {
            if (!FileManager.getSlotSettings().containsKey(key)) continue;

            ItemStack item = SlotAPI.getCacheSlotItem(player, key);
            if (MegumiUtil.isEmpty(item)) continue;

            role.putSource(key, item);
        }
    }

    public static void updateVanillaSlot(Player player, VanillaSlot slot) {
        ItemStack item = slot.getItem(player);
        updateSlot(player, slot, item);
    }

    public static void updateVanillaSlot(Player player, VanillaSlot slot, ItemStack item) {
        updateSlot(player, slot, item);
    }

    public static void updateMainHandSlot(Player player, int slot) {
        ItemStack item = player.getInventory().getItem(slot);
        updateSlot(player, VanillaSlot.MainHand.getIdent(), VanillaSlot.MainHand.getType(), item);
    }

    public static void updateCustomSlot(Player player, String ident, ItemStack item) {
        EquipClassify classify = EquipClassify.match(ConfigFile.slotSetting.get(ident));
        updateSlot(player, ident, classify, item);
    }

    public static void updateSlot(Player player, VanillaSlot slot, ItemStack item) {
        updateSlot(player, slot.getIdent(), slot.getType(), item);
    }

    private static void updateSlot(Player player, String ident, EquipClassify classify, ItemStack item) {
        RoleAttribute role = JustAttributeAPI.getRoleAttribute(player);
        if (role == null) return;

        if (MegumiUtil.isEmpty(item)) {
            role.putImmediateSource(ident, new AttributeSource());
        }
        else {
            role.putImmediateSource(ident, AttributeSource.getItemAttribute(item, classify));
        }
    }
}
