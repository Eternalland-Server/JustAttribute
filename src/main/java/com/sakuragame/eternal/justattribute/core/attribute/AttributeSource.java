package com.sakuragame.eternal.justattribute.core.attribute;

import com.sakuragame.eternal.justattribute.core.PetHandler;
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

    public AttributeSource(ItemStack item) {
        this(item, false);
    }

    public AttributeSource(ItemStream itemStream) {
        this(itemStream, false);
    }

    public AttributeSource(ItemStack item, boolean ignore) {
        this.ordinary = new HashMap<>();
        this.potency = new HashMap<>();

        read(ZaphkielAPI.INSTANCE.read(item), ignore);
    }

    public AttributeSource(ItemStream itemStream, boolean ignore) {
        this.ordinary = new HashMap<>();
        this.potency = new HashMap<>();

        read(itemStream, ignore);
    }

    public AttributeSource(Map<Attribute, Double> ordinary, Map<Attribute, Double> potency) {
        this.ordinary = ordinary;
        this.potency = potency;
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

        ItemTag tag = itemStream.getZaphkielData();

        if (!ignore) {
            Action action = SoulBound.getType(tag);
            if (action == Action.SEAL) return;
        }

        EquipClassify classify = EquipClassify.getClassify(tag);

        if (classify == EquipClassify.Pet_Egg) {
            for (int level : PetHandler.stage) {
                ItemTagData data = tag.getDeep(PetHandler.NBT_NODE_CAPACITY + "." + level);
                if (data == null) continue;
                String[] args = data.asString().split("\\|", 2);
                Attribute ident = Attribute.match(args[0]);
                if (ident == null) continue;
                double value = Double.parseDouble(args[1]);
                potency.merge(ident, value, Double::sum);
            }

            PetHandler.NBT_NODE_EQUIP.values().forEach(key -> {
                String node = key.getValue();
                ItemTagData data = tag.getDeep(node);
                if (data != null) {
                    ItemStream equip = ZaphkielAPI.INSTANCE.getRegisteredItem().get(data.asString()).build(null);
                    this.read(equip, false);
                }
            });
        }
        else {
            for (Attribute attr : Attribute.values()) {
                ordinary.merge(attr, tag.getDeepOrElse(attr.getOrdinaryNode(), new ItemTagData(0)).asDouble(), Double::sum);
                potency.merge(attr, tag.getDeepOrElse(attr.getPotencyNode(), new ItemTagData(0)).asDouble(), Double::sum);
            }
        }
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

    @Override
    public AttributeSource clone() {
        try {
            return (AttributeSource) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
