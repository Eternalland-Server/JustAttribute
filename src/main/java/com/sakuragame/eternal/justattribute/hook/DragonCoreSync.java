package com.sakuragame.eternal.justattribute.hook;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.api.JustAttributeAPI;
import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleState;
import com.sakuragame.eternal.justattribute.util.Utils;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class DragonCoreSync {

    private final static String RESTORE_HP_PAPI = "attribute_restore_hp";
    private final static String RESTORE_MP_PAPI = "attribute_restore_mp";
    private final static String DAMAGE_PROMOTE_PAPI = "attribute_damage_promote";
    private final static String DEFENCE_PROMOTE_PAPI = "attribute_defence_promote";
    private final static String ROLE_DAMAGE_PAPI = "attribute_role_damage";
    private final static String ROLE_DEFENCE_PAPI = "attribute_role_defence";
    private final static String ROLE_CURRENT_MANA = "attribute_role_current_mana";
    private final static String ROLE_MAX_MANA = "attribute_role_max_mana";

    public static void sendAttribute(Player player) {
        HashMap<String, String> map = new HashMap<>();

        RoleAttribute role = JustAttribute.getRoleManager().getPlayerAttribute(player.getUniqueId());
        RoleState state = JustAttribute.getRoleManager().getPlayerState(player.getUniqueId());

        for (Attribute ident : Attribute.values()) {
            double value = role.getTotalValue(ident);
            if (ident == Attribute.Damage) {
                map.put(ident.getPlaceholder(), ident.formatting(role.getActualDamage()));
                continue;
            }
            if (ident == Attribute.Defence) {
                map.put(ident.getPlaceholder(), ident.formatting(role.getActualDefence()));
                continue;
            }
            if (ident == Attribute.Health) {
                map.put(ident.getPlaceholder(), ident.formatting(role.getTotalHealth()));
                continue;
            }
            if (ident == Attribute.Mana) {
                map.put(ident.getPlaceholder(), ident.formatting(role.getTotalMana()));
                continue;
            }
            map.put(ident.getPlaceholder(), ident.formatting(value));
        }

        map.put(RESTORE_HP_PAPI, formatRestoreHP(state.getRestoreHP()));
        map.put(RESTORE_MP_PAPI, formatRestoreMP(state.getRestoreMP()));
        map.put(DAMAGE_PROMOTE_PAPI, formatPercent(Utils.getDamagePromote(player) - 1));
        map.put(DEFENCE_PROMOTE_PAPI, formatPercent(Utils.getDefencePromote(player) - 1));
        map.put(ROLE_DAMAGE_PAPI, Attribute.Damage.formatting(role.getTotalDamage()));
        map.put(ROLE_DEFENCE_PAPI, Attribute.Defence.formatting(role.getTotalDefence()));

        PacketSender.sendSyncPlaceholder(player, map);
    }

    public static void sendMana(Player player) {
        HashMap<String, String> map = new HashMap<>();

        RoleState state = JustAttributeAPI.getRoleState(player.getUniqueId());
        map.put(ROLE_CURRENT_MANA, String.valueOf((int) state.getMana()));
        map.put(ROLE_MAX_MANA, String.valueOf((int) state.getMaxMana()));

        System.out.println(map);

        PacketSender.sendSyncPlaceholder(player, map);
    }


    public static String formatRestoreHP(double value) {
        return Utils.a.format(value) + " HP/s";
    }

    public static String formatRestoreMP(double value) {
        return Utils.a.format(value) + " MP/s";
    }

    public static String formatPercent(double value) {
        return "+" + Utils.a.format(value * 100) + "%";
    }
}
