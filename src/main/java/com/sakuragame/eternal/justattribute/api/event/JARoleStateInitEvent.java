package com.sakuragame.eternal.justattribute.api.event;

import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleState;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

@Getter
public class JARoleStateInitEvent extends PlayerEvent {

    private final RoleState state;

    private final static HandlerList handlerList = new HandlerList();

    public JARoleStateInitEvent(Player who, RoleState state) {
        super(who);
        this.state = state;
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
