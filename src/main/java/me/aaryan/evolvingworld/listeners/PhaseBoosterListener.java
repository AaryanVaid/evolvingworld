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

        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (!PhaseBoosterItem.isPhaseBooster(item)) return;

        event.setCancelled(true);

        int boosterLevel = PhaseBoosterItem.getBoosterLevel(item);

        Phase playerPhase = plugin.getPlayerPhaseManager()
                .getPlayerPhase(player);

        // ❌ Booster must match current phase
        if (boosterLevel != playerPhase.getLevel()) {
            player.sendMessage("§cYou can only use your current phase booster.");
            return;
        }

        // 🚀 Try advancing player
        boolean advanced = plugin.getPlayerPhaseManager()
                .tryAdvancePlayerPhase(player);

        if (!advanced) {
            player.sendMessage("§cYou cannot advance any further.");
            return;
        }

        // ✅ SUCCESS
        Phase newPhase = plugin.getPlayerPhaseManager()
                .getPlayerPhase(player);

        player.sendMessage("§aYou have advanced to §ePhase " +
                newPhase.getLevel() + "§a!");

        // 🔥 ADD CONTRIBUTOR (CRITICAL)
        plugin.getWorldPhaseRewardManager().addContributor(player);

        // 🧪 consume booster safely
        if (item.getAmount() <= 1) {
            player.getInventory().setItemInMainHand(null);
        } else {
            item.setAmount(item.getAmount() - 1);
        }

        // 🌍 check world evolution AFTER contribution is registered
        plugin.getPhaseManager().checkWorldProgress();
    }
}