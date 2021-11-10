package com.sakuragame.eternal.justattribute.core.attribute.attribute;

import com.sakuragame.eternal.justattribute.core.attribute.Identifier;
import com.sakuragame.eternal.justattribute.core.attribute.BaseAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleAttribute;

public class RestoreMana extends BaseAttribute {

    public RestoreMana() {
        super(Identifier.Restore_Mana, "ㇶ", "法力恢复", 0, true);
    }

    @Override
    public double calculate(RoleAttribute role) {
        return role.getRestoreMP() * (1 + super.calculate(role));
    }
}
