package com.sakuragame.eternal.justattribute.core.soulbound;

import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class Owner {

    private final UUID uuid;
    private final String name;

    public UUID getUUID() {
        return uuid;
    }

    public String getName() {
        return name;
    }
}
