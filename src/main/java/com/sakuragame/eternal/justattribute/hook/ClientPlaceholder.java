package com.sakuragame.eternal.justattribute.hook;

import com.sakuragame.eternal.justattribute.api.JustAttributeAPI;
import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleState;
import com.sakuragame.eternal.justattribute.util.Utils;
import com.taylorswiftcn.justwei.util.UnitConvert;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class ClientPlaceholder {

    private final static String RESTORE_HP_PAPI = "attribute_restore_hp";
    private final static String RESTORE_MP_PAPI = "attribute_restore_mp";
    private final static String DAMAGE_PROMOTE_PAPI = "attribute_damage_promote";
    private final static String DEFENCE_PROMOTE_PAPI = "attribute_defence_promote";
    private final static String ROLE_DAMAGE_PAPI = "attribute_role_damage";
    private final static String ROLE_DEFENCE_PAPI = "attribute_role_defence";

    private final static String ROLE_CURRENT_HEALTH = "attribute_role_current_health";
    private final static String ROLE_MAX_HEALTH = "attribute_role_max_health";
    private final static String ROLE_CURRENT_MANA = "attribute_role_current_mana";
    private final static String ROLE_MAX_MANA = "attribute_role_max_mana";

    private final static String ROLE_TOTAL_COMBAT = "attribute_role_total_combat";

    public static void sendAttribute(Player player) {
        HashMap<String, String> map = new HashMap<>();

        RoleAttribute role = JustAttributeAPI.getRoleAttribute(player);
        RoleState state = JustAttributeAPI.getRoleState(player);

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
        map.put(DAMAGE_PROMOTE_PAPI, formatPercent(Utils.getRealmDamagePromote(player) - 1));
        map.put(DEFENCE_PROMOTE_PAPI, formatPercent(Utils.getRealmDefencePromote(player) - 1));
        map.put(ROLE_DAMAGE_PAPI, Attribute.Damage.formatting(role.getTotalDamage()));
        map.put(ROLE_DEFENCE_PAPI, Attribute.Defence.formatting(role.getTotalDefence()));
        map.put(ROLE_TOTAL_COMBAT, UnitConvert.formatCN(UnitConvert.TenThousand, role.getCombat()));

        PacketSender.sendSyncPlaceholder(player, map);
    }

    public static void sendState(Player player) {
        HashMap<String, String> map = new HashMap<>();

        RoleState state = JustAttributeAPI.getRoleState(player.getUniqueId());
        map.put(ROLE_CURRENT_HEALTH, String.valueOf((int) state.getHealth()));
        map.put(ROLE_MAX_HEALTH, String.valueOf((int) state.getMaxHealth()));
        map.put(ROLE_CURRENT_MANA, String.valueOf((int) state.getMana()));
        map.put(ROLE_MAX_MANA, String.valueOf((int) state.getMaxMana()));

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
