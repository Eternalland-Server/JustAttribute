package com.sakuragame.eternal.justattribute.listener.build;

import com.sakuragame.eternal.justattribute.core.PetHandler;
import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import com.sakuragame.eternal.justattribute.core.special.EquipQuality;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.api.Item;
import ink.ptms.zaphkiel.api.event.ItemReleaseEvent;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTagData;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.LinkedList;
import java.util.List;

public class PetListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDisplay(ItemReleaseEvent.Display e) {
        String display = e.getItemStream().getZaphkielItem().getDisplay();
        if (!display.equals("PET_COMMON_DISPLAY")) return;

        ItemTag tag = e.getItemStream().getZaphkielData();

        Player player = e.getPlayer();

        List<String> infoDisplay = new LinkedList<>();
        int level = tag.getDeepOrElse(PetHandler.NBT_NODE_LEVEL, new ItemTagData(0)).asInt();
        String showName = tag.getDeepOrElse(PetHandler.NBT_NODE_NAME, new ItemTagData("")).asString();
        int saddle = tag.getDeepOrElse(PetHandler.NBT_NODE_SADDLE, new ItemTagData(0)).asInt();
        infoDisplay.add("   ⓞ &3等级: &f" + level + (level == PetHandler.MAX_LEVEL ? "&e(MAX)" : ""));
        infoDisplay.add("   ⓥ &3名称： &f" + (showName.isEmpty() ? "&7&o未命名" : "&f" + showName));
        infoDisplay.add("   ⓔ &3装配鞍: " + (saddle == 1 ? "&b是" : "&7否"));

        PetHandler.NBT_NODE_EQUIP.values().forEach(elm -> {
            String name = elm.getKey();
            String node = elm.getValue();
            ItemTagData data = tag.getDeep(node);
            if (data == null) {
                infoDisplay.add("   ⓛ &b" + name + ": &7<无>");
            }
            else {
                String id = data.asString();
                Item item = ZaphkielAPI.INSTANCE.getRegisteredItem().get(id);
                infoDisplay.add("   " + item.getName().get("ICON") + " &b" + name + ": &f" + ChatColor.stripColor(MegumiUtil.onReplace(item.getName().get("NAME"))));
            }
        });

        e.addLore(PetHandler.DISPLAY_NODE_INFO, infoDisplay);

        List<String> capacityDisplay = new LinkedList<>();
        for (int i : PetHandler.stage) {
            if (i == 31) {
                EquipQuality quality = EquipQuality.getQuality(tag);
                if (quality == null) continue;
                if (quality.getId() < 5) continue;
            }

            ItemTagData data = tag.getDeep(PetHandler.NBT_NODE_CAPACITY + "." + i);
            if (data == null) {
                capacityDisplay.add("   ☎ " + (player != null ? "&8" + i + "级解锁" : "&7即将解锁"));
            }
            else {
                String[] args = data.asString().split("\\|", 2);
                Attribute ident = Attribute.match(args[0]);
                if (ident == null) continue;
                double value = Double.parseDouble(args[1]);
                capacityDisplay.add(ident.format(value, true));
            }
        }

        e.addLore(PetHandler.DISPLAY_NODE_CAPACITY, capacityDisplay);
    }
}
