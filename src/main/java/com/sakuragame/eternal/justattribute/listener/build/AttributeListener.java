package com.sakuragame.eternal.justattribute.listener.build;

import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import com.sakuragame.eternal.justattribute.core.smithy.factory.EnhanceFactory;
import com.sakuragame.eternal.justattribute.core.special.PotencyGrade;
import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import ink.ptms.zaphkiel.api.event.ItemBuildEvent;
import ink.ptms.zaphkiel.api.event.ItemReleaseEvent;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTagData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.LinkedList;

public class AttributeListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBuild(ItemBuildEvent.Pre e) {
        String display = e.getItemStream().getZaphkielItem().getDisplay();
        if (!display.equals("EQUIP_COMMON_DISPLAY")) return;
        if (e.getPlayer() == null) return;

        ItemTag tag = e.getItemStream().getZaphkielData();
        ItemTagData autoEnhance = tag.getDeep(Attribute.NBT_NODE_ORDINARY + "._auto_enhance_");
        if (autoEnhance == null) return;

        double original = tag.getDeep(EnhanceFactory.NBT_NODE_ORIGINAL).asDouble();
        int level = autoEnhance.asInt();

        tag.removeDeep(Attribute.NBT_NODE_ORDINARY + "._auto_enhance_");
        tag.putDeep(EnhanceFactory.NBT_NODE_ENHANCE, level);

        for (Attribute ident : EnhanceFactory.ATTRIBUTES) {
            tag.putDeep(ident.getOrdinaryNode(), EnhanceFactory.calculate(original, level));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDisplay(ItemReleaseEvent.Display e) {
        String display = e.getItemStream().getZaphkielItem().getDisplay();
        if (!display.equals("EQUIP_COMMON_DISPLAY")) return;

        ItemTag tag = e.getItemStream().getZaphkielData();

        LinkedList<String> ordinaryDisplay = new LinkedList<>();
        LinkedList<String> potencyDisplay = new LinkedList<>();

        ItemTagData gradeData = tag.getDeep(PotencyGrade.NBT_TAG);
        PotencyGrade grade = gradeData == null ? null : PotencyGrade.match(gradeData.asInt());
        if (grade != null) {
            potencyDisplay.add(grade.formatting());
            potencyDisplay.add("");
        }

        ItemTagData original = tag.getDeep(EnhanceFactory.NBT_NODE_ORIGINAL);
        ItemTagData enhance = tag.getDeep(EnhanceFactory.NBT_NODE_ENHANCE);

        for (Attribute attr : Attribute.values()) {
            ItemTagData ordinary = tag.getDeep(attr.getOrdinaryNode());
            ItemTagData potency = tag.getDeep(attr.getPotencyNode());

            if (ordinary != null) {
                double v = ordinary.asDouble();
                ordinaryDisplay.add(
                        original == null ?
                                attr.format(v, attr.isOnlyPercent()) :
                                attr.format(original.asDouble(), v - original.asDouble())
                );
            }

            if (potency != null && grade != null) {
                double v = potency.asDouble();
                potencyDisplay.add(attr.format(v, true));
            }
        }

        if (grade == PotencyGrade.NONE) {
            potencyDisplay.add(ConfigFile.potency_empty);
        }

        if (enhance != null) {
            e.addName(Attribute.DISPLAY_NODE_ENHANCE, "§6§l(+" + enhance.asInt() + ")");
        }

        e.addLore(Attribute.DISPLAY_NODE_ORDINARY, ordinaryDisplay);
        e.addLore(Attribute.DISPLAY_NODE_POTENCY, potencyDisplay);
    }

}
