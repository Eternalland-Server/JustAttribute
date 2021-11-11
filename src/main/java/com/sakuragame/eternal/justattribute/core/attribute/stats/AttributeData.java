package com.sakuragame.eternal.justattribute.core.attribute.stats;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.Identifier;
import com.sakuragame.eternal.justattribute.core.attribute.BaseAttribute;
import com.sakuragame.eternal.justattribute.core.codition.EquipType;
import com.sakuragame.eternal.justattribute.core.codition.Realm;
import com.sakuragame.eternal.justattribute.core.codition.SoulBound;
import com.taylorswiftcn.justwei.util.MegumiUtil;
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
    }

    public AttributeData(Player player, ItemStack item, EquipType type) {
        this.ordinary = new HashMap<>();
        this.potency = new HashMap<>();
        this.read(player, item, type);
    }

    public AttributeData(HashMap<Identifier, Double> ordinary, HashMap<Identifier, Double> potency) {
        this.ordinary = ordinary;
        this.potency = potency;
    }

    public void initBaseAttribute() {
        for (BaseAttribute attr : JustAttribute.getAttributeManager().getAttrProfile().values()) {
            ordinary.put(attr.getIdentifier(), attr.getBase());
        }
    }

    private void read(Player player, ItemStack item, EquipType type) {
        if (MegumiUtil.isEmpty(item)) return;

        ItemTag stream = ZaphkielAPI.INSTANCE.getData(item);
        if (stream == null) return;

        int typeID = stream.getDeepOrElse(EquipType.NBT_NODE, new ItemTagData(-1)).asInt();
        if (typeID != type.getId()) return;

        int realm = stream.getDeepOrElse(Realm.NBT_NODE, new ItemTagData(-1)).asInt();
        if (realm == -1 || JustLevelAPI.getData(player).getRealm() < realm) return;

        ItemTagData bound = stream.getDeep(SoulBound.NBT_UUID_NODE);
        if (bound == null) return;

        for (BaseAttribute attr : JustAttribute.getAttributeManager().getAttrProfile().values()) {
            ordinary.put(attr.getIdentifier(), stream.getDeepOrElse(attr.getOrdinaryNode(), new ItemTagData(0)).asDouble());
            potency.put(attr.getIdentifier(), stream.getDeepOrElse(attr.getPotencyNode(), new ItemTagData(0)).asDouble() / 100d);
        }
    }
}
