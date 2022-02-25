package com.sakuragame.eternal.justattribute.api.event.role;

import com.sakuragame.eternal.justattribute.api.event.JustEvent;
import lombok.Getter;
import org.bukkit.entity.Player;

public class RoleConsumeManaEvent {

    @Getter
    public static class Pre extends JustEvent {

        private double amount;

        public Pre(Player who, double amount) {
            super(who);
            this.amount = amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }
    }

    @Getter
    public static class Post extends JustEvent {

        private final double amount;

        public Post(Player who, double amount) {
            super(who);
            this.amount = amount;
        }
    }
}
