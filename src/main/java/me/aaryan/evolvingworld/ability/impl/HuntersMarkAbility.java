package me.aaryan.evolvingworld.ability.impl;

import me.aaryan.evolvingworld.ability.Ability;
import me.aaryan.evolvingworld.vfx.AbilityVFX;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HuntersMarkAbility extends Ability {

    public HuntersMarkAbility() {
        super("Hunter's Mark", 10000);
    }

    @Override
    public void use(Player p) {

        Player target = null;

        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (!pl.equals(p) && pl.getLocation().distance(p.getLocation()) < 15) {
                target = pl;
                break;
            }
        }

        if (target == null) return;

        AbilityVFX.playHunterMark(target);
        target.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 160, 0));
    }
}