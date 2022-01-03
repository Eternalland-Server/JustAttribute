package com.sakuragame.eternal.justattribute.api.event.role;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

@Getter
public class RoleHealthStoleEvent extends PlayerEvent {

    private final LivingEntity source;
    private final double vampire;

    private final static HandlerList handlerList = new HandlerList();

    public RoleHealthStoleEvent(Player who, LivingEntity source, double vampire) {
        super(who);
        this.source = source;
        this.vampire = vampire;
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
