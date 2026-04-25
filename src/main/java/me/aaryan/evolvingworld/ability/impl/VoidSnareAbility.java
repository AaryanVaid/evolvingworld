package me.aaryan.evolvingworld.ability.impl;

import me.aaryan.evolvingworld.ability.Ability;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class VoidSnareAbility extends Ability {

    public VoidSnareAbility() {
        super("Void Snare", 12000);
    }

    @Override
    public void use(Player p) {

        Location loc = p.getLocation();

        for (Player nearby : p.getWorld().getPlayers()) {
            if (nearby != p && nearby.getLocation().distance(loc) < 12) {

                Vector pull = loc.toVector().subtract(nearby.getLocation().toVector()).normalize();
                nearby.setVelocity(pull.multiply(1.5));

                nearby.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 80, 1));
                nearby.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 80, 1));

                nearby.damage(1);
            }
        }
    }
}