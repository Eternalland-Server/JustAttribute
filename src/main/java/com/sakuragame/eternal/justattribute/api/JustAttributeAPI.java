package com.sakuragame.eternal.justattribute.api;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleState;

import java.util.UUID;

public class JustAttributeAPI {

    public static RoleAttribute getRoleAttribute(UUID uuid) {
        return JustAttribute.getRoleManager().getPlayerAttribute(uuid);
    }

    public static RoleState getRoleState(UUID uuid) {
        return JustAttribute.getRoleManager().getPlayerState(uuid);
    }
}
