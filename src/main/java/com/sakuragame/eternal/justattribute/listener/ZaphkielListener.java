package com.sakuragame.eternal.justattribute.listener;

import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import ink.ptms.zaphkiel.api.ItemStream;
import ink.ptms.zaphkiel.api.event.ItemReleaseEvent;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTagData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ZaphkielListener implements Listener {

    @EventHandler
    public void onRelease(ItemReleaseEvent.Display e) {
        ItemStream itemStream = e.getItemStream();
        for (Attribute attr : Attribute.values()) {
            ItemTagData ordinary = itemStream.getZaphkielData().getDeep(attr.getOrdinaryNode());
            ItemTagData percent = itemStream.getZaphkielData().getDeep(attr.getPercentNode());

            if (ordinary != null) {
                e.addLore("ordinary." + attr.getIdentifier(), attr.format(ordinary.asDouble()));
            }
            if (percent != null) {
                e.addLore("percent." + attr.getIdentifier(), attr.format(percent.asDouble(), true));
            }
        }
    }
}
