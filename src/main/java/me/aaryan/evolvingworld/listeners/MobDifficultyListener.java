package me.aaryan.evolvingworld.listeners;

import me.aaryan.evolvingworld.EvolvingWorld;
import me.aaryan.evolvingworld.phase.Phase;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class MobDifficultyListener implements Listener {

    private final EvolvingWorld plugin;

    public MobDifficultyListener(EvolvingWorld plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMobSpawn(EntitySpawnEvent event) {

        if (!(event.getEntity() instanceof Monster mob)) return;

        Phase worldPhase = plugin.getPhaseManager().getCurrentPhase();
        int level = worldPhase.getLevel();

        if (level <= 1) return; // Phase 1 = vanilla

        // Health scaling
        double healthMultiplier = switch (level) {
            case 2 -> 1.25;
            case 3 -> 1.5;
            case 4 -> 2.0;
            default -> 1.0;
        };

        // Damage scaling
        double damageMultiplier = switch (level) {
            case 2 -> 1.2;
            case 3 -> 1.4;
            case 4 -> 1.75;
            default -> 1.0;
        };

        // Apply health
        if (mob.getAttribute(Attribute.GENERIC_MAX_HEALTH) != null) {
            double baseHealth = mob.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
            mob.getAttribute(Attribute.GENERIC_MAX_HEALTH)
                    .setBaseValue(baseHealth * healthMultiplier);
            mob.setHealth(mob.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
        }

        // Apply damage
        if (mob.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) != null) {
            double baseDamage = mob.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue();
            mob.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)
                    .setBaseValue(baseDamage * damageMultiplier);
        }
    }
}
