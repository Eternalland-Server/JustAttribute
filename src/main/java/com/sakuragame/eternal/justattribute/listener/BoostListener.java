package com.sakuragame.eternal.justattribute.listener;

import com.sakuragame.eternal.justattribute.api.JustAttributeAPI;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleAttribute;
import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import com.sakuragame.eternal.justattribute.hook.ClientPlaceholder;
import com.taylorswiftcn.justwei.util.UnitConvert;
import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.api.Item;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class BoostListener implements Listener {

    private final Map<String, Integer> map;

    public BoostListener() {
        map = new HashMap<String, Integer>() {{
            put("damage_boost_a", 1000);
            put("damage_boost_b", 10000);
            put("damage_boost_c", 100000);
            put("damage_boost_d", 1000000);
            put("damage_boost_e", 10000000);
        }};
    }

    @EventHandler
    public void onUse(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getHand() != EquipmentSlot.HAND) return;
        if (e.getAction() != Action.RIGHT_CLICK_AIR || e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        ItemStack item = e.getItem();
        if (item == null) return;

        Item zapItem = ZaphkielAPI.INSTANCE.getItem(item);
        if (zapItem == null) return;

        String id = zapItem.getId();
        if (!map.containsKey(id)) return;

        item.setAmount(item.getAmount() - 1);

        RoleAttribute role = JustAttributeAPI.getRoleAttribute(player);
        int value = map.get(id);
        role.addBoostDamageUpperLimit(value);

        player.sendMessage(ConfigFile.prefix + "§7你使用伤害突破石提升了 §a" + value + " §7点破功");

        Map<String, String> placeholder = new HashMap<>();
        placeholder.put(ClientPlaceholder.ROLE_DAMAGE_UPPER_LIMIT, UnitConvert.formatCN(UnitConvert.TenThousand, role.getDamageUpperLimit()));
        PacketSender.sendSyncPlaceholder(player, placeholder);
    }
}
