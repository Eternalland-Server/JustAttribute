package com.sakuragame.eternal.justattribute.core;

import com.sakuragame.eternal.justattribute.api.JustAttributeAPI;
import com.sakuragame.eternal.justattribute.core.attribute.AttributeSource;
import com.sakuragame.eternal.justattribute.core.attribute.character.PlayerCharacter;
import com.sakuragame.eternal.justattribute.core.special.EquipClassify;
import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import com.sakuragame.eternal.justattribute.util.Utils;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import net.sakuragame.eternal.dragoncore.api.SlotAPI;
import net.sakuragame.eternal.dragoncore.config.FileManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AttributeHandler {

    public static void loadVanillaSlot(Player player) {
        PlayerCharacter role = JustAttributeAPI.getRoleCharacter(player);
        if (role == null) return;

        for (VanillaSlot slot : VanillaSlot.values()) {
            ItemStack item = slot.getItem(player);
            if (MegumiUtil.isEmpty(item)) continue;

            role.putAttributeSource(slot.getIdent(), new AttributeSource(item));
        }
    }

    public static void loadCustomSlot(Player player) {
        PlayerCharacter role = JustAttributeAPI.getRoleCharacter(player);
        if (role == null) return;

        for (String key : ConfigFile.slotSetting.keySet()) {
            if (!FileManager.getSlotSettings().containsKey(key)) continue;

            ItemStack item = SlotAPI.getCacheSlotItem(player, key);
            if (MegumiUtil.isEmpty(item)) continue;

            role.putAttributeSource(key, new AttributeSource(item));
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
        PlayerCharacter role = JustAttributeAPI.getRoleCharacter(player);
        if (role == null) return;

        AttributeSource source = AttributeSource.getItemAttribute(item, classify);
        if (source == null) {
            role.removeAttributeSource(ident);
        }
        else {
            role.putAttributeSource(ident, source, true);
        }

        if (classify == EquipClassify.MainHand) return;
        int change = role.getCombatLastTimeChange();
        Utils.sendCombatChange(player, change);
    }
}
