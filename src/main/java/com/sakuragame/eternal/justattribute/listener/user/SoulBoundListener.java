package com.sakuragame.eternal.justattribute.listener.user;

import com.sakuragame.eternal.justattribute.api.event.EquipBoundEvent;
import com.sakuragame.eternal.justattribute.core.smithy.factory.SealFactory;
import com.sakuragame.eternal.justattribute.core.soulbound.Action;
import com.sakuragame.eternal.justattribute.core.soulbound.SoulBound;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.api.ItemStream;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import net.sakuragame.eternal.justmessage.api.MessageAPI;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class SoulBoundListener implements Listener {

    @EventHandler
    public void onBound(EquipBoundEvent e) {
        Player player = e.getPlayer();
        String display = e.getItem().getItemMeta().getDisplayName();

        MessageAPI.sendActionTip(player, "§a&l装备 " + display + " &a&l已绑定");
    }

    @EventHandler
    public void onUnseal(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        if (e.getInventory().getType() != InventoryType.CRAFTING) return;
        if (e.getAction() != InventoryAction.PICKUP_HALF) return;

        Player player = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();
        if (MegumiUtil.isEmpty(item)) return;

        Action action = SoulBound.getType(item);
        if (action != Action.SEAL) return;

        ItemStack unseal = SealFactory.unlock(player, item);
        e.setCurrentItem(unseal);
        e.setCancelled(true);

        MessageAPI.sendActionTip(player, "&6&l封印已解除，你是该道具新的主人!");
        player.playSound(player.getLocation(), Sound.ENTITY_LLAMA_CHEST, 1, 1);
    }
}
