package com.sakuragame.eternal.justattribute.util;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class CombatUtil {

    public static void offhandAnimation(Player player) {
        final EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
        final PlayerConnection playerConnection = entityPlayer.playerConnection;
        final PacketPlayOutAnimation packetPlayOutAnimation = new PacketPlayOutAnimation(entityPlayer, 3);
        playerConnection.sendPacket(packetPlayOutAnimation);
        playerConnection.a(new PacketPlayInArmAnimation(EnumHand.OFF_HAND));
    }
}
