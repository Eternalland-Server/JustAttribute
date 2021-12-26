package com.sakuragame.eternal.justattribute.core.special;

import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.AttributeData;
import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import com.sakuragame.eternal.justattribute.util.Debug;
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

        for (Attribute ident : Attribute.values()) {
            if (ident == Attribute.Critical_Chance || ident == Attribute.Critical_Damage) continue;

            double value = ident
                    .calculate(
                            data.getOrdinary().get(ident),
                            data.getPotency().get(ident)
                    );
            if (ident == Attribute.Damage || ident == Attribute.Energy) {
                damage += value;
            }

            int source = (int) value * ConfigFile.combatCapability.get(ident);

            Debug.info(Debug.CombatCapacity, ident.getId() + ": " + value + " x " + ConfigFile.combatCapability.get(ident) + " = " + source);

            combat += source;
        }

        double cDamage = Math.max(0,
                Attribute.Critical_Damage
                        .calculate(
                            data.getOrdinary().get(Attribute.Critical_Damage),
                            data.getPotency().get(Attribute.Critical_Damage)
                        ) - 1) * damage;
        double cChance = Math.min(0,
                Attribute.Critical_Chance
                        .calculate(
                                data.getOrdinary().get(Attribute.Critical_Chance),
                                data.getPotency().get(Attribute.Critical_Chance)
                        )) * cDamage;

        int cdSource = (int) cDamage * ConfigFile.combatCapability.get(Attribute.Critical_Damage);
        int ccSource = (int) cChance * ConfigFile.combatCapability.get(Attribute.Critical_Chance);

        Debug.info(Debug.CombatCapacity, Attribute.Critical_Damage.getId() + ": " + cDamage + " x " + ConfigFile.combatCapability.get(Attribute.Critical_Damage) + " = " + cdSource);
        Debug.info(Debug.CombatCapacity, Attribute.Critical_Chance.getId() + ": " + cChance + " x " + ConfigFile.combatCapability.get(Attribute.Critical_Chance) + " = " + ccSource);

        combat += cdSource;
        combat += ccSource;

        Debug.info(Debug.CombatCapacity, "Total Source: " + combat);

        return combat;
    }
}
