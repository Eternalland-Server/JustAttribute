package com.sakuragame.eternal.justattribute.util;

import com.sakuragame.eternal.justattribute.core.special.EquipClassify;
import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.api.ItemStream;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTagData;
import net.sakuragame.eternal.justlevel.api.JustLevelAPI;
import net.sakuragame.eternal.justlevel.core.user.PlayerLevelData;
import net.sakuragame.eternal.justmessage.api.MessageAPI;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.Random;

public class Utils {

    public final static DecimalFormat a = new DecimalFormat("0");
    public final static Random RANDOM = new Random();

    public static int getRandomInt(int value) {
        return RANDOM.nextInt(value);
    }

    public static int getRangeValue(String s) {
        if (!s.contains("-")) return Integer.parseInt(s);

        String[] args = s.split("-");
        int a = Integer.parseInt(args[0]);
        int b = Integer.parseInt(args[1]);

        return RANDOM.nextInt(b - a) + a;
    }

    public static String getZapID(ItemStack item) {
        ItemStream itemStream = ZaphkielAPI.INSTANCE.read(item);
        return itemStream.getZaphkielItem().getId();
    }

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

    public static double getRealmDamagePromote(Player player) {
        PlayerLevelData data = JustLevelAPI.getUserData(player);

        return (data.getStage() - 1) * 0.1 + (data.getRealm() - 1) + 1;
    }

    public static double getRealmDefencePromote(Player player) {
        PlayerLevelData data = JustLevelAPI.getUserData(player);

        return (data.getStage() - 1) * 0.1 + (data.getRealm() - 1) + 1;
    }

    public static void sendCombatChange(Player player, int value) {
        MessageAPI.sendActionTip(player, (value > 0 ? "&e&l+" : "&c&l-") + Math.abs(value) + "战斗力");
        player.playSound(player.getLocation(), value > 0 ? Sound.ENTITY_HORSE_SADDLE : Sound.ENTITY_FIREWORK_BLAST_FAR, 0.5f, 1);
    }
}