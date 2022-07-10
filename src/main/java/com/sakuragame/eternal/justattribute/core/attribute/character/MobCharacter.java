package com.sakuragame.eternal.justattribute.core.attribute.character;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import com.sakuragame.eternal.justattribute.core.attribute.mob.MobConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;

public class MobCharacter extends JustCharacter {

    private final String ID;
    private final ActiveMob mob;

    public MobCharacter(ActiveMob mob) {
        super(mob.getUniqueId());
        this.ID = mob.getType().getInternalName();
        this.mob = mob;
    }

    @Override
    public void init() {

    }

    @Override
    public void update() {

    }

    @Override
    public double getAttributeValue(Attribute identifier) {
        MobConfig config = JustAttribute.getFileManager().getMobConfig(this.ID);
        if (config == null) return 1d;
        return config.getAttributeValue(identifier, this.mob.getLevel());
    }
}
