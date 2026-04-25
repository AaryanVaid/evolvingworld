package me.aaryan.evolvingworld.vfx;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class AbilityVFX {

    private static Plugin plugin;

    public static void init(Plugin pl) {
        plugin = pl;
    }

    // ================= COMMON HELPERS =================

    private static boolean isInvalid(Player p) {
        return p == null || !p.isOnline();
    }

    // ================= COMMON =================

    public static void playDash(Player p) {
        Location loc = p.getLocation();
        loc.getWorld().spawnParticle(Particle.SWEEP_ATTACK, loc, 6, 0.2, 0.2, 0.2, 0.1);
        loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 0.7f, 1.4f);
    }

    public static void playWindBurst(Player p) {
        Location loc = p.getLocation();
        loc.getWorld().spawnParticle(Particle.CLOUD, loc, 20, 0.5, 0.5, 0.5, 0.1);
        loc.getWorld().playSound(loc, Sound.ENTITY_PHANTOM_FLAP, 0.8f, 1.2f);
    }

    // NEW Smoke (used by ability now)
    public static void playSmoke(Location loc) {
        new BukkitRunnable() {
            int t = 0;

            public void run() {
                if (t++ > 20) {
                    cancel();
                    return;
                }

                loc.getWorld().spawnParticle(
                        Particle.CLOUD,
                        loc,
                        100,             // Particle count
                        3.0, 2.0, 3.0,  // Increased offsets (X, Y, Z)
                        0.02            // Speed
                );
            }
        }.runTaskTimer(plugin, 0, 2);
    }

    // OLD (kept if anything else uses it)
    public static void playSmoke(Player p) {
        if (isInvalid(p)) return;
        playSmoke(p.getLocation());
    }

    // ================= RARE =================

    public static void playBridgeBuilder(Location loc) {
        loc.getWorld().spawnParticle(Particle.END_ROD, loc, 5, 0.2, 0.1, 0.2, 0.01);
    }

    public static void playSituationEscape(Player p) {
        Location loc = p.getLocation();
        loc.getWorld().spawnParticle(Particle.WHITE_ASH, loc, 40, 0.5, 1, 0.5, 0.1);
        loc.getWorld().playSound(loc, Sound.ENTITY_ENDERMAN_TELEPORT, 0.6f, 1.5f);
    }

    public static void playHunterMark(Player target) {
        if (isInvalid(target)) return;

        Location loc = target.getLocation();
        loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, loc, 40, 0.5, 1, 0.5, 0.05);

        new BukkitRunnable() {
            int t = 0;

            public void run() {
                if (isInvalid(target) || t++ > 20) {
                    cancel();
                    return;
                }

                target.getWorld().spawnParticle(
                        Particle.SMOKE,
                        target.getLocation(),
                        10,
                        0.3, 0.5, 0.3,
                        0.02
                );
            }
        }.runTaskTimer(plugin, 0, 2);
    }

    // ================= LEGENDARY =================

    public static void playBloodPact(Location loc) {
        loc.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR, loc, 10);

        Particle.DustOptions dust = new Particle.DustOptions(Color.RED, 1.0f);
        loc.getWorld().spawnParticle(Particle.DUST, loc, 30, 0.5, 1, 0.5, dust);

        loc.getWorld().playSound(loc, Sound.ENTITY_WITHER_HURT, 0.7f, 1f);
    }

    public static void playVoidSnare(Location loc) {
        loc.getWorld().spawnParticle(Particle.PORTAL, loc, 80, 1.2, 0.5, 1.2, 0.1);
        loc.getWorld().spawnParticle(Particle.SMOKE, loc, 60, 1, 0.3, 1, 0.05);
    }

    public static void playShockwave(Location loc) {
        loc.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, loc, 2);
        loc.getWorld().spawnParticle(Particle.CLOUD, loc, 60, 1.5, 0.3, 1.5, 0.2);
        loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f);
    }

    public static void playSoulSiphon(Location loc) {
        loc.getWorld().spawnParticle(Particle.SOUL, loc, 40, 0.8, 1, 0.8, 0.05);
        loc.getWorld().spawnParticle(Particle.DRAGON_BREATH, loc, 20, 0.5, 0.5, 0.5, 0.02);
    }

    // ================= MYTHIC =================

    public static void playBlink(Player p) {
        if (isInvalid(p)) return;
        Location loc = p.getLocation();

        loc.getWorld().spawnParticle(Particle.PORTAL, loc, 60, 0.5, 1, 0.5, 0.2);
        loc.getWorld().playSound(loc, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1.3f);

        new BukkitRunnable() {
            int t = 0;

            public void run() {
                if (isInvalid(p) || t++ > 5) {
                    cancel();
                    return;
                }

                p.getWorld().spawnParticle(
                        Particle.REVERSE_PORTAL,
                        p.getLocation(),
                        20,
                        0.3, 0.5, 0.3,
                        0.05
                );
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    public static void playGravityWell(Location loc) {
        loc.getWorld().spawnParticle(Particle.PORTAL, loc, 120, 1.8, 0.5, 1.8, 0.1);
        loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, loc, 80, 1.5, 0.5, 1.5, 0.05);
    }

    public static void playChronosMark(Player p) {
        Location loc = p.getLocation();

        Particle.DustTransition transition =
                new Particle.DustTransition(Color.fromRGB(0, 255, 255), Color.WHITE, 1.0f);

        loc.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, loc, 40, 0.5, 1, 0.5, transition);
        loc.getWorld().playSound(loc, Sound.BLOCK_BEACON_ACTIVATE, 0.6f, 1.8f);
    }

    public static void playChronosRewind(Player p) {
        Location loc = p.getLocation();

        Particle.DustOptions dust = new Particle.DustOptions(Color.AQUA, 1.2f);
        loc.getWorld().spawnParticle(Particle.DUST, loc, 80, 0.5, 1, 0.5, dust);

        loc.getWorld().playSound(loc, Sound.BLOCK_BEACON_DEACTIVATE, 1f, 0.8f);
    }

    // ================= DOMAIN SYSTEM =================

    public static void playDomainPlant(Location loc) {
        loc.getWorld().spawnParticle(Particle.PORTAL, loc, 40, 0.5, 0.2, 0.5, 0.1);
        loc.getWorld().spawnParticle(Particle.WITCH, loc, 20, 0.3, 0.2, 0.3, 0.05);
        loc.getWorld().playSound(loc, Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, 0.8f, 1.2f);
    }

    public static void playDomainExpand(Location loc) {

        loc.getWorld().spawnParticle(Particle.EXPLOSION, loc, 3);

        for (int i = 0; i < 360; i += 10) {
            double radians = Math.toRadians(i);
            double x = Math.cos(radians) * 10;
            double z = Math.sin(radians) * 10;

            Location ring = loc.clone().add(x, 0.2, z);
            loc.getWorld().spawnParticle(Particle.PORTAL, ring, 5, 0, 0, 0, 0);
        }

        loc.getWorld().playSound(loc, Sound.ITEM_TOTEM_USE, 1f, 0.7f);
    }

    public static void playDomainField(Location loc) {

        for (int i = 0; i < 40; i++) {
            double x = (Math.random() - 0.5) * 20;
            double y = Math.random() * 3;
            double z = (Math.random() - 0.5) * 20;

            loc.getWorld().spawnParticle(
                    Particle.PORTAL,
                    loc.clone().add(x, y, z),
                    0
            );
        }

        loc.getWorld().spawnParticle(Particle.SMOKE, loc, 10, 2, 0.5, 2, 0.02);
        loc.getWorld().playSound(loc, Sound.BLOCK_PORTAL_AMBIENT, 0.2f, 1.2f);
    }

    public static void playDomainCollapse(Location loc) {

        loc.getWorld().spawnParticle(Particle.REVERSE_PORTAL, loc, 120, 1.5, 1, 1.5, 0.1);
        loc.getWorld().spawnParticle(Particle.SMOKE, loc, 80, 1.2, 0.5, 1.2, 0.05);

        loc.getWorld().playSound(loc, Sound.BLOCK_BEACON_DEACTIVATE, 1f, 0.5f);
    }
}