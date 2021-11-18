package com.sakuragame.eternal.justattribute.util;

public class RoleInitSync {

    private boolean justLevel;
    private boolean dragonSlot;
    private boolean inventory;

    public RoleInitSync() {
        this.justLevel = false;
        this.dragonSlot = false;
        this.inventory = false;
    }

    public void setJustLevel(boolean justLevel) {
        this.justLevel = justLevel;
    }

    public void setDragonSlot(boolean dragonSlot) {
        this.dragonSlot = dragonSlot;
    }

    public void setInventory(boolean inventory) {
        this.inventory = inventory;
    }

    public boolean isFinished() {
        return justLevel && dragonSlot && inventory;
    }
}
