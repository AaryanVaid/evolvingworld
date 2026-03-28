package me.aaryan.evolvingworld.listeners;

import me.aaryan.evolvingworld.EvolvingWorld;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.persistence.PersistentDataType;

public class RiftMobListener implements Listener {

    private final EvolvingWorld plugin;
    private final NamespacedKey riftKey;

    public RiftMobListener(EvolvingWorld plugin) {
        this.plugin = plugin;
        this.riftKey = new NamespacedKey(plugin, "rift_mob");
    }

    @EventHandler
    public void onRiftMobDeath(EntityDeathEvent event) {
        // Only count mobs tagged with "rift_mob"
        if (!event.getEntity().getPersistentDataContainer().has(riftKey, PersistentDataType.BYTE)) {
            return;
        }

        Player killer = event.getEntity().getKiller();
        if (killer == null) return;

        // Add contribution point
        if (plugin.getGlobalRiftManager().getActiveRift() != null) {
            plugin.getGlobalRiftManager().addContribution(killer);
        }
    }
}