package me.aaryan.evolvingworld.listeners;

import me.aaryan.evolvingworld.EvolvingWorld;
import me.aaryan.evolvingworld.phase.Phase;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.generator.structure.Structure;
import org.bukkit.util.StructureSearchResult;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StructureLockListener implements Listener {

    private final EvolvingWorld plugin;

    private static final Map<Structure, Phase> STRUCTURE_PHASE_MAP = new HashMap<>();
    static {
        STRUCTURE_PHASE_MAP.put(Structure.VILLAGE_PLAINS, Phase.PHASE_1);
        STRUCTURE_PHASE_MAP.put(Structure.MINESHAFT, Phase.PHASE_1);
        STRUCTURE_PHASE_MAP.put(Structure.ANCIENT_CITY, Phase.PHASE_2);
        STRUCTURE_PHASE_MAP.put(Structure.STRONGHOLD, Phase.PHASE_3);
        STRUCTURE_PHASE_MAP.put(Structure.BASTION_REMNANT, Phase.PHASE_3);
        STRUCTURE_PHASE_MAP.put(Structure.FORTRESS, Phase.PHASE_3);
        STRUCTURE_PHASE_MAP.put(Structure.END_CITY, Phase.PHASE_4);
    }

    private static final Map<Structure, Integer> STRUCTURE_RADIUS = new HashMap<>();
    static {
        STRUCTURE_RADIUS.put(Structure.VILLAGE_PLAINS, 10);
        STRUCTURE_RADIUS.put(Structure.MINESHAFT, 5);
        STRUCTURE_RADIUS.put(Structure.ANCIENT_CITY, 10);
        STRUCTURE_RADIUS.put(Structure.STRONGHOLD, 50);
        STRUCTURE_RADIUS.put(Structure.BASTION_REMNANT, 30);
        STRUCTURE_RADIUS.put(Structure.FORTRESS, 30);
        STRUCTURE_RADIUS.put(Structure.END_CITY, 20);
    }

    private final Map<UUID, Location> lastChecked = new HashMap<>();

    public StructureLockListener(EvolvingWorld plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();

        if (to == null) return;

        // Ignore tiny movements
        if (from.getBlockX() == to.getBlockX() &&
                from.getBlockZ() == to.getBlockZ()) return;

        // Distance cooldown (WORLD SAFE)
        Location lastLoc = lastChecked.get(player.getUniqueId());
        if (lastLoc != null) {
            if (!lastLoc.getWorld().equals(to.getWorld())) {
                lastChecked.put(player.getUniqueId(), to);
            } else {
                if (lastLoc.distanceSquared(to) < 25) return;
                lastChecked.put(player.getUniqueId(), to);
            }
        } else {
            lastChecked.put(player.getUniqueId(), to);
        }

        Phase worldPhase = plugin.getPhaseManager().getCurrentPhase();
        Phase playerPhase = plugin.getPlayerPhaseManager().getPlayerPhase(player);

        for (Map.Entry<Structure, Phase> entry : STRUCTURE_PHASE_MAP.entrySet()) {
            Structure structure = entry.getKey();
            Phase requiredPhase = entry.getValue();

            // Skip if already unlocked
            if (playerPhase.getLevel() >= requiredPhase.getLevel() &&
                    worldPhase.getLevel() >= requiredPhase.getLevel())
                continue;

            StructureSearchResult result =
                    to.getWorld().locateNearestStructure(to, structure, 100, false);

            if (result == null) continue;

            Location structureLoc = result.getLocation();
            int radius = STRUCTURE_RADIUS.getOrDefault(structure, 10);

            if (structureLoc.distance(to) <= radius) {

                // Push player just outside the structure radius
                double dx = to.getX() - structureLoc.getX();
                double dz = to.getZ() - structureLoc.getZ();
                double angle = Math.atan2(dz, dx);

                double pushX = structureLoc.getX() + Math.cos(angle) * (radius + 1);
                double pushZ = structureLoc.getZ() + Math.sin(angle) * (radius + 1);

                Location safeLoc = new Location(
                        to.getWorld(),
                        pushX,
                        to.getY(),
                        pushZ,
                        to.getYaw(),
                        to.getPitch()
                );

                player.sendMessage("§cThis structure unlocks at §ePhase " + requiredPhase.getLevel());
                player.teleport(safeLoc);
                return;
            }
        }
    }
}
