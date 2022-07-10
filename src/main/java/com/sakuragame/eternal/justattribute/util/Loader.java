package com.sakuragame.eternal.justattribute.util;

import com.sakuragame.eternal.justattribute.JustAttribute;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Loader {

    private final UUID uuid;
    private final List<String> depend;

    public Loader(UUID uuid) {
        this.uuid = uuid;
        this.depend = new ArrayList<>();

        for (Identifier ident : Identifier.values()) {
            this.depend.add(ident.name());
        }
    }

    public void enter(String id) {
        this.depend.add(id);
    }

    public void leave(String id) {
        this.depend.remove(id);
        if (!this.depend.isEmpty()) return;

        JustAttribute.getRoleManager().init(this.uuid);
    }

    public enum Identifier {
        Level,Slot,Inventory
    }
}
