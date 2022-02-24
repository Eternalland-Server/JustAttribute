package com.sakuragame.eternal.justattribute.api.event.role;

import com.sakuragame.eternal.justattribute.api.event.JustEvent;
import lombok.Getter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

@Getter
public class RoleLaunchAttackEvent extends JustEvent {

    private final LivingEntity victim;
    private final double damage;
    private final boolean critical;
    private final Cause cause;

    public RoleLaunchAttackEvent(Player who, LivingEntity victim, double damage, boolean critical, Cause cause) {
        super(who);
        this.victim = victim;
        this.damage = damage;
        this.critical = critical;
        this.cause = cause;
    }

    public enum Cause {
        Normal,
        Custom,
        Plugin
    }
}
