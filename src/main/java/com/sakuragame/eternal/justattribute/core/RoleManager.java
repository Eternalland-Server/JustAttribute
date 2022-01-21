package com.sakuragame.eternal.justattribute.core;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.api.event.role.RoleAttributeLoadedEvent;
import com.sakuragame.eternal.justattribute.api.event.role.RoleStateLoadedEvent;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleState;
import com.sakuragame.eternal.justattribute.core.special.EquipClassify;
import com.sakuragame.eternal.justattribute.util.Scheduler;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RoleManager {

    private static final JustAttribute plugin = JustAttribute.getInstance();

    private final static Map<UUID, RoleAttribute> playerAttribute = new HashMap<>();
    private final static Map<UUID, RoleState> playerState = new HashMap<>();

    public static void loadAttributeData(Player player) {
        if (player == null) return;

        RoleAttribute attribute = new RoleAttribute(player);
        playerAttribute.put(player.getUniqueId(), attribute);

        plugin.getLogger().info(" 初始化 " + player.getName() + " 属性成功！");

        RoleAttributeLoadedEvent event = new RoleAttributeLoadedEvent(player, attribute);
        event.call();
    }

    public static void loadStateData(Player player) {
        if (player == null) return;

        Scheduler.runAsync(() -> {
            RoleState state = JustAttribute.getStorageManager().getPlayerDate(player);
            playerState.put(player.getUniqueId(), state);

            plugin.getLogger().info(" 初始化 " + player.getName() + " 角色成功！");

            RoleStateLoadedEvent event = new RoleStateLoadedEvent(player, state);
            event.call();
        });
    }

    public static void removeAttributeData(Player player) {
        playerAttribute.remove(player.getUniqueId());
    }

    public static void removeStateData(Player player) {
        RoleState state = playerState.remove(player.getUniqueId());
        if (state == null) return;

        state.save();
        plugin.getLogger().info(" 保存 " + player.getName() + " 角色数据成功！");
    }

    public static RoleAttribute getPlayerAttribute(Player player) {
        return getPlayerAttribute(player.getUniqueId());
    }

    public static RoleAttribute getPlayerAttribute(UUID uuid) {
        return playerAttribute.get(uuid);
    }

    public static RoleState getPlayerState(Player player) {
        return getPlayerState(player.getUniqueId());
    }

    public static RoleState getPlayerState(UUID uuid) {
        return playerState.get(uuid);
    }

    public static void updateVanillaSlot(Player player, VanillaSlot slot) {
        UUID uuid = player.getUniqueId();
        RoleAttribute attribute = playerAttribute.get(uuid);
        if (attribute == null) return;

        attribute.updateVanillaSlot(slot);
    }

    public static void updateVanillaSlot(Player player, VanillaSlot slot, ItemStack item) {
        UUID uuid = player.getUniqueId();
        RoleAttribute attribute = playerAttribute.get(uuid);
        if (attribute == null) return;

        attribute.updateVanillaSlot(slot, item);
    }

    public static void updateMainHandSlot(Player player, int slot) {
        UUID uuid = player.getUniqueId();
        RoleAttribute attribute = playerAttribute.get(uuid);
        if (attribute == null) return;

        attribute.updateMainHandSlot(slot);
    }

    public static void updateCustomSlot(Player player, String ident, EquipClassify type, ItemStack item) {
        UUID uuid = player.getUniqueId();
        RoleAttribute attribute = playerAttribute.get(uuid);
        if (attribute == null) return;

        attribute.updateCustomSlot(ident, type, item);
    }

    public static void saveAllRole() {
        for (RoleState state : playerState.values()) {
            state.save();
        }
    }
}
