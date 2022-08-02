package com.sakuragame.eternal.justattribute.listener.smithy;

import com.sakuragame.eternal.justattribute.core.smithy.SmithyManager;
import com.taylorswiftcn.megumi.uifactory.event.comp.UIFCompSubmitEvent;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class UIListener implements Listener {

    @EventHandler
    public void onSubmit(UIFCompSubmitEvent e) {
        Player player = e.getPlayer();
        String screenID = e.getScreenID();
        String compID = e.getCompID();

        if (!screenID.equals("smithy")) return;

        PacketSender.sendOpenGui(player, compID);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        SmithyManager.clearSlot(player);
    }
}
