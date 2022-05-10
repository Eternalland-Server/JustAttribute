package com.sakuragame.eternal.justattribute.listener.build;

import com.sakuragame.eternal.justattribute.core.attribute.AttributeSource;
import com.sakuragame.eternal.justattribute.core.soulbound.Action;
import com.sakuragame.eternal.justattribute.core.soulbound.SoulBound;
import com.sakuragame.eternal.justattribute.core.special.*;
import ink.ptms.zaphkiel.api.event.ItemBuildEvent;
import ink.ptms.zaphkiel.api.event.ItemReleaseEvent;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTagData;
import net.sakuragame.eternal.dragoncore.util.Pair;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ZaphkielListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onSoulBound(ItemBuildEvent.Pre e) {
        if (e.isCancelled()) return;

        Player player = e.getPlayer();
        ItemTag itemTag = e.getItemStream().getZaphkielData();

        ItemTagData data = itemTag.getDeep(SoulBound.NBT_ACTION_NODE);

        if (data == null) return;
        int id = data.asInt();

        Action action = Action.match(id);
        if (action == null) return;

        if (player != null && action.isInitiative()) {
            action.getHandler().build(player, itemTag);
            return;
        }

        e.addLore(SoulBound.DISPLAY_NODE, action.getHandler().getUnboundDesc());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onSoulBound(ItemReleaseEvent.Display e) {
        ItemTag itemTag = e.getItemStream().getZaphkielData();

        ItemTagData type = itemTag.getDeep(SoulBound.NBT_TYPE_NODE);
        if (type == null) return;
        Action action = Action.match(type.asInt());
        if (action == null) return;

        if (action == Action.SEAL) {
            e.addLore(SoulBound.DISPLAY_NODE, action.getHandler().getUnboundDesc());
            return;
        }

        ItemTagData owner = itemTag.getDeep(SoulBound.NBT_NAME_NODE);
        if (owner == null) return;

        e.addLore(SoulBound.DISPLAY_NODE, action.getHandler().getBoundDesc(owner.asString()));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onClassify(ItemReleaseEvent.Display e) {
        ItemTag itemTag = e.getItemStream().getZaphkielData();
        ItemTagData data = itemTag.getDeep(EquipClassify.NBT_NODE);

        if (data == null) return;
        int id = data.asInt();
        EquipClassify equipClassify = EquipClassify.match(id);
        if (equipClassify == null) return;

        e.addLore(EquipClassify.DISPLAY_NODE, equipClassify.formatting());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onEquipQuality(ItemReleaseEvent.Display e) {
        ItemTag itemTag = e.getItemStream().getZaphkielData();
        ItemTagData data = itemTag.getDeep(EquipQuality.NBT_NODE);

        if (data == null) return;
        int id = data.asInt();
        EquipQuality equipQuality = EquipQuality.match(id);
        if (equipQuality == null) return;

        e.addLore(EquipQuality.DISPLAY_NODE, equipQuality.formatting());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPropQuality(ItemReleaseEvent.Display e) {
        ItemTag itemTag = e.getItemStream().getZaphkielData();
        ItemTagData data = itemTag.getDeep(PropQuality.NBT_NODE);

        if (data == null) return;
        int id = data.asInt();
        PropQuality propQuality = PropQuality.match(id);
        if (propQuality == null) return;

        e.addLore(PropQuality.DISPLAY_NODE, propQuality.getName());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onCombat(ItemReleaseEvent.Display e) {
        String display = e.getItemStream().getZaphkielItem().getDisplay();
        if (!(display.equals("EQUIP_COMMON_DISPLAY") || display.equals("SKIN_COMMON_DISPLAY"))) return;

        int combat = CombatCapacity.get(new AttributeSource(e.getItemStream()));
        e.addLore(CombatCapacity.DISPLAY_NODE, CombatCapacity.format(combat));
    }
}
