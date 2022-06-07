package com.sakuragame.eternal.justattribute.listener.hook;

import com.sakuragame.eternal.justattribute.hook.ClientPlaceholder;
import net.sakuragame.eternal.dragoncore.api.SlotAPI;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import net.sakuragame.eternal.justlevel.api.JustLevelAPI;
import net.sakuragame.eternal.justmessage.api.event.PlayerGetInfoEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class InfoListener implements Listener {

    @EventHandler
    public void onGet(PlayerGetInfoEvent e) {
        Player player = e.getPlayer();
        Player target = e.getTarget();

        Map<String, ItemStack> slotItems = SlotAPI.getCacheAllSlotItem(target);
        slotItems.forEach((identifier, item) -> PacketSender.putClientSlotItem(player, "target_" + identifier, item));
        PacketSender.putClientSlotItem(player, "target_helmet", target.getInventory().getHelmet());
        PacketSender.putClientSlotItem(player, "target_chestplate", target.getInventory().getChestplate());
        PacketSender.putClientSlotItem(player, "target_leggings", target.getInventory().getLeggings());
        PacketSender.putClientSlotItem(player, "target_boots", target.getInventory().getBoots());
        PacketSender.putClientSlotItem(player, "target_offhand", target.getInventory().getItemInOffHand());

        String realm = JustLevelAPI.getRealmName(player);
        String stage = JustLevelAPI.getStage(player) + "";

        e.putPlaceholder("target_role_realm", realm);
        e.putPlaceholder("target_role_stage", stage);
        e.putPlaceholder("target_uuid", target.getUniqueId().toString());
        e.putPlaceholder("target_name", target.getName());
        e.putPlaceholder(ClientPlaceholder.getTargetPlaceholder(target));
    }
}
