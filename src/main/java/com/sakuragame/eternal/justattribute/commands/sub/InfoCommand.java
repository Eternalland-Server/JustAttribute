package com.sakuragame.eternal.justattribute.commands.sub;

import com.sakuragame.eternal.justattribute.api.JustAttributeAPI;
import com.sakuragame.eternal.justattribute.core.attribute.character.PlayerCharacter;
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
        PlayerCharacter role = JustAttributeAPI.getRoleCharacter(player);

        player.sendMessage(ConfigFile.prefix + "生命值: §a" + role.getHP() + "/" + role.getMaxHP());
        player.sendMessage(ConfigFile.prefix + "法力值: §a" + role.getMP() + "/" + role.getMaxMP());
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
