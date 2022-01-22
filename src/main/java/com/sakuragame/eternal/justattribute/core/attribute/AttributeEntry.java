package com.sakuragame.eternal.justattribute.core.attribute;

import lombok.Getter;

@Getter
public class AttributeEntry {

    private final Attribute attribute;
    private int min;
    private int max;

    public AttributeEntry(Attribute attribute, int min, int max) {
        this.attribute = attribute;
        this.min = min;
        this.max = max;
    }

    public AttributeEntry(Attribute attribute, String range) {
        this.attribute = attribute;
        this.convert(range);
    }

    private void convert(String range) {
        if (range.contains("-")) {
            String[] args = range.split("-");
            this.min = Integer.parseInt(args[0]);
            this.max = Integer.parseInt(args[1]);
        }
        else {
            int value = Integer.parseInt(range);
            this.min = value;
            this.max = value;
        }
    }
}
