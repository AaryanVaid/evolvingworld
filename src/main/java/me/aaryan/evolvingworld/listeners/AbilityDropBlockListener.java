package me.aaryan.evolvingworld.listeners;

import me.aaryan.evolvingworld.EvolvingWorld;
import me.aaryan.evolvingworld.ability.AbilityKeys;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class AbilityDropBlockListener implements Listener {

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {

        ItemStack item = e.getItemDrop().getItemStack();

        if (AbilityKeys.isAbilityItem(item)) {
            e.setCancelled(true);
        }
    }
}