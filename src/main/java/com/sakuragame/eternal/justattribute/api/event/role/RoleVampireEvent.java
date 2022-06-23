package com.sakuragame.eternal.justattribute.api.event.role;

import com.sakuragame.eternal.justattribute.api.event.JustEvent;
import lombok.Getter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class RoleVampireEvent {

    @Getter
    public static class Pre extends JustEvent {

        private final LivingEntity source;
        private double value;
        private final Cause cause;

        public Pre(Player who, LivingEntity source, double value, Cause cause) {
            super(who);
            this.source = source;
            this.value = value;
            this.cause = cause;
        }

        public void setValue(double value) {
            this.value = value;
        }
    }

    @Getter
    public static class Post extends JustEvent {

        private final LivingEntity source;
        private final double value;
        private final Cause cause;

        public Post(Player who, LivingEntity source, double value, Cause cause) {
            super(who);
            this.source = source;
            this.value = value;
            this.cause = cause;
        }
    }

    public enum Cause {
        Damage,
        Power
    }
}
