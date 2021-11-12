package com.sakuragame.eternal.justattribute.core.special;

import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.api.ItemStream;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTagData;
import net.sakuragame.eternal.justlevel.api.JustLevelAPI;
import net.sakuragame.eternal.justlevel.level.RealmSetting;
import org.bukkit.inventory.ItemStack;

public class RealmLimit {

    public final static String NBT_NODE = "justattribute.realm";
    public final static String DISPLAY_NODE = "display.realm";

    public static String format(int i) {
        RealmSetting setting = JustLevelAPI.getRealmSetting(i);
        return ConfigFile.realm_format
                .replace("<name>", setting == null ? "error" : setting.getName())
                .replace("<realm>", String.valueOf(i));
    }

    public static int getItemRealm(ItemStack item) {
        ItemStream itemStream = ZaphkielAPI.INSTANCE.read(item);
        if (itemStream.isVanilla()) return -1;

        ItemTag itemTag = itemStream.getZaphkielData();

        return itemTag.getDeepOrElse(RealmLimit.NBT_NODE, new ItemTagData(-1)).asInt();
    }
}
