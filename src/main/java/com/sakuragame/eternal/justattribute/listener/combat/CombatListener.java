package com.sakuragame.eternal.justattribute.listener.combat;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.api.event.JARoleAttackEvent;
import com.sakuragame.eternal.justattribute.core.CombatHandler;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleAttribute;
import com.sakuragame.eternal.justattribute.util.CombatUtil;
import com.sakuragame.eternal.justattribute.util.Utils;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.inventory.ItemStack;

public class CombatListener implements Listener {

    private final JustAttribute plugin = JustAttribute.getInstance();

    @EventHandler
    public void onAnimation(PlayerAnimationEvent e) {
        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (MegumiUtil.isEmpty(item)) return;
        if (!Utils.isWeaponClassify(item)) return;

        if (e.getAnimationType() != PlayerAnimationType.ARM_SWING) return;

        if (Math.random() < 0.5) return;

        CombatUtil.offhandAnimation(player);

        e.setCancelled(true);
    }

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
