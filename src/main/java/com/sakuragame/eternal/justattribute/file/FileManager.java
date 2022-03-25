package com.sakuragame.eternal.justattribute.file;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import com.sakuragame.eternal.justattribute.file.sub.MessageFile;
import com.taylorswiftcn.justwei.file.JustConfiguration;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;


public class FileManager extends JustConfiguration {

    private final JustAttribute plugin;
    @Getter private YamlConfiguration config;
    @Getter private YamlConfiguration message;

    public FileManager(JustAttribute plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    public void init() {
        config = initFile("config.yml");
        message = initFile("message.yml");

        ConfigFile.init();
        MessageFile.init();
        initSmithy();
    }

    private void initSmithy() {
        initFile("smithy/identify.yml");
        initFile("smithy/seal.yml");
        initFile("smithy/transfer.yml");
    }

    public YamlConfiguration getIdentifyConfig() {
        File file = new File(this.plugin.getDataFolder(), "smithy/identify.yml");
        return YamlConfiguration.loadConfiguration(file);
    }

    public YamlConfiguration getSealConfig() {
        File file = new File(this.plugin.getDataFolder(), "smithy/seal.yml");
        return YamlConfiguration.loadConfiguration(file);
    }

    public YamlConfiguration getTransferConfig() {
        File file = new File(this.plugin.getDataFolder(), "smithy/transfer.yml");
        return YamlConfiguration.loadConfiguration(file);
    }
}
