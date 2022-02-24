package com.sakuragame.eternal.justattribute.api.event.role;

import com.sakuragame.eternal.justattribute.api.event.JustEvent;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

@Getter
public class RoleUnderAttackEvent extends JustEvent {

    private final EntityDamageEvent.DamageCause cause;
    private final double damage;

    public RoleUnderAttackEvent(Player who, EntityDamageEvent.DamageCause cause, double damage) {
        super(who);
        this.cause = cause;
        this.damage = damage;
    }
}
