package com.sakuragame.eternal.justattribute.api.event;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
public class EquipBoundEvent extends JustEvent {

    private final ItemStack item;

    public EquipBoundEvent(Player who, ItemStack item) {
        super(who);
        this.item = item;
    }
}
