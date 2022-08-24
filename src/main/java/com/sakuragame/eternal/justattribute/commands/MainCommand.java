package com.sakuragame.eternal.justattribute.commands;

import com.sakuragame.eternal.justattribute.commands.sub.*;
import com.taylorswiftcn.justwei.commands.JustCommand;


public class MainCommand extends JustCommand {

    public MainCommand() {
        super(new HelpCommand());
        this.register(new InfoCommand());
        this.register(new SpawnCommand());
        this.register(new ItemCommand());
        this.register(new ReloadCommand());
    }
}
