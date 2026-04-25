package me.aaryan.evolvingworld.ability.impl;

import me.aaryan.evolvingworld.ability.Ability;
import me.aaryan.evolvingworld.vfx.AbilityVFX;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class GravityWellAbility extends Ability {

    public GravityWellAbility() {
        super("Gravity Well", 28000);
    }

    @Override
    public void use(Player p) {

        Location loc = p.getLocation();

        for (Entity e : p.getNearbyEntities(10,10,10)) {
            if (e instanceof Player target && target != p) {

                Vector pull = loc.toVector().subtract(target.getLocation().toVector()).normalize();
                target.setVelocity(pull.multiply(0.8));

                target.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 80, 0));
            }
        }

        AbilityVFX.playGravityWell(loc);
    }
}