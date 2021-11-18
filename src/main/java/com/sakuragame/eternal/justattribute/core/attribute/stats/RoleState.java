package com.sakuragame.eternal.justattribute.core.attribute.stats;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
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
        this.health = -1;
        this.mana = -1;
    }

    public RoleState(Player player, double health, double mana) {
        this.player = player;
        this.health = health;
        this.mana = mana;
    }

    public void update() {
        RoleAttribute role = JustAttribute.getRoleManager().getPlayerAttribute(player.getUniqueId());

        double maxHealth = role.getTotalHealth();
        double maxMana = role.getTotalMana();

        double currentHP = player.getHealth();

        if (maxHealth < currentHP) {
            this.setHealth(maxHealth);
        }

        this.setMaxHealth(maxHealth);
        this.setMaxHealth(maxMana);

        this.setHealth(health == -1 ? maxHealth : health);
        this.setMana(mana == -1 ? maxMana : mana);
        this.setRestoreHP((1 + Attribute.Restore_Health.calculate(role)) * role.getRestoreHP());
        this.setRestoreMP((1 + Attribute.Restore_Mana.calculate(role)) * role.getRestoreMP());
    }

    public void restore() {
        this.addHealth(getRestoreHP());
        this.addHealth(getRestoreMP());
    }

    public void save() {
        JustAttribute.getStorageManager().updatePlayerData(player, getHealth(), getMana());
    }

    public void setHealth(double value) {
        value = Math.min(value, getMaxHealth());
        this.health = value;
        this.player.setHealth(value);
    }

    public void setMaxHealth(double value) {
        this.maxHealth = value;
        this.player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).setBaseValue(value);
    }

    public void addHealth(double value) {
        value = Math.min(value + getHealth(), getMaxHealth());
        this.health = value;
        this.player.setHealth(value);
    }

    public double getHealth() {
        return this.health;
    }

    public double getMaxHealth() {
        return this.maxHealth;
    }

    public void setMana(double value) {
        mana = Math.min(value, this.maxMana);
    }

    public void setMaxMana(double value) {
        maxMana = value;
    }

    public void addMana(double value) {
        mana = Math.min(value + getMana(), maxMana);
    }

    public boolean takeMana(double value) {
        if (mana < value) return false;

        mana =- value;
        return true;
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
}
