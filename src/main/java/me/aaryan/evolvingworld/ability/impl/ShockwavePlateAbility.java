package me.aaryan.evolvingworld.ability.impl;

import me.aaryan.evolvingworld.ability.Ability;
import me.aaryan.evolvingworld.vfx.AbilityVFX;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ShockwavePlateAbility extends Ability {

    public ShockwavePlateAbility() {
        super("Shockwave Plate", 14000);
    }

    @Override
    public void use(Player p) {

        Location loc = p.getLocation();

        for (Entity e : p.getNearbyEntities(6,6,6)) {
            if (e instanceof Player target) {

                Vector knock = target.getLocation().toVector().subtract(loc.toVector()).normalize();
                target.setVelocity(knock.multiply(1.8));
            }
        }

        for (int i = 0; i < 3; i++) {
            Location strike = loc.clone().add(
                    Math.random() * 6 - 3,
                    0,
                    Math.random() * 6 - 3
            );
            loc.getWorld().strikeLightningEffect(strike);
        }

        AbilityVFX.playShockwave(loc);
    }
}