package com.sakuragame.eternal.justattribute.file.sub;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import net.sakuragame.eternal.dragoncore.util.Pair;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class PetFile {

    private final static Random RANDOM = new Random();
    public static Map<Integer, Map<Attribute, String>> profile;

    public static void init() {
        YamlConfiguration pet = JustAttribute.getFileManager().getPet();
        profile = new HashMap<>();

        ConfigurationSection section = pet.getConfigurationSection("");
        if (section == null) return;

        for (String key : section.getKeys(false)) {
            Map<Attribute, String> map = new HashMap<>();
            ConfigurationSection sub = section.getConfigurationSection(key);
            for (String ident : sub.getKeys(false)) {
                Attribute attribute = Attribute.match(ident);
                if (attribute == null) continue;
                map.put(attribute, sub.getString(ident));
            }
            profile.put(Integer.parseInt(key), map);
        }
    }

    public static Pair<Attribute, Double> random(int i) {
        Map<Attribute, String> map = profile.get(i);
        List<Attribute> keys = new ArrayList<>(map.keySet());
        int random = RANDOM.nextInt(keys.size());

        Attribute attribute = keys.get(random);
        String s = map.get(attribute);
        double min = Double.parseDouble(s.split(" ", 2)[0]);
        double max = Double.parseDouble(s.split(" ", 2)[1]);
        double value = new BigDecimal(min + (max - min) * Math.random()).setScale(3, RoundingMode.HALF_UP).doubleValue();

        return new Pair<>(attribute, value);
    }
}
