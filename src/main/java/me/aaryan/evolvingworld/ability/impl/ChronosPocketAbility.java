package me.aaryan.evolvingworld.ability.impl;

import me.aaryan.evolvingworld.ability.Ability;
import me.aaryan.evolvingworld.vfx.AbilityVFX;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChronosPocketAbility extends Ability {

    private final Map<UUID, Location> mark = new HashMap<>();

    public ChronosPocketAbility() {
        super("Chronos Pocket", 45000);
    }

    public void use(Player p) {

        UUID id = p.getUniqueId();

        if (!mark.containsKey(id)) {
            mark.put(id, p.getLocation());
            AbilityVFX.playChronosMark(p);
            return;
        }

        p.teleport(mark.get(id));
        mark.remove(id);

        AbilityVFX.playChronosRewind(p);
    }
}