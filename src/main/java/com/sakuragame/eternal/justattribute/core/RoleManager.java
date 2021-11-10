package com.sakuragame.eternal.justattribute.core;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.VanillaSlot;
import com.sakuragame.eternal.justattribute.core.codition.EquipType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class RoleManager {

    private final JustAttribute plugin;

    private final HashMap<UUID, RoleAttribute> playerAttribute;

    public RoleManager(JustAttribute plugin) {
        this.plugin = plugin;
        this.playerAttribute = new HashMap<>();
    }

    public RoleAttribute getPlayerAttribute(UUID uuid) {
        return playerAttribute.get(uuid);
    }

    public void loadAttributeData(Player player) {
        this.playerAttribute.put(player.getUniqueId(), new RoleAttribute(player));
    }

    public void removeAttributeData(Player player) {
        this.playerAttribute.remove(player.getUniqueId());
    }

    public void updateVanillaSlot(Player player, VanillaSlot slot) {
        playerAttribute.get(player.getUniqueId()).updateVanillaSlot(slot);
    }

    public void updateCustomSlot(Player player, String ident, EquipType type, ItemStack item) {
        playerAttribute.get(player.getUniqueId()).updateCustomSlot(ident, type, item);
    }
}
