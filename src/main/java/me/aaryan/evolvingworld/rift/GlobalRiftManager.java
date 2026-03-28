package me.aaryan.evolvingworld.rift;

import me.aaryan.evolvingworld.EvolvingWorld;
import me.aaryan.evolvingworld.items.ToolShard;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class GlobalRiftManager implements Listener {

    private final EvolvingWorld plugin;
    private final Random random = new Random();
    private final NamespacedKey riftMobKey;

    private GlobalRift activeRift;
    private BossBar riftBar;

    public GlobalRiftManager(EvolvingWorld plugin) {
        this.plugin = plugin;
        this.riftMobKey = new NamespacedKey(plugin, "rift_mob");
        // Crucial: Register events so the global penalties work
        Bukkit.getPluginManager().registerEvents(this, plugin);
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
        // Target 100 kills to close reality
        activeRift = new GlobalRift(loc, type, 100);

        // Initial BossBar setup
        String title = (type == RiftType.NETHER) ? "§4§lCORE BREACH: NETHER" : "§d§lEVENT HORIZON: VOID";
        riftBar = Bukkit.createBossBar(title, BarColor.RED, BarStyle.SEGMENTED_10);
        Bukkit.getOnlinePlayers().forEach(riftBar::addPlayer);

        generateColossalStructureOptimized(loc, type);
        announce(loc, type);
        startGlobalInfectionLoop();

        // Debug Log
        System.out.println("[RIFT] Spawned at " + loc.getBlockX() + " " + loc.getBlockZ() + ". Target: 100 kills.");
    }

    // ================= THE GLOBAL INFECTION LOOP =================

    private void startGlobalInfectionLoop() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (activeRift == null || !activeRift.isActive()) {
                    stopGlobalEffects();
                    this.cancel();
                    return;
                }

                // 1. Update BossBar (Progress = TotalKills / TargetKills)
                double progress = (double) activeRift.getTotalKills() / activeRift.getTargetKills();
                riftBar.setProgress(Math.min(1.0, progress));
                riftBar.setTitle("§4§lRIFT INSTABILITY: §f" + activeRift.getTotalKills() + "§7/§f" + activeRift.getTargetKills() + " Kills");

                // 2. Global Penalties (Applied to everyone regardless of location)
                for (Player p : Bukkit.getOnlinePlayers()) {
                    // Visual: Locked at Night
                    p.setPlayerTime(18000, false);

                    // Physical: Constant Weakness & Hunger
                    p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 0, true, false));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 100, 0, true, false));

                    // Distance-based rumble (louder if near)
                    if (p.getWorld().equals(activeRift.getCenter().getWorld())) {
                        double dist = p.getLocation().distance(activeRift.getCenter());
                        if (dist < 50 && random.nextDouble() < 0.2) {
                            p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 0.5f, 0.2f);
                        }
                    }
                }

                // 3. Local Visuals & Defender Spawning
                tickVisuals(activeRift.getCenter(), activeRift.getType());
                spawnRiftDefenders(activeRift.getCenter());

                // Check for completion
                if (activeRift.getTotalKills() >= activeRift.getTargetKills()) {
                    endRift();
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    // ================= GLOBAL EVENT LISTENERS =================

    @EventHandler
    public void onGlobalLootNerf(EntityDeathEvent event) {
        if (activeRift == null) return;
        // The world is unstable; loot is rare. Halve all drops.
        event.getDrops().forEach(stack -> stack.setAmount(Math.max(1, stack.getAmount() / 2)));
    }

    @EventHandler
    public void onGlobalInfectedSpawn(CreatureSpawnEvent event) {
        if (activeRift == null || event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.NATURAL) return;

        // 35% of all natural spawns worldwide become Rift Mobs
        if (random.nextDouble() < 0.35) {
            Location loc = event.getLocation();
            event.setCancelled(true);

            EntityType type = (activeRift.getType() == RiftType.NETHER) ? EntityType.WITHER_SKELETON : EntityType.ENDERMAN;
            Entity e = loc.getWorld().spawnEntity(loc, type);
            e.getPersistentDataContainer().set(riftMobKey, PersistentDataType.BYTE, (byte) 1);
            e.setCustomName("§c§lDimension Breaker");
        }
    }

    @EventHandler
    public void onRiftMobDeath(EntityDeathEvent event) {
        if (activeRift == null) return;
        if (!event.getEntity().getPersistentDataContainer().has(riftMobKey, PersistentDataType.BYTE)) return;

        Player killer = event.getEntity().getKiller();
        if (killer != null) {
            activeRift.incrementKills();
            UUID id = killer.getUniqueId();
            activeRift.getContributions().put(id, activeRift.getContributions().getOrDefault(id, 0) + 1);

            killer.sendActionBar("§c§lRift Kills: §f" + activeRift.getTotalKills() + "§7/§f" + activeRift.getTargetKills());
        }
    }

    // ================= TERRAFORMING & VISUALS =================

    private void generateColossalStructureOptimized(Location center, RiftType type) {
        int radius = 30;
        Material main = (type == RiftType.NETHER) ? Material.NETHERRACK : Material.END_STONE;
        Material deco = (type == RiftType.NETHER) ? Material.MAGMA_BLOCK : Material.CRYING_OBSIDIAN;

        new BukkitRunnable() {
            int currentR = 0;
            @Override
            public void run() {
                if (currentR > radius) { this.cancel(); return; }
                for (int step = 0; step < 5; step++) {
                    int r = currentR + step;
                    if (r > radius) break;
                    for (int x = -r; x <= r; x++) {
                        for (int z = -r; z <= r; z++) {
                            double dist = Math.sqrt(x*x + z*z);
                            if (dist > r - 1 && dist <= r) {
                                Location l = center.clone().add(x, -1, z);
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

    private void tickVisuals(Location center, RiftType type) {
        World world = center.getWorld();
        if (world == null) return;
        for (double y = 0; y < 100; y += 10) {
            world.spawnParticle(type == RiftType.NETHER ? Particle.FLAME : Particle.WITCH, center.clone().add(0, y, 0), 10, 0.5, 5, 0.5, 0.02);
        }
    }

    private void spawnRiftDefenders(Location center) {
        long count = center.getWorld().getNearbyEntities(center, 40, 20, 40).stream()
                .filter(e -> e.getPersistentDataContainer().has(riftMobKey, PersistentDataType.BYTE)).count();
        if (count >= 30) return;

        for (int i = 0; i < 3; i++) {
            Location spawn = center.clone().add(random.nextInt(30) - 15, 1, random.nextInt(30) - 15);
            EntityType type;
            if (activeRift.getType() == RiftType.NETHER) {
                type = random.nextBoolean() ? EntityType.WITHER_SKELETON : EntityType.BLAZE;
            } else {
                type = random.nextBoolean() ? EntityType.ENDERMAN : EntityType.SHULKER;
            }
            Entity e = center.getWorld().spawnEntity(spawn, type);
            e.getPersistentDataContainer().set(riftMobKey, PersistentDataType.BYTE, (byte) 1);
        }
    }

    // ================= CONCLUSION =================

    private void endRift() {
        if (activeRift == null) return;
        Location c = activeRift.getCenter();

        c.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, c, 15, 5, 5, 5, 0.2);
        c.getWorld().playSound(c, Sound.ENTITY_GENERIC_EXPLODE, 10f, 0.1f);

        createPermanentScar(c);

        UUID topUUID = activeRift.getContributions().entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse(null);

        String winnerName = "The Defenders";
        if (topUUID != null) {
            Player p = Bukkit.getPlayer(topUUID);
            if (p != null) {
                winnerName = p.getName();
                p.getInventory().addItem(ToolShard.create(12));
                p.sendMessage("§6§l[!] §fTop Slayer! Awarded 12 Shards.");
            }
        }

        spawnMonument(c, winnerName);
        Bukkit.broadcastMessage("§b§l» §fThe Rift has imploded. The world stabilizes.");
        stopGlobalEffects();
        cleanupRift();
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

    private void spawnMonument(Location loc, String winnerName) {
        TextDisplay display = (TextDisplay) loc.getWorld().spawnEntity(loc.clone().add(0, 3, 0), EntityType.TEXT_DISPLAY);
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        display.setText("§6§l— DIMENSIONAL SCAR —\n§7Date: §f" + date + "\n§7Champion: §b" + winnerName);
        display.setCustomName("SCAR_MONUMENT");
        display.setBillboard(Display.Billboard.CENTER);
    }

    private void stopGlobalEffects() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.resetPlayerTime();
            p.removePotionEffect(PotionEffectType.WEAKNESS);
            p.removePotionEffect(PotionEffectType.HUNGER);
        }
        if (riftBar != null) {
            riftBar.removeAll();
            riftBar = null;
        }
    }

    private void cleanupRift() {
        if (activeRift != null) {
            activeRift.close();
            activeRift = null;
        }
    }

    private void announce(Location loc, RiftType type) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1f, 1f);
        }

        String coordText = random.nextBoolean()
                ? "X: §e" + loc.getBlockX() + " §7/ Z: §k???"
                : "X: §k??? §7/ Z: §e" + loc.getBlockZ();

        Bukkit.broadcastMessage("§0§l[§4§l!§0§l] §f§lRIFT OPENED §7at " + coordText);
    }

    private Location getRandomLocation() {
        World world = Bukkit.getWorlds().get(0);
        int x = random.nextInt(4000) - 2000;
        int z = random.nextInt(4000) - 2000;
        return new Location(world, x, world.getHighestBlockYAt(x, z) + 1, z);
    }
}