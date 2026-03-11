package me.aaryan.evolvingworld.listeners;

import me.aaryan.evolvingworld.EvolvingWorld;
import me.aaryan.evolvingworld.items.PhaseBoosterItem;
import me.aaryan.evolvingworld.phase.Phase;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PhaseBoosterListener implements Listener {

    private final EvolvingWorld plugin;

    public PhaseBoosterListener(EvolvingWorld plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onUse(PlayerInteractEvent event) {

        Action action = event.getAction();

        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        ItemStack item = event.getItem();

        if (!PhaseBoosterItem.isPhaseBooster(item)) {
            return;
        }

        event.setCancelled(true);

        Player player = event.getPlayer();

        int boosterLevel = PhaseBoosterItem.getBoosterLevel(item);

        Phase playerPhase = plugin.getPlayerPhaseManager()
                .getPlayerPhase(player);

        // Booster must match player phase
        if (boosterLevel != playerPhase.getLevel()) {

            player.sendMessage("§cYou can only use your current phase booster.");
            return;

        }

        boolean advanced = plugin.getPlayerPhaseManager()
                .tryAdvancePlayerPhase(player);

        if (!advanced) {
            player.sendMessage("§cYou cannot advance any further.");
            return;
        }

        Phase newPhase = plugin.getPlayerPhaseManager()
                .getPlayerPhase(player);

        player.sendMessage("§aYou have advanced to §ePhase " +
                newPhase.getLevel() + "§a!");

        // consume booster safely
        int amount = item.getAmount();

        if (amount <= 1) {
            player.getInventory().remove(item);
        } else {
            item.setAmount(amount - 1);
        }

        // 🔥 IMPORTANT: check world evolution after player advances
        plugin.getPhaseManager().checkWorldProgress();

    }
}