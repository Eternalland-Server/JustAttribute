package com.sakuragame.eternal.justattribute.api.event.role;

import com.sakuragame.eternal.justattribute.api.event.JustEvent;
import lombok.Getter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public class RoleUnderAttackEvent {

    @Getter
    public static class Pre extends JustEvent {

        private final LivingEntity attacker;
        private double damage;
        private double criticalDamage;
        private final EntityDamageEvent.DamageCause cause;

        public Pre(Player who, LivingEntity attacker, double damage, double criticalDamage, EntityDamageEvent.DamageCause cause) {
            super(who);
            this.attacker = attacker;
            this.damage = damage;
            this.criticalDamage = criticalDamage;
            this.cause = cause;
        }

        public void setDamage(double damage) {
            this.damage = damage;
        }

        public void setCriticalDamage(double criticalDamage) {
            this.criticalDamage = criticalDamage;
        }
    }

    @Getter
    public static class Post extends JustEvent {

        private final LivingEntity attacker;
        private final double damage;
        private final double criticalDamage;
        private final EntityDamageEvent.DamageCause cause;

        public Post(Player who, LivingEntity attacker, double damage, double criticalDamage, EntityDamageEvent.DamageCause cause) {
            super(who);
            this.attacker = attacker;
            this.damage = damage;
            this.criticalDamage = criticalDamage;
            this.cause = cause;
        }

        public double getTotalDamage() {
            return this.damage * this.criticalDamage;
        }
    }
}
