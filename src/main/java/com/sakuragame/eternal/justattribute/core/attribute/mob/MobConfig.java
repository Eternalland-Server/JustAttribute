package com.sakuragame.eternal.justattribute.core.attribute.mob;

import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import com.sakuragame.eternal.justattribute.core.attribute.AttributeSource;
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
        return this.getAttributeValue(Attribute.Health, level);
    }

    public double getAttributeValue(Attribute attribute, double level) {
        double value = this.attributes.getOrDefault(attribute, 0d);
        if (attribute.isOnlyPercent()) return value;

        double additions = this.additions.getOrDefault(attribute, -1d);
        return (additions == -1) ? (value * (1 + level * this.promote)) : (value + level * additions);
    }

    public AttributeSource getAttributeSource(double level) {
        AttributeSource source = new AttributeSource();
        Map<Attribute, Double> map = new HashMap<>();
        attributes.keySet().forEach(k -> map.put(k, this.getAttributeValue(k, level)));
        source.setOrdinary(map);

        return source;
    }
}
