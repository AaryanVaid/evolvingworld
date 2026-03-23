package me.aaryan.evolvingworld.rift;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Rift {

    private final Location center;
    private final double radius = 10;

    private final Set<UUID> mobs = new HashSet<>();
    private boolean active = true;

    public Rift(Location center) {
        this.center = center;
    }

    public Location getCenter() {
        return center;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isInside(Location loc) {
        return loc.getWorld().equals(center.getWorld()) &&
                loc.distance(center) <= radius;
    }

    public void addMob(Entity entity) {
        mobs.add(entity.getUniqueId());
    }

    public boolean isRiftMob(Entity entity) {
        return mobs.contains(entity.getUniqueId());
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

        Bukkit.broadcastMessage("§5A Rift has been closed!");

        rewardTopPlayer();
    }

    // 🔥 FIND TOP PLAYER
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

            // 🔥 Placeholder reward (we replace with shard later)
            player.getInventory().addItem(
                    new org.bukkit.inventory.ItemStack(org.bukkit.Material.DIAMOND)
            );
        }
    }
}