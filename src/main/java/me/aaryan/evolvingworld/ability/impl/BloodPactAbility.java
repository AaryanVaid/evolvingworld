package me.aaryan.evolvingworld.ability.impl;

import me.aaryan.evolvingworld.ability.Ability;
import me.aaryan.evolvingworld.vfx.AbilityVFX;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BloodPactAbility extends Ability {

    public BloodPactAbility() {
        super("Blood Pact", 25000);
    }

    public void use(Player p) {

        p.damage(4);

        for (Entity e : p.getNearbyEntities(5,5,5)) {
            if (e instanceof Player target) {
                target.damage(6);
                AbilityVFX.playBloodPact(target.getLocation());
            }
        }

        p.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 160, 0));
    }
}