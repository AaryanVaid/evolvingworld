package me.aaryan.evolvingworld.phase;

import me.aaryan.evolvingworld.EvolvingWorld;
import org.bukkit.Bukkit;

public class PhaseManager {

    private final EvolvingWorld plugin;
    private Phase currentPhase;

    private final double REQUIRED_PERCENT = 60.0;

    public PhaseManager(EvolvingWorld plugin) {
        this.plugin = plugin;
        load();
    }

    private void load() {
        int level = plugin.getConfig().getInt("server-phase", 1);
        currentPhase = Phase.fromLevel(level);

    }

    public Phase getCurrentPhase() {
        return currentPhase;
    }

    public void checkWorldProgress() {

        Phase next = currentPhase.next();

        if (next == null) return;

        int totalPlayers = Bukkit.getOnlinePlayers().size();

        if (totalPlayers == 0) return;

        PlayerPhaseManager ppm = plugin.getPlayerPhaseManager();

        int readyPlayers = 0;

        for (Player player : Bukkit.getOnlinePlayers()) {

            int playerPhase = ppm.getPlayerPhase(player).getLevel();

            if (playerPhase >= next.getLevel()) {
                readyPlayers++;
            }

        }

        double percent = (readyPlayers * 100.0) / totalPlayers;

        if (percent >= REQUIRED_PERCENT) {
            advancePhase();
        }

    }

    public void advancePhase() {
        if (currentPhase == Phase.PHASE_4) return;

        currentPhase = currentPhase.next();

        plugin.getConfig().set("server-phase", currentPhase.getLevel());
        plugin.saveConfig();

        Bukkit.broadcastMessage(
                "§6⚡ The world has evolved to §ePhase " + currentPhase.getLevel()
        );
    }

}
