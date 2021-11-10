package com.sakuragame.eternal.justattribute.core.attribute.attribute;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.Identifier;
import com.sakuragame.eternal.justattribute.core.attribute.BaseAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleAttribute;
import net.sakuragame.eternal.justlevel.event.JustPlayerExpIncreaseEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ExpAddition extends BaseAttribute implements Listener {

    public ExpAddition() {
        super(Identifier.EXP_Addition, "け", "经验加成", 0, true);
    }

    @EventHandler
    public void onExpIncrease(JustPlayerExpIncreaseEvent e) {
        Player player = e.getPlayer();
        double increase = e.getIncrease();

        RoleAttribute attribute = JustAttribute.getRoleManager().getPlayerAttribute(player.getUniqueId());
        double expAddition = calculate(attribute);

        increase = increase * (1 + expAddition);
        e.setIncrease(increase);
    }
}
