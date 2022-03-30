package com.sakuragame.eternal.justattribute.core;

import com.sakuragame.eternal.justattribute.util.Scheduler;
import lombok.Getter;
import net.sakuragame.eternal.jungle.JungleAPI;
import net.sakuragame.eternal.jungle.stats.StatsType;

import java.util.UUID;

@Getter
public enum JungleStats {

    DamageBoost("damage:boost", "伤害突破石加成");

    private final String identifier;
    private final String displayName;

    JungleStats(String identifier, String displayName) {
        this.identifier = identifier;
        this.displayName = displayName;
    }

    public StatsType getStatsType() {
        return JungleAPI.getStatsTypeManager().getStatsType(this.getIdentifier());
    }

    public static void register() {
        for (JungleStats stats : values()) {
            JungleAPI.createStatsType(stats.getIdentifier(), stats.getDisplayName());
        }
    }

    public int get(UUID uuid) {
        return JungleAPI.getStatsValue(uuid, this.getIdentifier(), false);
    }

    public void set(UUID uuid, int value) {
        Scheduler.runAsync(() -> JungleAPI.setStatsValue(uuid, this.getIdentifier(), value));
    }
}
