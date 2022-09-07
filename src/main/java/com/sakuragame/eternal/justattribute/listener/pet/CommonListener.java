package com.sakuragame.eternal.justattribute.listener.pet;

import com.sakuragame.eternal.justattribute.core.PetHandler;
import net.sakuragame.eternal.kirrapet.event.PetLevelUpEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class CommonListener implements Listener {

    @EventHandler
    public void onPetUpgrade(PetLevelUpEvent e) {
        Player player = e.getPlayer();
        int level = e.getTo();
        ItemStack egg = e.getItem();
        e.setItem(PetHandler.unlockCapacity(player, egg, level));
    }
}
