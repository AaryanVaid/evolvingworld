package me.aaryan.evolvingworld.rift;

import me.aaryan.evolvingworld.items.RiftShard;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;

import java.util.*;

public class Rift {

    private final Location center;
    private final double radius = 10;

    private final RiftShard.ShardType type;

    private final Set<UUID> mobs = new HashSet<>();
    private final Map<UUID, Double> contributions = new HashMap<>();

    private boolean active = true;

    public Rift(Location center, RiftShard.ShardType type) {
        this.center = center;
        this.type = type;
    }

    public Location getCenter() {
        return center;
    }

    public RiftShard.ShardType getType() {
        return type;
    }

    public boolean isActive() {
        return active;
    }

    public void addMob(Entity entity) {
        mobs.add(entity.getUniqueId());
    }

    public boolean isRiftMob(Entity entity) {
        return mobs.contains(entity.getUniqueId());
    }

    public void addContribution(UUID playerId, double damage) {
        contributions.put(playerId,
                contributions.getOrDefault(playerId, 0.0) + damage
        );
    }

    public void mobDied(Entity entity) {
        mobs.remove(entity.getUniqueId());

        if (mobs.isEmpty()) {
            close();
        }
    }

    public void tickVisuals() {
        if (!active) return;

        center.getWorld().spawnParticle(
                Particle.PORTAL,
                center.clone().add(0, 1, 0),
                30,
                0.5, 1, 0.5,
                0.1
        );

        center.getWorld().spawnParticle(
                Particle.REVERSE_PORTAL,
                center.clone().add(0, 1, 0),
                20,
                0.5, 1, 0.5,
                0.05
        );
    }

    private void close() {
        active = false;

        Bukkit.broadcastMessage("§5A " + type.name() + " Rift has been closed!");

        rewardTopPlayer();
    }

    private void rewardTopPlayer() {

        if (contributions.isEmpty()) return;

        UUID top = null;
        double max = 0;

        for (Map.Entry<UUID, Double> entry : contributions.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                top = entry.getKey();
            }
        }

        if (top == null) return;

        var player = Bukkit.getPlayer(top);

        if (player != null) {
            player.sendMessage("§6You were the top contributor!");

            player.getInventory().addItem(
                    RiftShard.create(type)
            );
        }
    }
}