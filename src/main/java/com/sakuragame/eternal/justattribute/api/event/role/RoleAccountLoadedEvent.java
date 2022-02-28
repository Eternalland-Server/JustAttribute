package com.sakuragame.eternal.justattribute.api.event.role;

import com.sakuragame.eternal.justattribute.api.event.JustEvent;
import org.bukkit.entity.Player;

public class RoleAccountLoadedEvent extends JustEvent {

    public RoleAccountLoadedEvent(Player who) {
        super(who);
    }
}
