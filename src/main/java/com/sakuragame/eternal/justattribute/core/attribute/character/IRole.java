package com.sakuragame.eternal.justattribute.core.attribute.character;

public interface IRole {

    double getHP();

    double getMP();

    void setHP(double value);

    void setMP(double value);

    double getMaxHP();

    double getMaxMP();

    void restore();
    boolean consumeMP(double value);

    default void addHP(double value) {
        this.setHP(this.getHP() + value);
    }

    default void takeHP(double value) {
        this.setHP(this.getHP() - value);
    }

    default void addMP(double value) {
        this.setMP(this.getMP() + value);
    }

    default void takeMP(double value) {
        this.setMP(this.getMP() - value);
    }

    default boolean isFullStateHP() {
        return this.getHP() >= this.getMaxHP();
    }

    default boolean isFullStateMP() {
        return this.getMP() >= this.getMaxMP();
    }
}
