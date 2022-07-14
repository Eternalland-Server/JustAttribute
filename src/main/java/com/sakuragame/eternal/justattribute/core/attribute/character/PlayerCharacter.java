package com.sakuragame.eternal.justattribute.core.attribute.character;

import com.sakuragame.eternal.justattribute.api.event.role.RoleAttributeUpdateEvent;
import com.sakuragame.eternal.justattribute.api.event.role.RoleConsumeManaEvent;
import com.sakuragame.eternal.justattribute.api.event.role.RoleStateUpdateEvent;
import com.sakuragame.eternal.justattribute.core.AttributeHandler;
import com.sakuragame.eternal.justattribute.core.RoleManager;
import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import com.sakuragame.eternal.justattribute.core.attribute.AttributeSource;
import com.sakuragame.eternal.justattribute.core.special.CombatPower;
import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import com.sakuragame.eternal.justattribute.util.Utils;
import net.sakuragame.eternal.justlevel.api.JustLevelAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerCharacter extends JustCharacter implements IRole {

    private double health;
    private double mana;
    private final AttributeSource total;

    public PlayerCharacter(UUID uuid) {
        super(uuid);
        this.health = Attribute.Health.getBase();
        this.mana = Attribute.Mana.getBase();
        this.total = new AttributeSource();
    }

    public PlayerCharacter(UUID uuid, double hp, double mp) {
        super(uuid);
        this.health = hp;
        this.mana = mp;
        this.total = new AttributeSource();
    }

    @Override
    public void init() {
        Player player = Bukkit.getPlayer(this.getUUID());

        AttributeHandler.loadVanillaSlot(player);
        AttributeHandler.loadCustomSlot(player);
        this.putAttributeSource("_BASE_", Attribute.generateBase());
        this.updateLevelAddition();
    }

    @Override
    public void update() {
        if (RoleManager.isLoading(this.getUUID())) return;

        Player player = this.getPlayer();

        // update attribute
        this.calculateTotalAttribute();
        int value = CombatPower.calculate(this.total);
        this.getCombatPower().update(value);

        // update state
        double maxHP = this.getAttributeValue(Attribute.Health);
        double maxMP = this.getAttributeValue(Attribute.Mana);
        double current = player.getHealth();

        player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHP);

        if (maxHP < current) this.setHealth(maxHP);
        this.setHealth(Math.min(maxHP, this.health));
        this.setMana(Math.min(maxMP, this.mana));

        player.setWalkSpeed((float) (0.2 * this.getAttributeValue(Attribute.MovementSpeed)));

        RoleAttributeUpdateEvent event = new RoleAttributeUpdateEvent(player, this);
        event.call();
    }

    public void updateLevelAddition() {
        int totalLevel = JustLevelAPI.getTotalLevel(this.getUUID());
        AttributeSource source = new AttributeSource();

        source
                .addOrdinary(Attribute.Health, 4 * totalLevel)
                .addOrdinary(Attribute.Mana, 4 * totalLevel)
                .addOrdinary(Attribute.Damage, totalLevel * 0.33)
                .addOrdinary(Attribute.Defence, totalLevel * 0.33)
                .addOrdinary(Attribute.Energy, totalLevel)
                .addOrdinary(Attribute.Stamina, totalLevel)
                .addOrdinary(Attribute.Wisdom, totalLevel)
                .addOrdinary(Attribute.Technique, totalLevel);

        this.putAttributeSource("_LEVEL_", source);
    }

    public void calculateTotalAttribute() {
        Map<Attribute, Double> ordinary = new HashMap<>();
        Map<Attribute, Double> potency = new HashMap<>();

        this.getSources().keySet().forEach(key -> {
            AttributeSource source = this.getSources().get(key);

            source.getOrdinary().forEach((k, v) -> ordinary.merge(k, v, Double::sum));
            source.getPotency().forEach((k, v) -> potency.merge(k, v, Double::sum));

//            System.out.println(" === ");
//            System.out.println(key + " - hp: " + ordinary.getOrDefault(Attribute.Health, 0d));
//            System.out.println(key + " - mp: " + ordinary.getOrDefault(Attribute.Mana, 0d));
//            System.out.println(key + " - *hp: " + potency.getOrDefault(Attribute.Health, 0d));
//            System.out.println(key + " - *mp: " + potency.getOrDefault(Attribute.Mana, 0d));
        });

//        System.out.println(" ==== ");
//        System.out.println("total hp: " + ordinary.getOrDefault(Attribute.Health, 0d) + " - " + potency.getOrDefault(Attribute.Health, 0d));
//        System.out.println("total mp: " + ordinary.getOrDefault(Attribute.Mana, 0d) + " - " + potency.getOrDefault(Attribute.Mana, 0d));

        ordinary.merge(Attribute.Damage, Utils.getRealmDamagePromote(this.getUUID()), (a, b) -> a * b);
        ordinary.merge(Attribute.Defence, Utils.getRealmDefencePromote(this.getUUID()), (a, b) -> a * b);

        this.total.setOrdinary(ordinary);
        this.total.setPotency(potency);
    }

    @Override
    public double getAttributeValue(Attribute identifier) {
        return identifier.calculate(
                this.total.getOrdinary().getOrDefault(identifier, 0d),
                this.total.getPotency().getOrDefault(identifier, 0d)
        );
    }

    @Override
    public double getHP() {
        return this.health;
    }

    @Override
    public double getMP() {
        return this.mana;
    }

    @Override
    public void setHP(double value) {
        Player player = this.getPlayer();

        this.health = Math.max(0, Math.min(value, this.getMaxHP()));
        player.setHealth(this.health);

        RoleStateUpdateEvent event = new RoleStateUpdateEvent(player);
        event.call();
    }

    @Override
    public void setMP(double value) {
        Player player = this.getPlayer();

        this.mana = Math.max(0, Math.min(value, this.getMaxHP()));

        RoleStateUpdateEvent event = new RoleStateUpdateEvent(player);
        event.call();
    }

    @Override
    public double getMaxHP() {
        return this.getAttributeValue(Attribute.Health);
    }

    @Override
    public double getMaxMP() {
        return this.getAttributeValue(Attribute.Mana);
    }


    @Override
    public void restore() {
        if (this.isFullStateHP() && this.isFullStateMP()) return;

        this.setHealth(
                Math.min(
                        this.getMaxHP(),
                        this.getHP() + (ConfigFile.spring ? Math.max(this.getMaxHP() * 0.03, this.getRestoreHP()) : this.getRestoreHP())
                )
        );

        this.setMana(
                Math.min(
                        this.getMaxMP(),
                        this.getMP() + (ConfigFile.spring ? Math.max(this.getMaxMP() * 0.03, this.getRestoreMP()) : this.getRestoreMP())
                )
        );

        RoleStateUpdateEvent event = new RoleStateUpdateEvent(this.getPlayer());
        event.call();
    }

    @Override
    public boolean consumeMP(double value) {
        double current = this.getMP();
        if (current < value) return false;

        Player player = this.getPlayer();
        RoleConsumeManaEvent.Pre preEvent = new RoleConsumeManaEvent.Pre(player, value);
        preEvent.call();
        if (preEvent.isCancelled()) return false;

        this.takeMP(value);

        RoleConsumeManaEvent.Post postEvent = new RoleConsumeManaEvent.Post(player, value);
        postEvent.call();

        return true;
    }

    public void setHealth(double health) {
        this.health = health;
        this.getPlayer().setHealth(health);
    }

    public void setMana(double mana) {
        this.mana = mana;
    }

    public void updateHP(double value) {
        this.health = Math.max(0, Math.min(value, this.getMaxHP()));
        RoleStateUpdateEvent event = new RoleStateUpdateEvent(this.getPlayer());
        event.call();
    }

    public double getRestoreHP() {
        return (1 + this.getAttributeValue(Attribute.Restore_Health)) * (this.getAttributeValue(Attribute.Energy) / 100);
    }

    public double getRestoreMP() {
        return (1 + this.getAttributeValue(Attribute.Restore_Mana)) + (this.getAttributeValue(Attribute.Stamina) / 100);
    }

    public double getDamageVampire() {
        return this.getAttributeValue(Attribute.Wisdom) / 100;
    }

    public double getSkillVampire() {
        return this.getAttributeValue(Attribute.Technique) / 100;
    }
}
