package com.sakuragame.eternal.justattribute.api.event.role;

import com.sakuragame.eternal.justattribute.api.event.JustEvent;
import lombok.Getter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

@Getter
public class RoleHealthStoleEvent extends JustEvent {

    private final LivingEntity source;
    private final double vampire;

    public RoleHealthStoleEvent(Player who, LivingEntity source, double vampire) {
        super(who);
        this.source = source;
        this.vampire = vampire;
    }
}
