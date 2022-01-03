package com.sakuragame.eternal.justattribute.listener.combat;

import com.sakuragame.eternal.justattribute.api.event.role.RoleAttackEvent;
import com.sakuragame.eternal.justattribute.core.CombatHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class VampireListener implements Listener {

    @EventHandler
    public void onAttack(RoleAttackEvent e) {
        Player player = e.getPlayer();

        CombatHandler.physicalVampire(player, e.getVictim(), e.getDamage());
    }
}
