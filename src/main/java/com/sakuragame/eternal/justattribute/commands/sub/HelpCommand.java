package com.sakuragame.eternal.justattribute.commands.sub;

import com.sakuragame.eternal.justattribute.commands.CommandPerms;
import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import org.bukkit.command.CommandSender;

public class HelpCommand extends SubCommand {

    private final String[] commands;

    public HelpCommand() {
        this.commands = new String[] {
                " §7/attribute info - 查看角色状态",
                " §7/attribute spawn <mob> <level> - 召唤怪物",
                " §7/attribute reload - 重载配置文件"
        };
    }

    @Override
    public String getIdentifier() {
        return "help";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        for (String s : this.commands) {
            sender.sendMessage(s);
        }
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
