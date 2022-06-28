package com.sakuragame.eternal.justattribute.core.attribute.stats;

import com.sakuragame.eternal.justattribute.api.event.role.RoleAttributeUpdateEvent;
import com.sakuragame.eternal.justattribute.core.RoleManager;
import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import com.sakuragame.eternal.justattribute.core.attribute.AttributeSource;
import com.sakuragame.eternal.justattribute.core.special.CombatCapacity;
import com.sakuragame.eternal.justattribute.util.Debug;
import com.sakuragame.eternal.justattribute.util.Scheduler;
import com.sakuragame.eternal.justattribute.util.Utils;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import lombok.Getter;
import net.sakuragame.eternal.justlevel.api.JustLevelAPI;
import net.sakuragame.eternal.justlevel.core.user.PlayerLevelData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class RoleAttribute implements EntityAttribute {

    @Getter private final UUID uuid;
    private int levelDamageUpperLimit;

    private final AttributeSource base;
    private final HashMap<String, AttributeSource> source;
    @Getter private AttributeSource totalAttribute;

    @Getter private int combat;
    @Getter private int change;

    public RoleAttribute(UUID uuid) {
        this.uuid = uuid;
        this.base = AttributeSource.getRoleDefault();
        this.source = new HashMap<>();
    }

    public void updateDamageUpperLimit() {
        PlayerLevelData account = JustLevelAPI.getUserData(this.uuid);
        int realm = account.getRealm();
        int stage = account.getStage();
        int totalLevel = (realm - 1) * 2000 + (stage - 1) * 200 + account.getLevel();

        this.levelDamageUpperLimit = (realm - 1) * 20000 + stage * 500 + totalLevel * 3;
    }

    public void updateLevelPromote() {
        int total = JustLevelAPI.getTotalLevel(this.uuid);
        AttributeSource as = new AttributeSource();
        as
                .addOrdinary(Attribute.Health, 4 * total)
                .addOrdinary(Attribute.Mana, 4 * total)
                .addOrdinary(Attribute.Damage, total)
                .addOrdinary(Attribute.Defence, total)
                .addOrdinary(Attribute.Energy, total)
                .addOrdinary(Attribute.Stamina, total)
                .addOrdinary(Attribute.Wisdom, total)
                .addOrdinary(Attribute.Technique, total);

        this.source.put("_level_", as);
    }

    public void update() {
        this.update(false);
    }

    public void update(boolean combat) {
        if (RoleManager.isLoading(this.uuid)) return;

        HashMap<Attribute, Double> ordinary = new HashMap<>(base.getOrdinary());
        HashMap<Attribute, Double> potency = new HashMap<>(base.getPotency());

//        Debug.info(Debug.Attribute, "Base Ordinary:");
//        for (Attribute attr : ordinary.keySet()) {
//            Debug.info(Debug.Attribute, "" + attr.getId() + ": " + ordinary.get(attr));
//        }
//        Debug.info(Debug.Attribute, "Base Potency:");
//        for (Attribute attr : potency.keySet()) {
//            Debug.info(Debug.Attribute, "" + attr.getId() + ": " + potency.get(attr));
//        }
//
//        Debug.info(Debug.Attribute, "Realm Health: " + health);
//        Debug.info(Debug.Attribute, "Realm Mana: " + mana);
//        Debug.info(Debug.Attribute, "Realm Damage: " + damage);
//        Debug.info(Debug.Attribute, "Realm Defence: " + defence);
//        Debug.info(Debug.Attribute, "Realm restoreHP：" + restoreHP);
//        Debug.info(Debug.Attribute, "Realm restoreMP: " + restoreMP);

        Debug.info(Debug.Attribute, "Source: ");
        this.source.keySet().forEach(s -> {
            AttributeSource data = this.source.get(s);
            data.getOrdinary().forEach((key, value) -> {
                ordinary.merge(key, value, Double::sum);
                Debug.info(Debug.Attribute, "Ordinary " + key.getId() + ": " + value);
            });
            data.getPotency().forEach((key, value) -> {
                potency.merge(key, value, Double::sum);
                Debug.info(Debug.Attribute, "Potency " + key.getId() + ": " + value);
            });
        });

        Player player = this.getBukkitPlayer();
        ordinary.merge(Attribute.Damage, Utils.getRealmDamagePromote(player), (o, n) -> o * n);
        ordinary.merge(Attribute.Defence, Utils.getRealmDefencePromote(player), (o, n) -> o * n);

        this.totalAttribute = new AttributeSource(ordinary, potency);
        int value = CombatCapacity.get(totalAttribute);
        this.change = value - this.combat;
        this.combat = value;

        if (combat) Utils.sendCombatChange(player, this.change);

        RoleAttributeUpdateEvent event = new RoleAttributeUpdateEvent(this.getBukkitPlayer(), this);
        Scheduler.run(event::call);
    }

    public double getDamageUpperLimit() {
        return this.levelDamageUpperLimit + 233;
    }

    public void putImmediateSource(String key, AttributeSource source) {
        this.putImmediateSource(key, source, -1);
    }

    public void putImmediateSource(String key, AttributeSource source, int tick) {
        if (source == null) this.source.remove(key);
        else this.source.put(key, source);

        this.update();
        if (tick == -1) return;

        Scheduler.runLaterAsync(uuid, () -> {
            this.source.remove(key);
            this.update();
        }, tick);
    }

    public void putImmediateSource(String key, AttributeSource source, boolean combat) {
        if (source == null) this.source.remove(key);
        else this.source.put(key, source);

        this.update(combat);
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
        if (source == null) this.source.remove(key);
        else this.source.put(key, source);

        if (second == -1) return;

        Scheduler.runLaterAsync(uuid, () -> {
            this.source.remove(key);
            this.update();
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

    public double getOrdinaryTotalValue(Attribute ident) {
        return totalAttribute.getOrdinary().get(ident);
    }

    public double getPotencyTotalValue(Attribute ident) {
        return totalAttribute.getPotency().get(ident);
    }
    
    public Player getBukkitPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    @Override
    public double getValue(Attribute attribute) {
        return attribute.calculate(this);
    }
}
