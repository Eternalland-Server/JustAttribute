package com.sakuragame.eternal.justattribute.hook;

import com.sakuragame.eternal.justattribute.api.JustAttributeAPI;
import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import com.sakuragame.eternal.justattribute.core.attribute.character.PlayerCharacter;
import com.sakuragame.eternal.justattribute.util.Utils;
import com.taylorswiftcn.justwei.util.UnitConvert;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import net.sakuragame.eternal.justlevel.api.JustLevelAPI;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClientPlaceholder {

    protected final static String RESTORE_HP_PER_SEC = "restore_hp_ps";
    protected final static String RESTORE_MP_PER_SEC = "restore_mp_ps";
    protected final static String DAMAGE_VAMPIRE_PRE_ATTACK = "damage_vampire_pa";
    protected final static String SKILL_VAMPIRE_PRE_ATTACK = "skill_vampire_pa";
    protected final static String REALM_DAMAGE_PROMOTE = "realm_damage_promote";
    protected final static String REALM_DEFENCE_PROMOTE = "realm_defence_promote";
    protected final static String SUPPORT_EXP_ADDITION = "support_exp_addition";
    protected final static String EQUIP_EXP_ADDITION = "equip_exp_addition";
    protected final static String CARD_EXP_ADDITION = "card_exp_addition";
    protected final static String ROLE_COMBAT_POWER = "role_combat_power";


    protected final static String ROLE_CURRENT_HP = "role_current_hp";
    protected final static String ROLE_CURRENT_MP = "role_current_mp";
    protected final static String ROLE_MAX_HP = "role_max_hp";
    protected final static String ROLE_MAX_MP = "role_max_mp";

    public static void sendAttribute(Player player) {
        UUID uuid = player.getUniqueId();
        Map<String, String> placeholder = new HashMap<>();

        PlayerCharacter role = JustAttributeAPI.getRoleCharacter(uuid);

        for (Attribute ident : Attribute.values()) {
            double value = role.getAttributeValue(ident);

            if (ident == Attribute.EXP_Addition) {
                double addition = JustLevelAPI.getTotalAddition(uuid);
                placeholder.put(ident.getPlaceholder(), ident.formatting(value + addition));
                continue;
            }

            placeholder.put(ident.getPlaceholder(), ident.formatting(value));
        }

        placeholder.put(RESTORE_HP_PER_SEC, "+" + Utils.INTEGRAL.format(role.getAttributeValue(Attribute.Energy) / 100) + "HP/s");
        placeholder.put(RESTORE_MP_PER_SEC, "+" + Utils.INTEGRAL.format(role.getAttributeValue(Attribute.Stamina) / 100) + "MP/s");

        placeholder.put(DAMAGE_VAMPIRE_PRE_ATTACK, "+" + Utils.INTEGRAL.format(role.getDamageVampire()));
        placeholder.put(SKILL_VAMPIRE_PRE_ATTACK, "+" + Utils.INTEGRAL.format(role.getSkillVampire()));

        placeholder.put(REALM_DAMAGE_PROMOTE, "+" + Utils.INTEGRAL.format((Utils.getRealmDamagePromote(uuid) - 1) * 100) + "%");
        placeholder.put(REALM_DEFENCE_PROMOTE, "+" + Utils.INTEGRAL.format((Utils.getRealmDefencePromote(uuid) - 1) * 100) + "%");

        placeholder.put(SUPPORT_EXP_ADDITION, "+" + Utils.INTEGRAL.format(JustLevelAPI.getSupportAddition(uuid) * 100) + "%");
        placeholder.put(EQUIP_EXP_ADDITION, "+" + Utils.INTEGRAL.format(role.getAttributeValue(Attribute.EXP_Addition) * 100) + "%");
        placeholder.put(CARD_EXP_ADDITION, "+" + Utils.INTEGRAL.format(JustLevelAPI.getCardAddition(uuid) * 100) + "%");

        int combat = role.getCombatValue();
        placeholder.put(ROLE_COMBAT_POWER, combat < 100000 ? combat + "" : UnitConvert.formatCN(UnitConvert.TenThousand, role.getCombatValue()));

        PacketSender.sendSyncPlaceholder(player, placeholder);
    }

    public static void sendState(Player player) {
        UUID uuid = player.getUniqueId();
        Map<String, String> placeholder = new HashMap<>();

        PlayerCharacter role = JustAttributeAPI.getRoleCharacter(uuid);

        placeholder.put(ROLE_CURRENT_HP, (int) role.getHP() + "");
        placeholder.put(ROLE_CURRENT_MP, (int) role.getMP() + "");
        placeholder.put(ROLE_MAX_HP, (int) role.getMaxHP() + "");
        placeholder.put(ROLE_MAX_MP, (int) role.getMaxMP() + "");

        PacketSender.sendSyncPlaceholder(player, placeholder);
    }

    public static Map<String, String> getAttribute(Player player) {
        UUID uuid = player.getUniqueId();
        Map<String, String> placeholder = new HashMap<>();

        PlayerCharacter role = JustAttributeAPI.getRoleCharacter(uuid);
        if (role == null) return placeholder;

        for (Attribute ident : Attribute.values()) {
            double value = role.getAttributeValue(ident);

            if (ident == Attribute.EXP_Addition) {
                double addition = JustLevelAPI.getTotalAddition(uuid);
                placeholder.put("role_" + ident.getPlaceholder(), ident.formatting(value + addition));
                continue;
            }

            placeholder.put("role_" + ident.getPlaceholder(), ident.formatting(value));
        }

        int combat = role.getCombatValue();
        placeholder.put("target_" + ROLE_COMBAT_POWER, combat < 100000 ? combat + "" : UnitConvert.formatCN(UnitConvert.TenThousand, role.getCombatValue()));

        return placeholder;
    }
}
