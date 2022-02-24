package com.sakuragame.eternal.justattribute.api.event.role;

import com.sakuragame.eternal.justattribute.api.event.JustEvent;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleAttribute;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class RoleAttributeUpdateEvent extends JustEvent {

    private final RoleAttribute attribute;

    public RoleAttributeUpdateEvent(Player who, RoleAttribute attribute) {
        super(who);
        this.attribute = attribute;
    }
}
