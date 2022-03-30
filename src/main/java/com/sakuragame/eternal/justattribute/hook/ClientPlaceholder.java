package com.sakuragame.eternal.justattribute.hook;

import com.sakuragame.eternal.justattribute.api.JustAttributeAPI;
import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleState;
import com.sakuragame.eternal.justattribute.util.Utils;
import com.taylorswiftcn.justwei.util.UnitConvert;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import net.sakuragame.eternal.justlevel.api.JustLevelAPI;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ClientPlaceholder {

    public final static String RESTORE_HP_PAPI = "attribute_restore_hp";
    public final static String RESTORE_MP_PAPI = "attribute_restore_mp";
    public final static String DAMAGE_PROMOTE_PAPI = "attribute_damage_promote";
    public final static String DEFENCE_PROMOTE_PAPI = "attribute_defence_promote";
    public final static String ROLE_DAMAGE_PAPI = "attribute_role_damage";
    public final static String ROLE_DEFENCE_PAPI = "attribute_role_defence";

    public final static String ROLE_CURRENT_HEALTH = "attribute_role_current_health";
    public final static String ROLE_MAX_HEALTH = "attribute_role_max_health";
    public final static String ROLE_CURRENT_MANA = "attribute_role_current_mana";
    public final static String ROLE_MAX_MANA = "attribute_role_max_mana";

    public final static String ROLE_TOTAL_COMBAT = "attribute_role_total_combat";
    public final static String ROLE_DAMAGE_UPPER_LIMIT = "attribute_role_damage_upper_limit";

    public final static String EXP_ADDITION_SUPPORT = "exp_addition_support";
    public final static String EXP_ADDITION_EQUIP = "exp_addition_equip";
    public final static String EXP_ADDITION_CARD = "exp_addition_card";

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
            if (ident == Attribute.EXP_Addition) {
                double total = JustLevelAPI.getTotalAddition(player);
                map.put(ident.getPlaceholder(), ident.formatting(value + total));
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
        map.put(ROLE_DAMAGE_UPPER_LIMIT, UnitConvert.formatCN(UnitConvert.TenThousand, role.getDamageUpperLimit()));

        map.put(EXP_ADDITION_SUPPORT, formatPercent(JustLevelAPI.getSupportAddition(player)));
        map.put(EXP_ADDITION_EQUIP, "+" + Attribute.EXP_Addition.formatting(role.getTotalValue(Attribute.EXP_Addition)));
        map.put(EXP_ADDITION_CARD, formatPercent(JustLevelAPI.getCardAddition(player)));

        PacketSender.sendSyncPlaceholder(player, map);
    }

    public static Map<String, String> getTargetPlaceholder(Player target) {
        HashMap<String, String> map = new HashMap<>();

        RoleAttribute role = JustAttributeAPI.getRoleAttribute(target);
        RoleState state = JustAttributeAPI.getRoleState(target);

        for (Attribute ident : Attribute.values()) {
            double value = role.getTotalValue(ident);
            if (ident == Attribute.Damage) {
                map.put("target_" + ident.getPlaceholder(), ident.formatting(role.getActualDamage()));
                continue;
            }
            if (ident == Attribute.Defence) {
                map.put("target_" + ident.getPlaceholder(), ident.formatting(role.getActualDefence()));
                continue;
            }
            if (ident == Attribute.Health) {
                map.put("target_" + ident.getPlaceholder(), ident.formatting(role.getTotalHealth()));
                continue;
            }
            if (ident == Attribute.Mana) {
                map.put("target_" + ident.getPlaceholder(), ident.formatting(role.getTotalMana()));
                continue;
            }
            if (ident == Attribute.EXP_Addition) {
                double total = JustLevelAPI.getTotalAddition(target);
                map.put("target_" + ident.getPlaceholder(), ident.formatting(value + total));
                continue;
            }
            map.put("target_" + ident.getPlaceholder(), ident.formatting(value));
        }

        map.put("target_" + RESTORE_HP_PAPI, formatRestoreHP(state.getRestoreHP()));
        map.put("target_" + RESTORE_MP_PAPI, formatRestoreMP(state.getRestoreMP()));
        map.put("target_" + ROLE_DAMAGE_PAPI, Attribute.Damage.formatting(role.getTotalDamage()));
        map.put("target_" + ROLE_DEFENCE_PAPI, Attribute.Defence.formatting(role.getTotalDefence()));
        map.put("target_" + ROLE_TOTAL_COMBAT, UnitConvert.formatCN(UnitConvert.TenThousand, role.getCombat()));

        return map;
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
