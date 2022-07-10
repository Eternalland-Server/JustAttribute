package com.sakuragame.eternal.justattribute.listener.hook;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.mob.MobConfig;
import com.sakuragame.eternal.justattribute.util.Scheduler;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobSpawnEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicReloadedEvent;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class MobListener implements Listener {

    @EventHandler
    public void onReload(MythicReloadedEvent e) {
        JustAttribute.getFileManager().loadMobConfig();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSpawn(MythicMobSpawnEvent e) {
        if (!(e.getEntity() instanceof LivingEntity)) return;
        LivingEntity entity = (LivingEntity) e.getEntity();
        MythicMob mob = e.getMobType();

        MobConfig config = JustAttribute.getFileManager().getMobConfig(mob.getInternalName());
        double hp = config.getHealth(e.getMobLevel());

        Scheduler.runLater(() -> {
            entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp);
            entity.setHealth(hp);
        }, 1);
    }
}
