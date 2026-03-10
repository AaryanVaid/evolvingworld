package me.aaryan.evolvingworld.commands;

import me.aaryan.evolvingworld.EvolvingWorld;
import me.aaryan.evolvingworld.phase.Phase;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PhaseDebugCommand implements CommandExecutor {

    private final EvolvingWorld plugin;

    public PhaseDebugCommand(EvolvingWorld plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Phase worldPhase = plugin.getPhaseManager().getCurrentPhase();
        int worldLevel = worldPhase.getLevel();

        int totalPlayers = Bukkit.getOnlinePlayers().size();

        int readyPlayers = 0;

        for (Player player : Bukkit.getOnlinePlayers()) {

            Phase playerPhase = plugin.getPlayerPhaseManager().getPlayerPhase(player);

            if (playerPhase.getLevel() >= worldLevel + 1) {
                readyPlayers++;
            }
        }

        double percent = totalPlayers == 0 ? 0 :
                (readyPlayers * 100.0) / totalPlayers;

        sender.sendMessage("§6========== EMC PHASE DEBUG ==========");
        sender.sendMessage("§eWorld Phase: §f" + worldLevel);
        sender.sendMessage("§ePlayers Online: §f" + totalPlayers);
        sender.sendMessage("§ePlayers Ready: §f" + readyPlayers);
        sender.sendMessage("§eProgress: §f" + String.format("%.1f", percent) + "%");
        sender.sendMessage(" ");

        sender.sendMessage("§7--- Player Phases ---");

        for (Player player : Bukkit.getOnlinePlayers()) {

            Phase playerPhase = plugin.getPlayerPhaseManager().getPlayerPhase(player);

            sender.sendMessage(
                    "§f" + player.getName() +
                            " §7→ Phase §e" +
                            playerPhase.getLevel()
            );
        }

        sender.sendMessage("§6====================================");

        return true;
    }
}