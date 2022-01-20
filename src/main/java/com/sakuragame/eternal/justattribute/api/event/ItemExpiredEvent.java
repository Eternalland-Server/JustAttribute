package com.sakuragame.eternal.justattribute.api.event;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

@Getter
public class ItemExpiredEvent extends PlayerEvent {

    private final String itemID;
    private final String itemName;
    private final static HandlerList handlerList = new HandlerList();

    public ItemExpiredEvent(Player who, String itemID, String itemName) {
        super(who);
        this.itemID = itemID;
        this.itemName = itemName;
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
