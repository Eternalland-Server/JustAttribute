package com.sakuragame.eternal.justattribute.core.special;

import com.sakuragame.eternal.justattribute.api.event.JAEquipBoundEvent;
import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.api.ItemStream;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTagData;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class SoulBound {

    public final static String NBT_ACTION_NODE = "justattribute.soulbound.action";
    public final static String NBT_UUID_NODE = "justattribute.soulbound.uuid";
    public final static String NBT_NAME_NODE = "justattribute.soulbound.name";
    public final static String DISPLAY_NODE = "display.soulbound";

    @Getter
    public enum Action {

        AUTO(0, ConfigFile.soulbound_auto),
        USE(1, ConfigFile.soulbound_use),
        PROP(2, ConfigFile.soulbound_prop);

        private final int id;
        private final String desc;

        Action(int id, String desc) {
            this.id = id;
            this.desc = desc;
        }

        public String formatting() {
            return ConfigFile.unbound_format.replace("<desc>", getDesc());
        }

        public static Action getAction(int id) {
            for (Action action : values()) {
                if (action.getId() == id) {
                    return action;
                }
            }

            return null;
        }
    }

    public static String format(String owner) {
        return ConfigFile.bound_format.replace("<owner>", owner);
    }

    public static boolean isUseBind(ItemStack item) {
        ItemStream itemStream = ZaphkielAPI.INSTANCE.read(item);
        if (itemStream.isVanilla()) return false;

        ItemTag itemTag = itemStream.getZaphkielData();
        int actionType = itemTag.getDeepOrElse(SoulBound.NBT_ACTION_NODE, new ItemTagData(-1)).asInt();

        return actionType == SoulBound.Action.USE.getId();
    }

    public static UUID getItemOwner(ItemStack item) {
        ItemStream itemStream = ZaphkielAPI.INSTANCE.read(item);
        if (itemStream.isVanilla()) return null;

        ItemTag itemTag = itemStream.getZaphkielData();

        ItemTagData data = itemTag.getDeep(SoulBound.NBT_UUID_NODE);
        if (data == null) return null;

        return UUID.fromString(data.asString());
    }

    public static ItemStack binding(Player player, ItemStack item) {
        ItemStream itemStream = ZaphkielAPI.INSTANCE.read(item);
        if (itemStream.isVanilla()) return item;

        ItemTag itemTag = itemStream.getZaphkielData();

        int actionType = itemTag.getDeepOrElse(SoulBound.NBT_ACTION_NODE, new ItemTagData(-1)).asInt();
        if (actionType == -1 || actionType != SoulBound.Action.USE.getId()) return item;

        itemTag.removeDeep(SoulBound.NBT_ACTION_NODE);
        itemTag.putDeep(SoulBound.NBT_UUID_NODE, player.getUniqueId().toString());
        itemTag.putDeep(SoulBound.NBT_NAME_NODE, player.getName());

        ItemStack rebuild = itemStream.rebuildToItemStack(player);

        JAEquipBoundEvent event = new JAEquipBoundEvent(player, rebuild);
        event.call();

        return rebuild;
    }
}
