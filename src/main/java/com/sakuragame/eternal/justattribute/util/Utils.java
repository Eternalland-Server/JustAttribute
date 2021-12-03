package com.sakuragame.eternal.justattribute.util;

import com.sakuragame.eternal.justattribute.core.special.EquipClassify;
import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.api.ItemStream;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTagData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.sakuragame.eternal.justlevel.api.JustLevelAPI;
import net.sakuragame.eternal.justlevel.level.PlayerLevelData;
import net.sakuragame.eternal.justmessage.api.MessageAPI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.UUID;

public class Utils {

    @Getter
    @AllArgsConstructor
    public static class NoticeHistory {
        private String content;
        private long time;
    }

    public final static DecimalFormat a = new DecimalFormat("0");
    private final static HashMap<UUID, NoticeHistory> history = new HashMap<>();

    public static String formatValue(double value, boolean isPercent) {
        value = isPercent ? value * 100 : value;
        String s = value >= 0 ? "+" + a.format(value) : "-" + a.format(value);
        return isPercent ? s + "%" : s;
    }

    public static String format(double value, boolean isPercent) {
        value = isPercent ? value * 100 : value;
        String s = a.format(value);
        return isPercent ? s + "%" : s;
    }

    public static boolean isWeaponClassify(ItemStack item) {
        ItemStream itemStream = ZaphkielAPI.INSTANCE.read(item);
        if (itemStream.isVanilla()) return false;

        ItemTagData data = itemStream.getZaphkielData().getDeep(EquipClassify.NBT_NODE);
        if (data == null) return false;

        int i = data.asInt();
        return i <= 1;
    }

    public static boolean isArmor(Material material) {
        String name = material.name();
        return name.contains("HELMET") ||
                name.contains("CHESTPLATE") ||
                name.contains("LEGGINGS") ||
                name.contains("BOOTS");
    }

    public static String getSource(int i) {
        String s = String.valueOf(i);
        return s
                .replace("0", "❿")
                .replace("1", "❶")
                .replace("2", "❷")
                .replace("3", "❸")
                .replace("4", "❹")
                .replace("5", "❺")
                .replace("6", "❻")
                .replace("7", "❼")
                .replace("8", "❽")
                .replace("9", "❾");
    }

    public static void sendActionTip(Player player, String screenID, String message) {
        NoticeHistory record = history.get(player.getUniqueId());
        if (record != null && record.getContent().equals(message)) {
            long time = record.getTime();
            if (System.currentTimeMillis() - time < 1000) {
                return;
            }
        }

        MessageAPI.sendActionTip(player, screenID, message);
        history.put(player.getUniqueId(), new NoticeHistory(message, System.currentTimeMillis()));
    }

    public static double getDamagePromote(Player player) {
        PlayerLevelData data = JustLevelAPI.getData(player);

        return ((data.getStage() - 1) * 0.1 + (data.getRealm() - 1) * 0.7) * 0.5 + 1;
    }

    public static double getDefencePromote(Player player) {
        PlayerLevelData data = JustLevelAPI.getData(player);

        return ((data.getStage() - 1) * 0.1 + (data.getRealm() - 1) * 0.7) * 0.35 + 1;
    }
}