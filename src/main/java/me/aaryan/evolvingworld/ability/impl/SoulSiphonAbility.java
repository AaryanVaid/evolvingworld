package me.aaryan.evolvingworld.ability.impl;

import me.aaryan.evolvingworld.ability.Ability;
import me.aaryan.evolvingworld.vfx.AbilityVFX;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class SoulSiphonAbility extends Ability {

    public SoulSiphonAbility() {
        super("Soul Siphon", 18000);
    }

    public void use(Player p) {

        for (Entity e : p.getNearbyEntities(10,10,10)) {
            if (e instanceof Player target) {
                target.damage(4);
            }
        }

        p.setHealth(Math.min(p.getHealth() + 4, 20));
        AbilityVFX.playSoulSiphon(p.getLocation());
    }
}