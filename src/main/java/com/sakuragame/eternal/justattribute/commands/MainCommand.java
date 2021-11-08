package com.sakuragame.eternal.justattribute.commands;

import com.sakuragame.eternal.justattribute.commands.sub.HelpCommand;
import com.sakuragame.eternal.justattribute.commands.sub.ReloadCommand;
import com.taylorswiftcn.justwei.commands.JustCommand;


public class MainCommand extends JustCommand {

    public MainCommand() {
        super(new HelpCommand());
        register(new ReloadCommand());
    }
}
