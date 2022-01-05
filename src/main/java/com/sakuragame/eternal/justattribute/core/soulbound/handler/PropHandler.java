package com.sakuragame.eternal.justattribute.core.soulbound.handler;

import com.sakuragame.eternal.justattribute.core.soulbound.Action;
import com.sakuragame.eternal.justattribute.core.soulbound.SoulBound;
import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import org.bukkit.entity.Player;

public class PropHandler {

    public static class Normal extends BoundHandler {

        @Override
        public String getUnboundDesc() {
            return ConfigFile.SoulBound.prop;
        }

        @Override
        public String getBoundDesc(String owner) {
            return getUnboundDesc();
        }

        @Override
        public void build(Player player, ItemTag itemTag) {
            super.build(player, itemTag);
            itemTag.putDeep(SoulBound.NBT_TYPE_NODE, Action.PROP.getId());
        }

    }

    public static class Lock extends BoundHandler {

        @Override
        public String getUnboundDesc() {
            return ConfigFile.SoulBound.propLock;
        }

        @Override
        public String getBoundDesc(String owner) {
            return getUnboundDesc();
        }

        @Override
        public void build(Player player, ItemTag itemTag) {
            super.build(player, itemTag);
            itemTag.putDeep(SoulBound.NBT_TYPE_NODE, Action.PROP_LOCK.getId());
        }

    }
}
