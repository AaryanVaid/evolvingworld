package me.aaryan.evolvingworld.listeners;

import me.aaryan.evolvingworld.EvolvingWorld;
import me.aaryan.evolvingworld.phase.Phase;
import me.aaryan.evolvingworld.restrictions.DimensionPhase;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class DimensionLockListener implements Listener {

    private final EvolvingWorld plugin;

    public DimensionLockListener(EvolvingWorld plugin) {
        this.plugin = plugin;
    }

    private boolean allowed(Phase world, Phase player, Phase required) {
        return world.getLevel() >= required.getLevel()
                && player.getLevel() >= required.getLevel();
    }

    @EventHandler
    public void onPortal(PlayerPortalEvent event) {

        Phase required = DimensionPhase.required(event.getTo().getWorld());
        Phase worldPhase = plugin.getPhaseManager().getCurrentPhase();
        Phase playerPhase = plugin.getPlayerPhaseManager()
                .getPlayerPhase(event.getPlayer());

        if (!allowed(worldPhase, playerPhase, required)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(
                    "§cThis dimension unlocks at §ePhase " + required.getLevel()
            );
        }
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {

        Phase required = DimensionPhase.required(
                event.getPlayer().getWorld()
        );

        Phase worldPhase = plugin.getPhaseManager().getCurrentPhase();
        Phase playerPhase = plugin.getPlayerPhaseManager()
                .getPlayerPhase(event.getPlayer());

        if (!allowed(worldPhase, playerPhase, required)) {
            event.getPlayer().teleport(
                    plugin.getServer().getWorlds().get(0).getSpawnLocation()
            );
            event.getPlayer().sendMessage(
                    "§cYou are not allowed in this dimension yet."
            );
        }
    }
}
