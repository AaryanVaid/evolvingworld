package me.aaryan.evolvingworld;

import me.aaryan.evolvingworld.abilities.AbilityManager;
import me.aaryan.evolvingworld.aura.AuraManager;
import me.aaryan.evolvingworld.commands.*;
import me.aaryan.evolvingworld.items.PhaseBoosterItem;
import me.aaryan.evolvingworld.items.PhaseBoosterRecipe;
import me.aaryan.evolvingworld.items.RiftShard;
import me.aaryan.evolvingworld.items.ToolShard;
import me.aaryan.evolvingworld.listeners.*;
import me.aaryan.evolvingworld.phase.PhaseManager;
import me.aaryan.evolvingworld.rift.GlobalRiftManager;
import me.aaryan.evolvingworld.rift.RiftManager;
import me.aaryan.evolvingworld.world.WorldPhaseRewardManager;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import me.aaryan.evolvingworld.player.PlayerPhaseManager;
import org.bukkit.Bukkit;

public class EvolvingWorld extends JavaPlugin {
    private PlayerPhaseManager playerPhaseManager;
    private WorldPhaseRewardManager worldPhaseRewardManager;
    private PhaseManager phaseManager;
    private RiftManager riftManager;
    private AbilityManager abilityManager;
    private AuraManager auraManager;
    private GlobalRiftManager globalRiftManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        PhaseBoosterItem.init(this);
        RiftShard.init(this);
        ToolShard.init(this);

        phaseManager = new PhaseManager(this);

        getCommand("phase").setExecutor(new PhaseCommand(this));
        getCommand("phasebooster").setExecutor(
                new GivePhaseBoosterCommand()
        );
        getCommand("phasedebug").setExecutor(
                new PhaseDebugCommand(this)
        );
        getCommand("rift").setExecutor(new RiftCommand(this));
        PhaseBoosterRecipe.registerAll(this);
        getCommand("rifttest").setExecutor(new RiftTestCommand(this));


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
        riftManager = new RiftManager(this);

        Bukkit.getPluginManager().registerEvents(
                new RiftMobDeathListener(this),
                this
        );
        Bukkit.getPluginManager().registerEvents(
                new RiftDamageListener(this),
                this

        );
        abilityManager = new AbilityManager();

        Bukkit.getPluginManager().registerEvents(
                new ShardAbilityListener(this),
                this
        );
        NamespacedKey TOOL_MASTERY_KEY = new NamespacedKey(this, "tool_mastery");

        Bukkit.getPluginManager().registerEvents(new ToolShardListener(TOOL_MASTERY_KEY), this);
        Bukkit.getPluginManager().registerEvents(new SwordBuffListener(TOOL_MASTERY_KEY), this);
        Bukkit.getPluginManager().registerEvents(new AxeBuffListener(TOOL_MASTERY_KEY), this);
        Bukkit.getPluginManager().registerEvents(new PickaxeBuffListener(TOOL_MASTERY_KEY), this);
        Bukkit.getPluginManager().registerEvents(new ArmorBuffListener(TOOL_MASTERY_KEY), this);
        globalRiftManager = new GlobalRiftManager(this);
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            globalRiftManager.trySpawnRift();
        }, 1200L, 1200L);

        getServer().getPluginManager().registerEvents(
                new RiftMobListener(this), this
        );
        auraManager = new AuraManager(TOOL_MASTERY_KEY);
        Bukkit.getScheduler().runTaskTimer(this, () -> {

            for (Player player : Bukkit.getOnlinePlayers()) {
                auraManager.tick(player);
            }

        }, 0L, 10L); // every 0.5 sec

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

    public RiftManager getRiftManager() {
        return riftManager;
    }
    public AbilityManager getAbilityManager() {
        return abilityManager;
    }
    public GlobalRiftManager getGlobalRiftManager() {
        return globalRiftManager;
    }

}
