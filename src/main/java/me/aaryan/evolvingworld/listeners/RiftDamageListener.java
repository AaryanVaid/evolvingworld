package me.aaryan.evolvingworld.listeners;

import me.aaryan.evolvingworld.EvolvingWorld;
import me.aaryan.evolvingworld.rift.Rift;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class RiftDamageListener implements Listener {

    private final EvolvingWorld plugin;

    public RiftDamageListener(EvolvingWorld plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {

        if (!(event.getDamager() instanceof Player player)) return;

        var entity = event.getEntity();

        for (Rift rift : plugin.getRiftManager().getActiveRifts()) {

            if (!rift.isActive()) continue;

            if (rift.isRiftMob(entity)) {

                double damage = event.getFinalDamage();

                rift.addContribution(player.getUniqueId(), damage);

                break;
            }
        }
    }
}