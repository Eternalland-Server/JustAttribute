package com.sakuragame.eternal.justattribute.api.event.role;

import com.sakuragame.eternal.justattribute.api.event.JustEvent;
import lombok.Getter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class RoleSkillAttackEvent {

    @Getter
    public static class Pre extends JustEvent {

        private final LivingEntity victim;
        private double damage;

        public Pre(Player who, LivingEntity victim, double damage) {
            super(who);
            this.victim = victim;
            this.damage = damage;
        }

        public void setDamage(double damage) {
            this.damage = damage;
        }
    }

    @Getter
    public static class Post extends JustEvent {

        private final LivingEntity victim;
        private final double damage;

        public Post(Player who, LivingEntity victim, double damage) {
            super(who);
            this.victim = victim;
            this.damage = damage;
        }
    }
}
