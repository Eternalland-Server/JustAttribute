package com.sakuragame.eternal.justattribute.listener;

import com.sakuragame.eternal.justattribute.api.event.JAEquipBoundEvent;
import net.sakuragame.eternal.justmessage.api.MessageAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SoulBoundListener implements Listener {

    @EventHandler
    public void onBound(JAEquipBoundEvent e) {
        Player player = e.getPlayer();
        String display = e.getItem().getItemMeta().getDisplayName();

        MessageAPI.sendActionTip(player, "§a&l装备 " + display + " &a&l已绑定");
    }
}
