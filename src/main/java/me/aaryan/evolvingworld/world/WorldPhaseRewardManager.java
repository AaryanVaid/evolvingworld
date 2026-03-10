package me.aaryan.evolvingworld.world;

import me.aaryan.evolvingworld.phase.Phase;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WorldPhaseRewardManager {

    public void giveReward(Player player, Phase phase) {

        switch (phase) {

            case PHASE_2 -> {
                player.getInventory().addItem(
                        new ItemStack(Material.IRON_INGOT, 64)
                );
                player.giveExp(10);
                player.sendMessage("§6You pushed the world to Phase 2!");
            }

            case PHASE_3 -> {
                player.getInventory().addItem(
                        new ItemStack(Material.DIAMOND, 12)
                );
                player.giveExp(20);
                player.sendMessage("§bYou pushed the world to Phase 3!");
            }

            case PHASE_4 -> {
                player.getInventory().addItem(
                        new ItemStack(Material.NETHERITE_SCRAP, 4)
                );
                player.giveExp(30);
                player.sendMessage("§dYou pushed the world to the FINAL phase!");
            }
        }
    }
}
