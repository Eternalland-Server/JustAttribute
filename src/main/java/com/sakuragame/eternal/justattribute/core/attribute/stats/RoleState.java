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
        this.initState();
    }

    private void initState() {
        double health = 0d;
        mana = 0d;

        this.update();
    }

    public void update() {
        RoleAttribute role = JustAttribute.getRoleManager().getPlayerAttribute(player.getUniqueId());

        double maxHealth = role.getTotalHealth();
        double currentHP = player.getHealth();

        if (maxHealth < currentHP) {
            player.setHealth(maxHealth);
        }
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth);

        maxMana = role.getTotalMana();
    }

    public void restore() {
        RoleAttribute attribute = JustAttribute.getRoleManager().getPlayerAttribute(player.getUniqueId());
        double hp = JustAttribute.getAttributeManager().getAttribute(Identifier.Restore_Health).calculate(attribute);
        double mp = JustAttribute.getAttributeManager().getAttribute(Identifier.Restore_Mana).calculate(attribute);

        addHealth(hp);
        addHealth(mp);
    }

    public void setHealth(double value) {
        this.player.setHealth(Math.min(value, getMaxHealth()));
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
