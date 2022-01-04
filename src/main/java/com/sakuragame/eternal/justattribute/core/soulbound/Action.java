package com.sakuragame.eternal.justattribute.core.soulbound;

import com.sakuragame.eternal.justattribute.core.soulbound.handler.*;
import lombok.Getter;

@Getter
public enum Action {

    AUTO(0, new AutoHandler.Normal()),
    USE(1, new UseHandler.Normal()),
    PROP(2, new PropHandler.Normal()),
    AUTO_LOCK(10, new AutoHandler.Lock()),
    USE_LOCK(11, new UseHandler.Lock()),
    PROP_LOCK(12, new PropHandler.Lock());

    private final int id;
    private final BoundHandler handler;

    Action(int id, BoundHandler handler) {
        this.id = id;
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