package com.sakuragame.eternal.justattribute.api.event.smithy;

import com.sakuragame.eternal.justattribute.api.event.JustEvent;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SmithySealEvent {

    @Getter
    public static class Lock extends JustEvent {

        private final ItemStack item;

        public Lock(Player who, ItemStack item) {
            super(who);
            this.item = item;
        }
    }

    @Getter
    public static class Unlock extends JustEvent {

        private final ItemStack item;

        public Unlock(Player who, ItemStack item) {
            super(who);
            this.item = item;
        }
    }
}
