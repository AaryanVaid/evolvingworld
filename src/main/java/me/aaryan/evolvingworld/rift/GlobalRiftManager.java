package me.aaryan.evolvingworld.rift;

import me.aaryan.evolvingworld.EvolvingWorld;
import me.aaryan.evolvingworld.items.ToolShard;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.*;

public class GlobalRiftManager {

    private final EvolvingWorld plugin;
    private final Random random = new Random();
    private final NamespacedKey riftMobKey;

    private GlobalRift activeRift;
    private BossBar riftBar;

    public GlobalRiftManager(EvolvingWorld plugin) {
        this.plugin = plugin;
        this.riftMobKey = new NamespacedKey(plugin, "rift_mob");
    }

    public GlobalRift getActiveRift() { return activeRift; }

    // ================= SPAWNING LOGIC =================

    public void forceSpawn(RiftType type) {
        if (activeRift != null) cleanupRift();
        spawnProcess(type);
    }

    public void trySpawnRift() {
        if (activeRift != null && activeRift.isActive()) return;
        int worldPhase = plugin.getPhaseManager().getCurrentPhase().getLevel();
        if (worldPhase < 3) return;
        if (random.nextInt(100) > 8) return;

        RiftType type = (worldPhase >= 4 && random.nextBoolean()) ? RiftType.END : RiftType.NETHER;
        spawnProcess(type);
    }

    private void spawnProcess(RiftType type) {
        Location loc = getRandomLocation();
        activeRift = new GlobalRift(loc, type);

        // --- CONSOLE LOGGING (IMPOSSIBLE TO MISS) ---
        System.out.println(" ");
        System.out.println("==============================================");
        System.out.println("[RIFT DEBUG] SPAWNED " + type.name() + " RIFT");
        System.out.println("[COORDS] X: " + loc.getBlockX() + " Y: " + loc.getBlockY() + " Z: " + loc.getBlockZ());
        System.out.println("==============================================");
        System.out.println(" ");

        String title = (type == RiftType.NETHER) ? "§4§lCORE BREACH: NETHER" : "§d§lEVENT HORIZON: VOID";
        riftBar = Bukkit.createBossBar(title, BarColor.PURPLE, BarStyle.SEGMENTED_20);

        generateColossalStructureOptimized(loc, type);
        announce(loc, type);
        startLoop();
    }

    // ================= LAG-FREE TERRAFORMING =================

    private void generateColossalStructureOptimized(Location center, RiftType type) {
        int radius = 30; // 61x61 total area
        Material main = (type == RiftType.NETHER) ? Material.NETHERRACK : Material.END_STONE;
        Material deco = (type == RiftType.NETHER) ? Material.MAGMA_BLOCK : Material.CRYING_OBSIDIAN;

        new BukkitRunnable() {
            int currentR = 0;
            @Override
            public void run() {
                if (currentR > radius) { this.cancel(); return; }

                // Process 5 "rings" of the circle per tick to prevent server hang
                for (int step = 0; step < 5; step++) {
                    int r = currentR + step;
                    if (r > radius) break;

                    for (int x = -r; x <= r; x++) {
                        for (int z = -r; z <= r; z++) {
                            double dist = Math.sqrt(x*x + z*z);
                            if (dist > r - 1 && dist <= r) {
                                Location l = center.clone().add(x, -1, z);
                                // SetType with 'false' prevents block physics lag
                                if (random.nextDouble() > 0.1) {
                                    l.getBlock().setType(random.nextDouble() > 0.85 ? deco : main, false);
                                }
                            }
                        }
                    }
                }
                currentR += 5;
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    // ================= THE EVENT LOOP =================

    private void startLoop() {
        new BukkitRunnable() {
            int ticks = 0;
            final int maxTicks = 120; // 4 Minutes

            @Override
            public void run() {
                if (activeRift == null || !activeRift.isActive()) {
                    cleanupRift();
                    cancel();
                    return;
                }

                Location c = activeRift.getCenter();
                tickVisuals(c, activeRift.getType(), ticks);
                spawnMobs(c);
                updateBossBar(c, ticks, maxTicks);

                ticks++;
                if (ticks > maxTicks) {
                    endRift();
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 40);
    }

    private void tickVisuals(Location center, RiftType type, int ticks) {
        World world = center.getWorld();
        if (world == null) return;

        // 1. SKY PILLAR (100 blocks high)
        for (double y = 0; y < 100; y += 10) {
            world.spawnParticle(type == RiftType.NETHER ? Particle.FLAME : Particle.WITCH, center.clone().add(0, y, 0), 10, 0.5, 5, 0.5, 0.02);
        }

        // 2. EARTH SHAKE
        if (ticks % 5 == 0) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getWorld().equals(world) && p.getLocation().distance(center) < 45) {
                    p.playSound(center, Sound.ENTITY_ENDER_DRAGON_FLAP, 1f, 0.1f);
                    p.teleport(p.getLocation().add((random.nextDouble()-0.5)*0.08, 0, (random.nextDouble()-0.5)*0.08));
                }
            }
        }
    }

    private void spawnMobs(Location center) {
        long count = center.getWorld().getNearbyEntities(center, 40, 20, 40).stream()
                .filter(e -> e.getPersistentDataContainer().has(riftMobKey, PersistentDataType.BYTE)).count();
        if (count >= 35) return;

        for (int i = 0; i < 4; i++) {
            Location spawn = center.clone().add(random.nextInt(40) - 20, 1, random.nextInt(40) - 20);
            Entity entity;
            if (activeRift.getType() == RiftType.NETHER) {
                entity = (random.nextBoolean()) ? center.getWorld().spawn(spawn, WitherSkeleton.class) : center.getWorld().spawn(spawn, Blaze.class);
            } else {
                entity = (random.nextBoolean()) ? center.getWorld().spawn(spawn, Enderman.class) : center.getWorld().spawn(spawn, Shulker.class);
            }
            if (entity != null) entity.getPersistentDataContainer().set(riftMobKey, PersistentDataType.BYTE, (byte) 1);
        }
    }

    // ================= CONCLUSION & MONUMENT =================

    private void endRift() {
        if (activeRift == null) return;
        Location c = activeRift.getCenter();

        c.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, c, 10, 5, 5, 5, 0.2);
        c.getWorld().playSound(c, Sound.ENTITY_GENERIC_EXPLODE, 10f, 0.1f);

        // Leave the permanent Obsidian Scar
        createPermanentScar(c);

        UUID topUUID = activeRift.getContributions().entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse(null);

        String winnerName = "The Brave";
        if (topUUID != null) {
            Player p = Bukkit.getPlayer(topUUID);
            if (p != null) {
                winnerName = p.getName();
                p.getInventory().addItem(ToolShard.create(8));
            }
        }

        spawnMonument(c, winnerName, activeRift.getType());
        Bukkit.broadcastMessage("§b§l» §fThe Rift has imploded. A monument stands at §e" + c.getBlockX() + ", " + c.getBlockZ());
        cleanupRift();
    }

    private void spawnMonument(Location loc, String winnerName, RiftType type) {
        TextDisplay display = (TextDisplay) loc.getWorld().spawnEntity(loc.clone().add(0, 3, 0), EntityType.TEXT_DISPLAY);
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        display.setText("§6§l— DIMENSIONAL SCAR —\n" +
                "§7Date: §f" + date + "\n" +
                "§7Champion: §b" + winnerName);
        display.setBillboard(Display.Billboard.CENTER);
        display.setBackgroundColor(Color.fromARGB(150, 0, 0, 0));
    }

    private void createPermanentScar(Location center) {
        int radius = 10;
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (Math.sqrt(x*x + z*z) < radius && random.nextDouble() > 0.4) {
                    center.clone().add(x, -1, z).getBlock().setType(random.nextBoolean() ? Material.OBSIDIAN : Material.CRYING_OBSIDIAN);
                }
            }
        }
    }

    // ================= UTILS =================

    private void cleanupRift() {
        if (riftBar != null) { riftBar.removeAll(); riftBar = null; }
        if (activeRift != null) {
            Location center = activeRift.getCenter();
            center.getWorld().getNearbyEntities(center, 80, 80, 80).stream()
                    .filter(e -> e.getPersistentDataContainer().has(riftMobKey, PersistentDataType.BYTE))
                    .forEach(Entity::remove);
            activeRift.close();
            activeRift = null;
        }
    }

    private void updateBossBar(Location center, int current, int max) {
        if (riftBar == null) return;
        riftBar.setProgress(Math.max(0.0, 1.0 - ((double) current / max)));
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getWorld().equals(center.getWorld()) && p.getLocation().distance(center) < 80) riftBar.addPlayer(p);
            else riftBar.removePlayer(p);
        }
    }

    private void announce(Location loc, RiftType type) {
        boolean revealX = random.nextBoolean();
        String coord = revealX ? "X: " + loc.getBlockX() : "Z: " + loc.getBlockZ();
        Bukkit.broadcastMessage("§0§l[§4§l!§0§l] §f§lCATASTROPHIC RIFT §fdetected near §e" + coord);
        for (Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 0.2f);
    }

    private Location getRandomLocation() {
        World world = Bukkit.getWorlds().get(0);
        int x = random.nextInt(6000) - 3000;
        int z = random.nextInt(6000) - 3000;
        return new Location(world, x, world.getHighestBlockYAt(x, z) + 1, z);
    }

    public void addContribution(Player player) {
        if (activeRift != null) {
            UUID uuid = player.getUniqueId();
            // Use getOrDefault so it doesn't crash on the first kill
            int currentKills = activeRift.getContributions().getOrDefault(uuid, 0);
            activeRift.getContributions().put(uuid, currentKills + 1);

            System.out.println("[RIFT] Registered kill for " + player.getName() + ". Total: " + (currentKills + 1));
        }
    }
}