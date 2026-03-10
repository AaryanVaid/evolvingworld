package me.aaryan.evolvingworld;

import me.aaryan.evolvingworld.commands.GivePhaseBoosterCommand;
import me.aaryan.evolvingworld.commands.PhaseCommand;
import me.aaryan.evolvingworld.commands.PhaseDebugCommand;
import me.aaryan.evolvingworld.items.PhaseBoosterItem;
import me.aaryan.evolvingworld.items.PhaseBoosterRecipe;
import me.aaryan.evolvingworld.listeners.*;
import me.aaryan.evolvingworld.phase.PhaseManager;
import me.aaryan.evolvingworld.world.WorldPhaseRewardManager;
import org.bukkit.plugin.java.JavaPlugin;
import me.aaryan.evolvingworld.player.PlayerPhaseManager;
import org.bukkit.Bukkit;

public class EvolvingWorld extends JavaPlugin {
    private PlayerPhaseManager playerPhaseManager;
    private WorldPhaseRewardManager worldPhaseRewardManager;
    private PhaseManager phaseManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        PhaseBoosterItem.init(this);

        phaseManager = new PhaseManager(this);

        getCommand("phase").setExecutor(new PhaseCommand(this));
        getCommand("phasebooster").setExecutor(
                new GivePhaseBoosterCommand()
        );
        getCommand("phasedebug").setExecutor(
                new PhaseDebugCommand(this)
        );
        PhaseBoosterRecipe.registerAll(this);


        getLogger().info("EvolvingWorld enabled successfully.");

        playerPhaseManager = new PlayerPhaseManager(this);
        worldPhaseRewardManager = new WorldPhaseRewardManager();

        Bukkit.getPluginManager().registerEvents(
                new PlayerJoinListener(this),
                this

        );
        Bukkit.getPluginManager().registerEvents(
                new PhaseBoosterListener(this),
                this
        );
        Bukkit.getPluginManager().registerEvents(
                new BlockRestrictionListener(this),
                this
        );
        Bukkit.getPluginManager().registerEvents(
                new ToolRestrictionListener(this),
                this
        );

        Bukkit.getPluginManager().registerEvents(
                new ArmorRestrictionListener(this),
                this
        );

        Bukkit.getPluginManager().registerEvents(
                new DimensionLockListener(this),
                this
        );
        Bukkit.getPluginManager().registerEvents(
                new StructureLockListener(this),
                this
        );
        getServer().getPluginManager().registerEvents(
                new MobDifficultyListener(this), this
        );

    }
    public PlayerPhaseManager getPlayerPhaseManager() {
        return playerPhaseManager;
    }

    public PhaseManager getPhaseManager() {
        return phaseManager;
    }

    public WorldPhaseRewardManager getWorldPhaseRewardManager() {
        return worldPhaseRewardManager;
    }

}
