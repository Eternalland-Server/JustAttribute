package com.sakuragame.eternal.justattribute.core.attribute.attribute;

import com.sakuragame.eternal.justattribute.core.attribute.Identifier;
import com.sakuragame.eternal.justattribute.core.attribute.BaseAttribute;
import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;

public class DamageImmune extends BaseAttribute {

    public DamageImmune() {
        super(Identifier.Damage_Immune, "ㇲ", "伤害免疫", 0, true);
    }

    @Override
    public double calculate(double ordinaryValue, double potencyValue) {
        return Math.min(super.calculate(ordinaryValue, potencyValue), ConfigFile.damage_immune_limit);
    }
}
