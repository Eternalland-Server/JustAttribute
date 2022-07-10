package com.sakuragame.eternal.justattribute.util;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.character.PlayerCharacter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CatchTask extends BukkitRunnable {

    private final Player player;
    private int count;

    public CatchTask(Player player) {
        this.player = player;
        this.count = 0;
    }

    @Override
    public void run() {
        if (!this.player.isOnline()) return;
        if (count == 10) {
            player.kickPlayer("账户未被正确加载，请重新进入。");
            JustAttribute.getInstance().getLogger().info("玩家 " + player.getName() + " 账户数据载入失败!");
            cancel();
            return;
        }

        PlayerCharacter role = JustAttribute.getStorageManager().loadAccount(this.player.getUniqueId());
        if (role != null) {
            JustAttribute.getRoleManager().put(this.player.getUniqueId(), role);
            JustAttribute.getRoleManager().init(this.player.getUniqueId());
            cancel();
            return;
        }

        count++;
    }
}
