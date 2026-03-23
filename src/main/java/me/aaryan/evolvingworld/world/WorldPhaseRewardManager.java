package me.aaryan.evolvingworld.world;

import me.aaryan.evolvingworld.items.ToolShard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.LinkedHashSet;
import java.util.UUID;

public class WorldPhaseRewardManager {

    private final LinkedHashSet<UUID> contributors = new LinkedHashSet<>();

    // ✅ track contribution (order matters)
    public void addContributor(Player player) {
        contributors.add(player.getUniqueId());
    }

    // ✅ distribute rewards
    public void distributeRewards() {

        int position = 0;

        for (UUID uuid : contributors) {

            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;

            position++;

            int shards = switch (position) {
                case 1 -> 3;
                case 2 -> 2;
                case 3 -> 1;
                default -> 0;
            };

            if (shards > 0) {
                player.getInventory().addItem(
                        ToolShard.create().asQuantity(shards)
                );

                player.sendMessage("§aYou received " + shards + " Mastery Shard(s)!");
            } else {
                player.sendMessage("§7You contributed but didn't get shards.");
            }
        }

        contributors.clear(); // reset for next phase
    }
}