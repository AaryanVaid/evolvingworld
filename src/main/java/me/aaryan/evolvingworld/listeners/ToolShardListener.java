package me.aaryan.evolvingworld.listeners;

import me.aaryan.evolvingworld.items.ToolShard;
import me.aaryan.evolvingworld.util.ToolMasteryUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ToolShardListener implements Listener {

    private final NamespacedKey KEY;

    public ToolShardListener(NamespacedKey key) {
        this.KEY = key;
    }

    @EventHandler
    public void onApply(PlayerInteractEvent event) {

        if (!event.getAction().toString().contains("RIGHT_CLICK")) return;

        Player player = event.getPlayer();

        ItemStack shard = player.getInventory().getItemInMainHand();
        ItemStack target = player.getInventory().getItemInOffHand();

        if (!ToolShard.isShard(shard)) return;

        event.setCancelled(true);

        if (target == null || target.getType().isAir()) {
            player.sendMessage("§cHold a tool in your offhand.");
            return;
        }

        // 🔒 CLONE ITEM (VERY IMPORTANT)
        ItemStack newItem = target.clone();

        if (ToolMasteryUtil.isMastered(newItem, KEY)) {
            player.sendMessage("§cThat item is already mastered!");
            return;
        }

        ToolMasteryUtil.applyMastery(newItem, KEY);

        // 🔁 Replace ONLY that slot
        player.getInventory().setItemInOffHand(newItem);

        // consume shard
        if (shard.getAmount() <= 1) {
            player.getInventory().setItemInMainHand(null);
        } else {
            shard.setAmount(shard.getAmount() - 1);
        }

        player.sendMessage("§aItem mastered successfully!");
    }
}