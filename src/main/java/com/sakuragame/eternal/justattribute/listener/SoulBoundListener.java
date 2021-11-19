package com.sakuragame.eternal.justattribute.listener;

import com.sakuragame.eternal.justattribute.api.event.JAEquipBoundEvent;
import com.sakuragame.eternal.justattribute.util.Utils;
import net.sakuragame.eternal.dragoncore.api.CoreAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SoulBoundListener implements Listener {

    @EventHandler
    public void onBound(JAEquipBoundEvent e) {
        Player player = e.getPlayer();
        String current = CoreAPI.getOpenScreen(player.getUniqueId());
        String display = e.getItem().getItemMeta().getDisplayName();

        Utils.sendActionTip(player, current, "§a&l装备 " + display + " &a&l已绑定");
    }
}
