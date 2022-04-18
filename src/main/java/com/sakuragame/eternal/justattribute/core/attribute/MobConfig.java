package com.sakuragame.eternal.justattribute.core.attribute;

import io.lumine.xikage.mythicmobs.io.MythicConfig;

import java.util.HashMap;
import java.util.Map;

public class MobConfig {

    private final String ID;
    private final double minimumDamage;
    private final Map<Attribute, Double> attributes;
    private final Map<Attribute, Double> promotes;

    public MobConfig(String ID, MythicConfig config) {
        this.ID = ID;
        this.minimumDamage = config.getDouble("JustAttribute.minimum_damage", 0);
        this.attributes = new HashMap<>();
        this.promotes = new HashMap<>();

        if (config.isConfigurationSection("Promote")) {
            for (String key : config.getKeys("Promote")) {
                Attribute attribute = Attribute.match(key);
                if (key == null) return;
                double value = config.getDouble("Promote." + key);
                this.promotes.put(attribute, value);
            }
        }

        if (config.isConfigurationSection("JustAttribute")) {
            for (String key : config.getKeys("JustAttribute")) {
                Attribute attribute = Attribute.match(key);
                if (key == null) return;
                double value = config.getDouble("JustAttribute." + key);
                this.attributes.put(attribute, value);
            }
        }
    }

    public String getID() {
        return ID;
    }

    public double getMinimumDamage() {
        return minimumDamage;
    }

    public double getHealth(double level) {
        return this.getAttributeValue(Attribute.Health, level);
    }

    public double getAttributeValue(Attribute attribute, double level) {
        double normal = this.attributes.getOrDefault(attribute, 0d);
        double addition = this.promotes.getOrDefault(attribute, 0d);

        return normal * (1 + (level - 1) * addition);
    }
}
