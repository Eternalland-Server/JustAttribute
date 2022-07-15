package com.sakuragame.eternal.justattribute.listener.smithy;

import com.sakuragame.eternal.justattribute.api.event.smithy.SmithyIdentifyEvent;
import com.sakuragame.eternal.justattribute.core.smithy.factory.IdentifyFactory;
import com.sakuragame.eternal.justattribute.core.smithy.SmithyManager;
import com.sakuragame.eternal.justattribute.core.soulbound.Action;
import com.sakuragame.eternal.justattribute.core.soulbound.SoulBound;
import com.sakuragame.eternal.justattribute.core.special.PotencyGrade;
import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import com.sakuragame.eternal.justattribute.util.Utils;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import com.taylorswiftcn.megumi.uifactory.event.comp.UIFCompSubmitEvent;
import com.taylorswiftcn.megumi.uifactory.event.screen.UIFScreenCloseEvent;
import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.api.ItemStream;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTagData;
import net.sakuragame.eternal.dragoncore.api.event.PlayerSlotUpdateEvent;
import net.sakuragame.eternal.dragoncore.api.event.slot.PlayerSlotClickEvent;
import net.sakuragame.eternal.dragoncore.util.Pair;
import net.sakuragame.eternal.justmessage.api.MessageAPI;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class IdentifyListener implements Listener {

    private final List<String> scroll = Arrays.asList(
            "normal_potency_identify_scroll",
            "normal_potency_reset_scroll",
            "advanced_potency_reset_scroll"
    );

    private final List<Pair<Double, PotencyGrade>> scrollWeight = new LinkedList<Pair<Double, PotencyGrade>>() {{
        add(new Pair<>(0.05d, PotencyGrade.SSS));
        add(new Pair<>(0.4d, PotencyGrade.SS));
        add(new Pair<>(1d, PotencyGrade.S));
    }};

    @EventHandler
    public void onClose(UIFScreenCloseEvent e) {
        Player player = e.getPlayer();
        if (!e.getScreenID().equals(IdentifyFactory.SCREEN_ID)) return;

        SmithyManager.backSlot(
                player,
                IdentifyFactory.EQUIP_SLOT,
                IdentifyFactory.PROP_SLOT,
                IdentifyFactory.RESULT_SLOT
        );
    }

    @EventHandler
    public void onUpdate(PlayerSlotUpdateEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        String ident = e.getIdentifier();
        ItemStack item = e.getItemStack();

        if (!(ident.equals(IdentifyFactory.EQUIP_SLOT) || ident.equals(IdentifyFactory.PROP_SLOT))) return;
        if (item.getType() == Material.AIR) return;

        SmithyManager.putSlot(uuid, ident, item);
    }

    @EventHandler
    public void onClick(PlayerSlotClickEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        String ident = e.getIdentifier();
        ItemStack handItem = player.getItemOnCursor();

        if (ident.equals(IdentifyFactory.RESULT_SLOT)) {
            if (!MegumiUtil.isEmpty(handItem)) {
                player.sendMessage(ConfigFile.prefix + "该槽位不能放入物品");
                e.setCancelled(true);
                return;
            }

            e.setSlotItem(SmithyManager.removeSlot(uuid, ident));
            return;
        }

        if (ident.equals(IdentifyFactory.EQUIP_SLOT)) {
            if (!MegumiUtil.isEmpty(handItem)) {
                ItemStream itemStream = ZaphkielAPI.INSTANCE.read(handItem);
                if (itemStream.isVanilla()) {
                    e.setCancelled(true);
                    return;
                }

                ItemTag itemTag = itemStream.getZaphkielData();

                if (SoulBound.isSeal(itemTag)) {
                    player.sendMessage(ConfigFile.prefix + "该物品已被封印");
                    e.setCancelled(true);
                    return;
                }

                ItemTagData data = itemTag.getDeep(PotencyGrade.NBT_TAG);
                if (data == null) {
                    player.sendMessage(ConfigFile.prefix + "该物品不能被鉴定");
                    e.setCancelled(true);
                    return;
                }
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
                if (!this.scroll.contains(zapID)) {
                    player.sendMessage(ConfigFile.prefix + "该槽位只能放入鉴定卷轴!");
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

        if (!e.getScreenID().equals(IdentifyFactory.SCREEN_ID)) return;

        if (SmithyManager.getSlot(uuid, IdentifyFactory.RESULT_SLOT) != null) {
            player.sendMessage(ConfigFile.prefix + "鉴定前请取走已鉴定完的装备");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.5f, 1f);
            return;
        }

        ItemStack equip = SmithyManager.getSlot(uuid, IdentifyFactory.EQUIP_SLOT);
        ItemStack prop = SmithyManager.getSlot(uuid, IdentifyFactory.PROP_SLOT);

        if (MegumiUtil.isEmpty(equip) || equip.getType() == Material.AIR) {
            player.sendMessage(ConfigFile.prefix + "请放入需要鉴定的装备");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.5f, 1f);
            return;
        }

        if (MegumiUtil.isEmpty(prop) || prop.getType() == Material.AIR) {
            player.sendMessage(ConfigFile.prefix + "请放入鉴定卷轴");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.5f, 1f);
            return;
        }

        PotencyGrade currentGrade = PotencyGrade.getGrade(equip);
        String propID = Utils.getZapID(prop);
        if (currentGrade == PotencyGrade.NONE) {
            if (!propID.equals("normal_potency_identify_scroll")) {
                player.sendMessage(ConfigFile.prefix + "该装备还未被鉴定，不能使用重置卷轴");
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.5f, 1f);
                return;
            }

            Pair<PotencyGrade, ItemStack> result = IdentifyFactory.machining(player, equip.clone());
            PotencyGrade grade = result.getKey();
            ItemStack item = result.getValue();

            equip.setAmount(equip.getAmount() - 1);
            prop.setAmount(prop.getAmount() - 1);

            SmithyManager.putSlot(player, IdentifyFactory.EQUIP_SLOT, equip, true);
            SmithyManager.putSlot(player, IdentifyFactory.PROP_SLOT, prop, true);
            SmithyManager.putSlot(player, IdentifyFactory.RESULT_SLOT, item, true);

            MessageAPI.sendActionTip(player, "&a&l鉴定成功!");
            player.sendMessage(ConfigFile.prefix + "§7鉴定成功，装备获得了 §3" + grade.getName() + " §7级潜能");
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 0.5f, 1f);

            SmithyIdentifyEvent event = new SmithyIdentifyEvent(player, grade, item.clone());
            event.call();
        }
        else {
            if (propID.equals("normal_potency_identify_scroll")) {
                player.sendMessage(ConfigFile.prefix + "该装备已经被鉴定过了，请使用重置卷轴");
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.5f, 1f);
                return;
            }

            PotencyGrade identifyGrade = propID.equals("advanced_potency_reset_scroll") ? getRandomGrade() : null;

            Pair<PotencyGrade, ItemStack> result = IdentifyFactory.machining(player, equip.clone(), identifyGrade);
            PotencyGrade grade = result.getKey();
            ItemStack item = result.getValue();

            equip.setAmount(equip.getAmount() - 1);
            prop.setAmount(prop.getAmount() - 1);

            SmithyManager.putSlot(player, IdentifyFactory.EQUIP_SLOT, equip, true);
            SmithyManager.putSlot(player, IdentifyFactory.PROP_SLOT, prop, true);
            SmithyManager.putSlot(player, IdentifyFactory.RESULT_SLOT, item, true);

            MessageAPI.sendActionTip(player, "&a&l潜能重置成功!");
            player.sendMessage(ConfigFile.prefix + "§7潜能重置成功，装备新的潜能等级为 §3" + grade.getName() + " §7级");
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 0.5f, 1f);

            SmithyIdentifyEvent event = new SmithyIdentifyEvent(player, grade, item.clone());
            event.call();
        }
    }

    private PotencyGrade getRandomGrade() {
        List<Double> place = new ArrayList<>();
        scrollWeight.forEach(elm -> place.add(elm.getKey()));

        double random = Math.random();
        place.add(random);
        Collections.sort(place);

        return scrollWeight.get(place.indexOf(random)).getValue();
    }
}
