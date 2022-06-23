package com.sakuragame.eternal.justattribute.core.attribute;

import com.sakuragame.eternal.justattribute.core.soulbound.Action;
import com.sakuragame.eternal.justattribute.core.soulbound.SoulBound;
import com.sakuragame.eternal.justattribute.core.special.EquipClassify;
import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.api.ItemStream;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTagData;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

@Getter
public class AttributeSource implements Cloneable {

    private final HashMap<Attribute, Double> ordinary;
    private final HashMap<Attribute, Double> potency;

    public AttributeSource() {
        this.ordinary = new HashMap<>();
        this.potency = new HashMap<>();
    }

    public AttributeSource(ItemStream itemStream) {
        this.ordinary = new HashMap<>();
        this.potency = new HashMap<>();

        read(itemStream);
    }

    public AttributeSource(ItemStack item) {
        this.ordinary = new HashMap<>();
        this.potency = new HashMap<>();

        read(ZaphkielAPI.INSTANCE.read(item));
    }

    public AttributeSource(HashMap<Attribute, Double> ordinary, HashMap<Attribute, Double> potency) {
        this.ordinary = ordinary;
        this.potency = potency;
    }

    public static AttributeSource getItemAttribute(ItemStack item, EquipClassify classify) {
        ItemStream itemStream = ZaphkielAPI.INSTANCE.read(item);
        if (itemStream.isVanilla()) return null;

        ItemTag itemTag = itemStream.getZaphkielData();
        EquipClassify equip = EquipClassify.getClassify(itemTag);
        if (classify != equip || SoulBound.isSeal(itemTag)) return null;

        return new AttributeSource(item);
    }

    public static AttributeSource getRoleDefault() {
        AttributeSource source = new AttributeSource();
        for (Attribute attr : Attribute.values()) {
            source.addOrdinary(attr, attr.isOnlyPercent() ? 0 : attr.getBase());
            source.addPotency(attr, attr.isOnlyPercent() ? attr.getBase() : 0);
        }

        return source;
    }

    public static AttributeSource diff(AttributeSource a, AttributeSource b) {
        HashMap<Attribute, Double> ordinary = new HashMap<>(a.getOrdinary());
        HashMap<Attribute, Double> potency = new HashMap<>(a.getPotency());

        b.getOrdinary().forEach((k, v) -> ordinary.merge(k, v, (o, n) -> o - n));
        b.getPotency().forEach((k, v) -> potency.merge(k, v, (o, n) -> o - n));

        return new AttributeSource(ordinary, potency);
    }

    public AttributeSource addOrdinary(Attribute attribute, double value) {
        ordinary.put(attribute, value);
        return this;
    }

    public AttributeSource addPotency(Attribute attribute, double value) {
        potency.put(attribute, value);
        return this;
    }

    private void read(ItemStream itemStream) {
        if (itemStream.isVanilla()) return;

        ItemTag itemTag = itemStream.getZaphkielData();

        Action action = SoulBound.getType(itemTag);
        if (action == Action.SEAL) return;

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
