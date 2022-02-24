package com.sakuragame.eternal.justattribute.api.event;

import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class ItemExpiredEvent extends JustEvent {

    private final String itemID;
    private final String itemName;

    public ItemExpiredEvent(Player who, String itemID, String itemName) {
        super(who);
        this.itemID = itemID;
        this.itemName = itemName;
    }
}
