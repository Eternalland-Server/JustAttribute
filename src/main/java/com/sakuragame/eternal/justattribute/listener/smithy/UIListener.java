package com.sakuragame.eternal.justattribute.listener.smithy;

import com.taylorswiftcn.megumi.uifactory.event.comp.UIFCompSubmitEvent;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class UIListener implements Listener {

    @EventHandler
    public void onSubmit(UIFCompSubmitEvent e) {
        Player player = e.getPlayer();
        String screenID = e.getScreenID();
        String compID = e.getCompID();

        if (!screenID.equals("smithy")) return;

        PacketSender.sendOpenGui(player, compID);
    }
}
