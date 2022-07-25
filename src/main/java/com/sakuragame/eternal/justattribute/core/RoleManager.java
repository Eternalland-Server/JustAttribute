package com.sakuragame.eternal.justattribute.core;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.api.event.role.RoleAccountLoadedEvent;
import com.sakuragame.eternal.justattribute.core.attribute.character.PlayerCharacter;
import com.sakuragame.eternal.justattribute.util.CatchTask;
import com.sakuragame.eternal.justattribute.util.Loader;
import com.sakuragame.eternal.justattribute.util.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RoleManager {

    private final JustAttribute plugin;

    private final Map<UUID, PlayerCharacter> roles;

    private final static Map<UUID, Loader> LOAD_MAP = new HashMap<>();

    public RoleManager(JustAttribute plugin) {
        this.plugin = plugin;
        this.roles = new ConcurrentHashMap<>();
        this.start();
    }

    public Collection<PlayerCharacter> getAllRole() {
        return this.roles.values();
    }

    public void load(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;

        if (this.roles.containsKey(uuid)) return;

        PlayerCharacter role = JustAttribute.getStorageManager().loadAccount(uuid);
        if (role != null) return;

        player.kickPlayer("账户未被正确加载，请重新进入。");
        plugin.getLogger().info("玩家 " + player.getName() + " 账户数据载入失败!");
    }

    public void init(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;

        PlayerCharacter role = this.get(uuid);
        if (role == null) {
            CatchTask task = new CatchTask(player);
            task.runTaskTimerAsynchronously(this.plugin, 1, 1);
            return;
        }

        role.init();
        removeLoader(uuid);
        Scheduler.run(() -> {
            RoleAccountLoadedEvent event = new RoleAccountLoadedEvent(player);
            event.call();

            role.update();
        });



        plugin.getLogger().info(" 加载 " + player.getName() + " 角色数据成功！");
    }

    public void save(UUID uuid) {
        removeLoader(uuid);
        PlayerCharacter role = this.roles.remove(uuid);
        if (role == null) return;
        JustAttribute.getStorageManager().saveAccount(uuid, role.getHP(), role.getMP());
    }

    public void saveAll() {
        this.roles.values().forEach(role -> JustAttribute.getStorageManager().saveAccount(role.getUUID(), role.getHP(), role.getMP()));
    }

    public PlayerCharacter get(UUID uuid) {
        return this.roles.get(uuid);
    }

    public void put(UUID uuid, PlayerCharacter role) {
        this.roles.put(uuid, role);
    }

    public static void generateLoader(UUID uuid) {
        LOAD_MAP.put(uuid, new Loader(uuid));
    }

    public static Loader getLoader(UUID uuid) {
        return LOAD_MAP.get(uuid);
    }

    public static void removeLoader(UUID uuid) {
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
                    if (!player.isOnline() || player.isDead()) continue;

                    UUID uuid = player.getUniqueId();
                    PlayerCharacter role = roles.get(uuid);

                    if (role == null) continue;
                    role.restore();
                }
            }
        }.runTaskTimer(this.plugin, 20, 20);
    }
}
