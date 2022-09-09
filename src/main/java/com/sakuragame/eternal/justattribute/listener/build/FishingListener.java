package com.sakuragame.eternal.justattribute.listener.build;

import com.taylorswiftcn.justwei.util.MegumiUtil;
import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.api.ItemStream;
import ink.ptms.zaphkiel.api.event.ItemReleaseEvent;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTagData;
import net.sakuragame.eternal.justmessage.api.MessageAPI;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class FishingListener implements Listener {

    private final String NBT_NODE = "fish.bait";
    private final String DISPLAY_NODE = "fish.bait";

    private final List<String> baits = Arrays.asList("stosh_normal", "stosh_plus");
    private final List<String> rods = Arrays.asList("fishing_rod_normal", "fishing_rod_plus");

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDisplay(ItemReleaseEvent.Display e) {
        String display = e.getItemStream().getZaphkielItem().getDisplay();
        if (!display.equals("FISH_ROD_DISPLAY")) return;

        ItemTag tag = e.getItemStream().getZaphkielData();
        ItemTagData data = tag.getDeep(NBT_NODE);
        if (data == null) {
            e.addLore(DISPLAY_NODE, "&8&o<未使用鱼饵>");
            return;
        }

        String s = data.asString();
        String type = s.split("\\|", 2)[0];
        int amount = Integer.parseInt(s.split("\\|", 2)[1]);
        String baitName = ZaphkielAPI.INSTANCE.getRegisteredItem().get(type).getName().get("NAME");

        e.addLore(DISPLAY_NODE, "&f" + ChatColor.stripColor(baitName) + "&a(x" + amount + ")");
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.isCancelled()) return;
        if (!(e.getWhoClicked() instanceof Player)) return;

        Player player = (Player) e.getWhoClicked();
        Inventory gui = e.getInventory();
        if (!(gui.getType() == InventoryType.PLAYER || gui.getType() == InventoryType.CRAFTING)) return;

        InventoryAction action = e.getAction();
        if (action == InventoryAction.SWAP_WITH_CURSOR) {
            ItemStack baitItem = e.getCursor();
            ItemStack rodItem = e.getCurrentItem();

            ItemStream baitStream = ZaphkielAPI.INSTANCE.read(baitItem);
            ItemStream rodStream = ZaphkielAPI.INSTANCE.read(rodItem);
            if (baitStream.isVanilla() || rodStream.isVanilla()) return;
            String baitName = baitStream.getZaphkielName();
            String rodName = rodStream.getZaphkielName();
            if (!(baits.contains(baitName) && rods.contains(rodName))) return;

            int baitAmount = baitItem.getAmount();

            ItemTag rodTag = rodStream.getZaphkielData();
            ItemTagData data = rodTag.getDeep(NBT_NODE);
            int amount = 0;
            if (data != null) {
                String s = data.asString();
                String type = s.split("\\|", 2)[0];
                if (!type.equals(baitName)) {
                    MessageAPI.sendActionTip(player, "&c&l该鱼竿已经使用了其他鱼饵");
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.33f, 1f);
                    e.setCancelled(true);
                    return;
                }
                amount += Integer.parseInt(s.split("\\|", 2)[1]);
            }
            e.setCancelled(true);

            rodTag.putDeep(NBT_NODE, baitName + "|" + (amount + baitAmount));
            player.setItemOnCursor(null);
            e.setCurrentItem(rodStream.rebuildToItemStack(player));
            MessageAPI.sendActionTip(player, "&a&l放置鱼饵成功！");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_XYLOPHONE, 0.33f, 1f);
            return;
        }

        if (action == InventoryAction.PICKUP_HALF) {
            ItemStack rodItem = e.getCurrentItem();
            if (MegumiUtil.isEmpty(rodItem)) return;

            ItemStream rodStream = ZaphkielAPI.INSTANCE.read(rodItem);
            if (rodStream.isVanilla()) return;

            ItemTag tag = rodStream.getZaphkielData();
            ItemTagData data = tag.getDeep(NBT_NODE);
            if (data == null) return;

            String s = data.asString();
            String type = s.split("\\|", 2)[0];
            int amount = Integer.parseInt(s.split("\\|", 2)[1]);
            ItemStack bait = ZaphkielAPI.INSTANCE.getItemStack(type, player);
            if (bait == null) return;

            if (amount > 64) {
                bait.setAmount(64);
                amount -= 64;
                tag.putDeep(NBT_NODE, type + "|" + amount);
            }
            else {
                bait.setAmount(amount);
                tag.removeDeep(NBT_NODE);
            }

            e.setCancelled(true);

            player.setItemOnCursor(bait);
            e.setCurrentItem(rodStream.rebuildToItemStack(player));
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1f, 1f);
        }

    }
}
