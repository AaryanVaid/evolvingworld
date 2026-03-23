package me.aaryan.evolvingworld.rift;

import me.aaryan.evolvingworld.EvolvingWorld;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.*;

public class RiftManager {

    private final EvolvingWorld plugin;
    private final List<Rift> activeRifts = new ArrayList<>();
    private final Random random = new Random();

    public RiftManager(EvolvingWorld plugin) {
        this.plugin = plugin;
        startTasks();
    }

    public List<Rift> getActiveRifts() {
        return activeRifts;
    }

    // 🔥 Spawn Rift
    public void spawnRift(Location loc) {

        Rift rift = new Rift(loc);
        activeRifts.add(rift);

        Bukkit.broadcastMessage("§5A Rift has opened nearby!");

        spawnMobs(rift);
    }

    // 🔥 Spawn mobs inside radius
    private void spawnMobs(Rift rift) {

        for (int i = 0; i < 5; i++) {

            Location spawn = getRandomLocationInRadius(rift);

            LivingEntity mob = (LivingEntity) spawn.getWorld()
                    .spawnEntity(spawn, EntityType.ZOMBIE);

            rift.addMob(mob);
        }
    }

    // 🔥 Mob death handler
    public void handleMobDeath(LivingEntity entity) {

        for (Rift rift : new ArrayList<>(activeRifts)) {

            if (!rift.isActive()) continue;

            if (rift.isRiftMob(entity)) {

                rift.mobDied(entity);

                if (!rift.isActive()) {
                    activeRifts.remove(rift);
                }

                break;
            }
        }
    }

    // 🔥 Random location inside rift
    private Location getRandomLocationInRadius(Rift rift) {

        Location center = rift.getCenter();

        double x = (random.nextDouble() - 0.5) * 10;
        double z = (random.nextDouble() - 0.5) * 10;

        Location loc = center.clone().add(x, 0, z);

        return center.getWorld().getHighestBlockAt(loc).getLocation().add(0, 1, 0);
    }

    // 🔥 Auto tasks
    private void startTasks() {

        // Visual tick (every 10 ticks)
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Rift rift : activeRifts) {
                rift.tickVisuals();
            }
        }, 0L, 10L);

        // Auto spawn system (every 3 mins)
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {

            int players = Bukkit.getOnlinePlayers().size();

            int maxRifts = Math.max(1, players / 2);
            maxRifts = Math.min(maxRifts, 3);

            if (activeRifts.size() >= maxRifts) return;

            spawnRandomRift();

        }, 0L, 20L * 180);
    }

    // 🔥 Spawn near random player
    public void spawnRandomRift() {

        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        if (players.isEmpty()) return;

        Player player = players.get(random.nextInt(players.size()));

        Location base = player.getLocation();

        Location spawn = base.clone().add(
                (random.nextDouble() - 0.5) * 50,
                0,
                (random.nextDouble() - 0.5) * 50
        );

        spawn = base.getWorld().getHighestBlockAt(spawn).getLocation();

        spawnRift(spawn);
    }
}