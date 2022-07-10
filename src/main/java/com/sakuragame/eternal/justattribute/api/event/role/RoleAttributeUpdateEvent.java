package com.sakuragame.eternal.justattribute.api.event.role;

import com.sakuragame.eternal.justattribute.api.event.JustEvent;
import com.sakuragame.eternal.justattribute.core.attribute.character.PlayerCharacter;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class RoleAttributeUpdateEvent extends JustEvent {

    private final PlayerCharacter attribute;

    public RoleAttributeUpdateEvent(Player who, PlayerCharacter attribute) {
        super(who);
        this.attribute = attribute;
    }
}
