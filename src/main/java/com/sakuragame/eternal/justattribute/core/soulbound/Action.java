package com.sakuragame.eternal.justattribute.core.soulbound;

import com.sakuragame.eternal.justattribute.core.soulbound.handler.*;
import lombok.Getter;

@Getter
public enum Action {

    AUTO_LOCK(0, new AutoHandler.Lock()),
    USE_LOCK(1, new UseHandler.Lock()),
    PROP_LOCK(2, new PropHandler.Lock()),
    AUTO(10, new AutoHandler.Normal()),
    USE(11, new UseHandler.Normal()),
    PROP(22, new PropHandler.Normal());

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