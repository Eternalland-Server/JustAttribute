package com.sakuragame.eternal.justattribute.listener.combat;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.api.JustAttributeAPI;
import com.sakuragame.eternal.justattribute.api.event.role.RoleLaunchAttackEvent;
import com.sakuragame.eternal.justattribute.api.event.role.RoleUnderAttackEvent;
import com.sakuragame.eternal.justattribute.core.CombatHandler;
import com.sakuragame.eternal.justattribute.core.attribute.stats.EntityAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.MobAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleState;
import com.sakuragame.eternal.justattribute.hook.DamageModify;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import net.sakuragame.eternal.dragoncore.util.Pair;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class CombatListener implements Listener {

    private final MobManager mobManager;
    private final Map<UUID, MobAttribute> mobs;

    public CombatListener() {
        this.mobManager = MythicMobs.inst().getMobManager();
        this.mobs = new HashMap<>();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCombat(EntityDamageByEntityEvent e) {
        if (e.isCancelled()) return;

        LivingEntity attacker = getActualAttacker(e.getDamager());
        LivingEntity sufferer = getActualSufferer(e.getEntity());
        EntityDamageEvent.DamageCause cause = e.getCause();

        if (attacker == null || sufferer == null) return;

        // skill attack
        if (e.getCause() == EntityDamageEvent.DamageCause.CUSTOM) {
            double damage = e.getDamage();

            if (attacker instanceof Player) {
                ActiveMob mob = getMob(sufferer.getUniqueId());
                if (mob == null) return;

                double damageModify = mob.getType().getDamageModifiers().getOrDefault(DamageModify.ABILITY_ATTACK.name(), 1.0);
                double lastDamage = damage * damageModify;
                e.setDamage(lastDamage);
                return;
            }

            e.setDamage(damage);
            return;
        }

        // mob attack
        if (!(attacker instanceof Player)) {
            if (!(sufferer instanceof Player)) return;

            Player player = (Player) sufferer;
            EntityAttribute attackAttribute = getMobAttribute(attacker);
            EntityAttribute sufferAttribute = getPlayerAttribute(player);

            Pair<Double, Double> result = CombatHandler.calculate(attackAttribute, sufferAttribute);
            RoleUnderAttackEvent.Pre preEvent = new RoleUnderAttackEvent.Pre(player, attacker, result.getKey(), result.getValue(), cause);
            preEvent.call();
            if (preEvent.isCancelled()) {
                e.setCancelled(true);
                return;
            }

            double damage = preEvent.getDamage();
            double criticalDamage = preEvent.getCriticalDamage();
            double totalDamage = damage * criticalDamage;

            e.setDamage(totalDamage);

            RoleState state = JustAttributeAPI.getRoleState(player);
            state.updateHealth(player.getHealth() - totalDamage);

            RoleUnderAttackEvent.Post postEvent = new RoleUnderAttackEvent.Post(player, attacker, damage, criticalDamage, cause);
            postEvent.call();
            return;
        }

        // player attack
        Player player = (Player) attacker;

        EntityAttribute attackAttribute = getPlayerAttribute(player);
        EntityAttribute sufferAttribute = (sufferer instanceof Player) ? getPlayerAttribute((Player) sufferer) : getMobAttribute(sufferer);

        Pair<Double, Double> result = CombatHandler.calculate(attackAttribute, sufferAttribute);
        RoleLaunchAttackEvent.Pre preEvent = new RoleLaunchAttackEvent.Pre(player, sufferer, result.getKey(), result.getValue(), cause);
        preEvent.call();
        if (preEvent.isCancelled()) {
            e.setCancelled(true);
            return;
        }

        double damage = preEvent.getDamage();
        double criticalDamage = preEvent.getCriticalDamage();
        double totalDamage = damage * criticalDamage;

        e.setDamage(totalDamage);

        if (sufferer instanceof Player) {
            RoleState state = JustAttributeAPI.getRoleState(player);
            state.updateHealth(totalDamage);
        }

        RoleLaunchAttackEvent.Post postEvent = new RoleLaunchAttackEvent.Post(player, sufferer, damage, criticalDamage, cause);
        postEvent.call();
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        LivingEntity entity = e.getEntity();
        if (entity instanceof Player) return;

        mobs.remove(entity.getUniqueId());
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

    private RoleAttribute getPlayerAttribute(Player target) {
        RoleAttribute role = JustAttributeAPI.getRoleAttribute(target);
        if (role == null) {
            role = JustAttribute.getRoleManager().initRoleAttribute(target);
        }

        return role;
    }

    private MobAttribute getMobAttribute(LivingEntity entity) {
        UUID uuid = entity.getUniqueId();
        ActiveMob mob = getMob(entity.getUniqueId());
        return mobs.computeIfAbsent(uuid, key -> mob == null ? new MobAttribute(entity) : new MobAttribute(mob));
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
