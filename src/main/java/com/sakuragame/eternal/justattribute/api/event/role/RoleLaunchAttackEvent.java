package com.sakuragame.eternal.justattribute.api.event.role;

import com.sakuragame.eternal.justattribute.api.event.JustEvent;
import lombok.Getter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

@Getter
public class RoleLaunchAttackEvent extends JustEvent {

    private final LivingEntity victim;
    private final double baseDamage;
    private final double criticalDamage;
    private final Cause cause;

    public RoleLaunchAttackEvent(Player who, LivingEntity victim, double baseDamage, double criticalDamage, Cause cause) {
        super(who);
        this.victim = victim;
        this.baseDamage = baseDamage;
        this.criticalDamage = criticalDamage;
        this.cause = cause;
    }

    public enum Cause {
        Normal,
        Custom,
        Plugin
    }
}
