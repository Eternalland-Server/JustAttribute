package com.sakuragame.eternal.justattribute.listener.smithy;

import com.sakuragame.eternal.justattribute.core.smithy.SmithyManager;
import com.sakuragame.eternal.justattribute.core.smithy.factory.EnhanceFactory;
import com.sakuragame.eternal.justattribute.core.soulbound.SoulBound;
import com.sakuragame.eternal.justattribute.core.special.EquipClassify;
import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import com.taylorswiftcn.megumi.uifactory.event.comp.UIFCompSubmitEvent;
import com.taylorswiftcn.megumi.uifactory.event.screen.UIFScreenCloseEvent;
import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.api.ItemStream;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTagData;
import net.sakuragame.eternal.dragoncore.api.event.PlayerSlotUpdateEvent;
import net.sakuragame.eternal.dragoncore.api.event.slot.PlayerSlotClickEvent;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import net.sakuragame.eternal.dragoncore.util.Pair;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.*;

public class EnhanceListener implements Listener {

    private final DecimalFormat format = new DecimalFormat("0.00");

    private final Map<String, Integer> crystal = new HashMap<String, Integer>() {{
        put("enhance_v1_stone", 10);
        put("enhance_v2_stone", 18);
        put("enhance_v3_stone", 21);
    }};

    @EventHandler
    public void onClose(UIFScreenCloseEvent e) {
        Player player = e.getPlayer();
        if (!e.getScreenID().equals(EnhanceFactory.SCREEN_ID)) return;

        SmithyManager.backSlot(
                player,
                EnhanceFactory.EQUIP_SLOT,
                EnhanceFactory.PROP_SLOT
        );
    }

    @EventHandler
    public void onUpdate(PlayerSlotUpdateEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        String ident = e.getIdentifier();
        ItemStack item = e.getItemStack();

        if (!(ident.equals(EnhanceFactory.EQUIP_SLOT) || ident.equals(EnhanceFactory.PROP_SLOT))) return;

        if (ident.equals(EnhanceFactory.EQUIP_SLOT)) {
            Map<String, String> placeholder = new HashMap<>();
            if (item.getType() == Material.AIR) {
                placeholder.put("enhance_chance", "???");
            }
            else {
                ItemStream itemStream = ZaphkielAPI.INSTANCE.read(item);
                int count = itemStream.getZaphkielData().getDeepOrElse(EnhanceFactory.NBT_NODE_ENHANCE, new ItemTagData(0)).asInt();
                placeholder.put("enhance_chance", format.format(EnhanceFactory.chance.getOrDefault(count + 1, 0d) * 100) + "%");
            }
            PacketSender.sendSyncPlaceholder(player, placeholder);
        }

        if (item.getType() == Material.AIR) return;

        SmithyManager.putSlot(uuid, ident, item);
    }

    @EventHandler
    public void onClick(PlayerSlotClickEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        String ident = e.getIdentifier();
        ItemStack handItem = player.getItemOnCursor();

        if (ident.equals(EnhanceFactory.EQUIP_SLOT)) {
            if (!MegumiUtil.isEmpty(handItem)) {
                ItemStream itemStream = ZaphkielAPI.INSTANCE.read(handItem);
                if (itemStream.isVanilla()) {
                    e.setCancelled(true);
                    return;
                }

                ItemTag itemTag = itemStream.getZaphkielData();

                EquipClassify classify = EquipClassify.getClassify(itemTag);
                if (classify == null || !classify.isEnhance()) {
                    player.sendMessage(ConfigFile.prefix + "该物品不能被强化");
                    e.setCancelled(true);
                    return;
                }

                if (SoulBound.isSeal(itemTag)) {
                    player.sendMessage(ConfigFile.prefix + "该物品已被封印");
                    e.setCancelled(true);
                    return;
                }

                ItemTagData enhance = itemTag.getDeepOrElse(EnhanceFactory.NBT_NODE_ENHANCE, new ItemTagData(0));

                if (enhance.asInt() >= EnhanceFactory.MAX) {
                    player.sendMessage(ConfigFile.prefix + "该物品已被强化至最高等级");
                    e.setCancelled(true);
                    return;
                }
            }

            e.setSlotItem(SmithyManager.removeSlot(uuid, ident));
            return;
        }

        if (ident.equals(EnhanceFactory.PROP_SLOT)) {
            if (!MegumiUtil.isEmpty(handItem)) {
                ItemStream itemStream = ZaphkielAPI.INSTANCE.read(handItem);
                if (itemStream.isVanilla()) {
                    e.setCancelled(true);
                    return;
                }

                String zapID = itemStream.getZaphkielItem().getId();
                if (!this.crystal.containsKey(zapID)) {
                    player.sendMessage(ConfigFile.prefix + "该槽位只能放入强化水晶!");
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

        if (!e.getScreenID().equals(EnhanceFactory.SCREEN_ID)) return;

        ItemStack equip = SmithyManager.getSlot(uuid, EnhanceFactory.EQUIP_SLOT);
        ItemStack prop = SmithyManager.getSlot(uuid, EnhanceFactory.PROP_SLOT);

        if (MegumiUtil.isEmpty(equip) || equip.getType() == Material.AIR) {
            player.sendMessage(ConfigFile.prefix + "请放入需要强化的装备");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.5f, 1f);
            return;
        }

        if (MegumiUtil.isEmpty(prop) || prop.getType() == Material.AIR) {
            player.sendMessage(ConfigFile.prefix + "请放入强化水晶");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.5f, 1f);
            return;
        }

        ItemStream equipStream = ZaphkielAPI.INSTANCE.read(equip);
        ItemStream propStream = ZaphkielAPI.INSTANCE.read(prop);

        int level = equipStream.getZaphkielData().getDeepOrElse(EnhanceFactory.NBT_NODE_ENHANCE, new ItemTagData(0)).asInt() + 1;
        int max = this.crystal.get(propStream.getZaphkielName());

        if (max < level) {
            player.sendMessage(ConfigFile.prefix + "该强化水晶无法继续强化至更高级");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.5f, 1f);
            return;
        }

        Pair<Boolean, ItemStack> result = EnhanceFactory.machining(player, equip);
        boolean success = result.getKey();
        if (!success) {
            prop.setAmount(prop.getAmount() - 1);
            SmithyManager.putSlot(player, EnhanceFactory.PROP_SLOT, prop, true);
            player.sendMessage(ConfigFile.prefix + "§c强化失败");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.5f, 1f);
            return;
        }

        prop.setAmount(prop.getAmount() - 1);

        SmithyManager.putSlot(player, EnhanceFactory.EQUIP_SLOT, result.getValue(), true);
        SmithyManager.putSlot(player, EnhanceFactory.PROP_SLOT, prop, true);

        Map<String, String> placeholder = new HashMap<>();
        placeholder.put("enhance_chance", level < EnhanceFactory.MAX ? (format.format(EnhanceFactory.chance.getOrDefault(level + 1, 0d) * 100) + "%") : "???");
        PacketSender.sendSyncPlaceholder(player, placeholder);

        player.sendMessage(ConfigFile.prefix + "§a强化成功§6(+" + level + ")");
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 0.5f, 1f);
    }
}
