package com.sakuragame.eternal.justattribute.core.attribute.attribute;

import com.sakuragame.eternal.justattribute.core.attribute.Identifier;
import com.sakuragame.eternal.justattribute.core.attribute.BaseAttribute;

public class CriticalChance extends BaseAttribute {

    public CriticalChance() {
        super(Identifier.Critical_Chance, "ㇱ", "暴击几率", 0, true);
    }

    @Override
    public double calculate(double ordinaryValue, double potencyValue) {
        return Math.min(super.calculate(ordinaryValue, potencyValue), 1);
    }
}
