package me.aaryan.evolvingworld.commands;

import me.aaryan.evolvingworld.EvolvingWorld;
import me.aaryan.evolvingworld.phase.Phase;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PhaseCommand implements CommandExecutor {

    private final EvolvingWorld plugin;

    public PhaseCommand(EvolvingWorld plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        Phase worldPhase = plugin.getPhaseManager().getCurrentPhase();

        if (!(sender instanceof Player player)) {
            sender.sendMessage("World Phase: " + worldPhase.getLevel());
            return true;
        }

        Phase playerPhase = plugin.getPlayerPhaseManager().getPlayerPhase(player);

        player.sendMessage("§6World Phase: §e" + worldPhase.getLevel());
        player.sendMessage("§aYour Phase: §e" + playerPhase.getLevel());

        return true; // ✅ THIS fixes the error
    }
}
