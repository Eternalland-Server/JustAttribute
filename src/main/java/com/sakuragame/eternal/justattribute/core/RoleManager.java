package com.sakuragame.eternal.justattribute.core;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleState;
import com.sakuragame.eternal.justattribute.util.Load;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RoleManager {

    private final JustAttribute plugin;

    private final Map<UUID, RoleAttribute> playerAttribute;
    private final Map<UUID, RoleState> playerState;

    private final static Map<UUID, Load> LOAD_MAP = new HashMap<>();

    public RoleManager(JustAttribute plugin) {
        this.plugin = plugin;
        this.playerAttribute = new HashMap<>();
        this.playerState = new HashMap<>();
        this.start();
    }

    public void loadAttributeData(Player player) {
        UUID uuid = player.getUniqueId();
        RoleState state = getPlayerState(uuid);
        if (state == null) {
            player.kickPlayer("账户未被正确加载，请重新进入。");
            plugin.getLogger().info("玩家 " + player.getName() + " 账户数据载入失败!");
            return;
        }

        RoleAttribute role = new RoleAttribute(uuid);
        this.playerAttribute.put(uuid, role);

        AttributeHandler.loadVanillaSlot(player);
        AttributeHandler.loadCustomSlot(player);

        delLoad(uuid);

        role.updateRoleAttribute();

        plugin.getLogger().info(" 加载 " + player.getName() + " 角色数据成功！");
    }

    public void loadStateData(UUID uuid) {
        RoleState state = JustAttribute.getStorageManager().loadData(uuid);
        this.playerState.put(uuid, state);
    }

    public void removeAttributeData(Player player) {
        this.removeAttributeData(player.getUniqueId());
    }

    public void removeAttributeData(UUID uuid) {
        this.playerAttribute.remove(uuid);
    }

    public void removeStateData(Player player) {
        this.removeStateData(player.getUniqueId());
    }

    public void removeStateData(UUID uuid) {
        RoleState state = this.playerState.remove(uuid);
        if (state == null) return;
        state.save();
    }

    public void saveData(UUID uuid) {
        this.removeAttributeData(uuid);
        this.removeStateData(uuid);
        delLoad(uuid);
    }

    public RoleAttribute getPlayerAttribute(Player player) {
        return getPlayerAttribute(player.getUniqueId());
    }

    public RoleAttribute getPlayerAttribute(UUID uuid) {
        return this.playerAttribute.get(uuid);
    }

    public RoleState getPlayerState(Player player) {
        return getPlayerState(player.getUniqueId());
    }

    public RoleState getPlayerState(UUID uuid) {
        return this.playerState.get(uuid);
    }

    public void saveAllRole() {
        for (RoleState state : this.playerState.values()) {
            state.save();
        }
    }

    public RoleAttribute initRoleAttribute(Player player) {
        UUID uuid = player.getUniqueId();

        RoleAttribute role = new RoleAttribute(uuid);
        this.playerAttribute.put(uuid, role);

        AttributeHandler.loadVanillaSlot(player);
        AttributeHandler.loadCustomSlot(player);
        role.updateRoleAttribute();

        return role;
    }

    public static void addLoad(UUID uuid) {
        LOAD_MAP.put(uuid, new Load());
    }

    public static Load getLoad(UUID uuid) {
        return LOAD_MAP.get(uuid);
    }

    public static void delLoad(UUID uuid) {
        LOAD_MAP.remove(uuid);
    }

    public static boolean isLoading(UUID uuid) {
        return LOAD_MAP.containsKey(uuid);
    }

    private void start() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (!player.isOnline()) continue;

                    if (player.isDead()) continue;

                    UUID uuid = player.getUniqueId();
                    RoleState state = playerState.get(uuid);

                    if (state == null) continue;

                    state.restore();
                }
            }
        }.runTaskTimer(plugin, 20, 20);
    }
}
