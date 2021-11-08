package com.sakuragame.eternal.justattribute.commands;

import lombok.Getter;

public enum CommandPerms {

    USER("justattribute.user"),
    ADMIN("justattribute.admin");

    @Getter
    private final String node;

    CommandPerms(String node) {
        this.node = node;
    }
}
