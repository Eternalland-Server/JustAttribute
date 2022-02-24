package com.sakuragame.eternal.justattribute.commands.sub;

import com.sakuragame.eternal.justattribute.api.JustAttributeAPI;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleState;
import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InfoCommand extends SubCommand {
    @Override
    public String getIdentifier() {
        return "info";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        Player player = getPlayer();
        RoleState state = JustAttributeAPI.getRoleState(player);

        player.sendMessage(ConfigFile.prefix + "生命值: §a" + state.getHealth() + "/" + state.getMaxHealth());
        player.sendMessage(ConfigFile.prefix + "法力值: §a" + state.getMana() + "/" + state.getMaxMana());
    }

    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String getPermission() {
        return null;
    }
}
