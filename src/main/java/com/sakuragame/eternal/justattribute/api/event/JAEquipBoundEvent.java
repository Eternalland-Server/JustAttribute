package com.sakuragame.eternal.justattribute.api.event;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

@Getter
public class JAEquipBoundEvent extends PlayerEvent {

    private final ItemStack item;
    private final static HandlerList handlerList = new HandlerList();

    public JAEquipBoundEvent(Player who, ItemStack item) {
        super(who);
        this.item = item;
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
