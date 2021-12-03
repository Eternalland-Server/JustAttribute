package com.sakuragame.eternal.justattribute.util;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.sakuragame.eternal.justattribute.JustAttribute;
import org.bukkit.entity.Player;

public class CombatUtil {

    public static void offhandAnimation(Player player) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ANIMATION);
        packet.getIntegers().write(0, player.getEntityId());
        packet.getIntegers().write(1, 3);
        try {
            JustAttribute.getProtocolManager().sendServerPacket(player, packet);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
