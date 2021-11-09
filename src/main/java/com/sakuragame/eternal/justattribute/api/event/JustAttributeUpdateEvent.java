package com.sakuragame.eternal.justattribute.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class JustAttributeUpdateEvent extends PlayerEvent {



    public JustAttributeUpdateEvent(Player who) {
        super(who);
    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }
}
