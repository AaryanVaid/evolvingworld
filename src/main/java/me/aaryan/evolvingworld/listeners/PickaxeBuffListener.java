package me.aaryan.evolvingworld.listeners;

import me.aaryan.evolvingworld.util.ToolMasteryUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class PickaxeBuffListener implements Listener {

    private final NamespacedKey KEY;

    public PickaxeBuffListener(NamespacedKey key) {
        this.KEY = key;
    }

    @EventHandler
    public void onMine(BlockBreakEvent event) {

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item == null) return;
        if (!item.getType().toString().contains("PICKAXE")) return;

        if (!ToolMasteryUtil.isMastered(item, KEY)) return;

        Block block = event.getBlock();

        // ⛏️ extra XP
        player.giveExp(3);

        // 💎 25% chance double drop
        if (Math.random() < 0.25) {
            block.getWorld().dropItemNaturally(
                    block.getLocation(),
                    new ItemStack(block.getType())
            );
        }
    }
}