package com.sakuragame.eternal.justattribute.listener.smithy;

import com.sakuragame.eternal.justattribute.api.event.smithy.SmithySealEvent;
import com.sakuragame.eternal.justattribute.core.smithy.SmithyManager;
import com.sakuragame.eternal.justattribute.core.smithy.factory.SealFactory;
import com.sakuragame.eternal.justattribute.core.soulbound.Action;
import com.sakuragame.eternal.justattribute.core.soulbound.SoulBound;
import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import com.taylorswiftcn.megumi.uifactory.event.comp.UIFCompSubmitEvent;
import com.taylorswiftcn.megumi.uifactory.event.screen.UIFScreenCloseEvent;
import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.api.ItemStream;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import net.sakuragame.eternal.dragoncore.api.event.PlayerSlotUpdateEvent;
import net.sakuragame.eternal.dragoncore.api.event.slot.PlayerSlotClickEvent;
import net.sakuragame.eternal.gemseconomy.api.GemsEconomyAPI;
import net.sakuragame.eternal.gemseconomy.currency.EternalCurrency;
import net.sakuragame.eternal.justmessage.api.MessageAPI;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class SealListener implements Listener {

    @EventHandler
    public void onClose(UIFScreenCloseEvent e) {
        Player player = e.getPlayer();
        if (!e.getScreenID().equals(SealFactory.SCREEN_ID)) return;

        SmithyManager.backSlot(
                player,
                SealFactory.PROP_SLOT,
                SealFactory.RESULT_SLOT
        );
    }

    @EventHandler
    public void onUpdate(PlayerSlotUpdateEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        String ident = e.getIdentifier();
        ItemStack item = e.getItemStack();

        if (!ident.equals(SealFactory.PROP_SLOT)) return;
        if (item.getType() == Material.AIR) return;

        SmithyManager.putSlot(uuid, ident, item);
    }

    @EventHandler
    public void onClick(PlayerSlotClickEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        String ident = e.getIdentifier();
        ItemStack handItem = player.getItemOnCursor();

        if (ident.equals(SealFactory.RESULT_SLOT)) {
            if (!MegumiUtil.isEmpty(handItem)) {
                player.sendMessage(ConfigFile.prefix + "该槽位不能放入物品");
                e.setCancelled(true);
                return;
            }

            e.setSlotItem(SmithyManager.removeSlot(uuid, ident));
            return;
        }

        if (ident.equals(SealFactory.PROP_SLOT)) {
            if (!MegumiUtil.isEmpty(handItem)) {
                ItemStream itemStream = ZaphkielAPI.INSTANCE.read(handItem);
                if (itemStream.isVanilla()) {
                    e.setCancelled(true);
                    return;
                }

                ItemTag itemTag = itemStream.getZaphkielData();
                Action action = SoulBound.getType(itemTag);
                if (action == null || action.isLock() || action == Action.SEAL) {
                    player.sendMessage(ConfigFile.prefix + "这个道具无法被封印");
                    e.setCancelled(true);
                    return;
                }
            }

            e.setSlotItem(SmithyManager.removeSlot(uuid, ident));
        }
    }

    @EventHandler
    public void onSubmit(UIFCompSubmitEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        if (!e.getScreenID().equals(SealFactory.SCREEN_ID)) return;

        if (SmithyManager.getSlot(uuid, SealFactory.RESULT_SLOT) != null) {
            player.sendMessage(ConfigFile.prefix + "请先取走封印后的道具");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.5f, 1f);
            return;
        }

        ItemStack prop = SmithyManager.getSlot(uuid, SealFactory.PROP_SLOT);

        if (MegumiUtil.isEmpty(prop) || prop.getType() == Material.AIR) {
            player.sendMessage(ConfigFile.prefix + "请放入需要封印的道具");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.5f, 1f);
            return;
        }

        if (GemsEconomyAPI.getBalance(uuid, EternalCurrency.Points) < SealFactory.price) {
            player.sendMessage(ConfigFile.prefix + "你没有足够的神石");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.5f, 1f);
            return;
        }

        GemsEconomyAPI.withdraw(uuid, SealFactory.price, EternalCurrency.Points, "封印道具");
        ItemStack result = SealFactory.lock(player, prop.clone());

        prop.setAmount(prop.getAmount() - 1);

        SmithyManager.putSlot(player, SealFactory.PROP_SLOT, prop, true);
        SmithyManager.putSlot(player, SealFactory.RESULT_SLOT, result, true);

        MessageAPI.sendActionTip(player, "&3&l已封印道具");
        player.sendMessage(ConfigFile.prefix + "§7你花费了 §a" + SealFactory.price + " §7神石封印道具，现在这个道具可以自由交易了!");
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 0.6f, 1f);

        SmithySealEvent.Lock event = new SmithySealEvent.Lock(player, result.clone());
        event.call();
    }

}
