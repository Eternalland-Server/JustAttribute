package com.sakuragame.eternal.justattribute.listener.build;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.AttributeManager;
import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.AttributeData;
import com.sakuragame.eternal.justattribute.core.special.*;
import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import ink.ptms.zaphkiel.api.event.ItemBuildEvent;
import ink.ptms.zaphkiel.api.event.ItemReleaseEvent;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTagData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.LinkedList;

public class ZaphkielListener implements Listener {

    private final JustAttribute plugin = JustAttribute.getInstance();

    @EventHandler
    public void onAttribute(ItemReleaseEvent.Display e) {
        ItemTag tags = e.getItemStream().getZaphkielData();

        LinkedList<String> ordinaryDisplay = new LinkedList<>();
        LinkedList<String> potencyDisplay = new LinkedList<>();

        PotencyGrade grade = PotencyGrade.getGrade(tags.getDeepOrElse(PotencyGrade.NBT_TAG, new ItemTagData(-1)).asInt());
        potencyDisplay.add(grade == null ? PotencyGrade.NONE.formatting() : grade.formatting());
        potencyDisplay.add("");

        for (Attribute attr : Attribute.values()) {
            ItemTagData ordinary = tags.getDeep(attr.getOrdinaryNode());
            ItemTagData potency = tags.getDeep(attr.getPotencyNode());

            if (ordinary != null) {
                ordinaryDisplay.add(attr.format(ordinary.asDouble()));
            }
            if (potency != null) {
                potencyDisplay.add(attr.format(potency.asDouble(), true));
            }
        }

        e.addLore(AttributeManager.ORDINARY_DISPLAY_NODE, ordinaryDisplay);

        ItemTagData data = tags.getDeep(EquipClassify.NBT_NODE);
        if (data == null) return;
        int id = data.asInt();

        if (id >= 100) return;

        if (potencyDisplay.size() == 2) {
            potencyDisplay.add(ConfigFile.potency_empty);
        }
        e.addLore(AttributeManager.POTENCY_DISPLAY_NODE, potencyDisplay);
    }

    @EventHandler
    public void onSoulBound(ItemBuildEvent.Pre e) {
        Player player = e.getPlayer();
        ItemTag itemTag = e.getItemStream().getZaphkielData();

        ItemTagData data = itemTag.getDeep(SoulBound.NBT_ACTION_NODE);

        if (data == null) return;
        int id = data.asInt();

        SoulBound.Action action = SoulBound.Action.getAction(id);
        if (action == null) return;

        if (player != null) {
            if (action == SoulBound.Action.AUTO) {
                itemTag.removeDeep(SoulBound.NBT_ACTION_NODE);
                itemTag.putDeep(SoulBound.NBT_UUID_NODE, player.getUniqueId().toString());
                itemTag.putDeep(SoulBound.NBT_NAME_NODE, player.getName());

                e.addLore(SoulBound.DISPLAY_NODE, SoulBound.format(player.getName()));
                return;
            }

            if (action == SoulBound.Action.PROP) {
                itemTag.removeDeep(SoulBound.NBT_ACTION_NODE);
                itemTag.putDeep(SoulBound.NBT_UUID_NODE, player.getUniqueId().toString());
                itemTag.putDeep(SoulBound.NBT_NAME_NODE, player.getName());

                e.addLore(SoulBound.DISPLAY_NODE, action.formatting());
                return;
            }
        }

        e.addLore(SoulBound.DISPLAY_NODE, action.formatting());
    }

    /*@EventHandler
    public void onSoulBound(ItemReleaseEvent.Display e) {
        Player player = e.getPlayer();
        ItemTag itemTag = e.getItemStream().getZaphkielData();

        ItemTagData data = itemTag.getDeep(SoulBound.NBT_NAME_NODE);

        if (data == null) return;
        if (player == null) return;

        e.addLore(SoulBound.DISPLAY_NODE, SoulBound.format(player.getName()));
    }*/

    @EventHandler
    public void onClassify(ItemReleaseEvent.Display e) {
        ItemTag itemTag = e.getItemStream().getZaphkielData();

        ItemTagData data = itemTag.getDeep(EquipClassify.NBT_NODE);

        if (data == null) return;
        int id = data.asInt();
        EquipClassify equipClassify = EquipClassify.getType(id);
        if (equipClassify == null) return;

        e.addLore(EquipClassify.DISPLAY_NODE, equipClassify.formatting());
    }

    @EventHandler
    public void onQuality(ItemReleaseEvent.Display e) {
        ItemTag itemTag = e.getItemStream().getZaphkielData();

        ItemTagData data = itemTag.getDeep(EquipQuality.NBT_NODE);

        if (data == null) return;
        int id = data.asInt();
        EquipQuality equipQuality = EquipQuality.getQuality(id);
        if (equipQuality == null) return;

        e.addLore(EquipQuality.DISPLAY_NODE, equipQuality.formatting());
    }

    @EventHandler
    public void onCombat(ItemReleaseEvent.Display e) {
        AttributeData data = new AttributeData(e.getItemStream());

        int combat = CombatCapacity.get(data);

        e.addLore(CombatCapacity.DISPLAY_NODE, CombatCapacity.format(combat));
    }
}
