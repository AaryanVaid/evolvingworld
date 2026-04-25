package me.aaryan.evolvingworld.commands;

import me.aaryan.evolvingworld.EvolvingWorld;
import me.aaryan.evolvingworld.ability.*;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class GiveAbilityCommand implements CommandExecutor {

    private final EvolvingWorld plugin;

    public GiveAbilityCommand(EvolvingWorld plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player p)) return true;

        if (!p.isOp()) {
            p.sendMessage("§cNo permission.");
            return true;
        }

        if (args.length == 0) return false;

        String abilityName = String.join(" ", args);

        Ability ability = plugin.getAbilityManager().get(abilityName);
        if (ability == null) {
            p.sendMessage("§cAbility not found.");
            return true;
        }

        AbilityHotbarManager.giveAbility(
                p,
                AbilityItemUtil.createAbilityItem(ability.getName())
        );

        return true;
    }
}