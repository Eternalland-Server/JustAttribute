package com.sakuragame.eternal.justattribute.commands.sub;

import com.sakuragame.eternal.justattribute.file.sub.MessageFile;
import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import org.bukkit.command.CommandSender;

public class HelpCommand extends SubCommand {

    @Override
    public String getIdentifier() {
        return "help";
    }

    @Override
    public void perform(CommandSender CommandSender, String[] Strings) {
        MessageFile.help.forEach(CommandSender::sendMessage);
        if (CommandSender.hasPermission("justrpg.admin"))
            MessageFile.adminHelp.forEach(CommandSender::sendMessage);
    }

    @Override
    public boolean playerOnly() {
        return false;
    }

    @Override
    public String getPermission() {
        return null;
    }
}
