package com.sakuragame.eternal.justattribute.core;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.BaseAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.Identifier;
import com.sakuragame.eternal.justattribute.core.attribute.attribute.*;
import lombok.Getter;

import java.util.HashMap;

public class AttributeManager {

    private final JustAttribute plugin;

    @Getter
    private final HashMap<Identifier, BaseAttribute> attr;

    public final static String ORDINARY_DISPLAY_NODE = "ordinary.display";
    public final static String POTENCY_DISPLAY_NODE = "potency.display";

    public AttributeManager(JustAttribute plugin) {
        this.plugin = plugin;
        this.attr = new HashMap<>();
        this.registerAttr(new Energy());
        this.registerAttr(new Stamina());
        this.registerAttr(new Wisdom());
        this.registerAttr(new Technique());
        this.registerAttr(new Damage());
        this.registerAttr(new Defence());
        this.registerAttr(new Health());
        this.registerAttr(new Mana());
        this.registerAttr(new RestoreHealth());
        this.registerAttr(new RestoreMana());
        this.registerAttr(new VampireDamage());
        this.registerAttr(new VampireVersatile());
        this.registerAttr(new DefencePenetration());
        this.registerAttr(new DamageImmune());
        this.registerAttr(new CriticalChance());
        this.registerAttr(new CriticalDamage());
        this.registerAttr(new ExpAddition());
    }

    private void registerAttr(BaseAttribute attribute) {
        this.attr.put(attribute.getIdentifier(), attribute);
    }
}
