package com.sakuragame.eternal.justattribute.listener.combat;

import com.sakuragame.eternal.justattribute.api.event.role.RoleLaunchAttackEvent;
import com.sakuragame.eternal.justattribute.api.event.role.RoleSkillAttackEvent;
import com.sakuragame.eternal.justattribute.core.CombatHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class VampireListener implements Listener {

    @EventHandler
    public void onDamage(RoleLaunchAttackEvent.Post e) {
        Player player = e.getPlayer();

        CombatHandler.damageVampire(player, e.getVictim());
    }

    @EventHandler
    public void onPower(RoleSkillAttackEvent.Post e) {
        Player player = e.getPlayer();

        CombatHandler.powerVampire(player, e.getVictim());
    }

}
