package com.sakuragame.eternal.justattribute.core.attribute.stats;

import com.sakuragame.eternal.justattribute.api.event.role.RoleAttributeUpdateEvent;
import com.sakuragame.eternal.justattribute.core.RoleManager;
import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import com.sakuragame.eternal.justattribute.core.attribute.AttributeSource;
import com.sakuragame.eternal.justattribute.core.special.CombatCapacity;
import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import com.sakuragame.eternal.justattribute.util.Debug;
import com.sakuragame.eternal.justattribute.util.Scheduler;
import com.sakuragame.eternal.justattribute.util.Utils;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import lombok.Getter;
import net.sakuragame.eternal.justlevel.api.JustLevelAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class RoleAttribute {

    @Getter private final UUID uuid;

    private double health;
    private double mana;
    private double damage;
    private double defence;
    @Getter private double restoreHP;
    @Getter private double restoreMP;

    private final AttributeSource base;
    private final HashMap<String, AttributeSource> source;
    @Getter private AttributeSource totalAttribute;

    @Getter private int combat;

    public RoleAttribute(UUID uuid) {
        this.uuid = uuid;
        this.base = AttributeSource.getRoleDefault();
        this.source = new HashMap<>();
        this.updateStageGrowth();
    }

    public void updateStageGrowth() {
        int stage = JustLevelAPI.getTotalStage(uuid);

        this.health = ConfigFile.RoleBase.health + ConfigFile.RolePromote.health * stage;
        this.mana = ConfigFile.RoleBase.mana + ConfigFile.RolePromote.mana * stage;
        this.damage = ConfigFile.RoleBase.damage + ConfigFile.RolePromote.damage * stage;
        this.defence = ConfigFile.RoleBase.defence + ConfigFile.RolePromote.defence * stage;
        this.restoreHP = ConfigFile.RoleBase.restoreHP + ConfigFile.RolePromote.restoreHP * stage;
        this.restoreMP = ConfigFile.RoleBase.restoreMP + ConfigFile.RolePromote.restoreMP * stage;
    }

    public void updateRoleAttribute() {
        if (RoleManager.isLoading(this.uuid)) return;

        HashMap<Attribute, Double> ordinary = new HashMap<>(base.getOrdinary());
        HashMap<Attribute, Double> potency = new HashMap<>(base.getPotency());

        Debug.info(Debug.Attribute, "Base Ordinary:");
        for (Attribute attr : ordinary.keySet()) {
            Debug.info(Debug.Attribute, "" + attr.getId() + ": " + ordinary.get(attr));
        }
        Debug.info(Debug.Attribute, "Base Potency:");
        for (Attribute attr : potency.keySet()) {
            Debug.info(Debug.Attribute, "" + attr.getId() + ": " + potency.get(attr));
        }

        Debug.info(Debug.Attribute, "Realm Health: " + health);
        Debug.info(Debug.Attribute, "Realm Mana: " + mana);
        Debug.info(Debug.Attribute, "Realm Damage: " + damage);
        Debug.info(Debug.Attribute, "Realm Defence: " + defence);
        Debug.info(Debug.Attribute, "Realm restoreHP：" + restoreHP);
        Debug.info(Debug.Attribute, "Realm restoreMP: " + restoreMP);

        ordinary.merge(Attribute.Health, health, Double::sum);
        ordinary.merge(Attribute.Mana, mana, Double::sum);
        ordinary.merge(Attribute.Damage, damage, Double::sum);
        ordinary.merge(Attribute.Defence, defence, Double::sum);

        Debug.info(Debug.Attribute, "Source: ");
        source.keySet().forEach(s -> {
            AttributeSource data = source.get(s);
            data.getOrdinary().forEach((key, value) -> {
                ordinary.merge(key, value, Double::sum);
                Debug.info(Debug.Attribute, "Ordinary " + key.getId() + ": " + value);
            });
            data.getPotency().forEach((key, value) -> {
                potency.merge(key, value, Double::sum);
                Debug.info(Debug.Attribute, "Potency " + key.getId() + ": " + value);
            });
        });

        this.totalAttribute = new AttributeSource(ordinary, potency);
        this.combat = CombatCapacity.get(totalAttribute);

        RoleAttributeUpdateEvent event = new RoleAttributeUpdateEvent(this.getBukkitPlayer(), this);
        event.call();
    }

    @Deprecated
    public void addAttributeSource(String key, ItemStack item) {
        if (MegumiUtil.isEmpty(item)) item = new ItemStack(Material.AIR);

        this.source.put(key, new AttributeSource(item));
    }

    @Deprecated
    public void addAttributeSource(String key, AttributeSource source) {
        this.source.put(key, source);
    }

    @Deprecated
    public void addAttributeSource(String key, AttributeSource source, int time) {
        this.source.put(key, source);
        this.updateRoleAttribute();

        Scheduler.runLaterAsync(() -> {
            this.source.remove(key);
            this.updateRoleAttribute();
        }, time * 20);
    }

    public void putImmediateSource(String key, ItemStack item) {
        if (MegumiUtil.isEmpty(item)) {
            this.source.remove(key);
            this.updateRoleAttribute();
            return;
        }

        this.source.put(key, new AttributeSource(item));
    }

    public void putImmediateSource(String key, AttributeSource source) {
        this.putImmediateSource(key, source, -1);
    }

    public void putImmediateSource(String key, AttributeSource source, int second) {
        this.source.put(key, source);
        this.updateRoleAttribute();

        if (second == -1) return;

        Scheduler.runLaterAsync(() -> {
            this.source.remove(key);
            this.updateRoleAttribute();
        }, second * 20);
    }

    public void putSource(String key, ItemStack item) {
        if (MegumiUtil.isEmpty(item)) {
            this.source.remove(key);
            return;
        }

        this.source.put(key, new AttributeSource(item));
    }

    public void putSource(String key, AttributeSource source) {
        this.putImmediateSource(key, source, -1);
    }

    public void putSource(String key, AttributeSource source, int second) {
        this.source.put(key, source);

        if (second == -1) return;

        Scheduler.runLaterAsync(() -> {
            this.source.remove(key);
            this.updateRoleAttribute();
        }, second * 20);
    }

    public HashMap<Attribute, Double> getOrdinaryAttributes() {
        HashMap<Attribute, Double> ordinary = new HashMap<>(base.getOrdinary());
        source.keySet().forEach(s -> {
            AttributeSource data = source.get(s);
            data.getOrdinary().forEach((key, value) -> ordinary.merge(key, value, Double::sum));
        });

        return ordinary;
    }

    public HashMap<Attribute, Double> getPotencyAttributes() {
        HashMap<Attribute, Double> potency = new HashMap<>(base.getOrdinary());
        source.keySet().forEach(s -> {
            AttributeSource data = source.get(s);
            data.getPotency().forEach((key, value) -> potency.merge(key, value, Double::sum));
        });

        return potency;
    }

    public double getTotalValue(Attribute ident) {
        return ident.calculate(this);
    }

    public double getTotalHealth() {
        return getTotalValue(Attribute.Health) + getTotalValue(Attribute.Stamina) * 0.5;
    }

    public double getTotalMana() {
        return getTotalValue(Attribute.Mana) + getTotalValue(Attribute.Wisdom) * 0.5;
    }

    public double getTotalDamage() {
        return getTotalValue(Attribute.Damage) + getTotalValue(Attribute.Energy) * 0.5;
    }

    public double getTotalDefence() {
        return getTotalValue(Attribute.Defence) + getTotalValue(Attribute.Technique) * 0.5;
    }

    public double getActualDamage() {
        Player player = this.getBukkitPlayer();
        double damage = getTotalDamage();

        return damage * Utils.getRealmDamagePromote(player);
    }

    public double getActualDefence() {
        Player player = this.getBukkitPlayer();
        double defence = getTotalDefence();

        return defence * Utils.getRealmDefencePromote(player);
    }

    public double getOrdinaryTotalValue(Attribute ident) {
        return totalAttribute.getOrdinary().get(ident);
    }

    public double getPotencyTotalValue(Attribute ident) {
        return totalAttribute.getPotency().get(ident);
    }
    
    public Player getBukkitPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }
}
