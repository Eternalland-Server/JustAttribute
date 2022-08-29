package com.sakuragame.eternal.justattribute.commands.sub;

import com.sakuragame.eternal.justattribute.commands.CommandPerms;
import com.sakuragame.eternal.justattribute.core.smithy.factory.EnhanceFactory;
import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.api.Item;
import ink.ptms.zaphkiel.api.ItemStream;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemCommand extends SubCommand {
    @Override
    public String getIdentifier() {
        return "item";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length != 2) return;

        Player player = this.getPlayer();
        String s = args[0];
        int level = Integer.parseInt(args[1]);

        for (Item item : ZaphkielAPI.INSTANCE.getRegisteredItem().values()) {
            if (!item.getId().startsWith(s)) continue;

            ItemStream itemStream = item.build(player);
            ItemTag tag = itemStream.getZaphkielData();
            tag.putDeep(EnhanceFactory.NBT_NODE_PRE_ENHANCE, level);

            ItemStack itemStack = itemStream.rebuildToItemStack(player);

            player.getInventory().addItem(itemStack);
        }

        sender.sendMessage(ConfigFile.prefix + "done!");
    }

    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String getPermission() {
        return CommandPerms.ADMIN.getNode();
    }
}
