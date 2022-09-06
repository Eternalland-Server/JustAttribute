package com.sakuragame.eternal.justattribute;

import com.sakuragame.eternal.justattribute.commands.MainCommand;
import com.sakuragame.eternal.justattribute.core.RoleManager;
import com.sakuragame.eternal.justattribute.core.smithy.SmithyManager;
import com.sakuragame.eternal.justattribute.file.FileManager;
import com.sakuragame.eternal.justattribute.hook.AttributePlaceholder;
import com.sakuragame.eternal.justattribute.listener.*;
import com.sakuragame.eternal.justattribute.listener.hook.StorageListener;
import com.sakuragame.eternal.justattribute.listener.pet.SlotListener;
import com.sakuragame.eternal.justattribute.storage.StorageManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class JustAttribute extends JavaPlugin {
    @Getter private static JustAttribute instance;

    @Getter private static FileManager fileManager;
    @Getter private static StorageManager storageManager;
    @Getter private static RoleManager roleManager;

    public static boolean PLAYER_SQL = false;

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();

        instance = this;

        getLogger().info("初始化配置文件...");
        fileManager = new FileManager(this);
        fileManager.init();

        getLogger().info("初始化数据库...");
        storageManager = new StorageManager();
        storageManager.init();

        getLogger().info("初始化角色管理...");
        roleManager = new RoleManager(this);

        getLogger().info("初始化锻造功能...");
        SmithyManager.init();

        getLogger().info("注册PAPI变量...");
        new AttributePlaceholder().register();

        getLogger().info("注册事件...");
        new UserRegister();
        new BuildRegister();
        new CombatRegister();
        new HookRegister();
        new SmithyRegister();
        this.registerListener(new SlotListener());

        if (Bukkit.getPluginManager().getPlugin("PlayerSQL") != null) {
            PLAYER_SQL = true;
            registerListener(new StorageListener());
            getLogger().info("已兼容PlayerSQL...");
        }
        else {
            PLAYER_SQL = false;
            getLogger().info("未兼容PlayerSQL...");
        }

        getLogger().info("注册命令...");
        getCommand("jattribute").setExecutor(new MainCommand());

        long end = System.currentTimeMillis();

        getLogger().info("加载成功! 用时 " + (end - start) + " ms");
    }

    @Override
    public void onDisable() {
        roleManager.saveAll();
        Bukkit.getScheduler().cancelTasks(this);
        getLogger().info("卸载成功!");
    }

    public void reload() {
        fileManager.init();
    }

    public String getVersion() {
        String packet = Bukkit.getServer().getClass().getPackage().getName();
        return packet.substring(packet.lastIndexOf('.') + 1);
    }

    public void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }
}
