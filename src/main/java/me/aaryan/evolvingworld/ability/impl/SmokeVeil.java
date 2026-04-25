package me.aaryan.evolvingworld.ability.impl;

import me.aaryan.evolvingworld.ability.Ability;
import me.aaryan.evolvingworld.vfx.AbilityVFX;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SmokeVeil extends Ability {

    public SmokeVeil() {
        super("Smoke Veil", 20000);
    }

    @Override
    public void use(Player p) {

        AbilityVFX.playSmoke(p.getLocation());

    }
}