package com.sakuragame.eternal.justattribute.listener.hook;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobSpawnEvent;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MobListener implements Listener {

    @EventHandler
    public void onSpawn(MythicMobSpawnEvent e) {
        if (!(e.getEntity() instanceof LivingEntity)) return;
        LivingEntity entity = (LivingEntity) e.getEntity();
        MythicMob mob = e.getMobType();

        double level = e.getMobLevel();
        double promote = 1 + (level - 1) * 0.5;
        double health = mob.getHealth().get();

        double hp = health * promote;

        entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp);
        entity.setHealth(hp);
    }
}
