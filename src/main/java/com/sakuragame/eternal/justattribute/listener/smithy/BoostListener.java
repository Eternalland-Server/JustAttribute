package com.sakuragame.eternal.justattribute.listener.smithy;

import com.sakuragame.eternal.justattribute.api.event.smithy.SmithyBoostEvent;
import com.sakuragame.eternal.justattribute.core.smithy.SmithyManager;
import com.sakuragame.eternal.justattribute.core.smithy.factory.BoostFactory;
import com.sakuragame.eternal.justattribute.core.smithy.factory.IdentifyFactory;
import com.sakuragame.eternal.justattribute.core.special.EquipClassify;
import com.sakuragame.eternal.justattribute.util.Utils;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import com.taylorswiftcn.megumi.uifactory.event.comp.UIFCompSubmitEvent;
import com.taylorswiftcn.megumi.uifactory.event.screen.UIFScreenCloseEvent;
import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.api.ItemStream;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import net.sakuragame.eternal.dragoncore.api.event.slot.PlayerSlotClickEvent;
import net.sakuragame.eternal.justmessage.api.MessageAPI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class BoostListener implements Listener {

    @EventHandler
    public void onClose(UIFScreenCloseEvent e) {
        Player player = e.getPlayer();
        if (!e.getScreenID().equals(BoostFactory.SCREEN_ID)) return;

        SmithyManager.backSlot(
                player,
                BoostFactory.EQUIP_SLOT,
                BoostFactory.PROP_SLOT,
                BoostFactory.RESULT_SLOT
        );
    }

    @EventHandler
    public void onClick(PlayerSlotClickEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        String ident = e.getIdentifier();
        ItemStack handItem = player.getItemOnCursor();

        if (ident.equals(BoostFactory.RESULT_SLOT)) {
            if (!MegumiUtil.isEmpty(handItem)) {
                MessageAPI.sendActionTip(player, "&c&l该槽位不能放入物品");
                e.setCancelled(true);
                return;
            }

            e.setSlotItem(SmithyManager.removeSlot(uuid, ident));
            return;
        }

        if (ident.equals(BoostFactory.EQUIP_SLOT)) {
            if (!MegumiUtil.isEmpty(handItem)) {
                ItemStream itemStream = ZaphkielAPI.INSTANCE.read(handItem);
                if (itemStream.isVanilla()) {
                    e.setCancelled(true);
                    return;
                }

                ItemTag itemTag = itemStream.getZaphkielData();
                EquipClassify classify = EquipClassify.getClassify(itemTag);

                if (classify != EquipClassify.MainHand) {
                    MessageAPI.sendActionTip(player, "&c&l该物品不能突破伤害");
                    e.setCancelled(true);
                    return;
                }

                e.setSlotItem(SmithyManager.removeSlot(uuid, ident));
                SmithyManager.putSlot(uuid, ident, handItem);
            }

            e.setSlotItem(SmithyManager.removeSlot(uuid, ident));
            return;
        }

        if (ident.equals(IdentifyFactory.PROP_SLOT)) {
            if (!MegumiUtil.isEmpty(handItem)) {
                ItemStream itemStream = ZaphkielAPI.INSTANCE.read(handItem);
                if (itemStream.isVanilla()) {
                    e.setCancelled(true);
                    return;
                }

                String zapID = itemStream.getZaphkielItem().getId();
                if (!BoostFactory.stones.containsKey(zapID)) {
                    MessageAPI.sendActionTip(player, "&c&l该槽位只能放入伤害突破石!");
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

        if (!e.getScreenID().equals(BoostFactory.SCREEN_ID)) return;
        if (e.getParams().getParamI(0) != 1) return;

        ItemStack equip = SmithyManager.getSlot(uuid, BoostFactory.EQUIP_SLOT);
        ItemStack prop = SmithyManager.getSlot(uuid, BoostFactory.PROP_SLOT);

        String propID = Utils.getZapID(prop);
        int value = BoostFactory.stones.get(propID);

        if (MegumiUtil.isEmpty(equip) || equip.getType() == Material.AIR) {
            MessageAPI.sendActionTip(player, "&c&l请放入武器");
            return;
        }

        if (MegumiUtil.isEmpty(prop) || equip.getType() == Material.AIR) {
            MessageAPI.sendActionTip(player, "&c&l请放入突破石");
            return;
        }

        ItemStack result = BoostFactory.machining(player, equip.clone(), prop.clone());

        equip.setAmount(equip.getAmount() - 1);
        prop.setAmount(prop.getAmount() - 1);

        SmithyManager.putSlot(player, BoostFactory.EQUIP_SLOT, equip, true);
        SmithyManager.putSlot(player, BoostFactory.PROP_SLOT, prop, true);
        SmithyManager.putSlot(player, BoostFactory.RESULT_SLOT, result, true);

        MessageAPI.sendActionTip(player, "&a&l伤害突破成功&6&l(" + value + ")");

        SmithyBoostEvent event = new SmithyBoostEvent(player, result, value);
        event.call();
    }
}
