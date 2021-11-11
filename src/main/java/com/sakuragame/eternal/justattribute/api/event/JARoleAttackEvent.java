package com.sakuragame.eternal.justattribute.api.event;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class JARoleAttackEvent extends Event implements Cancellable {

    private final Player player;
    private final LivingEntity victim;
    private final double damage;
    private final boolean critical;
    private boolean cancel;

    private final static HandlerList handlerList = new HandlerList();

    public JARoleAttackEvent(Player player, LivingEntity victim, double damage, boolean critical) {
        this.player = player;
        this.victim = victim;
        this.damage = damage;
        this.critical = critical;
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
