package com.sakuragame.eternal.justattribute.listener;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.AttributeManager;
import com.sakuragame.eternal.justattribute.core.attribute.BaseAttribute;
import com.sakuragame.eternal.justattribute.core.codition.EquipType;
import com.sakuragame.eternal.justattribute.core.codition.Realm;
import com.sakuragame.eternal.justattribute.core.codition.SoulBound;
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

        for (BaseAttribute attr : JustAttribute.getAttributeManager().getAttr().values()) {
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
        if (potencyDisplay.isEmpty()) {
            e.addLore(AttributeManager.POTENCY_DISPLAY_NODE, ConfigFile.none_potency);
        }
        else {
            e.addLore(AttributeManager.POTENCY_DISPLAY_NODE, potencyDisplay);
        }
    }

    @EventHandler
    public void onSoulBound(ItemReleaseEvent.Display e) {
        Player player = e.getPlayer();
        ItemTag itemTag = e.getItemStream().getZaphkielData();

        ItemTagData data = itemTag.getDeep(SoulBound.ACTION_NODE);

        if (data == null) return;
        int id = data.asInt();

        SoulBound.Action action = SoulBound.Action.getAction(id);
        if (action == null) return;

        if (id == 0) {
            if (player == null) {
                e.addLore(SoulBound.DISPLAY_NODE, ConfigFile.unbound_format.replace("<desc>", action.getDesc()));
            }
            else {
                itemTag.remove(SoulBound.ACTION_NODE);
                itemTag.put(SoulBound.UUID_NODE, player.getUniqueId().toString());
                itemTag.put(SoulBound.NAME_NODE, player.getName());
                e.addLore(SoulBound.DISPLAY_NODE, ConfigFile.bound_format.replace("<owner>", player.getName()));
            }
        }
    }

    @EventHandler
    public void onRealm(ItemReleaseEvent.Display e) {
        ItemTag itemTag = e.getItemStream().getZaphkielData();

        ItemTagData data = itemTag.getDeep(Realm.REALM_NODE);

        if (data == null) return;
        int limit = data.asInt();

        e.addLore(Realm.DISPLAY_NODE, ConfigFile.realm_format.replace("<realm>", String.valueOf(limit)));
    }

    @EventHandler
    public void onType(ItemReleaseEvent.Display e) {
        ItemTag itemTag = e.getItemStream().getZaphkielData();

        ItemTagData data = itemTag.getDeep(EquipType.TYPE_NODE);

        if (data == null) return;
        int id = data.asInt();
        EquipType type = EquipType.getType(id);
        if (type == null) return;

        e.addLore(Realm.DISPLAY_NODE, ConfigFile.type_format.replace("<type>", type.getName()));
    }
}
