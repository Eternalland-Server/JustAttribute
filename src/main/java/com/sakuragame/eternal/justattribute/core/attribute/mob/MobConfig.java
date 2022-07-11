package com.sakuragame.eternal.justattribute.core.attribute.mob;

import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import io.lumine.xikage.mythicmobs.io.MythicConfig;

import java.util.HashMap;
import java.util.Map;

public class MobConfig {

    private final String ID;
    private final Map<Attribute, Double> attributes;
    private final double additions;

    public MobConfig(String ID, MythicConfig config) {
        this.ID = ID;
        this.attributes = new HashMap<>();
        this.additions = config.getDouble("eternal.additions", 0.01);

        if (config.isConfigurationSection("eternal.attribute")) {
            for (String key : config.getKeys("eternal.attribute")) {
                Attribute attribute = Attribute.match(key);
                if (key == null) return;
                double value = config.getDouble("eternal.attribute." + key);
                this.attributes.put(attribute, value);
            }
        }
    }

    public String getID() {
        return ID;
    }

    public double getHealth(int level) {
        return this.getAttributeValue(Attribute.Health, level);
    }

    public double getAttributeValue(Attribute attribute, double level) {
        double value = this.attributes.getOrDefault(attribute, 0d);
        return value * (1 + (level - 1) * this.additions);
    }
}
