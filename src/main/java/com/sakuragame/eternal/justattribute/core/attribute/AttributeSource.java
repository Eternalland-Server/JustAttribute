package com.sakuragame.eternal.justattribute.core.attribute;

import com.sakuragame.eternal.justattribute.core.soulbound.Action;
import com.sakuragame.eternal.justattribute.core.soulbound.SoulBound;
import com.sakuragame.eternal.justattribute.core.special.EquipClassify;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.api.ItemStream;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTagData;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class AttributeSource implements Cloneable {

    private Map<Attribute, Double> ordinary;
    private Map<Attribute, Double> potency;

    public AttributeSource() {
        this.ordinary = new HashMap<>();
        this.potency = new HashMap<>();
    }

    public AttributeSource(ItemStream itemStream) {
        this(itemStream, false);
    }

    public AttributeSource(ItemStream itemStream, boolean ignore) {
        this.ordinary = new HashMap<>();
        this.potency = new HashMap<>();

        read(itemStream, ignore);
    }

    public AttributeSource(ItemStack item) {
        this(item, false);
    }

    public AttributeSource(ItemStack item, boolean ignore) {
        this.ordinary = new HashMap<>();
        this.potency = new HashMap<>();

        read(ZaphkielAPI.INSTANCE.read(item), ignore);
    }

    public AttributeSource(Map<Attribute, Double> ordinary, Map<Attribute, Double> potency) {
        this.ordinary = ordinary;
        this.potency = potency;
    }

    public static AttributeSource getItemAttribute(ItemStack item, EquipClassify classify) {
        if (MegumiUtil.isEmpty(item)) return null;

        ItemStream itemStream = ZaphkielAPI.INSTANCE.read(item);
        if (itemStream.isVanilla()) return null;

        ItemTag itemTag = itemStream.getZaphkielData();
        EquipClassify equip = EquipClassify.getClassify(itemTag);
        if (classify != equip || SoulBound.isSeal(itemTag)) return null;

        return new AttributeSource(item);
    }

    public AttributeSource addOrdinary(Attribute attribute, double value) {
        ordinary.put(attribute, value);
        return this;
    }

    public AttributeSource addPotency(Attribute attribute, double value) {
        potency.put(attribute, value);
        return this;
    }

    private void read(ItemStream itemStream, boolean ignore) {
        if (itemStream.isVanilla()) return;

        ItemTag itemTag = itemStream.getZaphkielData();

        if (!ignore) {
            Action action = SoulBound.getType(itemTag);
            if (action == Action.SEAL) return;
        }

        for (Attribute attr : Attribute.values()) {
            ordinary.put(attr, itemTag.getDeepOrElse(attr.getOrdinaryNode(), new ItemTagData(0)).asDouble());
            potency.put(attr, itemTag.getDeepOrElse(attr.getPotencyNode(), new ItemTagData(0)).asDouble());
        }
    }

    @Override
    public AttributeSource clone() {
        try {
            return (AttributeSource) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
