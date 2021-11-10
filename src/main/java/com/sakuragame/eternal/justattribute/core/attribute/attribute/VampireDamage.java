package com.sakuragame.eternal.justattribute.core.attribute.attribute;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.api.event.JAPlayerAttackEvent;
import com.sakuragame.eternal.justattribute.core.attribute.Identifier;
import com.sakuragame.eternal.justattribute.core.attribute.BaseAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleAttribute;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class VampireDamage extends BaseAttribute implements Listener {

    public VampireDamage() {
        super(Identifier.Vampire_Damage, "ㇵ", "生命汲取", 0, true);
        Bukkit.getPluginManager().registerEvents(this, JustAttribute.getInstance());
    }

    @EventHandler
    public void onAttack(JAPlayerAttackEvent e) {
        Player player = e.getPlayer();

        RoleAttribute attribute = JustAttribute.getRoleManager().getPlayerAttribute(player.getUniqueId());

        double vDamage = this.calculate(
                attribute.getOrdinaryTotalValue(getIdentifier()),
                attribute.getPotencyTotalValue(getIdentifier())
        );
        double vVersatile = this.calculate(
                attribute.getOrdinaryTotalValue(Identifier.Vampire_Versatile),
                attribute.getPotencyTotalValue(Identifier.Vampire_Versatile)
        );

        double lastDamage = e.getDamage();
        double vampire = lastDamage * (vDamage + vVersatile);

        player.setHealth(Math.min(player.getHealth() + vampire, player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()));
    }
}
