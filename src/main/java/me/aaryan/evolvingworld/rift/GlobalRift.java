package me.aaryan.evolvingworld.rift;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GlobalRift {

    private final Location center;
    private final RiftType type;
    private boolean active = true;

    private final Map<UUID, Integer> contributions = new HashMap<>();

    public GlobalRift(Location center, RiftType type) {
        this.center = center;
        this.type = type;
    }

    public Location getCenter() { return center; }
    public RiftType getType() { return type; }

    public boolean isActive() { return active; }
    public void close() { active = false; }

    public void addContribution(UUID uuid) {
        contributions.put(uuid, contributions.getOrDefault(uuid, 0) + 1);
    }

    public Map<UUID, Integer> getContributions() {
        return contributions;
    }
}