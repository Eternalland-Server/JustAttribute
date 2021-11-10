package com.sakuragame.eternal.justattribute.core.attribute.attribute;

import com.sakuragame.eternal.justattribute.core.attribute.BaseAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.Identifier;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleAttribute;

public class RestoreHealth extends BaseAttribute {

    public RestoreHealth() {
        super(Identifier.Restore_Health, "ㇷ", "生命恢复", 0, true);
    }

    @Override
    public double calculate(RoleAttribute role) {
        return role.getRestoreHP() * (1 + super.calculate(role));
    }
}
