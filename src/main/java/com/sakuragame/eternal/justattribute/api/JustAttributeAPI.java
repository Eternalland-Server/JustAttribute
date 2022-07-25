package com.sakuragame.eternal.justattribute.api;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.character.PlayerCharacter;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.UUID;

public class JustAttributeAPI {

    public static PlayerCharacter getRoleCharacter(Player player) {
        return getRoleCharacter(player.getUniqueId());
    }

    public static PlayerCharacter getRoleCharacter(UUID uuid) {
        return JustAttribute.getRoleManager().get(uuid);
    }

    public static Collection<PlayerCharacter> getAllRole() {
        return JustAttribute.getRoleManager().getAllRole();
    }

}
