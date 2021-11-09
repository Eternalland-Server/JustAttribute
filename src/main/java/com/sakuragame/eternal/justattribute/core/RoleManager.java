package com.sakuragame.eternal.justattribute.core;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.VanillaSlot;
import com.sakuragame.eternal.justattribute.core.codition.EquipType;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class RoleManager {

    private final JustAttribute plugin;

    @Getter private final HashMap<UUID, RoleAttribute> role;

    public RoleManager(JustAttribute plugin) {
        this.plugin = plugin;
        this.role = new HashMap<>();
    }

    public void loadPlayerAttr(Player player) {
        this.role.put(player.getUniqueId(), new RoleAttribute(player));
    }

    public void updateVanillaSlot(Player player, VanillaSlot slot) {
        role.get(player.getUniqueId()).updateVanillaSlot(slot);
    }

    public void updateCustomSlot(Player player, String ident, EquipType type, ItemStack item) {
        role.get(player.getUniqueId()).updateCustomSlot(ident, type, item);
    }
}
