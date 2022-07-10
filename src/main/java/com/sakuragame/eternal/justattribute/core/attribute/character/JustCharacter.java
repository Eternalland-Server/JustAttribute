package com.sakuragame.eternal.justattribute.core.attribute.character;


import com.sakuragame.eternal.justattribute.core.attribute.AttributeSource;
import com.sakuragame.eternal.justattribute.core.special.CombatPower;
import com.sakuragame.eternal.justattribute.util.Scheduler;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class JustCharacter implements ICharacter {

    private final UUID uuid;
    private final Map<String, AttributeSource> sources;
    private final CombatPower.History combatPower;

    public JustCharacter(UUID uuid) {
        this.uuid = uuid;
        this.sources = new ConcurrentHashMap<>();
        this.combatPower = new CombatPower.History(0);
    }

    @Override
    public UUID getUUID() {
        return this.uuid;
    }

    public Map<String, AttributeSource> getSources() {
        return this.sources;
    }

    @Override
    public CombatPower.History getCombatPower() {
        return combatPower;
    }

    @Override
    public int getCombatValue() {
        return this.combatPower.getValue();
    }

    @Override
    public int getCombatLastTimeChange() {
        return this.combatPower.getLastTimeChange();
    }

    @Override
    public void putAttributeSource(String key, AttributeSource source) {
        this.putAttributeSource(key, source, false);
    }

    @Override
    public void putAttributeSource(String key, AttributeSource source, boolean update) {
        this.sources.put(key, source);
        if (!update) return;
        this.update();
    }

    @Override
    public void putAttributeSource(String key, AttributeSource source, long tick) {
        this.sources.put(key, source);
        this.update();

        Scheduler.runLaterAsync(uuid, () -> {
            this.sources.remove(key);
            this.update();
        }, tick);
    }

    @Override
    public void removeAttributeSource(String key) {
        this.sources.remove(key);
        this.update();
    }
}
