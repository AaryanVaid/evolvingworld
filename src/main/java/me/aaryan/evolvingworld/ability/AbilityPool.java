package me.aaryan.evolvingworld.ability;

import java.util.*;

public class AbilityPool {

    private static final Map<String, List<String>> pool = new HashMap<>();
    private static final Random r = new Random();

    static {

        // COMMON
        pool.put("COMMON", Arrays.asList(
                "Dash Core",
                "Wind Burst",
                "Smoke Veil"
        ));

        // RARE
        pool.put("RARE", Arrays.asList(
                "Bridge Builder",
                "Situation Escape",
                "Hunter’s Mark"
        ));

        // LEGENDARY
        pool.put("LEGENDARY", Arrays.asList(
                "Blood Pact",
                "Void Snare",
                "Shockwave Plate",
                "Soul Siphon"
        ));

        // MYTHIC
        pool.put("MYTHIC", Arrays.asList(
                "Blink Dagger",
                "Domain Seed",
                "Gravity Well",
                "Chronos Pocket"
        ));
    }

    public static String getRandomAbility(String rarity) {
        List<String> list = pool.get(rarity);
        return list.get(r.nextInt(list.size()));
    }
}