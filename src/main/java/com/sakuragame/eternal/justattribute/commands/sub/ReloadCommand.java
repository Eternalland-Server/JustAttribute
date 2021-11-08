package com.sakuragame.eternal.justattribute.commands.sub;

import com.sakuragame.eternal.justattribute.commands.CommandPerms;
import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends SubCommand {

    @Override
    public String getIdentifier() {
        return "reload";
    }

    @Override
    public void perform(CommandSender CommandSender, String[] Strings) {

    }

    @Override
    public boolean playerOnly() {
        return false;
    }

    @Override
    public String getPermission() {
        return CommandPerms.ADMIN.getNode();
    }
}
