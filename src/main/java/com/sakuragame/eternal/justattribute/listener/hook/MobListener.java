package com.sakuragame.eternal.justattribute.listener.hook;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.mob.MobConfig;
import com.taylorswiftcn.justwei.util.UnitConvert;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobSpawnedEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicReloadedEvent;
import io.lumine.xikage.mythicmobs.io.MythicConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
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

        for (ActiveMob am : e.getInstance().getMobManager().getActiveMobs()) {
            this.apply(am);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSpawn(MythicMobSpawnedEvent e) {
        if (!(e.getEntity() instanceof LivingEntity)) return;

        this.apply(e.getMob());
    }

    private void apply(ActiveMob am) {
        MythicMob mob = am.getType();
        MythicConfig mythicConfig = mob.getConfig();

        boolean prefix = mythicConfig.getBoolean("eternal.prefix", false);
        if (!prefix) return;

        int level = (int) am.getLevel();
        LivingEntity entity = (LivingEntity) BukkitAdapter.adapt(am.getEntity());
        entity.setCustomName(this.getDisplayName(level, am.getDisplayName()));

        MobConfig config = JustAttribute.getFileManager().getMobConfig(mob.getInternalName());
        double hp = config.getHealth(level);

        entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp);
        entity.setHealth(hp);
    }

    private String getDisplayName(int level, String name) {
        String format = "§3§l⊰§eLv." + UnitConvert.formatEN(UnitConvert.TenThousand, level) + "§3§l⊱";
        return format + " " + name;
    }
}
