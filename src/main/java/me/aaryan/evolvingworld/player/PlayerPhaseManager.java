package me.aaryan.evolvingworld.player; // Correct package

import me.aaryan.evolvingworld.EvolvingWorld;
import me.aaryan.evolvingworld.phase.Phase;
import org.bukkit.entity.Player;
import java.util.UUID;

public class PlayerPhaseManager {

    private final EvolvingWorld plugin;

    public PlayerPhaseManager(EvolvingWorld plugin) {
        this.plugin = plugin;
    }

    private String path(UUID uuid) {
        return "players." + uuid + ".phase";
    }

    public void ensurePlayer(Player player) {
        String key = path(player.getUniqueId());
        if (!plugin.getConfig().isInt(key)) {
            plugin.getConfig().set(key, 1);
            plugin.saveConfig();
        }
    }

    public Phase getPlayerPhase(Player player) {
        int level = plugin.getConfig().getInt(path(player.getUniqueId()), 1);
        return Phase.fromLevel(level);
    }

    public void setPlayerPhase(Player player, Phase phase) {
        plugin.getConfig().set(path(player.getUniqueId()), phase.getLevel());
        plugin.saveConfig();
    }

    // Renamed this to match your Listener's call!
    public boolean tryAdvancePlayerPhase(Player player) {
        Phase current = getPlayerPhase(player);

        if (current == Phase.PHASE_4) return false;

        Phase next = Phase.fromLevel(current.getLevel() + 1);
        if (next == null) return false;

        setPlayerPhase(player, next);
        return true;
    }
}
