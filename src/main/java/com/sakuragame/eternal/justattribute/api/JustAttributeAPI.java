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

    public static boolean consumeMP(UUID uuid, double value) {
        RoleState state = getRoleState(uuid);
        double mp = state.getMana();
        if (mp < value) return false;

        state.setMana(mp - value);
        return true;
    }
}
