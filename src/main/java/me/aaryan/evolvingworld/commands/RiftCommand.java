package me.aaryan.evolvingworld.commands;

import me.aaryan.evolvingworld.EvolvingWorld;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class RiftCommand implements CommandExecutor {

    private final EvolvingWorld plugin;

    public RiftCommand(EvolvingWorld plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player player)) return true;

        if (args.length == 1 && args[0].equalsIgnoreCase("spawn")) {

            plugin.getRiftManager().spawnRift(player.getLocation());
            player.sendMessage("§aRift spawned!");

        }

        return true;
    }
}