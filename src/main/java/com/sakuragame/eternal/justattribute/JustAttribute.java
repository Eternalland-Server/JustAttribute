package com.sakuragame.eternal.justattribute;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.sakuragame.eternal.justattribute.commands.MainCommand;
import com.sakuragame.eternal.justattribute.core.AttributeManager;
import com.sakuragame.eternal.justattribute.core.RoleManager;
import com.sakuragame.eternal.justattribute.file.FileManager;
import com.sakuragame.eternal.justattribute.hook.AttributePlaceholder;
import com.sakuragame.eternal.justattribute.listener.*;
import com.sakuragame.eternal.justattribute.listener.build.SkinListener;
import com.sakuragame.eternal.justattribute.listener.build.ZaphkielListener;
import com.sakuragame.eternal.justattribute.listener.combat.CombatListener;
import com.sakuragame.eternal.justattribute.listener.combat.VampireListener;
import com.sakuragame.eternal.justattribute.listener.hook.PlayerSQLListener;
import com.sakuragame.eternal.justattribute.storage.StorageManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class JustAttribute extends JavaPlugin {
    @Getter private static JustAttribute instance;

    @Getter private FileManager fileManager;

    private AttributeManager attributeManager;
    private RoleManager roleManager;
    private StorageManager storageManager;

    public ProtocolManager protocolManager;

    public static boolean playerSQL = false;

    public final static boolean debug = false;

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();

        instance = this;

        protocolManager = ProtocolLibrary.getProtocolManager();

        fileManager = new FileManager(this);
        attributeManager = new AttributeManager(this);
        roleManager = new RoleManager(this);
        storageManager = new StorageManager(this);
        fileManager.init();
        storageManager.init();

        new AttributePlaceholder().register();

        registerListener(new ZaphkielListener());
        registerListener(new SkinListener());
        registerListener(new PlayerListener());
        registerListener(new RoleListener());
        registerListener(new SlotListener());
        registerListener(new CombatListener());
        registerListener(new VampireListener());
        registerListener(new SoulBoundListener());

        if (Bukkit.getPluginManager().getPlugin("PlayerSQL") != null) {
            playerSQL = true;
            registerListener(new PlayerSQLListener());
            getLogger().info("Hook: 已关联 PlayerSQL");
        }
        else {
            playerSQL = false;
            getLogger().info("Hook: 未关联 PlayerSQL");
        }

        getCommand("jattribute").setExecutor(new MainCommand());

        long end = System.currentTimeMillis();

        getLogger().info("加载成功! 用时 %time% ms".replace("%time%", String.valueOf(end - start)));
    }

    @Override
    public void onDisable() {
        roleManager.saveAllRole();
        Bukkit.getScheduler().cancelTasks(this);
        getLogger().info("卸载成功!");
    }

    public String getVersion() {
        String packet = Bukkit.getServer().getClass().getPackage().getName();
        return packet.substring(packet.lastIndexOf('.') + 1);
    }

    public void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    public static AttributeManager getAttributeManager() {
        return instance.attributeManager;
    }

    public static RoleManager getRoleManager() {
        return instance.roleManager;
    }

    public static StorageManager getStorageManager() {
        return instance.storageManager;
    }

    public static ProtocolManager getProtocolManager() {
        return instance.protocolManager;
    }
}
