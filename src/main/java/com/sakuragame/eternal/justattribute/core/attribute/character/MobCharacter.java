package com.sakuragame.eternal.justattribute.core.attribute.character;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import com.sakuragame.eternal.justattribute.core.attribute.mob.MobConfig;
import com.sakuragame.eternal.justattribute.core.special.CombatPower;
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
        MobConfig config = JustAttribute.getFileManager().getMobConfig(this.ID);
        int value = CombatPower.calculate(config.getAttributeSource(this.mob.getLevel()));
        this.getCombatPower().update(value);
    }

    @Override
    public double getAttributeValue(Attribute identifier) {
        MobConfig config = JustAttribute.getFileManager().getMobConfig(this.ID);
        if (config == null) return 1d;
        return config.getAttributeValue(identifier, this.mob.getLevel());
    }

    public double getSkillDefense() {
        double defense = this.getAttributeValue(Attribute.Defence);
        return defense * 0.618;
    }
}
