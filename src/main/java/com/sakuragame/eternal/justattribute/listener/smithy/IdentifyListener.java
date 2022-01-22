package com.sakuragame.eternal.justattribute.listener.smithy;

import com.sakuragame.eternal.justattribute.core.special.PotencyGrade;
import com.sakuragame.eternal.justattribute.util.Utils;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import com.taylorswiftcn.megumi.uifactory.event.comp.UIFCompSubmitEvent;
import com.taylorswiftcn.megumi.uifactory.event.screen.UIFScreenCloseEvent;
import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.api.ItemStream;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTagData;
import net.sakuragame.eternal.dragoncore.api.event.PlayerSlotHandleEvent;
import net.sakuragame.eternal.dragoncore.api.event.slot.PlayerSlotClickEvent;
import net.sakuragame.eternal.dragoncore.api.event.slot.PlayerSlotClickedEvent;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import net.sakuragame.eternal.justmessage.api.MessageAPI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class IdentifyListener implements Listener {

    private Map<UUID, ItemStack> equipCache = new HashMap<>();
    private Map<UUID, ItemStack> propCache = new HashMap<>();
    private Map<UUID, ItemStack> resultCache = new HashMap<>();

    private final List<String> scroll = new ArrayList<String>() {{
        add("normal_potency_identify_scroll");
        add("normal_potency_reset_scroll");
        add("advanced_potency_reset_scroll");
    }};

    @EventHandler
    public void onClose(UIFScreenCloseEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!e.getScreenID().equals("identify")) return;

        ItemStack equip = equipCache.remove(uuid);
        ItemStack prop = equipCache.remove(uuid);
        ItemStack result = resultCache.remove(uuid);

        if (equip != null) player.getInventory().addItem(equip);
        if (prop != null) player.getInventory().addItem(prop);
        if (result != null) player.getInventory().addItem(result);

        PacketSender.putClientSlotItem(player, "identify_equip", new ItemStack(Material.AIR));
        PacketSender.putClientSlotItem(player, "identify_prop", new ItemStack(Material.AIR));
        PacketSender.putClientSlotItem(player, "identify_result", new ItemStack(Material.AIR));
    }

    @EventHandler
    public void onClick(PlayerSlotClickEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        String ident = e.getIdentifier();
        ItemStack handItem = player.getItemOnCursor();

        if (ident.equals("identify_result")) {
            if (!MegumiUtil.isEmpty(handItem)) {
                MessageAPI.sendActionTip(player, "&c&l该槽位不能放入物品");
                e.setCancelled(true);
                return;
            }
            ItemStack result = resultCache.remove(uuid);
            if (result == null) return;
            e.setSlotItem(result);
            return;
        }

        if (ident.equals("identify_equip")) {
            if (!MegumiUtil.isEmpty(handItem)) {
                ItemStream itemStream = ZaphkielAPI.INSTANCE.read(handItem);
                if (itemStream.isVanilla()) {
                    e.setCancelled(true);
                    return;
                }

                ItemTag itemTag = itemStream.getZaphkielData();
                ItemTagData data = itemTag.getDeep(PotencyGrade.NBT_TAG);
                if (data == null) {
                    MessageAPI.sendActionTip(player, "&c&l该物品不能鉴定");
                    e.setCancelled(true);
                    return;
                }

                ItemStack equip = equipCache.remove(uuid);
                if (equip != null) {
                    e.setSlotItem(equip);
                }
                equipCache.put(uuid, handItem);
                return;
            }

            ItemStack equip = equipCache.remove(uuid);
            if (equip != null) {
                e.setSlotItem(equip);
            }
            return;
        }

        if (ident.equals("identify_prop")) {
            if (!MegumiUtil.isEmpty(handItem)) {
                ItemStream itemStream = ZaphkielAPI.INSTANCE.read(handItem);
                if (itemStream.isVanilla()) {
                    e.setCancelled(true);
                    return;
                }

                String zapID = itemStream.getZaphkielItem().getId();
                if (!scroll.contains(zapID)) {
                    MessageAPI.sendActionTip(player, "&c&l该槽位只能放入鉴定卷轴!");
                    e.setCancelled(true);
                    return;
                }

                ItemStack prop = propCache.remove(uuid);
                if (prop != null) {
                    e.setSlotItem(prop);
                }
                propCache.put(uuid, handItem);
                return;
            }

            ItemStack prop = propCache.remove(uuid);
            if (prop != null) {
                e.setSlotItem(prop);
            }
        }
    }

    @EventHandler
    public void onSubmit(UIFCompSubmitEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        if (!e.getScreenID().equals("identify")) return;
        if (e.getParams().getParamI(0) != 1) return;

        if (resultCache.containsKey(uuid)) {
            MessageAPI.sendActionTip(player, "&a&l鉴定前请取走已鉴定完的装备");
            return;
        }

        ItemStack equip = equipCache.get(uuid);
        ItemStack prop = propCache.get(uuid);

        PotencyGrade grade = PotencyGrade.getGrade(equip);
        String propID = Utils.getZapID(prop);
        if (grade == PotencyGrade.NONE) {
            if (!propID.equals("normal_potency_identify_scroll")) {
                MessageAPI.sendActionTip(player, "&c&l该装备还未被鉴定，不能使用重置卷轴");
                return;
            }

            // TODO
            MessageAPI.sendActionTip(player, "&6&l[TEST] &3&l鉴定装备");
        }
        else {
            if (propID.equals("normal_potency_identify_scroll")) {
                MessageAPI.sendActionTip(player, "&c&l改装备已经被鉴定过了，请使用重置卷轴");
                return;
            }

            // TODO
            MessageAPI.sendActionTip(player, "&6&l[TEST] &3&l重置鉴定");
        }
    }
}
