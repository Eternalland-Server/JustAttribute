package com.sakuragame.eternal.justattribute.core.special;

import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.api.ItemStream;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTagData;
import lombok.Getter;
import net.sakuragame.eternal.justmessage.JustMessage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
public enum PotencyGrade {

    NONE(-1, "ㅐ", ""),
    B(0, "ㅑ", "B"),
    A(1, "ㅒ", "A"),
    S(2, "ㅓ", "S"),
    SS(3, "ㅔ", "SS", new String[]{"⒝ §a恭喜§7", "<player>", "§a在鉴定 §f", "<item>", "§a 时,鉴定出§6SS§a级潜能"}),
    SSS(4, "ㅏ", "SSS",  new String[]{"⒠ §e恭喜§7", "<player>", "§e在鉴定 §f", "<item>", "§e 时,鉴定出§cSSS§e级潜能"});

    private final int id;
    private final String symbol;
    private final String name;

    private final String[] broadcast;

    public final static String NBT_TAG = "justattribute.grade";
    private final static String POTENCY_DESC = "⌚";

    PotencyGrade(int id, String symbol, String name) {
        this.id = id;
        this.symbol = symbol;
        this.name = name;
        this.broadcast = null;
    }

    PotencyGrade(int id, String symbol, String name, String[] broadcast) {
        this.id = id;
        this.symbol = symbol;
        this.name = name;
        this.broadcast = broadcast;
    }

    public String formatting() {
        return ConfigFile.format.potency
                .replace("<symbol>", getSymbol())
                .replace("<desc>", POTENCY_DESC);
    }

    public void sendBroadcast(Player player, ItemStack item) {
        if (this.broadcast == null) return;
        String[] params = this.broadcast.clone();
        params[1] = player.getName();

        JustMessage.getChatManager().send(item, params);
    }

    public static PotencyGrade match(int id) {
        for (PotencyGrade grade : values()) {
            if (grade.getId() == id) {
                return grade;
            }
        }

        return NONE;
    }

    public static PotencyGrade getGrade(ItemStack item) {
        ItemStream itemStream = ZaphkielAPI.INSTANCE.read(item);
        if (itemStream.isVanilla()) return null;

        ItemTag itemTag = itemStream.getZaphkielData();
        return getGrade(itemTag);
    }

    public static PotencyGrade getGrade(ItemTag tag) {
        ItemTagData data = tag.getDeep(NBT_TAG);
        if (data == null) return null;

        return match(data.asInt());
    }
}
