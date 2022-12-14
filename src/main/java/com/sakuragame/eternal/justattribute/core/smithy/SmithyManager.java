package com.sakuragame.eternal.justattribute.core.smithy;

import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import com.sakuragame.eternal.justattribute.core.smithy.factory.EnhanceFactory;
import com.sakuragame.eternal.justattribute.core.smithy.factory.IdentifyFactory;
import com.sakuragame.eternal.justattribute.core.smithy.factory.SealFactory;
import com.sakuragame.eternal.justattribute.core.smithy.factory.TransferFactory;
import net.sakuragame.eternal.dragoncore.network.PacketSender;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SmithyManager {

    private final static Map<UUID, Map<String, ItemStack>> slotCache = new HashMap<>();
    public final static List<Attribute> ATTRIBUTES = Arrays.asList(
            Attribute.Energy,
            Attribute.Stamina,
            Attribute.Wisdom,
            Attribute.Technique,
            Attribute.Damage,
            Attribute.Defence,
            Attribute.Health,
            Attribute.Mana
    );

    public static void init() {
        IdentifyFactory.init();
        SealFactory.init();
        TransferFactory.init();
        EnhanceFactory.init();
    }

    public static void putSlot(Player player, String ident, ItemStack item) {
        putSlot(player.getUniqueId(), ident, item);
    }

    public static void putSlot(UUID uuid, String ident, ItemStack item) {
        if (item.getAmount() == 0) item = new ItemStack(Material.AIR);

        Map<String, ItemStack> cache = slotCache.computeIfAbsent(uuid, k -> new HashMap<>());
        cache.put(ident, item);
    }

    public static void putSlot(Player player, String ident, ItemStack item, boolean syncClient) {
        if (item.getAmount() == 0) item = new ItemStack(Material.AIR);

        Map<String, ItemStack> cache = slotCache.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>());
        cache.put(ident, item);

        if (!syncClient) return;
        PacketSender.putClientSlotItem(player, ident, item);
    }

    @Nullable
    public static ItemStack getSlot(Player player, String ident) {
        return getSlot(player.getUniqueId(), ident);
    }

    @Nullable
    public static ItemStack getSlot(UUID uuid, String ident) {
        Map<String, ItemStack> cache = slotCache.get(uuid);
        if (cache == null) return null;
        return cache.get(ident);
    }

    @Nullable
    public static ItemStack removeSlot(Player player, String ident) {
        return removeSlot(player.getUniqueId(), ident);
    }

    @Nullable
    public static ItemStack removeSlot(UUID uuid, String ident) {
        Map<String, ItemStack> cache = slotCache.get(uuid);
        if (cache == null) return null;
        return cache.remove(ident);
    }

    public static void backSlot(Player player, String ...keys) {
        for (String ident : keys) {
            ItemStack item = removeSlot(player, ident);
            if (item != null) {
                player.getInventory().addItem(item);
            }
            PacketSender.putClientSlotItem(player, ident, new ItemStack(Material.AIR));
        }
    }

    public static void clearSlot(Player player) {
        UUID uuid = player.getUniqueId();
        Map<String, ItemStack> cache = slotCache.remove(uuid);
        if (cache == null) return;
        cache.values().forEach(item -> player.getInventory().addItem(item));
    }
}
