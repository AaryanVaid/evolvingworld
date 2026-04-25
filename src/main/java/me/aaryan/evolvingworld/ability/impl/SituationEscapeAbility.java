package me.aaryan.evolvingworld.ability.impl;

import me.aaryan.evolvingworld.ability.Ability;
import me.aaryan.evolvingworld.vfx.AbilityVFX;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SituationEscapeAbility extends Ability {

    public SituationEscapeAbility() {
        super("Phantom Shift", 25000);;
    }

    public void use(Player p) {
        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 80, 0));
        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 80, 1));
        AbilityVFX.playSituationEscape(p);
    }
}