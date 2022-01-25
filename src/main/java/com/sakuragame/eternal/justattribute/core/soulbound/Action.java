package com.sakuragame.eternal.justattribute.core.soulbound;

import com.sakuragame.eternal.justattribute.core.soulbound.handler.*;
import lombok.Getter;

@Getter
public enum Action {

    SEAL(-1, false, false, new SealHandler()),
    AUTO_LOCK(0, true, true, new AutoHandler.Lock()),
    USE_LOCK(1, false, false, new UseHandler.Lock()),
    PROP_LOCK(2, true, true, new PropHandler.Lock()),
    AUTO(10, true, true, new AutoHandler.Normal()),
    USE(11, false, false, new UseHandler.Normal()),
    PROP(22, true, true, new PropHandler.Normal());

    private final int id;
    private final boolean lock;
    private final boolean initiative;
    private final BoundHandler handler;

    Action(int id, boolean lock, boolean initiative, BoundHandler handler) {
        this.id = id;
        this.lock = lock;
        this.initiative = initiative;
        this.handler = handler;
    }

    public static Action match(int id) {
        for (Action action : values()) {
            if (action.getId() == id) {
                return action;
            }
        }

        return null;
    }
}