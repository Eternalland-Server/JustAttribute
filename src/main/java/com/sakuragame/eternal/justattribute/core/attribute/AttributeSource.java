package com.sakuragame.eternal.justattribute.core.attribute;

import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.api.ItemStream;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTagData;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

@Getter
public class AttributeSource {

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

        if (item.getType() == Material.AIR) return;
        read(ZaphkielAPI.INSTANCE.read(item));
    }

    public AttributeSource(HashMap<Attribute, Double> ordinary, HashMap<Attribute, Double> potency) {
        this.ordinary = ordinary;
        this.potency = potency;
    }

    public static AttributeSource getRoleDefault() {
        AttributeSource source = new AttributeSource();
        for (Attribute attr : Attribute.values()) {
            source.addOrdinary(attr, attr.isOnlyPercent() ? 0 : attr.getBase());
            source.addPotency(attr, attr.isOnlyPercent() ? attr.getBase() : 0);
        }

        return source;
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

        for (Attribute attr : Attribute.values()) {
            ordinary.put(attr, itemTag.getDeepOrElse(attr.getOrdinaryNode(), new ItemTagData(0)).asDouble());
            potency.put(attr, itemTag.getDeepOrElse(attr.getPotencyNode(), new ItemTagData(0)).asDouble());
        }
    }
}
