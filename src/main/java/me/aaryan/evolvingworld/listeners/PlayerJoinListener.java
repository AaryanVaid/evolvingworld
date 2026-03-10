package me.aaryan.evolvingworld.listeners;

import me.aaryan.evolvingworld.EvolvingWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final EvolvingWorld plugin;

    public PlayerJoinListener(EvolvingWorld plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        plugin.getPlayerPhaseManager().ensurePlayer(event.getPlayer());
    }
}
