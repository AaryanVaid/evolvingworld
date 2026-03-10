package me.aaryan.evolvingworld.listeners;

import me.aaryan.evolvingworld.EvolvingWorld;
import me.aaryan.evolvingworld.phase.Phase;
import me.aaryan.evolvingworld.restrictions.BlockPhase;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockRestrictionListener implements Listener {

    private final EvolvingWorld plugin;

    public BlockRestrictionListener(EvolvingWorld plugin) {
        this.plugin = plugin;
    }

    private boolean isAllowed(Phase world, Phase player, Phase required) {
        return world.getLevel() >= required.getLevel()
                && player.getLevel() >= required.getLevel();
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {

        Phase worldPhase = plugin.getPhaseManager().getCurrentPhase();
        Phase playerPhase = plugin.getPlayerPhaseManager()
                .getPlayerPhase(event.getPlayer());
        Phase required = BlockPhase.requiredPhase(event.getBlock().getType());

        if (!isAllowed(worldPhase, playerPhase, required)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(
                    "§cThis block requires §ePhase " + required.getLevel()
                            + " §c(Your phase: " + playerPhase.getLevel() + ")"
            );
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {

        Phase worldPhase = plugin.getPhaseManager().getCurrentPhase();
        Phase playerPhase = plugin.getPlayerPhaseManager()
                .getPlayerPhase(event.getPlayer());
        Phase required = BlockPhase.requiredPhase(event.getBlock().getType());

        if (!isAllowed(worldPhase, playerPhase, required)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(
                    "§cYou cannot place this block until §ePhase " + required.getLevel()
            );
        }
    }
}
