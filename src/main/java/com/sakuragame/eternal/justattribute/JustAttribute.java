package com.sakuragame.eternal.justattribute;

import com.sakuragame.eternal.justattribute.commands.MainCommand;
import com.sakuragame.eternal.justattribute.file.FileManager;
import com.sakuragame.eternal.justattribute.listener.ZaphkielListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class JustAttribute extends JavaPlugin {
    @Getter private static JustAttribute instance;

    @Getter private FileManager fileManager;

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();

        instance = this;

        fileManager = new FileManager(this);
        fileManager.init();

        Bukkit.getPluginManager().registerEvents(new ZaphkielListener(), this);
        getCommand("jattribute").setExecutor(new MainCommand());

        long end = System.currentTimeMillis();

        getLogger().info("加载成功! 用时 %time% ms".replace("%time%", String.valueOf(end - start)));
    }

    @Override
    public void onDisable() {
        getLogger().info("卸载成功!");
    }

    public String getVersion() {
        String packet = Bukkit.getServer().getClass().getPackage().getName();
        return packet.substring(packet.lastIndexOf('.') + 1);
    }
}
