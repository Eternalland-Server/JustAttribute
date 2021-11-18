package com.sakuragame.eternal.justattribute.core.special;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.Identifier;
import com.sakuragame.eternal.justattribute.core.attribute.stats.AttributeData;
import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import com.sakuragame.eternal.justattribute.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class CombatCapacity {

    public final static String DISPLAY_NODE = "display.combat";

    public static List<String> format(int combat) {
        List<String> display = new ArrayList<>();
        ConfigFile.combat_format.forEach(s -> display.add(s.replace("<combat>", Utils.getSource(combat))));

        return display;
    }

    public static int get(AttributeData data) {
        int combat = 0;
        double damage = 0d;

        for (Identifier ident : Identifier.values()) {
            if (ident == Identifier.Critical_Chance || ident == Identifier.Critical_Damage) continue;

            double value = JustAttribute.getAttributeManager().getAttribute(ident)
                    .calculate(
                            data.getOrdinary().get(ident),
                            data.getPotency().get(ident)
                    );
            if (ident == Identifier.Damage || ident == Identifier.Energy) {
                damage += value;
            }

            combat += value * ConfigFile.combatCapability.get(ident);
        }

        double cDamage = (JustAttribute.getAttributeManager().getAttribute(Identifier.Critical_Damage)
                .calculate(
                        data.getOrdinary().get(Identifier.Critical_Damage),
                        data.getPotency().get(Identifier.Critical_Damage)
                ) - 1) * damage;
        double cChance = JustAttribute.getAttributeManager().getAttribute(Identifier.Critical_Chance)
                .calculate(
                        data.getOrdinary().get(Identifier.Critical_Chance),
                        data.getPotency().get(Identifier.Critical_Chance)
                ) * cDamage;

        combat += cDamage * ConfigFile.combatCapability.get(Identifier.Critical_Damage);
        combat += cChance * ConfigFile.combatCapability.get(Identifier.Critical_Chance);

        return combat;
    }
}
