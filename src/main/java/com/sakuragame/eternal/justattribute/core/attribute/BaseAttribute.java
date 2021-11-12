package com.sakuragame.eternal.justattribute.core.attribute;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleAttribute;
import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import com.sakuragame.eternal.justattribute.util.Utils;
import lombok.Getter;

@Getter
public abstract class BaseAttribute {

    private final Identifier identifier;
    private final String symbol;
    private final String name;
    private final double base;
    private final boolean onlyPercent;

    public BaseAttribute(Identifier identifier, String symbol, String name, double base) {
        this(identifier, symbol, name, base, false);
    }

    public BaseAttribute(Identifier identifier, String symbol, String name, double base, boolean onlyPercent) {
        this.identifier = identifier;
        this.symbol = symbol;
        this.name = name;
        this.base = base;
        this.onlyPercent = onlyPercent;
    }

    public double calculate(RoleAttribute role) {
        return calculate(role.getOrdinaryTotalValue(identifier), role.getPotencyTotalValue(identifier));
    }

    public double calculate(double ordinaryValue, double potencyValue) {
        if (onlyPercent) {
            return ordinaryValue + potencyValue;
        }

        return ordinaryValue * (1 + potencyValue);
    }

    public String format(double value) {
        return format(value, onlyPercent);
    }

    public String format(double value, boolean isPercent) {
        return ConfigFile.attribute_format
                .replace("<symbol>", getSymbol())
                .replace("<identifier>", getName())
                .replace("<value>", Utils.formatValue(value, isPercent));
    }

    public String getOrdinaryNode() {
        return (JustAttribute.getInstance().getName() + ".ordinary." + identifier).toLowerCase();
    }

    public String getPotencyNode() {
        return (JustAttribute.getInstance().getName() + ".potency.addition." + identifier).toLowerCase();
    }
}
