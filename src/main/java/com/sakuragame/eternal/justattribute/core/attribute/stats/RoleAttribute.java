package com.sakuragame.eternal.justattribute.core.attribute.stats;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.api.event.JAUpdateAttributeEvent;
import com.sakuragame.eternal.justattribute.core.attribute.Identifier;
import com.sakuragame.eternal.justattribute.core.attribute.VanillaSlot;
import com.sakuragame.eternal.justattribute.core.special.EquipClassify;
import com.sakuragame.eternal.justattribute.core.special.CombatCapacity;
import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import lombok.Getter;
import net.sakuragame.eternal.dragoncore.api.SlotAPI;
import net.sakuragame.eternal.dragoncore.config.FileManager;
import net.sakuragame.eternal.justlevel.api.JustLevelAPI;
import org.bukkit.Material;
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

    @Getter private int combat;

    public RoleAttribute(LivingEntity role) {
        this.role = role;
        this.base = new AttributeData();
        this.source = new HashMap<>();
        this.initRole();
    }

    private void initRole() {
        if (!(role instanceof Player)) return;

        this.base.initBaseAttribute();

        this.updateStageGrowth();
        this.loadVanillaSlot();
        this.loadDragonSlot();
        this.updateRoleAttribute();
    }

    public void updateStageGrowth() {
        Player player = getPlayer();
        int stage = JustLevelAPI.getTotalStage(player);

        this.health = ConfigFile.RoleBase.health + ConfigFile.RolePromote.health * stage;
        this.mana = ConfigFile.RoleBase.mana + ConfigFile.RolePromote.mana * stage;
        this.damage = ConfigFile.RoleBase.damage + ConfigFile.RolePromote.damage * stage;
        this.defence = ConfigFile.RoleBase.defence + ConfigFile.RolePromote.defence * stage;
        this.restoreHP = ConfigFile.RoleBase.restoreHP + ConfigFile.RolePromote.restoreHP * stage;
        this.restoreMP = ConfigFile.RoleBase.restoreMP + ConfigFile.RolePromote.restoreMP * stage;
    }

    private void loadVanillaSlot() {
        this.updateVanillaSlot(VanillaSlot.Helmet);
        this.updateVanillaSlot(VanillaSlot.ChestPlate);
        this.updateVanillaSlot(VanillaSlot.Leggings);
        this.updateVanillaSlot(VanillaSlot.Boots);
        this.updateVanillaSlot(VanillaSlot.OffHand);
        this.updateVanillaSlot(VanillaSlot.MainHand);
    }

    private void loadDragonSlot() {
        Player player = getPlayer();

        for (String key : ConfigFile.slotSetting.keySet()) {
            if (!FileManager.getSlotSettings().containsKey(key)) continue;

            int id = ConfigFile.slotSetting.get(key);
            EquipClassify classify = EquipClassify.getType(id);
            if (classify == null) continue;

            ItemStack item = SlotAPI.getCacheSlotItem(player, key);

            updateCustomSlot(key, classify, item);
        }
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
        this.combat = CombatCapacity.get(totalAttribute);

        JAUpdateAttributeEvent event = new JAUpdateAttributeEvent(getPlayer(), this);
        event.call();
    }

    public void updateVanillaSlot(VanillaSlot slot) {
        Player player = getPlayer();

        ItemStack item = (slot == VanillaSlot.MainHand) ? player.getInventory().getItemInMainHand() : player.getInventory().getItem(slot.getIndex());
        updateSlot(slot.getIdent(), slot.getType(), item);
    }

    public void updateMainHandSlot(int slot) {
        Player player = getPlayer();

        ItemStack item = player.getInventory().getItem(slot);
        if (MegumiUtil.isEmpty(item)) item = new ItemStack(Material.AIR);

        this.source.put(VanillaSlot.MainHand.getIdent(), new AttributeData(player, item, VanillaSlot.MainHand.getType()));

        this.updateRoleAttribute();
    }

    public void updateCustomSlot(String ident, EquipClassify type, ItemStack item) {
        updateSlot(ident, type, item);
    }

    private void updateSlot(String ident, EquipClassify type, ItemStack item) {
        Player player = getPlayer();

        if (MegumiUtil.isEmpty(item)) item = new ItemStack(Material.AIR);

        this.source.put(ident, new AttributeData(player, item, type));

        this.updateRoleAttribute();
    }

    private Player getPlayer() {
        return (Player) role;
    }

    public HashMap<Identifier, Double> getOrdinaryAttributes() {
        HashMap<Identifier, Double> ordinary = new HashMap<>(base.getOrdinary());
        source.keySet().forEach(s -> {
            AttributeData data = source.get(s);
            data.getOrdinary().forEach((key, value) -> ordinary.merge(key, value, Double::sum));
        });

        return ordinary;
    }

    public HashMap<Identifier, Double> getPotencyAttributes() {
        HashMap<Identifier, Double> potency = new HashMap<>(base.getOrdinary());
        source.keySet().forEach(s -> {
            AttributeData data = source.get(s);
            data.getPotency().forEach((key, value) -> potency.merge(key, value, Double::sum));
        });

        return potency;
    }

    public double getTotalValue(Identifier ident) {
        return JustAttribute.getAttributeManager().getAttribute(ident).calculate(this);
    }

    public double getTotalHealth() {
        return getTotalValue(Identifier.Health) + getTotalValue(Identifier.Stamina);
    }

    public double getTotalMana() {
        return getTotalValue(Identifier.Mana) + getTotalValue(Identifier.Wisdom);
    }

    public double getTotalDamage() {
        return getTotalValue(Identifier.Damage) + getTotalValue(Identifier.Energy);
    }

    public double getTotalDefence() {
        return getTotalValue(Identifier.Defence) + getTotalValue(Identifier.Technique);
    }

    public double getOrdinaryTotalValue(Identifier ident) {
        return totalAttribute.getOrdinary().get(ident);
    }

    public double getPotencyTotalValue(Identifier ident) {
        return totalAttribute.getPotency().get(ident);
    }
}
