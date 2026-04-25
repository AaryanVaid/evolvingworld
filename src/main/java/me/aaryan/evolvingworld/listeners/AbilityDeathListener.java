package me.aaryan.evolvingworld.listeners;

import me.aaryan.evolvingworld.ability.AbilityKeys;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class AbilityDeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        e.getDrops().removeIf(item ->
                item != null && AbilityKeys.isAbilityItem(item)
        );
        for (int i = 6; i <= 8; i++) {
            e.getEntity().getInventory().setItem(i, null);
        }
    }
}