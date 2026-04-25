package me.aaryan.evolvingworld;

import me.aaryan.evolvingworld.ability.*;
import me.aaryan.evolvingworld.listeners.AbilityLockListener;
import me.aaryan.evolvingworld.ability.impl.BlinkAbility;
import me.aaryan.evolvingworld.ability.impl.DashCore;
import me.aaryan.evolvingworld.ability.impl.SmokeVeil;
import me.aaryan.evolvingworld.ability.impl.WindBurst;
import me.aaryan.evolvingworld.commands.*;
import me.aaryan.evolvingworld.items.PhaseBoosterItem;
import me.aaryan.evolvingworld.items.PhaseBoosterRecipe;
import me.aaryan.evolvingworld.listeners.*;
import me.aaryan.evolvingworld.phase.PhaseManager;
import me.aaryan.evolvingworld.vfx.AbilityVFX;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import me.aaryan.evolvingworld.player.PlayerPhaseManager;
import org.bukkit.Bukkit;
import me.aaryan.evolvingworld.ability.impl.*;
public class EvolvingWorld extends JavaPlugin {
    private PlayerPhaseManager playerPhaseManager;
    private PhaseManager phaseManager;
    private AbilityManager abilityManager;
    private CooldownManager cooldownManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        PhaseBoosterItem.init(this);
        AbilityItemUtil.init(this);
        AbilityVFX.init(this);
        AbilityKeys.init(this);

        phaseManager = new PhaseManager(this);

        getCommand("phase").setExecutor(new PhaseCommand(this));
        getCommand("phasebooster").setExecutor(
                new GivePhaseBoosterCommand()
        );
        getCommand("phasedebug").setExecutor(
                new PhaseDebugCommand(this)
        );
        getCommand("giveability").setExecutor(new GiveAbilityCommand(this));

        getLogger().info("EvolvingWorld enabled successfully.");

        playerPhaseManager = new PlayerPhaseManager(this);

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
        abilityManager = new AbilityManager();
        cooldownManager = new CooldownManager();

        abilityManager.register(new DashCore());
        abilityManager.register(new BlinkAbility());
        abilityManager.register(new WindBurst());
        abilityManager.register(new SmokeVeil());
        abilityManager.register(new BridgeBuilderAbility());
        abilityManager.register(new SituationEscapeAbility());
        abilityManager.register(new HuntersMarkAbility());

        abilityManager.register(new BloodPactAbility());
        abilityManager.register(new VoidSnareAbility());
        abilityManager.register(new ShockwavePlateAbility());
        abilityManager.register(new SoulSiphonAbility());

        abilityManager.register(new GravityWellAbility());
        abilityManager.register(new ChronosPocketAbility());
        abilityManager.register(new DomainSeedAbility());

        Bukkit.getPluginManager().registerEvents(new AbilityUseListener(this), this);
        Bukkit.getPluginManager().registerEvents(new AbilityLockListener(), this);
        Bukkit.getPluginManager().registerEvents(new AbilityDeathListener(), this);
        AbilityMasterRecipe.register(this);
        Bukkit.getPluginManager().registerEvents(new AbilityMasterListener(), this);
        Bukkit.getPluginManager().registerEvents(new AbilityMasterCraftListener(), this);
        getServer().getPluginManager().registerEvents(
                new AbilityDropBlockListener(),
                this
        );

        getServer().getPluginManager().registerEvents(
                new AbilityLockListener(),
                this
        );
    }

    public PlayerPhaseManager getPlayerPhaseManager() {
        return playerPhaseManager;
    }

    public PhaseManager getPhaseManager() {
        return phaseManager;
    }
    public AbilityManager getAbilityManager() {
        return abilityManager;
    }

    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }

}
