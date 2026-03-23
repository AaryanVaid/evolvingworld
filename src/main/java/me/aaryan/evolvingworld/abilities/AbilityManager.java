package me.aaryan.evolvingworld.abilities;

import me.aaryan.evolvingworld.items.RiftShard;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AbilityManager {

    private final Map<UUID, Long> cooldowns = new HashMap<>();

    private final long COOLDOWN = 5000; // 5 sec

    public boolean tryUseAbility(Player player, RiftShard.ShardType type) {

        long now = System.currentTimeMillis();

        if (cooldowns.containsKey(player.getUniqueId())) {
            long last = cooldowns.get(player.getUniqueId());

            if (now - last < COOLDOWN) {
                long left = (COOLDOWN - (now - last)) / 1000;
                player.sendMessage("§cAbility on cooldown: " + left + "s");
                return false;
            }
        }

        cooldowns.put(player.getUniqueId(), now);

        useAbility(player, type);

        return true;
    }

    private void useAbility(Player player, RiftShard.ShardType type) {

        switch (type) {

            case FIRE -> fireAbility(player);
            case VOID -> voidAbility(player);
            case STORM -> stormAbility(player);
        }
    }

    // 🔥 FIRE → explosion knockback
    private void fireAbility(Player player) {

        Location loc = player.getLocation();

        loc.getWorld().createExplosion(loc, 2F, false, false);

        for (var entity : loc.getWorld().getNearbyEntities(loc, 5, 5, 5)) {
            if (entity instanceof Player target && target != player) {

                Vector dir = target.getLocation().toVector()
                        .subtract(player.getLocation().toVector())
                        .normalize();

                target.setVelocity(dir.multiply(1.5));
            }
        }

        player.sendMessage("§cFIRE ability unleashed!");
    }

    // 🌌 VOID → teleport forward
    private void voidAbility(Player player) {

        Location loc = player.getLocation();
        Vector dir = loc.getDirection().normalize();

        Location target = loc.add(dir.multiply(8));

        player.teleport(target);

        player.getWorld().spawnParticle(
                Particle.PORTAL,
                target,
                50,
                0.5, 1, 0.5
        );

        player.sendMessage("§5VOID blink!");
    }

    // ⚡ STORM → speed burst
    private void stormAbility(Player player) {

        player.setVelocity(player.getVelocity().add(new Vector(0, 1, 0)));

        player.getWorld().spawnParticle(
                Particle.CLOUD,
                player.getLocation(),
                30,
                0.5, 0.5, 0.5
        );

        player.sendMessage("§bSTORM burst!");
    }
}