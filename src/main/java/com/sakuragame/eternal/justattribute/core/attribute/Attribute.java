package com.sakuragame.eternal.justattribute.core.attribute;

import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import com.sakuragame.eternal.justattribute.util.Utils;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum Attribute {

    Energy("energy", "ㇿ", "气力", 100, 1, false),
    Stamina("stamina", "ㇾ", "耐力", 100, 1, false),
    Wisdom("wisdom", "ㇽ", "慧根", 0,  1, false),
    Technique("technique", "ㇼ", "技巧", 0, 1, false),
    Damage("damage", "ㇻ", "攻击力", 15, 8, false),
    Defence("defence", "ㇺ", "防御力", 10,  8, false),
    Health("health", "ㇹ", "生命值", 300,  5, false),
    Mana("mana", "ㇸ", "法力值", 500,  5, false),
    Restore_Health("restore_health", "ㇷ", "生命恢复", 0, 0, true),
    Restore_Mana("restore_mana", "ㇶ", "法力恢复", 0, 0,true),
    Vampire_Damage("vampire_damage", "ㇵ", "生命汲取", 0, 0,true),
    Vampire_Versatile("vampire_versatile", "ㇴ", "技能吸血", 0, 0,true),
    Defence_Penetration("defence_penetration", "ㇳ", "护甲穿透", 0, 0,true),
    Damage_Immune("damage_immune", "ㇲ", "伤害免疫", 0, 0, true),
    Critical_Chance("critical_chance", "ㇱ", "暴击几率", 0, 0, true),
    Critical_Damage("critical_damage", "ㇰ", "暴击伤害", 1.25, 0, true),
    EXP_Addition("exp_addition", "け", "经验加成", 0, 0, true),
    MovementSpeed("movement_speed", "ふ", "移动速度", 0, 0, true),
    AttackSpeed("attack_speed", "ぶ", "攻击速度", 0, 0, true);

    private final String id;
    private final String symbol;
    private final String display;
    private final double base;
    private final int score;
    private final boolean onlyPercent;

    public final static String DISPLAY_NODE_ORDINARY = "display.ordinary";
    public final static String DISPLAY_NODE_POTENCY = "display.potency";
    public final static String NBT_NODE_ORDINARY = "justattribute.ordinary";
    public final static String NBT_NODE_POTENCY = "justattribute.potency";

    Attribute(String id, String symbol, String display, double base, int score, boolean onlyPercent) {
        this.id = id;
        this.symbol = symbol;
        this.display = display;
        this.base = base;
        this.score = score;
        this.onlyPercent = onlyPercent;
    }

    public static Attribute match(String id) {
        for (Attribute ident : values()) {
            if (ident.getId().equals(id)) {
                return ident;
            }
        }

        return null;
    }

    public static AttributeSource generateBase() {
        Map<Attribute, Double> ordinary = new HashMap<>();
        Map<Attribute, Double> potency = new HashMap<>();

        for (Attribute ident : Attribute.values()) {
            if (!ident.isOnlyPercent()) {
                ordinary.put(ident, ident.getBase());
            }
            else {
                potency.put(ident, ident.getBase());
            }
        }

        return new AttributeSource(ordinary, potency);
    }

    public double calculate(double ordinaryValue, double potencyValue) {
        double value = onlyPercent ? (ordinaryValue + potencyValue) : ordinaryValue * (1 + potencyValue);

        if (this == Critical_Chance) {
            value = Math.min(1, value);
        }

        if (this == Damage_Immune) {
            value = Math.min(ConfigFile.damage_immune_limit, value);
        }

        if (this == MovementSpeed) {
            value += 1;
        }

        return value;
    }

    public String getPlaceholder() {
        return "attribute_" + getId();
    }

    public String format(double value) {
        return format(value, this.onlyPercent);
    }

    public String format(double origin, double enhance) {
        String v1 = (origin >= 0 ? "+" : "") + Utils.FORMAT_A.format(origin);
        String v2 = (enhance >= 0 ? "+" : "") + Utils.FORMAT_A.format(enhance);
        String v3 = v1 + "§a(" + v2 + ")";

        return ConfigFile.format.attribute
                .replace("<symbol>", getSymbol())
                .replace("<identifier>", getDisplay())
                .replace("<value>", v3);
    }

    public String format(double value, boolean isPercent) {
        value = isPercent ? value * 100 : value;
        String str = (value >= 0 ? "+" : "") + Utils.FORMAT_C.format(value);
        str = isPercent ? str + "%" : str;

        return ConfigFile.format.attribute
                .replace("<symbol>", getSymbol())
                .replace("<identifier>", getDisplay())
                .replace("<value>", str);
    }

    public String formatting(double value) {
        return Utils.format(value, this.onlyPercent);
    }

    public String getOrdinaryNode() {
        return (NBT_NODE_ORDINARY + "." + getId()).toLowerCase();
    }

    public String getPotencyNode() {
        return (NBT_NODE_POTENCY + "." + getId()).toLowerCase();
    }
}
