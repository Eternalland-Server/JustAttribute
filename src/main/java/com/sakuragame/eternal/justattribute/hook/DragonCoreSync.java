package com.sakuragame.eternal.justattribute.hook;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleAttribute;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class DragonCoreSync {

    public static void send(Player player) {
        HashMap<String, String> map = new HashMap<>();

        RoleAttribute role = JustAttribute.getRoleManager().getPlayerAttribute(player.getUniqueId());

        for (Attribute attr : Attribute.values()) {
            double value = role.getTotalValue(attr);
            map.put(attr.getPlaceholder(), attr.formatting(value));
        }

        PacketSender.sendSyncPlaceholder(player, map);
    }
}
