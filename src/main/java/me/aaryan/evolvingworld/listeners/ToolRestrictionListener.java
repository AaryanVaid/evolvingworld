package me.aaryan.evolvingworld.listeners;

import me.aaryan.evolvingworld.EvolvingWorld;
import me.aaryan.evolvingworld.phase.Phase;
import me.aaryan.evolvingworld.restrictions.ToolPhase;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class ToolRestrictionListener implements Listener {

    private final EvolvingWorld plugin;

    public ToolRestrictionListener(EvolvingWorld plugin) {
        this.plugin = plugin;
    }

    private boolean isAllowed(Player player, ItemStack item) {

        if (item == null || item.getType().isAir()) return true;

        Phase required = ToolPhase.requiredPhase(item.getType());
        Phase playerPhase = plugin.getPlayerPhaseManager().getPlayerPhase(player);
        Phase worldPhase = plugin.getPhaseManager().getCurrentPhase();

        return playerPhase.getLevel() >= required.getLevel()
                && worldPhase.getLevel() >= required.getLevel();
    }

    // ❌ Prevent mining with restricted tools
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();

        if (!isAllowed(player, tool)) {
            event.setCancelled(true);
            player.sendMessage("§cYou are not evolved enough to use this tool.");
        }
    }

    // ❌ Prevent PvP & mob combat with restricted weapons
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {

        if (!(event.getDamager() instanceof Player player)) return;

        ItemStack weapon = player.getInventory().getItemInMainHand();

        if (!isAllowed(player, weapon)) {
            event.setCancelled(true);
            player.sendMessage("§cYou are not evolved enough to fight with this.");
        }
    }
}
