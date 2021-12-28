package com.sakuragame.eternal.justattribute.core.attribute.stats;

import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import io.lumine.xikage.mythicmobs.io.MythicConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import lombok.Getter;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

@Getter
public class MobAttribute {
    
    private final LivingEntity entity;
    private final double damage;
    private final double defence;
    private final double criticalChance;
    private final double criticalDamage;
    private final double defencePenetration;
    private final Map<String, Double> damageModifiers;

    public MobAttribute(ActiveMob mob) {
        this.entity = (LivingEntity) mob.getEntity().getBukkitEntity();

        MythicMob type = mob.getType();
        MythicConfig config = type.getConfig();
        this.damage = config.getDouble("JustAttribute." + Attribute.Damage.getId(), type.getDamage().get());
        this.defence = config.getDouble("JustAttribute." + Attribute.Defence.getId(), type.getArmor().get());
        this.criticalChance = config.getDouble("JustAttribute." + Attribute.Critical_Chance.getId(), 0);
        this.criticalDamage = config.getDouble("JustAttribute." + Attribute.Critical_Damage.getId(), 0);
        this.defencePenetration = config.getDouble("JustAttribute." + Attribute.Defence_Penetration.getId(), 0);
        this.damageModifiers = type.getDamageModifiers();
    }
}
