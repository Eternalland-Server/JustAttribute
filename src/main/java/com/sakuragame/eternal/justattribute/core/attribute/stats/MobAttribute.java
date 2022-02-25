package com.sakuragame.eternal.justattribute.core.attribute.stats;

import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import io.lumine.xikage.mythicmobs.io.MythicConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import lombok.Getter;
import org.bukkit.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;

@Getter
public class MobAttribute implements EntityAttribute {
    
    private final LivingEntity entity;
    private final double minimumDamage;
    private final Map<Attribute, Double> attributes;
    private final Map<String, Double> damageModifiers;

    public MobAttribute(LivingEntity entity) {
        this.entity = entity;
        this.attributes = new HashMap<Attribute, Double>() {{
            put(Attribute.Damage, 10d);
            put(Attribute.Defence, 10d);
        }};
        this.minimumDamage = 2;
        this.damageModifiers = new HashMap<>();
    }

    public MobAttribute(ActiveMob mob) {
        this.entity = (LivingEntity) mob.getEntity().getBukkitEntity();
        this.attributes = new HashMap<>();

        double level = mob.getLevel();
        double promote =  1 + (level - 1) * 0.5;

        MythicMob type = mob.getType();
        MythicConfig config = type.getConfig();
        this.attributes.put(Attribute.Damage, config.getDouble("JustAttribute." + Attribute.Damage.getId(), type.getDamage().get()) * promote);
        this.attributes.put(Attribute.Defence, config.getDouble("JustAttribute." + Attribute.Defence.getId(), type.getArmor().get()) * promote);
        this.attributes.put(Attribute.Critical_Chance, config.getDouble("JustAttribute." + Attribute.Critical_Chance.getId(), 0));
        this.attributes.put(Attribute.Critical_Damage, config.getDouble("JustAttribute." + Attribute.Critical_Damage.getId(), 0));
        this.attributes.put(Attribute.Defence_Penetration, config.getDouble("JustAttribute." + Attribute.Defence_Penetration.getId(), 0));
        this.minimumDamage = config.getDouble("JustAttribute.minimum_damage", 0);
        this.damageModifiers = type.getDamageModifiers();
    }

    @Override
    public double getValue(Attribute attribute) {
        return attributes.getOrDefault(attribute, 0d);
    }
}
