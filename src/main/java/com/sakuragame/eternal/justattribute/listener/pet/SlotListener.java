package com.sakuragame.eternal.justattribute.listener.pet;

import com.sakuragame.eternal.justattribute.core.PetHandler;
import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.api.Item;
import ink.ptms.zaphkiel.api.ItemStream;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTagData;
import net.sakuragame.eternal.dragoncore.api.SlotAPI;
import net.sakuragame.eternal.dragoncore.api.event.PlayerSlotUpdateEvent;
import net.sakuragame.eternal.dragoncore.api.event.slot.PlayerSlotClickEvent;
import net.sakuragame.eternal.dragoncore.api.gui.event.CustomPacketEvent;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import net.sakuragame.eternal.dragoncore.util.Pair;
import net.sakuragame.eternal.justmessage.api.MessageAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class SlotListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSlotRequireEvent(CustomPacketEvent e) {
        if (!e.getIdentifier().equals("DragonCore_RetrieveSlot")) return;
        if (e.isCancelled()) return;
        if (e.getData().size() != 1) return;

        Player player = e.getPlayer();
        String ident = e.getData().get(0);
        if (ident.equals(PetHandler.EGG_SLOT)) return;

        ItemStack egg = SlotAPI.getCacheSlotItem(player, PetHandler.EGG_SLOT);
        if (MegumiUtil.isEmpty(egg)) return;

        this.loadEgg(player, egg);
    }

    @EventHandler
    public void onSlotUpdate(PlayerSlotUpdateEvent e) {
        Player player = e.getPlayer();
        String ident = e.getIdentifier();
        ItemStack item = e.getItemStack();

        if (ident.equals(PetHandler.EGG_SLOT)) {
            if (MegumiUtil.isEmpty(item)) {
                PetHandler.NBT_NODE_EQUIP.keySet().forEach(slot -> PacketSender.putClientSlotItem(player, slot, null));
                PacketSender.putClientSlotItem(player, PetHandler.SADDLE_SLOT, null);
            }
            else {
                this.loadEgg(player, item);
            }
            return;
        }

        if (MegumiUtil.isEmpty(item)) return;

        if (!ConfigFile.slotSetting.containsKey(ident)) return;
        int id = ConfigFile.slotSetting.get(ident);

        ItemStack egg = SlotAPI.getCacheSlotItem(player, PetHandler.EGG_SLOT);
        if (id == 22) {
            egg = PetHandler.setSaddle(player, egg);
            SlotAPI.setSlotItem(player, PetHandler.EGG_SLOT, egg, true);
            return;
        }

        if (id > 22 && id < 26) {
            egg = PetHandler.setEquip(player, ident, egg, item);
            SlotAPI.setSlotItem(player, PetHandler.EGG_SLOT, egg, true);
        }
    }

    @EventHandler
    public void onSlotClick(PlayerSlotClickEvent e) {
        Player player = e.getPlayer();
        String ident = e.getIdentifier();

        ItemStack egg = SlotAPI.getCacheSlotItem(player, PetHandler.EGG_SLOT);
        if (MegumiUtil.isEmpty(egg)) return;

        if (ident.equals(PetHandler.SADDLE_SLOT) && PetHandler.isUseSaddle(egg) == 1) {
            MessageAPI.sendActionTip(player, "&c&l宠物鞍无法取下");
            e.setCancelled(true);
            return;
        }

        if (!PetHandler.NBT_NODE_EQUIP.containsKey(ident)) return;

        Pair<ItemStack, ItemStack> result = PetHandler.removeEquip(player, ident, egg);
        if (result == null) return;

        SlotAPI.setSlotItem(player, PetHandler.EGG_SLOT, result.getKey(), true);
        e.setSlotItem(result.getValue());
    }

    private void loadEgg(Player player, ItemStack egg) {
        ItemStream itemStream = ZaphkielAPI.INSTANCE.read(egg);
        ItemTag tag = itemStream.getZaphkielData();
        PetHandler.NBT_NODE_EQUIP.forEach((slot, v) -> {
            ItemTagData data = tag.getDeep(v.getValue());
            if (data != null) {
                Item zapItem = ZaphkielAPI.INSTANCE.getRegisteredItem().get(data.asString());
                PacketSender.putClientSlotItem(player, slot, zapItem.buildItemStack(player));
            }
        });

        int saddle = tag.getDeepOrElse(PetHandler.NBT_NODE_SADDLE, new ItemTagData(0)).asInt();
        if (saddle == 1) {
            ItemStack saddleItem = ZaphkielAPI.INSTANCE.getItemStack("pet_saddle", player);
            PacketSender.putClientSlotItem(player, PetHandler.SADDLE_SLOT, saddleItem);
        }
    }
}
