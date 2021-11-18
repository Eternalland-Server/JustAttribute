package com.sakuragame.eternal.justattribute.core.attribute;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleAttribute;
import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import com.sakuragame.eternal.justattribute.util.Utils;
import lombok.Getter;

@Getter
public enum Attribute {

    Energy("energy", "ㇿ", "气力", 0, false),
    Stamina("stamina", "ㇾ", "耐力", 0, false),
    Wisdom("wisdom", "ㇽ", "慧根", 0, false),
    Technique("technique", "ㇼ", "技巧", 0, false),
    Damage("damage", "ㇻ", "攻击力", 0, false),
    Defence("defence", "ㇺ", "防御力", 0, false),
    Health("health", "ㇹ", "生命值", 0, false),
    Mana("mana", "ㇸ", "法力值", 0, false),
    Restore_Health("restore_health", "ㇷ", "生命恢复", 0, true),
    Restore_Mana("restore_mana", "ㇶ", "法力恢复", 0, true),
    Vampire_Damage("vampire_damage", "ㇵ", "生命汲取", 0, true),
    Vampire_Versatile("vampire_versatile", "ㇴ", "全能吸血", 0, true),
    Defence_Penetration("defence_penetration", "ㇳ", "护甲穿透", 0, true),
    Damage_Immune("damage_immune", "ㇲ", "伤害免疫", 0, true),
    Critical_Chance("critical_chance", "ㇱ", "暴击几率", 0, true),
    Critical_Damage("critical_damage", "ㇰ", "暴击伤害", 1.25, true),
    EXP_Addition("exp_addition", "け", "经验加成", 0, true);

    private final String id;
    private final String symbol;
    private final String display;
    private final double base;
    private final boolean onlyPercent;

    Attribute(String id, String symbol, String display, double base, boolean onlyPercent) {
        this.id = id;
        this.symbol = symbol;
        this.display = display;
        this.base = base;
        this.onlyPercent = onlyPercent;
    }

    public static Attribute get(String id) {
        for (Attribute ident : values()) {
            if (ident.getId().equals(id)) {
                return ident;
            }
        }

        return null;
    }

    public double calculate(RoleAttribute role) {
        return calculate(role.getOrdinaryTotalValue(this), role.getPotencyTotalValue(this));
    }

    public double calculate(double ordinaryValue, double potencyValue) {
        double value = onlyPercent ? (ordinaryValue + potencyValue) : ordinaryValue * (1 + potencyValue);

        if (this == Critical_Chance) {
            value = Math.min(1, value);
        }

        if (this == Damage_Immune) {
            value = Math.min(ConfigFile.damage_immune_limit, value);
        }

        return value;
    }

    public String getPlaceholder() {
        return "attribute_" + getId();
    }

    public String format(double value) {
        return format(value, onlyPercent);
    }

    public String formatting(double value) {
        return Utils.format(value, onlyPercent);
    }

    public String format(double value, boolean isPercent) {
        return ConfigFile.attribute_format
                .replace("<symbol>", getSymbol())
                .replace("<identifier>", getDisplay())
                .replace("<value>", Utils.formatValue(value, isPercent));
    }

    public String getOrdinaryNode() {
        return (JustAttribute.getInstance().getName() + ".ordinary." + getId()).toLowerCase();
    }

    public String getPotencyNode() {
        return (JustAttribute.getInstance().getName() + ".potency.addition." + getId()).toLowerCase();
    }
}
