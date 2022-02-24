package com.sakuragame.eternal.justattribute.api.event.role;

import com.sakuragame.eternal.justattribute.api.event.JustEvent;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class RoleStateUpdateEvent extends JustEvent {

    public RoleStateUpdateEvent(Player who) {
        super(who);
    }
}
