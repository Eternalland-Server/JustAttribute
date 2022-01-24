package com.sakuragame.eternal.justattribute.core.soulbound.handler;

import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import org.bukkit.entity.Player;

public class SealHandler extends BoundHandler {

    @Override
    public String getUnboundDesc() {
        return ConfigFile.SoulBound.seal;
    }

    @Override
    public String getBoundDesc(String owner) {
        return getUnboundDesc();
    }

    @Override
    public void build(Player player, ItemTag itemTag) {
    }
}
