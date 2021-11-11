package com.sakuragame.eternal.justattribute.listener.combat;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.api.event.JARoleAttackEvent;
import com.sakuragame.eternal.justattribute.core.CombatHandler;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleAttribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CombatListener implements Listener {

    private final JustAttribute plugin = JustAttribute.getInstance();

    @EventHandler(priority = EventPriority.HIGH)
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.isCancelled()) return;

        LivingEntity attacker = getActualAttacker(e.getDamager());
        LivingEntity sufferer = getActualSufferer(e.getEntity());

        if (attacker == null || sufferer == null) return;
        if (!(attacker instanceof Player)) return;

        RoleAttribute attackData = getTargetAttrData(attacker);
        RoleAttribute sufferData = getTargetAttrData(sufferer);

        JARoleAttackEvent attackEvent = CombatHandler.calculate(attackData, sufferData);

        if (attackEvent.call()) {
            e.setCancelled(true);
            return;
        }

        e.setDamage(attackEvent.getDamage());
    }

    private LivingEntity getActualAttacker(Entity target) {
        if (target instanceof Projectile) {
            Projectile projectile = (Projectile) target;
            if (!(projectile.getShooter() instanceof LivingEntity)) {
                return null;
            }

            return  (LivingEntity) projectile.getShooter();
        }

        if (target instanceof LivingEntity) {
            return (LivingEntity) target;
        }

        return null;
    }

    private LivingEntity getActualSufferer(Entity target) {
        if (target instanceof LivingEntity) {
            return (LivingEntity) target;
        }

        return null;
    }

    private RoleAttribute getTargetAttrData(LivingEntity target) {
        if (target instanceof Player) {
            return JustAttribute.getRoleManager().getPlayerAttribute(target.getUniqueId());
        }

        return new RoleAttribute(target);
    }
}
