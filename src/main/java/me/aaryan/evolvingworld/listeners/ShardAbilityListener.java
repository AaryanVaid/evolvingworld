package me.aaryan.evolvingworld.listeners;

import me.aaryan.evolvingworld.EvolvingWorld;
import me.aaryan.evolvingworld.items.RiftShard;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.block.Action;

public class ShardAbilityListener implements Listener {

    private final EvolvingWorld plugin;

    public ShardAbilityListener(EvolvingWorld plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onUse(PlayerInteractEvent event) {

        if (event.getAction() != Action.RIGHT_CLICK_AIR &&
                event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (!RiftShard.isShard(item)) return;

        event.setCancelled(true);

        var type = RiftShard.getType(item);

        boolean used = plugin.getAbilityManager()
                .tryUseAbility(player, type);

        if (used) {
            item.setAmount(item.getAmount() - 1); // consume shard
        }
    }
}