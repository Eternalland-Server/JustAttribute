package com.sakuragame.eternal.justattribute.core.attribute.stats;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.Identifier;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

public class RoleState {

    private final Player player;
    private double mana;
    private double maxMana;

    public RoleState(Player player) {
        this.player = player;
        this.initRole();
    }

    public RoleState(Player player, double health, double mana) {
        this.player = player;
        this.setHealth(health);
        this.setMana(mana);
        this.update();
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
    }

    public void restore() {
        RoleAttribute role = JustAttribute.getRoleManager().getPlayerAttribute(player.getUniqueId());
        double hp = JustAttribute.getAttributeManager().getAttribute(Identifier.Restore_Health).calculate(role);
        double mp = JustAttribute.getAttributeManager().getAttribute(Identifier.Restore_Mana).calculate(role);

        this.addHealth(hp);
        this.addHealth(mp);
    }

    private void initRole() {
        RoleAttribute role = JustAttribute.getRoleManager().getPlayerAttribute(player.getUniqueId());

        double maxHealth = role.getTotalHealth();
        double maxMana = role.getTotalMana();

        this.setMaxHealth(maxHealth);
        this.setMaxHealth(maxMana);
        this.setHealth(maxHealth);
        this.setMana(maxMana);
    }

    public void save() {
        JustAttribute.getStorageManager().updatePlayerData(player, getHealth(), getMana());
    }

    public void setHealth(double value) {
        this.player.setHealth(Math.min(value, getMaxHealth()));
    }

    public void setMaxHealth(double value) {
        this.player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(value);
    }

    public void addHealth(double value) {
        this.player.setHealth(Math.min(value + getHealth(), getMaxHealth()));
    }

    public double getHealth() {
        return player.getHealth();
    }

    public double getMaxHealth() {
        return player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
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
}
