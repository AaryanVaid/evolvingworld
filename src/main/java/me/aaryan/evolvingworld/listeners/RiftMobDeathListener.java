package me.aaryan.evolvingworld.listeners;

import me.aaryan.evolvingworld.EvolvingWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class RiftMobDeathListener implements Listener {

    private final EvolvingWorld plugin;

    public RiftMobDeathListener(EvolvingWorld plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMobDeath(EntityDeathEvent event) {

        if (!(event.getEntity() instanceof LivingEntity entity)) return;

        plugin.getRiftManager().handleMobDeath(entity);
    }
}