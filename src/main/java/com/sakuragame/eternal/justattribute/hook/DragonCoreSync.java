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

        for (Attribute ident : Attribute.values()) {
            double value = role.getTotalValue(ident);
            if (ident == Attribute.Damage) {
                map.put(ident.getPlaceholder(), ident.format(role.getTotalDamage()));
                continue;
            }
            if (ident == Attribute.Defence) {
                map.put(ident.getPlaceholder(), ident.format(role.getTotalDefence()));
                continue;
            }
            if (ident == Attribute.Health) {
                map.put(ident.getPlaceholder(), ident.format(role.getTotalHealth()));
                continue;
            }
            if (ident == Attribute.Mana) {
                map.put(ident.getPlaceholder(), ident.format(role.getTotalMana()));
                continue;
            }
            map.put(ident.getPlaceholder(), ident.formatting(value));
        }

        PacketSender.sendSyncPlaceholder(player, map);
    }
}
