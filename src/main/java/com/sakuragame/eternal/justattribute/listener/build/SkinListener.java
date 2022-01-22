package com.sakuragame.eternal.justattribute.listener.build;

import de.tr7zw.nbtapi.NBTItem;
import ink.ptms.zaphkiel.api.event.ItemReleaseEvent;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTagData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class SkinListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSkin(ItemReleaseEvent.Final e) {
        ItemTag itemTag = e.getItemStream().getZaphkielData();

        ItemTagData data = itemTag.getDeep("justattribute.skin");
        if (data == null) return;

        String skin = data.asString();

        ItemStack item = e.getItemStack();
        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setString("skin", skin);
        nbtItem.applyNBT(item);

        e.setItemStack(item);
    }
}
