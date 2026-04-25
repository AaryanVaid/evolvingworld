package me.aaryan.evolvingworld.ability;

import org.bukkit.entity.Player;

public abstract class Ability {

    private final String name;
    private final long cooldown;

    public Ability(String name, long cooldown) {
        this.name = name;
        this.cooldown = cooldown;
    }

    public String getName() {
        return name;
    }

    public long getCooldown() {
        return cooldown;
    }

    public abstract void use(Player player);
}