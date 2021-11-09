package com.sakuragame.eternal.justattribute.listener;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.VanillaSlot;
import com.sakuragame.eternal.justattribute.core.codition.EquipType;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import net.sakuragame.eternal.dragoncore.api.event.PlayerSlotClickEvent;
import net.sakuragame.eternal.dragoncore.api.event.PlayerSlotUpdateEvent;
import net.sakuragame.eternal.dragoncore.config.FileManager;
import net.sakuragame.eternal.dragoncore.config.SlotSetting;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class SlotListener implements Listener {

    private final JustAttribute plugin = JustAttribute.getInstance();

    @EventHandler
    public void onVanilla(PlayerSlotClickEvent e) {
        Player player = e.getPlayer();
        String ident = e.getIdentifier();

        if (!ident.startsWith("container_")) return;

        int index = Integer.parseInt(ident.substring(9));

        VanillaSlot slot = VanillaSlot.getSlot(index);
        if (slot == null) return;

        Bukkit.getScheduler().runTaskLater(plugin, () -> JustAttribute.getRoleManager().updateVanillaSlot(player, slot), 1);
    }

    @EventHandler
    public void onCustom(PlayerSlotUpdateEvent e) {
        Player player = e.getPlayer();
        String ident = e.getIdentifier();
        ItemStack item = e.getItemStack();

        if (MegumiUtil.isEmpty(item)) return;

        SlotSetting setting = FileManager.getSlotSettings().get(ident);
        if (!setting.isAttribute()) return;

        EquipType type = EquipType.getType(setting.getEquipType());
        if (type == null) return;

        JustAttribute.getRoleManager().updateCustomSlot(player, ident, type, item);
    }
}
