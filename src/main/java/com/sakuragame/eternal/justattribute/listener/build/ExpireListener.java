package com.sakuragame.eternal.justattribute.listener.build;

import com.sakuragame.eternal.justattribute.api.event.ItemExpiredEvent;
import com.sakuragame.eternal.justattribute.core.special.ItemExpire;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import ink.ptms.zaphkiel.api.event.ItemBuildEvent;
import ink.ptms.zaphkiel.api.event.ItemReleaseEvent;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTagData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ExpireListener implements Listener {

    @EventHandler
    public void onBuild(ItemBuildEvent.Pre e) {
        if (e.isCancelled()) return;
        if (e.getPlayer() == null) return;

        ItemTag tag = e.getItemStream().getZaphkielData();
        ItemTagData data = tag.getDeep(ItemExpire.NBT_CONFIG_NODE);
        if (data == null) return;

        String value = data.asString();
        if (value.equalsIgnoreCase("MonthEnd")) {
            long expired = ItemExpire.getMonthEndDay();
            tag.removeDeep(ItemExpire.NBT_CONFIG_NODE);
            tag.putDeep(ItemExpire.NBT_EXPIRE_NODE, expired);
            return;
        }

        if (!MegumiUtil.isNumber(value)) return;
        int day = Integer.parseInt(value);

        long expired = ItemExpire.getExpiredDay(day);
        tag.removeDeep(ItemExpire.NBT_CONFIG_NODE);
        tag.putDeep(ItemExpire.NBT_EXPIRE_NODE, expired);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCheck(ItemBuildEvent.Pre e) {
        if (e.isCancelled()) return;
        if (e.getPlayer() == null) return;

        ItemTag tag = e.getItemStream().getZaphkielData();
        ItemTagData data = tag.getDeep(ItemExpire.NBT_EXPIRE_NODE);
        if (data == null) return;

        long time = data.asLong();

        if (!ItemExpire.isExpired(time)) return;

        ItemExpiredEvent event = new ItemExpiredEvent(e.getPlayer(), e.getItemStream().getZaphkielItem().getId(), e.getName().get("NAME"));
        event.call();
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDisplay(ItemReleaseEvent.Display e) {
        if (e.getPlayer() == null) return;

        ItemTag tag = e.getItemStream().getZaphkielData();
        ItemTagData data = tag.getDeep(ItemExpire.NBT_EXPIRE_NODE);
        if (data == null) return;

        long time = data.asLong();

        String expiredFormat = ItemExpire.formatting(time);
        e.addLore(ItemExpire.DISPLAY_NODE, expiredFormat);
    }
}
