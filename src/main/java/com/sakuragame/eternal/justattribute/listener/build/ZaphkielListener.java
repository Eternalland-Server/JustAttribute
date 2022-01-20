package com.sakuragame.eternal.justattribute.listener.build;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.AttributeManager;
import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.AttributeData;
import com.sakuragame.eternal.justattribute.core.soulbound.Action;
import com.sakuragame.eternal.justattribute.core.soulbound.SoulBound;
import com.sakuragame.eternal.justattribute.core.special.*;
import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import ink.ptms.zaphkiel.api.event.ItemBuildEvent;
import ink.ptms.zaphkiel.api.event.ItemReleaseEvent;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTagData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.LinkedList;

public class ZaphkielListener implements Listener {

    private final JustAttribute plugin = JustAttribute.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSoulBound(ItemBuildEvent.Pre e) {
        if (e.isCancelled()) return;

        Player player = e.getPlayer();
        ItemTag itemTag = e.getItemStream().getZaphkielData();

        ItemTagData data = itemTag.getDeep(SoulBound.NBT_ACTION_NODE);

        if (data == null) return;
        int id = data.asInt();

        Action action = Action.match(id);
        if (action == null || action == Action.USE) return;

        if (player != null) {
            action.getHandler().build(player, itemTag);
            return;
        }

        e.addLore(SoulBound.DISPLAY_NODE, action.getHandler().getUnboundDesc());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSoulBound(ItemReleaseEvent.Display e) {
        if (e.isCancelled()) return;

        Player player = e.getPlayer();
        if (player == null) return;

        ItemTag itemTag = e.getItemStream().getZaphkielData();

        ItemTagData type = itemTag.getDeep(SoulBound.NBT_TYPE_NODE);
        if (type == null) return;
        Action action = Action.match(type.asInt());
        if (action == null) return;

        ItemTagData owner = itemTag.getDeep(SoulBound.NBT_NAME_NODE);
        if (owner == null) return;

        e.addLore(SoulBound.DISPLAY_NODE, action.getHandler().getBoundDesc(owner.asString()));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClassify(ItemReleaseEvent.Display e) {
        if (e.isCancelled()) return;

        ItemTag itemTag = e.getItemStream().getZaphkielData();
        ItemTagData data = itemTag.getDeep(EquipClassify.NBT_NODE);

        if (data == null) return;
        int id = data.asInt();
        EquipClassify equipClassify = EquipClassify.getType(id);
        if (equipClassify == null) return;

        e.addLore(EquipClassify.DISPLAY_NODE, equipClassify.formatting());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuality(ItemReleaseEvent.Display e) {
        if (e.isCancelled()) return;

        ItemTag itemTag = e.getItemStream().getZaphkielData();
        ItemTagData data = itemTag.getDeep(EquipQuality.NBT_NODE);

        if (data == null) return;
        int id = data.asInt();
        EquipQuality equipQuality = EquipQuality.getQuality(id);
        if (equipQuality == null) return;

        e.addLore(EquipQuality.DISPLAY_NODE, equipQuality.formatting());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCombat(ItemReleaseEvent.Display e) {
        if (e.isCancelled()) return;

        int combat = CombatCapacity.get(new AttributeData(e.getItemStream()));
        e.addLore(CombatCapacity.DISPLAY_NODE, CombatCapacity.format(combat));
    }
}
