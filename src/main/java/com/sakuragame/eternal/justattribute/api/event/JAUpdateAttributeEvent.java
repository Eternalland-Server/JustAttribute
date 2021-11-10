package com.sakuragame.eternal.justattribute.api.event;

import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleAttribute;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

@Getter
public class JAUpdateAttributeEvent extends PlayerEvent {

    private final RoleAttribute attribute;

    private final static HandlerList handlerList = new HandlerList();

    public JAUpdateAttributeEvent(Player who, RoleAttribute attribute) {
        super(who);
        this.attribute = attribute;
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
