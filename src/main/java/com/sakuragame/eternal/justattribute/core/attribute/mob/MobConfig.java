package com.sakuragame.eternal.justattribute.core.attribute.mob;

import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import io.lumine.xikage.mythicmobs.io.MythicConfig;

import java.util.HashMap;
import java.util.Map;

public class MobConfig {

    private final String ID;
    private final Map<Attribute, Double> attributes;
    private final Map<Attribute, Double> additions;

    public MobConfig(String ID, MythicConfig config) {
        this.ID = ID;
        this.attributes = new HashMap<>();
        this.additions = new HashMap<>();

        if (config.isConfigurationSection("eternal.addition")) {
            for (String key : config.getKeys("eternal.addition")) {
                Attribute attribute = Attribute.match(key);
                if (key == null) return;
                double value = config.getDouble("eternal.addition." + key);
                this.additions.put(attribute, value);
            }
        }

        if (config.isConfigurationSection("eternal.attribute")) {
            for (String key : config.getKeys("eternal.attribute")) {
                Attribute attribute = Attribute.match(key);
                if (key == null) return;
                double value = config.getDouble("eternal.attribute." + key);
                this.attributes.put(attribute, value);
            }
        }
        this.attributes.put(Attribute.Health, config.getDouble("Health"));
    }

    public String getID() {
        return ID;
    }

    public double getHealth(double level) {
        return this.getAttributeValue(Attribute.Health, level);
    }

    public double getAttributeValue(Attribute attribute, double level) {
        double value = this.attributes.getOrDefault(attribute, 0d);
        double addition = this.additions.getOrDefault(attribute, 0d);

        return value * (1 + (level - 1) * addition);
    }
}
