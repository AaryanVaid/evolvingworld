package me.aaryan.evolvingworld.ability.impl;

import me.aaryan.evolvingworld.ability.Ability;
import me.aaryan.evolvingworld.vfx.AbilityVFX;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DomainSeedAbility extends Ability {

    private final Set<Location> veilBlocks = new HashSet<>();

    public DomainSeedAbility() {
        super("Domain Seed", 25000);
    }

    @Override
    public void use(Player caster) {

        Location center = caster.getLocation().clone();

        caster.sendMessage("§5Domain Seed planted...");
        AbilityVFX.playDomainPlant(center);

        new BukkitRunnable() {

            int t = 0;

            @Override
            public void run() {

                if (!caster.isOnline()) {
                    cancel();
                    return;
                }

                t += 5;

                center.getWorld().spawnParticle(Particle.PORTAL, center, 30, 0.5, 0.5, 0.5, 0.1);

                if (t >= 60) {
                    cancel();
                    expandDomain(caster, center);
                }

            }

        }.runTaskTimer(Bukkit.getPluginManager().getPlugin("EvolvingWorld"), 0, 5);
    }

    // ================= DOMAIN =================

    private void expandDomain(Player caster, Location center) {

        final double radius = 20;

        caster.sendMessage("§d§lDomain Expanded.");
        AbilityVFX.playDomainExpand(center);

        // LOCK PLAYERS
        Set<UUID> lockedInside = new HashSet<>();

        for (Player p : center.getWorld().getPlayers()) {
            if (p.getLocation().distance(center) <= radius) {
                lockedInside.add(p.getUniqueId());
            }
        }

        // 🟣 CREATE REAL VEIL
        createVeil(center, radius);

        new BukkitRunnable() {

            int ticks = 0;

            @Override
            public void run() {

                if (!caster.isOnline()) {
                    cleanup();
                    cancel();
                    return;
                }

                ticks++;

                // BUFF CASTER
                caster.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 40, 1));
                caster.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, 1));
                caster.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 40, 0));

                for (Player p : center.getWorld().getPlayers()) {

                    double dist = p.getLocation().distance(center);

                    boolean inside = dist <= radius;

                    // 🚫 EXIT BLOCK
                    if (lockedInside.contains(p.getUniqueId()) && dist > radius) {

                        Vector pull = center.toVector()
                                .subtract(p.getLocation().toVector())
                                .normalize()
                                .multiply(1.7);

                        p.setVelocity(pull);
                    }

                    // 🚫 ENTRY BLOCK
                    if (!lockedInside.contains(p.getUniqueId()) && inside) {

                        Vector push = p.getLocation().toVector()
                                .subtract(center.toVector())
                                .normalize()
                                .multiply(1.7);

                        p.setVelocity(push);
                    }

                    if (p.equals(caster)) continue;

                    if (lockedInside.contains(p.getUniqueId())) {

                        // cinematic blindness flicker
                        if (ticks % 10 < 5) {
                            p.addPotionEffect(new PotionEffect(
                                    PotionEffectType.BLINDNESS,
                                    10,
                                    0,
                                    false,
                                    false
                            ));
                        }

                        p.addPotionEffect(new PotionEffect(
                                PotionEffectType.DARKNESS,
                                40,
                                0,
                                false,
                                false
                        ));
                    }
                }

                AbilityVFX.playDomainField(center);

                if (ticks >= 200) {
                    cleanup();
                    AbilityVFX.playDomainCollapse(center);
                    caster.sendMessage("§7Domain collapsed.");
                    cancel();
                }
            }

            private void cleanup() {
                removeVeil();
            }

        }.runTaskTimer(Bukkit.getPluginManager().getPlugin("EvolvingWorld"), 0, 1);
    }

    // ================= VEIL SYSTEM =================

    private void createVeil(Location center, double radius) {

        World world = center.getWorld();

        for (double theta = 0; theta < Math.PI; theta += Math.PI / 35) {
            for (double phi = 0; phi < 2 * Math.PI; phi += Math.PI / 35) {

                double x = radius * Math.sin(theta) * Math.cos(phi);
                double y = radius * Math.cos(theta);
                double z = radius * Math.sin(theta) * Math.sin(phi);

                Location loc = center.clone().add(x, y, z).getBlock().getLocation();

                if (loc.getBlock().getType().isAir()) {
                    loc.getBlock().setType(Material.BLACK_CONCRETE);
                    veilBlocks.add(loc);
                }
            }
        }
    }

    private void removeVeil() {

        for (Location loc : veilBlocks) {
            if (loc.getBlock().getType() == Material.BLACK_CONCRETE) {
                loc.getBlock().setType(Material.AIR);
            }
        }

        veilBlocks.clear();
    }
}