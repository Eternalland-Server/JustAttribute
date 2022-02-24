package com.sakuragame.eternal.justattribute.api.event.role;

import com.sakuragame.eternal.justattribute.api.event.JustEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

@Getter
public class RoleHealthStealEvent extends JustEvent {

    private final LivingEntity source;
    @Setter private double vampire;

    private final static HandlerList handlerList = new HandlerList();

    public RoleHealthStealEvent(Player who, LivingEntity source, double vampire) {
        super(who);
        this.source = source;
        this.vampire = vampire;
    }
}
