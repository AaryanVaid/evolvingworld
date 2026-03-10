package me.aaryan.evolvingworld.commands;

import me.aaryan.evolvingworld.items.PhaseBoosterItem;
import me.aaryan.evolvingworld.phase.Phase;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GivePhaseBoosterCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        // Check if player is an operator
        if (!player.isOp()) {
            player.sendMessage("§cYou must be an operator to use this command.");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage("§cUsage: /phasebooster <phase>");
            return true;
        }

        int level;
        try {
            level = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            player.sendMessage("§cInvalid phase number.");
            return true;
        }

        Phase phase = Phase.fromLevel(level);
        if (phase == null) {
            player.sendMessage("§cInvalid phase.");
            return true;
        }

        player.getInventory().addItem(PhaseBoosterItem.create(phase));
        player.sendMessage("§aYou received a Phase " + level + " Booster.");

        return true;
    }
}
