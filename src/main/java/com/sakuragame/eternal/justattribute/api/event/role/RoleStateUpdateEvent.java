package com.sakuragame.eternal.justattribute.api.event.role;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

@Getter
public class RoleStateUpdateEvent extends PlayerEvent {

    private final static HandlerList handlerList = new HandlerList();

    public RoleStateUpdateEvent(Player who) {
        super(who);
    }

    @Override
    public HandlerList getHandlers() {
        return getHandlerList();
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public void call() {
        Bukkit.getPluginManager().callEvent(this);
    }
}
