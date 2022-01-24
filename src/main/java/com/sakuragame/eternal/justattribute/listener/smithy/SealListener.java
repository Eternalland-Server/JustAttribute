package com.sakuragame.eternal.justattribute.listener.smithy;

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
import net.sakuragame.eternal.dragoncore.api.event.slot.PlayerSlotClickEvent;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
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
    public void onClick(PlayerSlotClickEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        String ident = e.getIdentifier();
        ItemStack handItem = player.getItemOnCursor();

        if (ident.equals(SealFactory.RESULT_SLOT)) {
            if (!MegumiUtil.isEmpty(handItem)) {
                MessageAPI.sendActionTip(player, "&c&l该槽位不能放入物品");
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
                if (action == null || action.isLock()) {
                    MessageAPI.sendActionTip(player, "&a&l这个道具无法被封印");
                    e.setCancelled(true);
                    return;
                }

                switchButton(player, action == Action.SEAL);

                e.setSlotItem(SmithyManager.removeSlot(uuid, ident));
                SmithyManager.putSlot(uuid, ident, handItem);
                return;
            }

            e.setSlotItem(SmithyManager.removeSlot(uuid, ident));
        }
    }

    private void switchButton(Player player, boolean lock) {
        PacketSender.sendRunFunction(player, "default", "global.seal_mode = " + (lock ? 1 : 0) + ";", false);
    }

    @EventHandler
    public void onSubmit(UIFCompSubmitEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        if (!e.getScreenID().equals(SealFactory.SCREEN_ID)) return;
        int operate = e.getParams().getParamI(0);

        if (SmithyManager.getSlot(uuid, SealFactory.RESULT_SLOT) != null) {
            MessageAPI.sendActionTip(player, "&a&l请先取走封印/解封后的道具");
            return;
        }

        ItemStack prop = SmithyManager.getSlot(uuid, SealFactory.PROP_SLOT);

        if (MegumiUtil.isEmpty(prop) || prop.getType() == Material.AIR) {
            MessageAPI.sendActionTip(player, "&c&l请放入需要封印或解封的道具");
            return;
        }

        if (operate == 0) {
            ItemStack result = SealFactory.unlock(player, prop);

            prop.setAmount(prop.getAmount() - 1);

            SmithyManager.putSlot(player, SealFactory.PROP_SLOT, prop, true);
            SmithyManager.putSlot(player, SealFactory.RESULT_SLOT, result, true);

            MessageAPI.sendActionTip(player, "&6&l已解封道具");
            player.sendMessage(ConfigFile.prefix + "§7解封成功，你是该道具新的主人!");
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 0.6f, 1f);
            return;
        }

        if (operate == 1) {
            ItemStack result = SealFactory.lock(prop);

            prop.setAmount(prop.getAmount() - 1);

            SmithyManager.putSlot(player, SealFactory.PROP_SLOT, prop, true);
            SmithyManager.putSlot(player, SealFactory.RESULT_SLOT, result, true);

            MessageAPI.sendActionTip(player, "&3&l已封印道具");
            player.sendMessage(ConfigFile.prefix + "§7封印成功，现在这个道具可以自由交易了!");
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 0.6f, 1f);
        }
    }

}
