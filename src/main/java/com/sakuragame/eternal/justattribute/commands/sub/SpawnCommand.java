package com.sakuragame.eternal.justattribute.commands.sub;

import com.sakuragame.eternal.justattribute.commands.CommandPerms;
import com.sakuragame.eternal.justattribute.core.attribute.character.MobCharacter;
import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import com.sakuragame.eternal.justattribute.util.Utils;
import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class SpawnCommand extends SubCommand {

    @Override
    public String getIdentifier() {
        return "spawn";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length != 2) return;

        String id = args[0];
        String s = args[1];

        if (!MegumiUtil.isNumber(s)) return;

        int level = Utils.getRangeValue(s);

        Player player = this.getPlayer();
        try {
            Entity entity = MythicMobs.inst().getAPIHelper().spawnMythicMob(id, player.getLocation(), level);
            if (entity == null) return;

            ActiveMob am = MythicMobs.inst().getMobManager().getActiveMob(entity.getUniqueId()).get();
            MobCharacter mob = new MobCharacter(am);
            mob.update();
            sender.sendMessage(ConfigFile.prefix + "怪物战斗力: §f" + mob.getCombatValue());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            sender.sendMessage(ConfigFile.prefix + "召唤成功");
        }
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
