package me.aaryan.evolvingworld.ability.impl;

import me.aaryan.evolvingworld.ability.Ability;
import me.aaryan.evolvingworld.vfx.AbilityVFX;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class DashCore extends Ability {

    public DashCore() {
        super("Dash Core", 4000);
    }

    @Override
    public void use(Player p) {

        Vector dir = p.getLocation().getDirection().normalize();

        // 🔥 FIX: horizontal only, no insane lift
        dir.setY(0);

        p.setVelocity(dir.multiply(0.8)); // balanced dash strength

        AbilityVFX.playDash(p);
    }
}
