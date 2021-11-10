package com.sakuragame.eternal.justattribute.core;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.api.event.JAPlayerAttackEvent;
import com.sakuragame.eternal.justattribute.core.attribute.Identifier;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleAttribute;
import org.bukkit.entity.Player;


public class CombatHandler {

    public static JAPlayerAttackEvent calculate(RoleAttribute attacker, RoleAttribute sufferer) {
        boolean critical = false;
        double damage = getValue(attacker, Identifier.Damage) + getValue(attacker, Identifier.Energy);
        double dp = getValue(attacker, Identifier.Defence_Penetration);
        double cc = getValue(attacker, Identifier.Critical_Chance);
        double cd = getValue(attacker, Identifier.Critical_Damage);

        double defence = getValue(sufferer, Identifier.Defence) + getValue(sufferer, Identifier.Technique);
        double di = getValue(sufferer, Identifier.Damage_Immune);

        double lastDamage = damage - (Math.max(defence - dp, 0));


        if (cc >= 1 || cc > Math.random()) {
            lastDamage = lastDamage * cd;
            critical = true;
        }
        
        lastDamage = lastDamage * (1 - di);

        return new JAPlayerAttackEvent((Player) attacker.getRole(), sufferer.getRole(), lastDamage, critical);
    }

    private static double getValue(RoleAttribute role, Identifier ident) {
        if (role == null) return 0d;
        return JustAttribute.getAttributeManager().getAttribute(ident).calculate(role);
    }
}
