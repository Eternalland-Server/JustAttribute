package com.sakuragame.eternal.justattribute.core.attribute;

import com.sakuragame.eternal.justattribute.core.special.PotencyGrade;
import lombok.Getter;

import java.util.List;

@Getter
public class PotencyLayer {

    private final PotencyGrade grade;
    private final double weight;
    private final List<AttributeEntry> entries;

    public PotencyLayer(PotencyGrade grade, double weight, List<AttributeEntry> entries) {
        this.grade = grade;
        this.weight = weight;
        this.entries = entries;
    }
}
