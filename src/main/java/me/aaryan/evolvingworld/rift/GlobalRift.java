package me.aaryan.evolvingworld.rift;

import org.bukkit.Location;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GlobalRift {

    private final Location center;
    private final RiftType type;
    private final int targetKills;
    private int totalKills = 0;
    private boolean active = true;

    private final Map<UUID, Integer> contributions = new HashMap<>();

    public GlobalRift(Location center, RiftType type, int targetKills) {
        this.center = center;
        this.type = type;
        this.targetKills = targetKills;
    }

    // --- GETTERS ---

    public Location getCenter() { return center; }
    public RiftType getType() { return type; }
    public boolean isActive() { return active; }

    public int getTotalKills() { return totalKills; }
    public int getTargetKills() { return targetKills; }

    public Map<UUID, Integer> getContributions() {
        return contributions;
    }

    // --- SETTERS / LOGIC ---

    public void close() {
        active = false;
    }

    /**
     * Increments the global kill counter for the rift.
     */
    public void incrementKills() {
        this.totalKills++;
    }

    /**
     * Adds a kill to a specific player's contribution.
     * Use this in tandem with incrementKills() in the Manager.
     */
    public void addContribution(UUID uuid) {
        contributions.put(uuid, contributions.getOrDefault(uuid, 0) + 1);
    }
}