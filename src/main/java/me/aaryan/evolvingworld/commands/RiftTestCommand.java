package me.aaryan.evolvingworld.commands;

import me.aaryan.evolvingworld.EvolvingWorld;
import me.aaryan.evolvingworld.rift.RiftType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RiftTestCommand implements CommandExecutor {

    private final EvolvingWorld plugin;

    public RiftTestCommand(EvolvingWorld plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("evolvingworld.admin")) return true;

        RiftType type = null;

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("nether")) {
                type = RiftType.NETHER;
            } else if (args[0].equalsIgnoreCase("end")) {
                type = RiftType.END;
            }
        }

        // If no valid type was typed, pick random
        if (type == null) {
            type = Math.random() > 0.5 ? RiftType.NETHER : RiftType.END;
        }

        sender.sendMessage("§eForce-spawning " + type.name() + " Rift...");
        plugin.getGlobalRiftManager().forceSpawn(type); // Pass the type here

        return true;
    }

}