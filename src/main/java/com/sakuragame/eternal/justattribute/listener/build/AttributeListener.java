package com.sakuragame.eternal.justattribute.listener.build;

import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import com.sakuragame.eternal.justattribute.core.smithy.factory.EnhanceFactory;
import com.sakuragame.eternal.justattribute.core.smithy.factory.TransferFactory;
import com.sakuragame.eternal.justattribute.core.special.EquipClassify;
import com.sakuragame.eternal.justattribute.core.special.PotencyGrade;
import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.api.Item;
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

        Item item = e.getItem();
        ItemTag tag = e.getItemStream().getZaphkielData();

        EquipClassify classify = EquipClassify.getClassify(tag);
        if (classify == null) return;

        ItemTagData autoEnhance = tag.getDeep(EnhanceFactory.NBT_NODE_PRE_ENHANCE);
        if (autoEnhance == null) return;

        int level = autoEnhance.asInt();

        tag.removeDeep(EnhanceFactory.NBT_NODE_PRE_ENHANCE);
        tag.putDeep(EnhanceFactory.NBT_NODE_ENHANCE, level);

        for (Attribute ident : EnhanceFactory.ATTRIBUTES) {
            double original = item.getData().getDouble(ident.getOrdinaryNode(), -1);
            if (original == -1) continue;
            tag.putDeep(ident.getOrdinaryNode(), EnhanceFactory.calculate(classify, original, level));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDisplay(ItemReleaseEvent.Display e) {
        String display = e.getItemStream().getZaphkielItem().getDisplay();
        if (!display.equals("EQUIP_COMMON_DISPLAY")) return;

        Item item = e.getItemStream().getZaphkielItem();
        ItemTag tag = e.getItemStream().getZaphkielData();

        ItemTagData extend = tag.getDeep(TransferFactory.NBT_NODE_EXTENDS);
        if (extend != null) {
            Item exItem = ZaphkielAPI.INSTANCE.getRegisteredItem().get(extend.asString());
            if (exItem != null) item = exItem;
        }

        LinkedList<String> ordinaryDisplay = new LinkedList<>();
        LinkedList<String> potencyDisplay = new LinkedList<>();

        ItemTagData gradeData = tag.getDeep(PotencyGrade.NBT_TAG);
        PotencyGrade grade = gradeData == null ? null : PotencyGrade.match(gradeData.asInt());
        if (grade != null) {
            potencyDisplay.add(grade.formatting());
            potencyDisplay.add("");
        }

        ItemTagData enhance = tag.getDeepOrElse(EnhanceFactory.NBT_NODE_ENHANCE, new ItemTagData(0));

        for (Attribute attr : Attribute.values()) {
            ItemTagData ordinary = tag.getDeep(attr.getOrdinaryNode());
            ItemTagData potency = tag.getDeep(attr.getPotencyNode());

            if (ordinary != null) {
                double v1 = item.getData().getDouble(attr.getOrdinaryNode(), -1);
                double v2 = ordinary.asDouble();
                ordinaryDisplay.add(
                        v1 == -1 ?
                                attr.format(v2, attr.isOnlyPercent()) :
                                attr.format(v1, v2 - v1)
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
            e.addName(EnhanceFactory.DISPLAY_NODE_ENHANCE, "§6§l(+" + enhance.asInt() + ")");
        }

        e.addLore(Attribute.DISPLAY_NODE_ORDINARY, ordinaryDisplay);
        e.addLore(Attribute.DISPLAY_NODE_POTENCY, potencyDisplay);
    }

}
