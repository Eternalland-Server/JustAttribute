package com.sakuragame.eternal.justattribute.core;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.api.event.JARoleAttributeInitEvent;
import com.sakuragame.eternal.justattribute.api.event.JARoleStateInitEvent;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.VanillaSlot;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleState;
import com.sakuragame.eternal.justattribute.core.special.EquipClassify;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class RoleManager {

    private final JustAttribute plugin;

    private final HashMap<UUID, RoleAttribute> playerAttribute;
    private final HashMap<UUID, RoleState> playerState;

    public RoleManager(JustAttribute plugin) {
        this.plugin = plugin;
        this.playerAttribute = new HashMap<>();
        this.playerState = new HashMap<>();
    }

    public void loadAttributeData(Player player) {
        if (player == null) return;

        RoleAttribute attribute = new RoleAttribute(player);
        playerAttribute.put(player.getUniqueId(), attribute);

        JARoleAttributeInitEvent event = new JARoleAttributeInitEvent(player, attribute);
        event.call();
    }

    public void loadStateData(Player player) {
        if (player == null) return;

        RoleState state = JustAttribute.getStorageManager().getPlayerDate(player);
        playerState.put(player.getUniqueId(), state);

        JARoleStateInitEvent event = new JARoleStateInitEvent(player, state);
        event.call();
    }

    public void removeAttributeData(UUID uuid) {
        this.playerAttribute.remove(uuid);
    }

    public void removeStateData(UUID uuid) {
        this.playerState.remove(uuid).save();
    }

    public RoleAttribute getPlayerAttribute(UUID uuid) {
        return playerAttribute.get(uuid);
    }

    public RoleState getPlayerState(UUID uuid) {
        return playerState.get(uuid);
    }

    public void updateVanillaSlot(Player player, VanillaSlot slot) {
        if (!playerAttribute.containsKey(player.getUniqueId())) return;

        playerAttribute.get(player.getUniqueId()).updateVanillaSlot(slot);
    }

    public void updateCustomSlot(Player player, String ident, EquipClassify type, ItemStack item) {
        if (!playerAttribute.containsKey(player.getUniqueId())) return;

        playerAttribute.get(player.getUniqueId()).updateCustomSlot(ident, type, item);
    }
}
