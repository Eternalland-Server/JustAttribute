package com.sakuragame.eternal.justattribute.listener;

import com.sakuragame.eternal.justattribute.JustAttribute;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CombatListener implements Listener {

    private final JustAttribute plugin = JustAttribute.getInstance();

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
    }
}
