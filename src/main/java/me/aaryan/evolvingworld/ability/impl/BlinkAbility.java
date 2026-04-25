package me.aaryan.evolvingworld.ability.impl;

import me.aaryan.evolvingworld.ability.Ability;
import me.aaryan.evolvingworld.vfx.AbilityVFX;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class BlinkAbility extends Ability {

    public BlinkAbility() {
        super("Blink Dagger", 10000);
    }

    @Override
    public void use(Player p) {

        AbilityVFX.playBlink(p);

        Vector dir = p.getLocation().getDirection().normalize().multiply(15);
        p.teleport(p.getLocation().add(dir));

        for (Entity e : p.getNearbyEntities(10, 10, 10)) {
            if (e instanceof Player target && target != p) {
                target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 1));
            }
        }

        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 30, 1));
    }
}