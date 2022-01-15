package com.sakuragame.eternal.justattribute.core.attribute.stats;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.api.event.role.RoleStateUpdateEvent;
import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import com.sakuragame.eternal.justattribute.hook.DragonCoreSync;
import com.sakuragame.eternal.justattribute.util.Debug;
import com.sakuragame.eternal.justattribute.util.Scheduler;
import org.bukkit.entity.Player;

public class RoleState {

    private final Player player;
    private double health;
    private double maxHealth;
    private double mana;
    private double maxMana;
    private double restoreHP;
    private double restoreMP;

    public RoleState(Player player) {
        this.player = player;
        this.health = 20;
        this.mana = 20;
    }

    public RoleState(Player player, double health, double mana) {
        this.player = player;
        this.health = health;
        this.mana = mana;
        Debug.info(Debug.Role, "storage hp: " + health);
        Debug.info(Debug.Role, "storage mp: " + mana);
    }

    public void update() {
        RoleAttribute role = JustAttribute.getRoleManager().getPlayerAttribute(player.getUniqueId());

        double maxHealth = role.getTotalHealth();
        double maxMana = role.getTotalMana();

        double currentHP = player.getHealth();

        Debug.info(Debug.Role, "max hp: " + maxHealth);
        Debug.info(Debug.Role, "max mp: " + maxMana);
        Debug.info(Debug.Role, "current hp: " + currentHP);
        Debug.info(Debug.Role, "current max hp: " + player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getBaseValue());
        Debug.info(Debug.Role, "hp: " + health);
        Debug.info(Debug.Role, "mp: " + mana);

        if (maxHealth < currentHP) {
            this.setHealth(maxHealth);
        }

        this.setMaxHealth(maxHealth);
        this.setMaxMana(maxMana);

        this.setHealth(health <= 20 ? maxHealth : Math.min(getMaxHealth(), health));
        this.setMana(mana <= 20 ? maxMana : Math.min(getMaxMana(), mana));
        this.setRestoreHP((1 + Attribute.Restore_Health.calculate(role)) * role.getRestoreHP());
        this.setRestoreMP((1 + Attribute.Restore_Mana.calculate(role)) * role.getRestoreMP());

        player.setWalkSpeed((float) role.getTotalValue(Attribute.MovementSpeed));
    }

    public void restore() {
        if (health >= getMaxHealth() && mana >= getMaxMana()) return;

        this.health = Math.min(getMaxHealth(), getHealth() + getRestoreHP());
        this.mana = Math.min(getMaxMana(), getMana() + getRestoreMP());

        RoleStateUpdateEvent event = new RoleStateUpdateEvent(player, this);
        event.call();
    }

    public void save() {
        JustAttribute.getStorageManager().updatePlayerData(player, getHealth(), getMana());
    }

    public void addHealth(double value) {
        setHealth(getHealth() + value);
    }

    public void takeHealth(double value) {
        setHealth(Math.max(0, getHealth() - value));
    }

    public void setHealth(double value) {
        value = Math.min(value, getMaxHealth());
        this.health = value;
        this.player.setHealth(value);

        RoleStateUpdateEvent event = new RoleStateUpdateEvent(player, this);
        event.call();
    }

    public void setMaxHealth(double value) {
        this.maxHealth = value;
        this.player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).setBaseValue(value);
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

        RoleStateUpdateEvent event = new RoleStateUpdateEvent(player, this);
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

    public double getRestoreHP() {
        return restoreHP;
    }

    public double getRestoreMP() {
        return restoreMP;
    }

    public void updateHealth(double health) {
        this.health = Math.max(0, Math.min(health, getMaxHealth()));

        RoleStateUpdateEvent event = new RoleStateUpdateEvent(player, this);
        event.call();
    }

    public double getHealthPercentage() {
        return getHealth() / getMaxHealth();
    }

    public double getManaPercentage() {
        return getMana() / getMaxMana();
    }
}
