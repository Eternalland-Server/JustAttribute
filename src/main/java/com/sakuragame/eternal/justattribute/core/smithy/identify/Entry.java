package com.sakuragame.eternal.justattribute.core.smithy.identify;

import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import lombok.Getter;

import java.util.Map;

@Getter
public class Entry {

    private final Level level;
    private final double weight;
    private final Map<Attribute, String> contents;

    public Entry(Level level, double weight, Map<Attribute, String> contents) {
        this.level = level;
        this.weight = weight;
        this.contents = contents;
    }
}
