package com.sakuragame.eternal.justattribute.api.event.role;

import com.sakuragame.eternal.justattribute.api.event.JustEvent;
import lombok.Getter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

@Getter
public class RoleNearDeathEvent extends JustEvent {

    private final LivingEntity attacker;
    private double finalDamage;
    private final EntityDamageEvent.DamageCause cause;

    public RoleNearDeathEvent(Player who, LivingEntity attacker, double finalDamage, EntityDamageEvent.DamageCause cause) {
        super(who);
        this.attacker = attacker;
        this.finalDamage = finalDamage;
        this.cause = cause;
    }

    public void setFinalDamage(double finalDamage) {
        this.finalDamage = finalDamage;
    }
}
