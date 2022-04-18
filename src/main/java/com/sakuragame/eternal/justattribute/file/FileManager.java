package com.sakuragame.eternal.justattribute.file;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.MobConfig;
import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import com.sakuragame.eternal.justattribute.file.sub.MessageFile;
import com.taylorswiftcn.justwei.file.JustConfiguration;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.io.MythicConfig;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class FileManager extends JustConfiguration {

    private final JustAttribute plugin;
    @Getter private YamlConfiguration config;
    @Getter private YamlConfiguration message;

    private Map<String, MobConfig> mobConfig;

    public FileManager(JustAttribute plugin) {
        super(plugin);
        this.plugin = plugin;
        this.mobConfig = new HashMap<>();
    }

    public void init() {
        config = initFile("config.yml");
        message = initFile("message.yml");

        ConfigFile.init();
        MessageFile.init();
        initSmithy();
        loadMobConfig();
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

    public void loadMobConfig() {
        this.mobConfig.clear();
        for (MythicMob mob : MythicMobs.inst().getMobManager().getMobTypes()) {
            String mobID = mob.getInternalName();
            MythicConfig config = mob.getConfig();
            this.mobConfig.put(mobID, new MobConfig(mobID, config));
        }
    }

    public MobConfig getMobConfig(String id) {
        return this.mobConfig.get(id);
    }
}
