package com.sakuragame.eternal.justattribute.core.attribute.stats;

import com.sakuragame.eternal.justattribute.core.attribute.Identifier;
import com.sakuragame.eternal.justattribute.core.codition.EquipType;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class RoleAttribute {

    private final Player player;
    private final AttributeData base;
    private final HashMap<String, AttributeData> source;

    public RoleAttribute(Player player) {
        this.player = player;
        this.base = new AttributeData();
        this.source = new HashMap<>();
        this.updateVanillaSlot(VanillaSlot.Helmet);
        this.updateVanillaSlot(VanillaSlot.ChestPlate);
        this.updateVanillaSlot(VanillaSlot.Leggings);
        this.updateVanillaSlot(VanillaSlot.Boots);
        this.updateVanillaSlot(VanillaSlot.OffHand);
    }

    public void updateVanillaSlot(VanillaSlot slot) {
        ItemStack item = player.getInventory().getItem(slot.getIndex());
        if (MegumiUtil.isEmpty(item)) return;
        updateSlot(slot.getIdent(), slot.getType(), item);
    }

    public void updateCustomSlot(String ident, EquipType type, ItemStack item) {
        updateSlot(ident, type, item);
    }

    private void updateSlot(String ident, EquipType type, ItemStack item) {
        this.source.put(ident, new AttributeData(player, item, type));
    }

    public HashMap<Identifier, Double> getOrdinaryAttr() {
        HashMap<Identifier, Double> ordinary = new HashMap<>(base.getOrdinary());
        source.keySet().forEach(s -> {
            AttributeData data = source.get(s);
            data.getOrdinary().forEach((key, value) -> ordinary.merge(key, value, Double::sum));
        });

        return ordinary;
    }

    public HashMap<Identifier, Double> getPotencyAttr() {
        HashMap<Identifier, Double> potency = new HashMap<>(base.getOrdinary());
        source.keySet().forEach(s -> {
            AttributeData data = source.get(s);
            data.getPotency().forEach((key, value) -> potency.merge(key, value, Double::sum));
        });

        return potency;
    }

    public AttributeData getTotalAttr() {
        HashMap<Identifier, Double> ordinary = new HashMap<>(base.getOrdinary());
        HashMap<Identifier, Double> potency = new HashMap<>(base.getOrdinary());
        source.keySet().forEach(s -> {
            AttributeData data = source.get(s);
            data.getOrdinary().forEach((key, value) -> ordinary.merge(key, value, Double::sum));
            data.getPotency().forEach((key, value) -> potency.merge(key, value, Double::sum));
        });

        return new AttributeData(ordinary, potency);
    }
}
