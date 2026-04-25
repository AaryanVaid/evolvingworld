package me.aaryan.evolvingworld.ability;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class CooldownManager {

    // UUID → slot → cooldown
    private final HashMap<UUID, HashMap<Integer, Long>> cooldowns = new HashMap<>();

    public boolean canUse(Player p, int slot) {
        long now = System.currentTimeMillis();

        return cooldowns
                .getOrDefault(p.getUniqueId(), new HashMap<>())
                .getOrDefault(slot, 0L) <= now;
    }

    public void apply(Player p, int slot, long cooldown) {
        long now = System.currentTimeMillis();

        cooldowns
                .computeIfAbsent(p.getUniqueId(), k -> new HashMap<>())
                .put(slot, now + cooldown);
    }
}