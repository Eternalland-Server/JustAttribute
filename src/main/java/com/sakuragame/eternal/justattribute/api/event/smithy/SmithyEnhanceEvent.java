package com.sakuragame.eternal.justattribute.api.event.smithy;

import com.sakuragame.eternal.justattribute.api.event.JustEvent;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
public class SmithyEnhanceEvent extends JustEvent {

    private final ItemStack equip;
    private final int level;
    private final boolean success;

    public SmithyEnhanceEvent(Player who, ItemStack equip, int level, boolean success) {
        super(who);
        this.equip = equip;
        this.level = level;
        this.success = success;
    }
}
