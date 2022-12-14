package com.sakuragame.eternal.justattribute.listener.combat;

import com.sakuragame.eternal.justattribute.api.JustAttributeAPI;
import com.sakuragame.eternal.justattribute.api.event.role.RoleLaunchAttackEvent;
import com.sakuragame.eternal.justattribute.api.event.role.RoleNearDeathEvent;
import com.sakuragame.eternal.justattribute.api.event.role.RoleSkillAttackEvent;
import com.sakuragame.eternal.justattribute.api.event.role.RoleUnderAttackEvent;
import com.sakuragame.eternal.justattribute.core.CombatHandler;
import com.sakuragame.eternal.justattribute.core.attribute.character.ICharacter;
import com.sakuragame.eternal.justattribute.core.attribute.character.MobCharacter;
import com.sakuragame.eternal.justattribute.core.attribute.character.PlayerCharacter;
import com.sakuragame.eternal.justattribute.hook.DamageModify;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import net.sakuragame.eternal.dragoncore.util.Pair;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class CombatListener implements Listener {

    private final MobManager mobManager;
    private final Map<UUID, MobCharacter> mobs;

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
                Player player = (Player) attacker;
                MobCharacter mob = this.getMobCharacter(sufferer);
                if (mob == null) return;

                double defense = mob.getSkillDefense();
                damage = damage - defense;

                RoleSkillAttackEvent.Pre preEvent = new RoleSkillAttackEvent.Pre(player, sufferer, damage);
                preEvent.call();
                if (preEvent.isCancelled()) return;

                e.setDamage(preEvent.getDamage());

                RoleSkillAttackEvent.Post postEvent = new RoleSkillAttackEvent.Post(player, sufferer, preEvent.getDamage());
                postEvent.call();
                return;
            }

            e.setDamage(damage);
            return;
        }

        if (e.getDamage() == 10) return;

        // mob attack
        if (!(attacker instanceof Player)) {
            ICharacter mob = this.getMobCharacter(attacker);

            if (mob == null) return;
            if (!(sufferer instanceof Player)) return;

            Player player = (Player) sufferer;
            PlayerCharacter role = this.getPlayerCharacter(player);

            Pair<Double, Double> result = CombatHandler.calculate(mob, role);
            RoleUnderAttackEvent.Pre preEvent = new RoleUnderAttackEvent.Pre(player, attacker, result.getKey(), result.getValue(), cause);
            preEvent.call();
            if (preEvent.isCancelled()) {
                e.setCancelled(true);
                return;
            }

            double damage = preEvent.getDamage();
            double criticalDamage = preEvent.getCriticalDamage();
            double totalDamage = damage * criticalDamage;

            if (player.getHealth() - totalDamage <= 0) {
                RoleNearDeathEvent dieEvent = new RoleNearDeathEvent(player, attacker, totalDamage, cause);
                dieEvent.call();

                totalDamage = dieEvent.getFinalDamage();
            }

            e.setDamage(totalDamage);

            role.updateHP(Math.max(0, player.getHealth() - totalDamage));

            RoleUnderAttackEvent.Post postEvent = new RoleUnderAttackEvent.Post(player, attacker, damage, criticalDamage, cause);
            postEvent.call();
            return;
        }

        // player attack
        Player player = (Player) attacker;
        ItemStack hand = player.getInventory().getItemInMainHand();
        boolean weapon = !MegumiUtil.isEmpty(hand) && hand.getType() == Material.IRON_SWORD;

        PlayerCharacter role = this.getPlayerCharacter(player);
        if (sufferer instanceof Player) {
            PlayerCharacter target = this.getPlayerCharacter((Player) sufferer);

            Pair<Double, Double> result = CombatHandler.calculate(role, target);
            double charge = weapon ? (e.getDamage() / 6d) : 1;
            double key = result.getKey();
            double value = result.getValue();

            RoleLaunchAttackEvent.Pre preEvent = new RoleLaunchAttackEvent.Pre(player, sufferer, key, value, charge, cause);
            preEvent.call();
            if (preEvent.isCancelled()) {
                e.setCancelled(true);
                return;
            }

            double damage = preEvent.getDamage();
            double criticalDamage = preEvent.getCriticalDamage();
            double totalDamage = damage * criticalDamage;

            e.setDamage(totalDamage);
            target.updateHP(sufferer.getHealth() - totalDamage);

            RoleLaunchAttackEvent.Post postEvent = new RoleLaunchAttackEvent.Post(player, sufferer, damage, criticalDamage, charge, cause);
            postEvent.call();
        }
        else {
            ActiveMob activeMob = this.getMob(sufferer.getUniqueId());
            if (activeMob == null) return;
            MobCharacter mob = this.getMobCharacter(sufferer);
            if (mob == null) return;

            Pair<Double, Double> result = CombatHandler.calculate(role, mob);
            double charge = weapon ? (e.getDamage() / 6d) : 1;
            double key = result.getKey();
            double value = result.getValue();

            RoleLaunchAttackEvent.Pre preEvent = new RoleLaunchAttackEvent.Pre(player, sufferer, key, value, charge, cause);
            preEvent.call();
            if (preEvent.isCancelled()) {
                e.setCancelled(true);
                return;
            }

            double damage = preEvent.getDamage();
            double criticalDamage = preEvent.getCriticalDamage();
            double totalDamage = damage * criticalDamage;

            e.setDamage(totalDamage);

            RoleLaunchAttackEvent.Post postEvent = new RoleLaunchAttackEvent.Post(player, sufferer, damage, criticalDamage, charge, cause);
            postEvent.call();
        }
    }

    @EventHandler
    public void onEntityDeath(MythicMobDeathEvent e) {
        Entity entity = e.getEntity();
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

    private PlayerCharacter getPlayerCharacter(Player target) {
        return JustAttributeAPI.getRoleCharacter(target);
    }

    private MobCharacter getMobCharacter(LivingEntity entity) {
        UUID uuid = entity.getUniqueId();
        ActiveMob mob = getMob(entity.getUniqueId());
        if (mob == null) return null;

        MobCharacter mobCharacter = this.mobs.get(uuid);
        if (mobCharacter == null) {
            mobCharacter = new MobCharacter(mob);
            this.mobs.put(uuid, mobCharacter);
        }

        return mobCharacter;
    }

    private ActiveMob getMob(UUID uuid) {
        if (this.mobManager.isActiveMob(uuid)) {
            Optional<ActiveMob> optional = this.mobManager.getActiveMob(uuid);
            if (optional.isPresent()) {
                return optional.get();
            }
        }

        return null;
    }
}
