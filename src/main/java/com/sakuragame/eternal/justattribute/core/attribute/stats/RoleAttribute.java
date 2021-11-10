package com.sakuragame.eternal.justattribute.core.attribute.stats;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.api.event.JAUpdateAttributeEvent;
import com.sakuragame.eternal.justattribute.core.attribute.Identifier;
import com.sakuragame.eternal.justattribute.core.attribute.VanillaSlot;
import com.sakuragame.eternal.justattribute.core.codition.EquipType;
import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import lombok.Getter;
import net.sakuragame.eternal.justlevel.api.JustLevelAPI;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class RoleAttribute {

    @Getter private final LivingEntity role;

    private double health;
    private double mana;
    private double damage;
    private double defence;
    @Getter private double restoreHP;
    @Getter private double restoreMP;

    private final AttributeData base;
    private final HashMap<String, AttributeData> source;
    @Getter private AttributeData totalAttribute;

    public RoleAttribute(LivingEntity role) {
        this.role = role;
        this.initRole();
        this.base = new AttributeData();
        this.source = new HashMap<>();
    }

    private void initRole() {
        if (!(role instanceof Player)) return;

        this.updateStageGrowth();
        this.base.initPlayerAttr();
        this.updateVanillaSlot(VanillaSlot.Helmet);
        this.updateVanillaSlot(VanillaSlot.ChestPlate);
        this.updateVanillaSlot(VanillaSlot.Leggings);
        this.updateVanillaSlot(VanillaSlot.Boots);
        this.updateVanillaSlot(VanillaSlot.OffHand);
        this.updateRoleAttribute();
        this.applyToRole();
    }

    public void updateStageGrowth() {
        Player player = (Player) role;
        int stage = JustLevelAPI.getTotalStage(player);

        this.health = ConfigFile.RoleBase.health + ConfigFile.RolePromote.health * stage;
        this.mana = ConfigFile.RoleBase.mana + ConfigFile.RolePromote.mana * stage;
        this.damage = ConfigFile.RoleBase.damage + ConfigFile.RolePromote.damage * stage;
        this.defence = ConfigFile.RoleBase.defence + ConfigFile.RolePromote.defence * stage;
        this.restoreHP = ConfigFile.RoleBase.restoreHP + ConfigFile.RolePromote.restoreHP * stage;
        this.restoreMP = ConfigFile.RoleBase.restoreMP + ConfigFile.RolePromote.restoreMP * stage;
    }

    public void updateRoleAttribute() {
        HashMap<Identifier, Double> ordinary = new HashMap<>(base.getOrdinary());
        HashMap<Identifier, Double> potency = new HashMap<>(base.getOrdinary());

        ordinary.merge(Identifier.Health, health, Double::sum);
        ordinary.merge(Identifier.Mana, mana, Double::sum);
        ordinary.merge(Identifier.Damage, damage, Double::sum);
        ordinary.merge(Identifier.Defence, defence, Double::sum);

        source.keySet().forEach(s -> {
            AttributeData data = source.get(s);
            data.getOrdinary().forEach((key, value) -> ordinary.merge(key, value, Double::sum));
            data.getPotency().forEach((key, value) -> potency.merge(key, value, Double::sum));
        });

        this.totalAttribute = new AttributeData(ordinary, potency);
    }

    public void applyToRole() {
        double actualHealth = JustAttribute.getAttributeManager().getAttribute(Identifier.Health).calculate(this);
        role.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(actualHealth);
    }

    public void updateVanillaSlot(VanillaSlot slot) {
        if (!(role instanceof Player)) return;
        Player player = (Player) role;

        ItemStack item = slot == VanillaSlot.MainHand ? player.getInventory().getItemInMainHand() : player.getInventory().getItem(slot.getIndex());
        updateSlot(slot.getIdent(), slot.getType(), item);
    }

    public void updateCustomSlot(String ident, EquipType type, ItemStack item) {
        updateSlot(ident, type, item);
    }

    private void updateSlot(String ident, EquipType type, ItemStack item) {
        if (!(role instanceof Player)) return;
        Player player = (Player) role;

        this.source.put(ident, new AttributeData(player, item, type));
        this.updateRoleAttribute();

        JAUpdateAttributeEvent updateEvent = new JAUpdateAttributeEvent(player, this);
        updateEvent.call();
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

    public double getOrdinaryTotalValue(Identifier ident) {
        return totalAttribute.getOrdinary().get(ident);
    }

    public double getPotencyTotalValue(Identifier ident) {
        return totalAttribute.getPotency().get(ident);
    }
}
