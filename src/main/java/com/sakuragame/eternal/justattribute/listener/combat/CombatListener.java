package com.sakuragame.eternal.justattribute.listener.combat;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.api.event.JARoleAttackEvent;
import com.sakuragame.eternal.justattribute.core.CombatHandler;
import com.sakuragame.eternal.justattribute.core.attribute.stats.MobAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleAttribute;
import com.sakuragame.eternal.justattribute.hook.DamageModify;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Optional;
import java.util.UUID;

public class CombatListener implements Listener {

    private final MobManager mobManager;

    public CombatListener() {
        this.mobManager = MythicMobs.inst().getMobManager();
    }

    /*@EventHandler
    public void onAnimation(PlayerAnimationEvent e) {
        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (MegumiUtil.isEmpty(item)) return;
        if (!Utils.isWeaponClassify(item)) return;

        if (e.getAnimationType() != PlayerAnimationType.ARM_SWING) return;

        if (Math.random() < 0.5) return;

        CombatUtil.offhandAnimation(player);

        e.setCancelled(true);
    }*/

    @EventHandler(priority = EventPriority.HIGH)
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.isCancelled()) return;

        LivingEntity attacker = getActualAttacker(e.getDamager());
        LivingEntity sufferer = getActualSufferer(e.getEntity());

        if (attacker == null || sufferer == null) return;

        if (e.getCause() == EntityDamageEvent.DamageCause.CUSTOM) {
            double damage = e.getDamage();

            if (!(attacker instanceof Player)) return;

            ActiveMob mob = getMob(sufferer.getUniqueId());
            if (mob == null) return;

            double damageModify = mob.getType().getDamageModifiers().getOrDefault(DamageModify.ABILITY_ATTACK.name(), 1.0);
            double lastDamage = damage * damageModify;

            e.setDamage(lastDamage);
            return;
        }

        JARoleAttackEvent event;

        if (attacker instanceof Player) {
            RoleAttribute attackData = getTargetAttrData((Player) attacker);

            if (sufferer instanceof Player) {
                RoleAttribute sufferData = getTargetAttrData((Player) sufferer);
                event = CombatHandler.calculate(attackData, sufferData);
            }
            else {
                ActiveMob mob = getMob(sufferer.getUniqueId());
                if (mob == null) return;
                event = CombatHandler.calculate(attackData, new MobAttribute(mob));
            }
        }
        else {
            if (!(sufferer instanceof Player)) return;

            ActiveMob mob = getMob(attacker.getUniqueId());
            if (mob == null) return;

            double damage = CombatHandler.calculate(new MobAttribute(mob), getTargetAttrData((Player) sufferer));
            e.setDamage(damage);
            return;
        }

        if (event.call()) {
            e.setCancelled(true);
            return;
        }

        e.setDamage(event.getDamage());
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

    private RoleAttribute getTargetAttrData(Player target) {
        RoleAttribute role = JustAttribute.getRoleManager().getPlayerAttribute(target.getUniqueId());
        if (role == null) {
            role = new RoleAttribute(target);
        }

        return role;
    }

    private ActiveMob getMob(UUID uuid) {
        if (mobManager.isActiveMob(uuid)) {
            Optional<ActiveMob> optional = mobManager.getActiveMob(uuid);
            if (optional.isPresent()) {
                return optional.get();
            }
        }

        return null;
    }
}
