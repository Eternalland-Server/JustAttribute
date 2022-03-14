package com.sakuragame.eternal.justattribute.api.event.smithy;

import com.sakuragame.eternal.justattribute.api.event.JustEvent;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
public class SmithyBoostEvent extends JustEvent {

    private final ItemStack equip;
    private final int boost;

    public SmithyBoostEvent(Player who, ItemStack equip, int boost) {
        super(who);
        this.equip = equip;
        this.boost = boost;
    }
}
