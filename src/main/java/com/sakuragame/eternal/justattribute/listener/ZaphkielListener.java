package com.sakuragame.eternal.justattribute.listener;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.AttributeManager;
import com.sakuragame.eternal.justattribute.core.attribute.BaseAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.AttributeData;
import com.sakuragame.eternal.justattribute.core.special.*;
import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import ink.ptms.zaphkiel.api.event.ItemReleaseEvent;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTagData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class ZaphkielListener implements Listener {

    private final JustAttribute plugin = JustAttribute.getInstance();

    @EventHandler
    public void onAttribute(ItemReleaseEvent.Display e) {
        ItemTag tags = e.getItemStream().getZaphkielData();

        List<String> ordinaryDisplay = new ArrayList<>();
        List<String> potencyDisplay = new ArrayList<>();

        PotencyGrade grade = PotencyGrade.getGrade(tags.getDeepOrElse(PotencyGrade.NBT_TAG, new ItemTagData(-1)).asInt());
        potencyDisplay.add(grade == null ? PotencyGrade.NONE.formatting() : grade.formatting());

        for (BaseAttribute attr : JustAttribute.getAttributeManager().getAttrProfile().values()) {
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
        if (potencyDisplay.size() == 1) {
            e.addLore(AttributeManager.POTENCY_DISPLAY_NODE, ConfigFile.potency_empty);
        }
        else {
            e.addLore(AttributeManager.POTENCY_DISPLAY_NODE, potencyDisplay);
        }
    }

    @EventHandler
    public void onSoulBound(ItemReleaseEvent.Display e) {
        Player player = e.getPlayer();
        ItemTag itemTag = e.getItemStream().getZaphkielData();

        ItemTagData data = itemTag.getDeep(SoulBound.NBT_ACTION_NODE);

        if (data == null) return;
        int id = data.asInt();

        SoulBound.Action action = SoulBound.Action.getAction(id);
        if (action == null) return;

        if (player != null && id == 0) {
            itemTag.removeDeep(SoulBound.NBT_ACTION_NODE);
            itemTag.putDeep(SoulBound.NBT_UUID_NODE, player.getUniqueId().toString());
            itemTag.putDeep(SoulBound.NBT_NAME_NODE, player.getName());

            e.addLore(SoulBound.DISPLAY_NODE, SoulBound.format(player.getName()));
            return;
        }

        e.addLore(SoulBound.DISPLAY_NODE, action.formatting());
    }

    @EventHandler
    public void onRealm(ItemReleaseEvent.Display e) {
        ItemTag itemTag = e.getItemStream().getZaphkielData();

        ItemTagData data = itemTag.getDeep(RealmLimit.NBT_NODE);

        if (data == null) return;
        int limit = data.asInt();

        e.addLore(RealmLimit.DISPLAY_NODE, RealmLimit.format(limit));
    }

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
