package com.sakuragame.eternal.justattribute.listener.build;

import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import com.sakuragame.eternal.justattribute.core.special.PotencyGrade;
import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import com.sakuragame.eternal.justattribute.util.Utils;
import ink.ptms.zaphkiel.api.event.ItemBuildEvent;
import ink.ptms.zaphkiel.api.event.ItemReleaseEvent;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTagData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.LinkedList;

public class AttributeListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBuild(ItemBuildEvent.Pre e) {
        if (e.isCancelled()) return;

        String display = e.getItemStream().getZaphkielItem().getDisplay();
        if (!(display.equals("EQUIP_COMMON_DISPLAY") || display.equals("SKIN_COMMON_DISPLAY"))) return;

        ItemTag tag = e.getItemStream().getZaphkielData();

        for (Attribute attr :Attribute.values()) {
            ItemTagData ordinary = tag.getDeep(attr.getOrdinaryNode());
            ItemTagData potency = tag.getDeep(attr.getPotencyNode());

            if (ordinary != null) {
                String s = ordinary.asString();
                if (s.contains("-")) {
                    int random = Utils.getRangeValue(s);
                    tag.putDeep(attr.getOrdinaryNode(), random);
                }
            }
            if (potency != null) {
                String s = potency.asString();
                if (s.contains("-")) {
                    double random = Utils.getRangeValue(s);
                    tag.putDeep(attr.getPotencyNode(), random);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDisplay(ItemReleaseEvent.Display e) {
        String display = e.getItemStream().getZaphkielItem().getDisplay();
        if (!(display.equals("EQUIP_COMMON_DISPLAY") || display.equals("SKIN_COMMON_DISPLAY"))) return;

        ItemTag tag = e.getItemStream().getZaphkielData();

        LinkedList<String> ordinaryDisplay = new LinkedList<>();
        LinkedList<String> potencyDisplay = new LinkedList<>();

        ItemTagData gradeData = tag.getDeep(PotencyGrade.NBT_TAG);
        PotencyGrade grade = gradeData == null ? null : PotencyGrade.match(gradeData.asInt());
        if (grade != null) {
            potencyDisplay.add(grade.formatting());
            potencyDisplay.add("");
        }

        for (Attribute attr : Attribute.values()) {
            ItemTagData ordinary = tag.getDeep(attr.getOrdinaryNode());
            ItemTagData potency = tag.getDeep(attr.getPotencyNode());

            if (ordinary != null) {
                ordinaryDisplay.add(attr.format(ordinary.asDouble()));
            }
            if (potency != null && grade != null) {
                potencyDisplay.add(attr.format(potency.asDouble(), true));
            }
        }

        if (grade == PotencyGrade.NONE) {
            potencyDisplay.add(ConfigFile.potency_empty);
        }

        e.addLore(Attribute.ORDINARY_DISPLAY_NODE, ordinaryDisplay);
        e.addLore(Attribute.POTENCY_DISPLAY_NODE, potencyDisplay);
    }


}
