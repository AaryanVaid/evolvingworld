package me.aaryan.evolvingworld.ability;

import java.util.HashMap;

public class AbilityManager {

    private final HashMap<String, Ability> abilities = new HashMap<>();

    public void register(Ability a) {
        abilities.put(a.getName().toLowerCase(), a);
    }

    public Ability get(String name) {
        return abilities.get(name.toLowerCase());
    }

}