package com.sakuragame.eternal.justattribute.core.attribute;

import lombok.Getter;

@Getter
public enum Identifier {

    Energy("energy"),
    Stamina("stamina"),
    Wisdom("wisdom"),
    Technique("technique"),
    Damage("damage"),
    Defence("defence"),
    Health("health"),
    Mana("mana"),
    Restore_Health("restore_health"),
    Restore_Mana("restore_mana"),
    Vampire_Damage("vampire_damage"),
    Vampire_Versatile("vampire_versatile"),
    Defence_Penetration("defence_penetration"),
    Damage_Immune("damage_immune"),
    Critical_Chance("critical_chance"),
    Critical_Damage("critical_damage"),
    EXP_Addition("exp_addition");

    private final String id;

    Identifier(String id) {
        this.id = id;
    }

}
