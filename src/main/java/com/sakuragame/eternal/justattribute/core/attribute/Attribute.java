package com.sakuragame.eternal.justattribute.core.attribute;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import com.sakuragame.eternal.justattribute.util.Utils;
import lombok.Getter;

@Getter
public enum Attribute {

    Energy("energy", "ㇿ", "气力"),
    Stamina("stamina", "ㇾ", "耐力"),
    Wisdom("wisdom", "ㇽ", "慧根"),
    Technique("technique", "ㇼ", "技巧"),
    Damage("damage", "ㇻ", "攻击力"),
    Defence("defence", "ㇺ", "防御力"),
    Health("health", "ㇹ", "生命值"),
    Mana("mana", "ㇸ", "法力值"),
    Restore_Health("restore_health", "ㇷ", "生命恢复"),
    Restore_Mana("restore_mana", "ㇶ", "法力恢复"),
    Vampire_Damage("vampire_damage", "ㇵ", "生命汲取"),
    Vampire_Versatile("vampire_versatile", "ㇴ", "全能吸血"),
    Defence_Penetration("defence_penetration", "ㇳ", "护甲穿透"),
    Damage_Immune("damage_immune", "ㇲ", "伤害免疫"),
    Critical_Chance("critical_chance", "ㇱ", "暴击几率"),
    Critical_Damage("critical_damage", "ㇰ", "暴击伤害");


    private final String identifier;
    private final String symbol;
    private final String name;

    Attribute(String identifier, String symbol, String name) {
        this.identifier = identifier;
        this.symbol = symbol;
        this.name = name;
    }

    public String format(double value) {
        return format(value, false);
    }

    public String format(double value, boolean isPercent) {
        return ConfigFile.format
                .replace("<symbol>", getSymbol())
                .replace("<identifier>", getName())
                .replace("<value>", Utils.formatValue(value, isPercent));
    }

    public String getOrdinaryNode() {
        return (JustAttribute.getInstance().getName() + ".ordinary." + identifier).toLowerCase();
    }

    public String getPercentNode() {
        return (JustAttribute.getInstance().getName() + ".percent." + identifier).toLowerCase();
    }
}
