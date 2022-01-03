package com.sakuragame.eternal.justattribute.api.event;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

@Getter
public class PlayerUnderAttackedEvent extends PlayerEvent {

    private final double damage;

    private final static HandlerList handlerList = new HandlerList();

    public PlayerUnderAttackedEvent(Player who, double damage) {
        super(who);
        this.damage = damage;
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
