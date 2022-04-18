package com.sakuragame.eternal.justattribute.core.attribute.stats;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import com.sakuragame.eternal.justattribute.core.attribute.MobConfig;
import io.lumine.xikage.mythicmobs.io.MythicConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import io.lumine.xikage.mythicmobs.utils.config.file.FileConfiguration;
import lombok.Getter;
import org.bukkit.entity.LivingEntity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Getter
public class MobAttribute implements EntityAttribute {

    private final String mobID;
    private final LivingEntity entity;
    private final double level;

    public MobAttribute(LivingEntity entity) {
        this.mobID = null;
        this.entity = entity;
        this.level = 1;
    }

    public MobAttribute(ActiveMob mob) {
        this.mobID = mob.getType().getInternalName();
        this.entity = (LivingEntity) mob.getEntity().getBukkitEntity();
        this.level = mob.getLevel();
    }

    public double getMinimumDamage() {
        if (this.mobID == null) return 1d;

        MobConfig config = JustAttribute.getFileManager().getMobConfig(this.mobID);
        if (config == null) return 1d;

        return config.getMinimumDamage();
    }

    @Override
    public double getValue(Attribute attribute) {
        if (this.mobID == null) return 1d;

        MobConfig config = JustAttribute.getFileManager().getMobConfig(this.mobID);
        if (config == null) return 1d;

        return config.getAttributeValue(attribute, this.level);
    }
}
