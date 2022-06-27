package com.sakuragame.eternal.justattribute.core.attribute.stats;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.api.JustAttributeAPI;
import com.sakuragame.eternal.justattribute.api.event.role.RoleConsumeManaEvent;
import com.sakuragame.eternal.justattribute.api.event.role.RoleStateUpdateEvent;
import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import com.sakuragame.eternal.justattribute.util.Debug;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class RoleState {

    private final UUID uuid;
    private double health;
    private double maxHealth;
    private double mana;
    private double maxMana;
    private double restoreHP;
    private double restoreMP;
    private double damageVampire;
    private double powerVampire;

    public RoleState(UUID uuid) {
        this.uuid = uuid;
        this.health = 20;
        this.mana = 20;
    }

    public RoleState(UUID uuid, double health, double mana) {
        this.uuid = uuid;
        this.health = health;
        this.mana = mana;
        Debug.info(Debug.Role, "storage hp: " + health);
        Debug.info(Debug.Role, "storage mp: " + mana);
    }

    public void update() {
        Player player = this.getBukkitPlayer();
        RoleAttribute role = JustAttributeAPI.getRoleAttribute(uuid);

        double maxHealth = role.getValue(Attribute.Health);
        double maxMana = role.getValue(Attribute.Mana);

        double currentHP = player.getHealth();

//        Debug.info(Debug.Role, "max hp: " + maxHealth);
//        Debug.info(Debug.Role, "max mp: " + maxMana);
//        Debug.info(Debug.Role, "current hp: " + currentHP);
//        Debug.info(Debug.Role, "current max hp: " + player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getBaseValue());
//        Debug.info(Debug.Role, "hp: " + health);
//        Debug.info(Debug.Role, "mp: " + mana);

        this.setMaxHealth(maxHealth);
        this.setMaxMana(maxMana);

        if (maxHealth < currentHP) {
            this.setHealth(maxHealth);
        }

        this.setHealth(health <= 20 ? maxHealth : Math.min(maxHealth, health));
        this.setMana(mana <= 20 ? maxMana : Math.min(maxMana, mana));

        this.setRestoreHP((1 + Attribute.Restore_Health.calculate(role)) * role.getValue(Attribute.Energy) / 100);
        this.setRestoreMP((1 + Attribute.Restore_Mana.calculate(role)) * role.getValue(Attribute.Stamina) / 100);
        this.setDamageVampire(role.getValue(Attribute.Wisdom) / 100);
        this.setPowerVampire(role.getValue(Attribute.Technique) / 100);

        player.setWalkSpeed((float) (0.2 * role.getValue(Attribute.MovementSpeed)));
    }

    public void restore() {
        if (health >= getMaxHealth() && mana >= getMaxMana()) return;

        this.health = Math.min(this.getMaxHealth(), this.getHealth() + (ConfigFile.spring ? this.getMaxHealth() * 0.03 : this.getRestoreHP()));
        this.mana = Math.min(this.getMaxMana(), this.getMana() + (ConfigFile.spring ? this.getMaxMana() * 0.03 : this.getRestoreMP()));

        RoleStateUpdateEvent event = new RoleStateUpdateEvent(this.getBukkitPlayer());
        event.call();
    }

    public void save() {
        JustAttribute.getStorageManager().saveData(uuid, getHealth(), getMana());
    }

    public void addHealth(double value) {
        setHealth(getHealth() + value);
    }

    public void takeHealth(double value) {
        setHealth(Math.max(0, getHealth() - value));
    }

    public void setHealth(double value) {
        Player player = this.getBukkitPlayer();

        value = Math.min(value, getMaxHealth());
        this.health = value;
        player.setHealth(value);

        RoleStateUpdateEvent event = new RoleStateUpdateEvent(player);
        event.call();
    }

    public void setMaxHealth(double value) {
        this.maxHealth = value;
        this.getBukkitPlayer().getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).setBaseValue(value);
    }

    public double getHealth() {
        return health;
    }

    public double getMaxHealth() {
        return this.maxHealth;
    }

    public void addMana(double value) {
        setMana(getMana() + value);
    }

    public void takeMana(double value) {
        setMana(Math.max(0, getMana() - value));
    }

    public void setMana(double value) {
        mana = Math.max(0, Math.min(value, this.maxMana));

        RoleStateUpdateEvent event = new RoleStateUpdateEvent(this.getBukkitPlayer());
        event.call();
    }

    public void setMaxMana(double value) {
        maxMana = value;
    }

    public double getMana() {
        return mana;
    }

    public double getMaxMana() {
        return maxMana;
    }

    public void setRestoreHP(double restoreHP) {
        this.restoreHP = restoreHP;
    }

    public void setRestoreMP(double restoreMP) {
        this.restoreMP = restoreMP;
    }

    public void setDamageVampire(double damageVampire) {
        this.damageVampire = damageVampire;
    }

    public void setPowerVampire(double powerVampire) {
        this.powerVampire = powerVampire;
    }

    public double getRestoreHP() {
        return restoreHP;
    }

    public double getRestoreMP() {
        return restoreMP;
    }

    public double getDamageVampire() {
        return damageVampire;
    }

    public double getPowerVampire() {
        return powerVampire;
    }

    public void updateHealth(double health) {
        this.health = Math.max(0, Math.min(health, getMaxHealth()));

        RoleStateUpdateEvent event = new RoleStateUpdateEvent(this.getBukkitPlayer());
        event.call();
    }

    public double getHealthPercentage() {
        return getHealth() / getMaxHealth();
    }

    public double getManaPercentage() {
        return getMana() / getMaxMana();
    }

    public boolean consumeMana(double value) {
        double mp = this.getMana();
        if (mp < value) return false;

        Player player = this.getBukkitPlayer();
        RoleConsumeManaEvent.Pre preEvent = new RoleConsumeManaEvent.Pre(player, value);
        preEvent.call();
        if (preEvent.isCancelled()) return false;

        this.setMana(mp - value);

        RoleConsumeManaEvent.Post postEvent = new RoleConsumeManaEvent.Post(player, value);
        postEvent.call();
        return true;
    }

    public Player getBukkitPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }
}
