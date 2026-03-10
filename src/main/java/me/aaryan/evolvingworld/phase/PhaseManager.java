package me.aaryan.evolvingworld.phase;

import me.aaryan.evolvingworld.EvolvingWorld;
import org.bukkit.Bukkit;

public class PhaseManager {

    private final EvolvingWorld plugin;
    private Phase currentPhase;

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
