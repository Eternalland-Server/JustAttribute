package com.sakuragame.eternal.justattribute.listener.build;

import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import com.sakuragame.eternal.justattribute.core.smithy.SmithyManager;
import com.sakuragame.eternal.justattribute.core.smithy.factory.EnhanceFactory;
import com.sakuragame.eternal.justattribute.core.smithy.factory.TransferFactory;
import com.sakuragame.eternal.justattribute.core.special.EquipClassify;
import com.sakuragame.eternal.justattribute.core.special.PotencyGrade;
import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.api.Item;
import ink.ptms.zaphkiel.api.event.ItemBuildEvent;
import ink.ptms.zaphkiel.api.event.ItemReleaseEvent;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTagData;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTagList;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.LinkedList;
import java.util.List;

public class EquipListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEquipBuild(ItemBuildEvent.Pre e) {
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

        for (Attribute ident : SmithyManager.ATTRIBUTES) {
            double original = item.getData().getDouble(ident.getOrdinaryNode(), -1);
            if (original == -1) continue;
            tag.putDeep(ident.getOrdinaryNode(), EnhanceFactory.calculate(classify, original, level));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEquipRelease(ItemReleaseEvent.Display e) {
        String display = e.getItemStream().getZaphkielItem().getDisplay();
        if (!display.equals("EQUIP_COMMON_DISPLAY")) return;

        Item item = e.getItemStream().getZaphkielItem();
        ItemTag tag = e.getItemStream().getZaphkielData();

        ItemTagData extend = tag.getDeep(TransferFactory.NBT_NODE_EXTENDS);
        if (extend != null) {
            String id = new StringBuilder(extend.asString()).reverse().toString();
            Item exItem = ZaphkielAPI.INSTANCE.getRegisteredItem().get(id);
            if (exItem != null) {
                item = exItem;
                e.addLore(TransferFactory.DISPLAY_NODE_EXTENDS, "   &3已继承: &f" + ChatColor.stripColor(MegumiUtil.onReplace(exItem.getName().get("NAME"))));
            }
        }

        ItemTagData enhance = tag.getDeepOrElse(EnhanceFactory.NBT_NODE_ENHANCE, new ItemTagData(0));
        if (enhance != null) {
            e.addName(EnhanceFactory.DISPLAY_NODE_ENHANCE, "§6§l(+" + enhance.asInt() + ")");
        }

        List<String> ordinaryDisplay = new LinkedList<>();
        for (Attribute attr : Attribute.values()) {
            ItemTagData ordinary = tag.getDeep(attr.getOrdinaryNode());

            if (ordinary != null) {
                double v1 = item.getData().getDouble(attr.getOrdinaryNode(), 0);
                double v2 = ordinary.asDouble();
                ordinaryDisplay.add(attr.format(v1, v2 - v1));
            }
        }
        e.addLore(Attribute.DISPLAY_NODE_ORDINARY, ordinaryDisplay);

        List<String> potencyDisplay = new LinkedList<>();
        ItemTagData gradeData = tag.getDeep(PotencyGrade.NBT_TAG);
        PotencyGrade grade = gradeData == null ? null : PotencyGrade.match(gradeData.asInt());
        if (grade != null) {
            potencyDisplay.add(grade.formatting());
            potencyDisplay.add("");

            ItemTagData potencyData = tag.getDeep(Attribute.NBT_NODE_POTENCY);
            if (potencyData != null) {
                ItemTagList potencyList = potencyData.asList();
                potencyList.forEach(elm -> {
                    String s = elm.asString();
                    Attribute ident = Attribute.match(s.split("\\|", 2)[0]);
                    if (ident != null) {
                        double value = Double.parseDouble(s.split("\\|", 2)[1]);
                        potencyDisplay.add(ident.format(value, true));
                    }
                });
            }
        }

        if (grade == PotencyGrade.NONE) {
            potencyDisplay.add(ConfigFile.potency_empty);
        }

        e.addLore(Attribute.DISPLAY_NODE_POTENCY, potencyDisplay);
    }

    @EventHandler
    public void onPetEquipRelease(ItemReleaseEvent.Display e) {
        String display = e.getItemStream().getZaphkielItem().getDisplay();
        if (!display.equals("PET_EQUIP_COMMON_DISPLAY")) return;

        Item item = e.getItemStream().getZaphkielItem();
        ItemTag tag = e.getItemStream().getZaphkielData();

        List<String> ordinaryDisplay = new LinkedList<>();

        for (Attribute attr : Attribute.values()) {
            ItemTagData ordinary = tag.getDeep(attr.getOrdinaryNode());
            ItemTagData potency = tag.getDeep(attr.getPotencyNode());

            if (ordinary != null) {
                double v = ordinary.asDouble();
                ordinaryDisplay.add(attr.format(v));
            }
        }

        e.addLore(Attribute.DISPLAY_NODE_ORDINARY, ordinaryDisplay);
    }
}
