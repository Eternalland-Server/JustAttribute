package com.sakuragame.eternal.justattribute.listener.smithy;

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
import net.sakuragame.eternal.dragoncore.api.event.slot.PlayerSlotClickEvent;
import net.sakuragame.eternal.gemseconomy.api.GemsEconomyAPI;
import net.sakuragame.eternal.gemseconomy.currency.EternalCurrency;
import net.sakuragame.eternal.justmessage.api.MessageAPI;
import org.bukkit.Material;
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
                TransferFactory.PROP_SLOT,
                TransferFactory.RESULT_SLOT
        );
    }

    @EventHandler
    public void onClick(PlayerSlotClickEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        String ident = e.getIdentifier();
        ItemStack handItem = player.getItemOnCursor();

        if (ident.equals(TransferFactory.RESULT_SLOT)) {
            if (!MegumiUtil.isEmpty(handItem)) {
                MessageAPI.sendActionTip(player, "&c&l该槽位不能放入物品");
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
                if (classify == null) {
                    MessageAPI.sendActionTip(player, "&a&l该道具无法转移属性");
                    e.setCancelled(true);
                    return;
                }

                if (SoulBound.isSeal(itemTag)) {
                    MessageAPI.sendActionTip(player, "&c&l该物品已被封印");
                    e.setCancelled(true);
                    return;
                }

                e.setSlotItem(SmithyManager.removeSlot(uuid, ident));
                SmithyManager.putSlot(uuid, ident, handItem);
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
        if (e.getParams().getParamI(0) != 1) return;

        if (SmithyManager.getSlot(uuid, TransferFactory.RESULT_SLOT) != null) {
            MessageAPI.sendActionTip(player, "&a&l请取走已转移属性的道具");
            return;
        }

        ItemStack equip = SmithyManager.getSlot(uuid, TransferFactory.EQUIP_SLOT);
        ItemStack prop = SmithyManager.getSlot(uuid, TransferFactory.PROP_SLOT);

        if (MegumiUtil.isEmpty(equip) || equip.getType() == Material.AIR) {
            MessageAPI.sendActionTip(player, "&c&l请放入主道具");
            return;
        }

        if (MegumiUtil.isEmpty(prop) || equip.getType() == Material.AIR) {
            MessageAPI.sendActionTip(player, "&c&l请放入副道具");
            return;
        }

        if (GemsEconomyAPI.getBalance(uuid, EternalCurrency.Points) < TransferFactory.price) {
            MessageAPI.sendActionTip(player, "&c&l你没有足够的神石");
            return;
        }

        GemsEconomyAPI.withdraw(uuid, TransferFactory.price, EternalCurrency.Points, "属性转移");

        ItemStack result = TransferFactory.machining(player, equip.clone(), prop.clone());

        equip.setAmount(equip.getAmount() - 1);
        prop.setAmount(prop.getAmount() - 1);

        SmithyManager.putSlot(player, TransferFactory.EQUIP_SLOT, equip, true);
        SmithyManager.putSlot(player, TransferFactory.PROP_SLOT, prop, true);
        SmithyManager.putSlot(player, TransferFactory.RESULT_SLOT, result, true);

        MessageAPI.sendActionTip(player, "&a&l转移成功!");
        player.sendMessage(ConfigFile.prefix + "§7转移属性成功!你花费了 §a" + TransferFactory.price + " §7神石");
    }
}
