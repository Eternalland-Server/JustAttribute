package com.sakuragame.eternal.justattribute.util;

import com.sakuragame.eternal.justattribute.JustAttribute;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Loader {

    private final UUID uuid;
    private boolean justLevel;
    private boolean dragonSlot;
    private boolean inventory;
    private final List<String> influence;

    public Loader(UUID uuid) {
        this.uuid = uuid;
        this.justLevel = false;
        this.dragonSlot = false;
        this.inventory = false;
        this.influence = new ArrayList<>();
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

    public void enter(String id) {
        this.influence.add(id);
    }

    public void leave(String id) {
        this.influence.remove(id);

        this.tryExecute();
    }

    public void tryExecute() {
        if (justLevel && dragonSlot && (!JustAttribute.PLAYER_SQL || inventory) && influence.isEmpty()) {
            JustAttribute.getRoleManager().loadAttributeData(uuid);
        }
    }
}
