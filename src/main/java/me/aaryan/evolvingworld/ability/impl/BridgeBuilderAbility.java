package me.aaryan.evolvingworld.ability.impl;

import me.aaryan.evolvingworld.ability.Ability;
import me.aaryan.evolvingworld.vfx.AbilityVFX;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import me.aaryan.evolvingworld.EvolvingWorld;
import org.bukkit.util.Vector;

public class BridgeBuilderAbility extends Ability {

    public BridgeBuilderAbility() {
        super("Bridge Builder", 12000);
    }

    public void use(Player p) {

        Location loc = p.getLocation();
        Vector dir = loc.getDirection().setY(0).normalize();

        for (int i = 1; i <= 50; i++) {
            Location place = loc.clone().add(dir.clone().multiply(i));
            place.getBlock().setType(Material.GLASS);
            AbilityVFX.playBridgeBuilder(place);
        }
    }
}