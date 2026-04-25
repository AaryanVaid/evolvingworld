package me.aaryan.evolvingworld.ability.impl;

import me.aaryan.evolvingworld.ability.Ability;
import me.aaryan.evolvingworld.vfx.AbilityVFX;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class WindBurst extends Ability {

    public WindBurst() {
        super("Wind Burst", 6000);
    }

    public void use(Player p) {
        Vector v = p.getLocation().getDirection().normalize().multiply(1.2).setY(1.2);
        p.setVelocity(v);
        AbilityVFX.playWindBurst(p);
    }
}