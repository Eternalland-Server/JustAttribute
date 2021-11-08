package com.sakuragame.eternal.justattribute.file;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import com.sakuragame.eternal.justattribute.file.sub.MessageFile;
import com.taylorswiftcn.justwei.file.JustConfiguration;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;


public class FileManager extends JustConfiguration {

    @Getter private YamlConfiguration config;
    @Getter private YamlConfiguration message;

    public FileManager(JustAttribute plugin) {
        super(plugin);
    }

    public void init() {
        config = initFile("config.yml");
        message = initFile("message.yml");

        ConfigFile.init();
        MessageFile.init();
    }
}
