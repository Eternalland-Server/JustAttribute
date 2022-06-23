package com.sakuragame.eternal.justattribute.core.special;

import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import com.sakuragame.eternal.justattribute.core.attribute.AttributeSource;
import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import com.sakuragame.eternal.justattribute.util.Debug;
import com.sakuragame.eternal.justattribute.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class CombatCapacity {

    public final static String DISPLAY_NODE = "display.combat";

    public static List<String> format(int combat) {
        List<String> display = new ArrayList<>();
        ConfigFile.format.combat.forEach(s -> display.add(s.replace("<combat>", Utils.getSource(combat))));

        return display;
    }

    public static int get(AttributeSource data) {
        int combat = 0;

        for (Attribute ident : Attribute.values()) {

            double value = ident
                    .calculate(
                            data.getOrdinary().getOrDefault(ident, 0d),
                            data.getPotency().getOrDefault(ident, 0d)
                    );

            int source = (int) value * ident.getScore();

            Debug.info(Debug.CombatCapacity, ident.getId() + ": " + value + " x " + ident.getScore() + " = " + source);

            combat += source;
        }

        Debug.info(Debug.CombatCapacity, "Total Source: " + combat);

        return combat;
    }
}
