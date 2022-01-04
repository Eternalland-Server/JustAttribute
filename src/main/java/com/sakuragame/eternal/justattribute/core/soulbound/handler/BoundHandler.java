package com.sakuragame.eternal.justattribute.core.soulbound.handler;

import com.sakuragame.eternal.justattribute.core.soulbound.SoulBound;
import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import org.bukkit.entity.Player;

public abstract class BoundHandler {

    public abstract String getUnboundDesc();

    public String getBoundDesc(String owner) {
        return ConfigFile.format.bound.replace("<owner>", owner);
    }

    public void build(Player player, ItemTag itemTag) {
        itemTag.removeDeep(SoulBound.NBT_ACTION_NODE);
        itemTag.putDeep(SoulBound.NBT_UUID_NODE, player.getUniqueId().toString());
        itemTag.putDeep(SoulBound.NBT_NAME_NODE, player.getName());
    }
}
