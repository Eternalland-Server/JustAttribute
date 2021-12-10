package com.sakuragame.eternal.justattribute.core;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.api.event.JARoleAttributeInitEvent;
import com.sakuragame.eternal.justattribute.api.event.JARoleStateInitEvent;
import com.sakuragame.eternal.justattribute.core.attribute.VanillaSlot;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleState;
import com.sakuragame.eternal.justattribute.core.special.EquipClassify;
import org.bukkit.Bukkit;
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

        plugin.getLogger().info(" 初始化 " + player.getName() + " 属性成功！");

        JARoleAttributeInitEvent event = new JARoleAttributeInitEvent(player, attribute);
        event.call();
    }

    public void loadStateData(Player player) {
        if (player == null) return;

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            RoleState state = JustAttribute.getStorageManager().getPlayerDate(player);
            playerState.put(player.getUniqueId(), state);

            plugin.getLogger().info(" 初始化 " + player.getName() + " 角色成功！");

            JARoleStateInitEvent event = new JARoleStateInitEvent(player, state);
            event.call();
        });
    }

    public void removeAttributeData(Player player) {
        this.playerAttribute.remove(player.getUniqueId());
    }

    public void removeStateData(Player player) {
        RoleState state = this.playerState.remove(player.getUniqueId());
        if (state != null) {
            state.save();
            plugin.getLogger().info(" 保存 " + player.getName() + " 角色数据成功！");
        }
    }

    public RoleAttribute getPlayerAttribute(UUID uuid) {
        return playerAttribute.get(uuid);
    }

    public RoleState getPlayerState(UUID uuid) {
        return playerState.get(uuid);
    }

    public void updateVanillaSlot(Player player, VanillaSlot slot) {
        UUID uuid = player.getUniqueId();

        RoleAttribute attribute = playerAttribute.get(uuid);
        if (attribute == null) return;

        attribute.updateVanillaSlot(slot);
    }

    public void updateMainHandSlot(Player player, int slot) {
        UUID uuid = player.getUniqueId();

        RoleAttribute attribute = playerAttribute.get(uuid);
        if (attribute == null) return;

        attribute.updateMainHandSlot(slot);
    }

    public void updateCustomSlot(Player player, String ident, EquipClassify type, ItemStack item) {
        UUID uuid = player.getUniqueId();

        RoleAttribute attribute = playerAttribute.get(uuid);
        if (attribute == null) return;

        attribute.updateCustomSlot(ident, type, item);
    }

    public void saveAllRole() {
        for (RoleState state : playerState.values()) {
            state.save();
        }
    }
}
