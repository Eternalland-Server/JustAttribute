package com.sakuragame.eternal.justattribute.util;

import com.sakuragame.eternal.justattribute.JustAttribute;

public class RoleSync {

    private boolean justLevel;
    private boolean dragonSlot;
    private boolean inventory;
    private boolean heldEvent;

    public RoleSync() {
        this.justLevel = false;
        this.dragonSlot = false;
        this.inventory = false;
        this.heldEvent = false;
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

    public void setHeldEvent(boolean heldEvent) {
        this.heldEvent = heldEvent;
    }

    public boolean isFinished() {
        return justLevel && dragonSlot && heldEvent && (!JustAttribute.playerSQL || inventory);
    }
}
