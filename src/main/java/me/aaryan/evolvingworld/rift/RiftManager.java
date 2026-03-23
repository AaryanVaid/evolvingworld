package me.aaryan.evolvingworld.rift;

import me.aaryan.evolvingworld.EvolvingWorld;
import me.aaryan.evolvingworld.items.RiftShard;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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

    public void spawnRift(Location loc) {

        RiftShard.ShardType type = RiftShard.ShardType.values()[
                random.nextInt(RiftShard.ShardType.values().length)
                ];

        Rift rift = new Rift(loc, type);
        activeRifts.add(rift);

        Bukkit.broadcastMessage("§5A " + type.name() + " Rift has opened!");

        spawnMobs(rift);
    }

    private void spawnMobs(Rift rift) {

        for (int i = 0; i < 5; i++) {

            Location spawn = getRandomLocationInRadius(rift);

            LivingEntity mob;

            switch (rift.getType()) {

                case FIRE -> mob = (LivingEntity) spawn.getWorld()
                        .spawnEntity(spawn, EntityType.ZOMBIE);

                case VOID -> mob = (LivingEntity) spawn.getWorld()
                        .spawnEntity(spawn, EntityType.SKELETON);

                case STORM -> mob = (LivingEntity) spawn.getWorld()
                        .spawnEntity(spawn, EntityType.CREEPER);

                default -> mob = (LivingEntity) spawn.getWorld()
                        .spawnEntity(spawn, EntityType.ZOMBIE);
            }

            applyBuffs(mob, rift);

            rift.addMob(mob);
        }
    }

    private void applyBuffs(LivingEntity mob, Rift rift) {

        // Health
        var health = mob.getAttribute(Attribute.MAX_HEALTH);
        if (health != null) {
            health.setBaseValue(40.0);
            mob.setHealth(40.0);
        }

        // Damage
        var damage = mob.getAttribute(Attribute.ATTACK_DAMAGE);
        if (damage != null) {
            damage.setBaseValue(6.0);
        }

        switch (rift.getType()) {

            case FIRE -> {
                mob.setFireTicks(Integer.MAX_VALUE);

                mob.addPotionEffect(new PotionEffect(
                        PotionEffectType.FIRE_RESISTANCE,
                        Integer.MAX_VALUE,
                        1,
                        false,
                        false
                ));
            }

            case VOID -> {
                mob.addPotionEffect(new PotionEffect(
                        PotionEffectType.INVISIBILITY,
                        999999,
                        0,
                        false,
                        false
                ));
            }

            case STORM -> {
                mob.addPotionEffect(new PotionEffect(
                        PotionEffectType.SPEED,
                        999999,
                        1,
                        false,
                        false
                ));
            }
        }
    }

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

    private Location getRandomLocationInRadius(Rift rift) {

        Location center = rift.getCenter();

        double x = (random.nextDouble() - 0.5) * 10;
        double z = (random.nextDouble() - 0.5) * 10;

        Location loc = center.clone().add(x, 0, z);

        return center.getWorld().getHighestBlockAt(loc).getLocation().add(0, 1, 0);
    }

    private void startTasks() {

        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Rift rift : activeRifts) {
                rift.tickVisuals();
            }
        }, 0L, 10L);

        Bukkit.getScheduler().runTaskTimer(plugin, () -> {

            int players = Bukkit.getOnlinePlayers().size();

            int maxRifts = Math.max(1, players / 2);
            maxRifts = Math.min(maxRifts, 3);

            if (activeRifts.size() >= maxRifts) return;

            spawnRandomRift();

        }, 0L, 20L * 180);
    }

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