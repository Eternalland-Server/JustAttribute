package com.sakuragame.eternal.justattribute.listener.smithy;

import com.sakuragame.eternal.justattribute.api.event.smithy.SmithyTransferEvent;
import com.sakuragame.eternal.justattribute.core.smithy.SmithyManager;
import com.sakuragame.eternal.justattribute.core.smithy.factory.TransferFactory;
import com.sakuragame.eternal.justattribute.core.soulbound.SoulBound;
import com.sakuragame.eternal.justattribute.core.special.EquipClassify;
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

public class TransferListener implements Listener {

    @EventHandler
    public void onClose(UIFScreenCloseEvent e) {
        Player player = e.getPlayer();
        if (!e.getScreenID().equals(TransferFactory.SCREEN_ID)) return;

        SmithyManager.backSlot(
                player,
                TransferFactory.EQUIP_SLOT,
                TransferFactory.PROP_SLOT,
                TransferFactory.RESULT_SLOT
        );
    }

    @EventHandler
    public void onUpdate(PlayerSlotUpdateEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        String ident = e.getIdentifier();
        ItemStack item = e.getItemStack();

        if (!(ident.equals(TransferFactory.EQUIP_SLOT) || ident.equals(TransferFactory.PROP_SLOT))) return;
        if (item.getType() == Material.AIR) return;

        SmithyManager.putSlot(uuid, ident, item);
    }

    @EventHandler
    public void onClick(PlayerSlotClickEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        String ident = e.getIdentifier();
        ItemStack handItem = player.getItemOnCursor();

        if (ident.equals(TransferFactory.RESULT_SLOT)) {
            if (!MegumiUtil.isEmpty(handItem)) {
                player.sendMessage(ConfigFile.prefix + "???????????????????????????");
                e.setCancelled(true);
                return;
            }

            e.setSlotItem(SmithyManager.removeSlot(uuid, ident));
            return;
        }

        if (ident.equals(TransferFactory.EQUIP_SLOT) || ident.equals(TransferFactory.PROP_SLOT)) {
            if (!MegumiUtil.isEmpty(handItem)) {
                ItemStream itemStream = ZaphkielAPI.INSTANCE.read(handItem);
                if (itemStream.isVanilla()) {
                    e.setCancelled(true);
                    return;
                }

                ItemTag itemTag = itemStream.getZaphkielData();
                EquipClassify classify = EquipClassify.getClassify(itemTag);
                if (classify == null || !classify.isTransfer()) {
                    player.sendMessage(ConfigFile.prefix + "???????????????????????????");
                    e.setCancelled(true);
                    return;
                }

                if (SoulBound.isSeal(itemTag)) {
                    player.sendMessage(ConfigFile.prefix + "?????????????????????");
                    e.setCancelled(true);
                    return;
                }

                e.setSlotItem(SmithyManager.removeSlot(uuid, ident));
                return;
            }

            e.setSlotItem(SmithyManager.removeSlot(uuid, ident));
        }
    }

    @EventHandler
    public void onSubmit(UIFCompSubmitEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        if (!e.getScreenID().equals(TransferFactory.SCREEN_ID)) return;

        if (SmithyManager.getSlot(uuid, TransferFactory.RESULT_SLOT) != null) {
            player.sendMessage(ConfigFile.prefix + "?????????????????????????????????");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.5f, 1f);
            return;
        }

        ItemStack accept = SmithyManager.getSlot(uuid, TransferFactory.EQUIP_SLOT);
        ItemStack consume = SmithyManager.getSlot(uuid, TransferFactory.PROP_SLOT);

        if (MegumiUtil.isEmpty(accept) || accept.getType() == Material.AIR) {
            player.sendMessage(ConfigFile.prefix + "?????????????????????");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.5f, 1f);
            return;
        }

        if (MegumiUtil.isEmpty(consume) || consume.getType() == Material.AIR) {
            player.sendMessage(ConfigFile.prefix + "????????????????????????");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.5f, 1f);
            return;
        }

        EquipClassify a = EquipClassify.getClassify(accept);
        EquipClassify b = EquipClassify.getClassify(consume);

        if (b != EquipClassify.Unknown && a != b) {
            player.sendMessage(ConfigFile.prefix + "???????????????????????????????????????");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.5f, 1f);
            return;
        }

        if (GemsEconomyAPI.getBalance(uuid, EternalCurrency.Points) < TransferFactory.price) {
            player.sendMessage(ConfigFile.prefix + "????????????????????????");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.5f, 1f);
            return;
        }

        GemsEconomyAPI.withdraw(uuid, TransferFactory.price, EternalCurrency.Points, "????????????");

        ItemStack result = TransferFactory.machining(player, accept.clone(), consume.clone());
        ItemStack transfer = consume.clone();
        transfer.setAmount(1);

        accept.setAmount(accept.getAmount() - 1);
        consume.setAmount(consume.getAmount() - 1);

        SmithyManager.putSlot(player, TransferFactory.EQUIP_SLOT, accept, true);
        SmithyManager.putSlot(player, TransferFactory.PROP_SLOT, consume, true);
        SmithyManager.putSlot(player, TransferFactory.RESULT_SLOT, result, true);

        MessageAPI.sendActionTip(player, "&a&l????????????!");
        player.sendMessage(ConfigFile.prefix + "??7??????????????????!???????????? ??a" + TransferFactory.price + " ??7??????");
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 0.5f, 1f);

        SmithyTransferEvent event = new SmithyTransferEvent(player, transfer, result.clone());
        event.call();
    }
}
