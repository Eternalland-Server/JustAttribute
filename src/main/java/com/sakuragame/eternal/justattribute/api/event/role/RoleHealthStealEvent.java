package com.sakuragame.eternal.justattribute.api.event.role;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

@Getter
public class RoleHealthStealEvent extends PlayerEvent implements Cancellable {

    private final LivingEntity source;
    @Setter private double vampire;
    private boolean cancel;

    private final static HandlerList handlerList = new HandlerList();

    public RoleHealthStealEvent(Player who, LivingEntity source, double vampire) {
        super(who);
        this.source = source;
        this.vampire = vampire;
        this.cancel = false;
    }

    @Override
    public HandlerList getHandlers() {
        return getHandlerList();
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public boolean call() {
        Bukkit.getPluginManager().callEvent(this);
        return isCancelled();
    }
}
