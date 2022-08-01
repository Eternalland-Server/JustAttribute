package com.sakuragame.eternal.justattribute.core.attribute.mob;

import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import io.lumine.xikage.mythicmobs.io.MythicConfig;

import java.util.HashMap;
import java.util.Map;

public class MobConfig {

    private final String ID;
    private final Map<Attribute, Double> attributes;
    private final Map<Attribute, Double> additions;
    private final double promote;

    public MobConfig(String ID, MythicConfig config) {
        this.ID = ID;
        this.attributes = new HashMap<>();
        this.additions = new HashMap<>();
        this.promote = config.getDouble("eternal.promote", 0.025);

        this.attributes.put(Attribute.Health, config.getDouble("Health"));
        if (config.isConfigurationSection("eternal.attribute")) {
            for (String key : config.getKeys("eternal.attribute")) {
                Attribute attribute = Attribute.match(key);
                if (key == null) return;
                double value = config.getDouble("eternal.attribute." + key);
                this.attributes.put(attribute, value);
            }
        }

        if (config.isConfigurationSection("eternal.additions")) {
            for (String key : config.getKeys("eternal.additions")) {
                Attribute attribute = Attribute.match(key);
                if (key == null) return;
                double value = config.getDouble("eternal.additions." + key);
                this.additions.put(attribute, value);
            }
        }
    }

    public String getID() {
        return ID;
    }

    public double getHealth(int level) {
        double value = this.attributes.getOrDefault(Attribute.Health, 0d);
        double additions = this.additions.getOrDefault(Attribute.Health, -1d);
        return (additions == -1) ? (value * (1 + (level - 1) * this.promote)) : (value + (level - 1) * additions);
    }

    public double getAttributeValue(Attribute attribute, double level) {
        double value = this.attributes.getOrDefault(attribute, 0d);
        if (attribute.isOnlyPercent()) return value;

        double additions = this.additions.getOrDefault(attribute, -1d);
        return (additions == -1) ? (value * (1 + level * this.promote)) : (value + level * additions);
    }
}
