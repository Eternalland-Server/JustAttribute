package com.sakuragame.eternal.justattribute.listener.smithy;

import com.sakuragame.eternal.justattribute.api.event.smithy.SmithyIdentifyEvent;
import com.sakuragame.eternal.justattribute.core.special.PotencyGrade;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class CommonListener implements Listener {

    @EventHandler
    public void onIdentify(SmithyIdentifyEvent e) {
        Player player = e.getPlayer();
        PotencyGrade grade = e.getGrade();
        ItemStack item = e.getItem();

        grade.sendBroadcast(player, item);
    }
}
