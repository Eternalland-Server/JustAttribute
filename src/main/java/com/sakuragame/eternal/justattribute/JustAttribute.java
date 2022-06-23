package com.sakuragame.eternal.justattribute;

import com.sakuragame.eternal.justattribute.commands.MainCommand;
import com.sakuragame.eternal.justattribute.core.RoleManager;
import com.sakuragame.eternal.justattribute.core.smithy.SmithyManager;
import com.sakuragame.eternal.justattribute.file.FileManager;
import com.sakuragame.eternal.justattribute.hook.AttributePlaceholder;
import com.sakuragame.eternal.justattribute.listener.*;
import com.sakuragame.eternal.justattribute.listener.build.AttributeListener;
import com.sakuragame.eternal.justattribute.listener.build.ExpireListener;
import com.sakuragame.eternal.justattribute.listener.build.SkinListener;
import com.sakuragame.eternal.justattribute.listener.build.ZaphkielListener;
import com.sakuragame.eternal.justattribute.listener.combat.CombatListener;
import com.sakuragame.eternal.justattribute.listener.combat.VampireListener;
import com.sakuragame.eternal.justattribute.listener.hook.InfoListener;
import com.sakuragame.eternal.justattribute.listener.hook.LevelListener;
import com.sakuragame.eternal.justattribute.listener.hook.MobListener;
import com.sakuragame.eternal.justattribute.listener.hook.StorageListener;
import com.sakuragame.eternal.justattribute.listener.smithy.*;
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
        storageManager = new StorageManager(this);
        storageManager.init();

        getLogger().info("初始化角色管理...");
        roleManager = new RoleManager(this);

        getLogger().info("初始化锻造功能...");
        SmithyManager.init();

        getLogger().info("注册PAPI变量...");
        new AttributePlaceholder().register();

        getLogger().info("注册事件...");
        registerListener(new ZaphkielListener());
        registerListener(new SkinListener());
        registerListener(new AttributeListener());
        registerListener(new ExpireListener());

        registerListener(new UIListener());
        registerListener(new IdentifyListener());
        registerListener(new SealListener());
        registerListener(new TransferListener());
        registerListener(new CommonListener());

        registerListener(new PlayerListener());
        registerListener(new RoleListener());
        registerListener(new SlotListener());
        registerListener(new CombatListener());
        registerListener(new VampireListener());
        registerListener(new SoulBoundListener());
        registerListener(new LevelListener());
        registerListener(new MobListener());
        registerListener(new InfoListener());

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
        roleManager.saveAllRole();
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
