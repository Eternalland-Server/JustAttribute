package com.sakuragame.eternal.justattribute.core.attribute.character;

import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import com.sakuragame.eternal.justattribute.core.attribute.AttributeSource;
import com.sakuragame.eternal.justattribute.core.special.CombatPower;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface ICharacter {

    UUID getUUID();

    void init();

    void update();

    CombatPower.History getCombatPower();

    int getCombatValue();

    int getCombatLastTimeChange();

    void putAttributeSource(String key, AttributeSource source);

    void putAttributeSource(String key, AttributeSource source, boolean update);

    void putAttributeSource(String key, AttributeSource source, long tick);

    void removeAttributeSource(String key);

    double getAttributeValue(Attribute identifier);

    default Player getPlayer() {
        return Bukkit.getPlayer(this.getUUID());
    }
}
