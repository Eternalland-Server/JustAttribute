package com.sakuragame.eternal.justattribute.api.event.smithy;

import com.sakuragame.eternal.justattribute.api.event.JustEvent;
import com.sakuragame.eternal.justattribute.core.special.PotencyGrade;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
public class SmithyIdentifyEvent extends JustEvent {

    private final PotencyGrade grade;
    private final ItemStack item;

    public SmithyIdentifyEvent(Player who, PotencyGrade grade, ItemStack item) {
        super(who);
        this.grade = grade;
        this.item = item;
    }
}
