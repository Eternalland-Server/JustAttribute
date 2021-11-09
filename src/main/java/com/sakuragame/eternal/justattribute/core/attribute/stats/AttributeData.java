package com.sakuragame.eternal.justattribute.core.attribute.stats;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.Identifier;
import com.sakuragame.eternal.justattribute.core.attribute.BaseAttribute;
import com.sakuragame.eternal.justattribute.core.codition.EquipType;
import com.sakuragame.eternal.justattribute.core.codition.Realm;
import com.sakuragame.eternal.justattribute.core.codition.SoulBound;
import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTagData;
import lombok.Getter;
import net.sakuragame.eternal.justlevel.api.JustLevelAPI;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

@Getter
public class AttributeData {

    private final HashMap<Identifier, Double> ordinary;
    private final HashMap<Identifier, Double> potency;

    public AttributeData() {
        this.ordinary = new HashMap<>();
        this.potency = new HashMap<>();
        this.readBaseAttr();
    }

    public AttributeData(HashMap<Identifier, Double> ordinary, HashMap<Identifier, Double> potency) {
        this.ordinary = ordinary;
        this.potency = potency;
    }

    public AttributeData(Player player, ItemStack item, EquipType type) {
        this.ordinary = new HashMap<>();
        this.potency = new HashMap<>();
        this.readItemAttr(player, item, type);
    }

    private void readBaseAttr() {
        for (BaseAttribute attr : JustAttribute.getAttributeManager().getAttr().values()) {
            ordinary.put(attr.getIdentifier(), attr.getBase());
        }
    }

    private void readItemAttr(Player player, ItemStack item, EquipType type) {
        ItemTag stream = ZaphkielAPI.INSTANCE.getData(item);
        if (stream == null) return;

        int typeID = stream.getDeepOrElse(EquipType.TYPE_NODE, new ItemTagData(-1)).asInt();
        if (typeID != type.getId()) return;

        int realm = stream.getDeepOrElse(Realm.REALM_NODE, new ItemTagData(-1)).asInt();
        if (realm == -1 || JustLevelAPI.getData(player).getRealm() < realm) return;

        ItemTagData bound = stream.getDeep(SoulBound.UUID_NODE);
        if (bound == null) return;

        for (BaseAttribute attr : JustAttribute.getAttributeManager().getAttr().values()) {
            ordinary.put(attr.getIdentifier(), stream.getDeepOrElse(attr.getOrdinaryNode(), new ItemTagData(0)).asDouble());
            potency.put(attr.getIdentifier(), stream.getDeepOrElse(attr.getPotencyNode(), new ItemTagData(0)).asDouble() / 100d);
        }
    }
}
