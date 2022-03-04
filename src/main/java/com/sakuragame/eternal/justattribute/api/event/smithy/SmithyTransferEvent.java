package com.sakuragame.eternal.justattribute.api.event.smithy;

import com.sakuragame.eternal.justattribute.api.event.JustEvent;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
public class SmithyTransferEvent extends JustEvent {

    private final ItemStack transfer;
    private final ItemStack accepter;

    public SmithyTransferEvent(Player who, ItemStack transfer, ItemStack accepter) {
        super(who);
        this.transfer = transfer;
        this.accepter = accepter;
    }
}
