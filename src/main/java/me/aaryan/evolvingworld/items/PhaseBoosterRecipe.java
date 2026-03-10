package me.aaryan.evolvingworld.items;

import me.aaryan.evolvingworld.phase.Phase;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;

public class PhaseBoosterRecipe {

    public static void registerAll(Plugin plugin) {

        register(plugin, Phase.PHASE_1);
        register(plugin, Phase.PHASE_2);
        register(plugin, Phase.PHASE_3);
    }

    private static void register(Plugin plugin, Phase phase) {

        NamespacedKey key = new NamespacedKey(
                plugin,
                "phase_booster_" + phase.getLevel()
        );

        ShapedRecipe recipe = new ShapedRecipe(key, PhaseBoosterItem.create(phase));

        switch (phase) {

            case PHASE_1 -> {
                recipe.shape("IGI", "GRG", "IGI");
                recipe.setIngredient('I', Material.IRON_BLOCK);
                recipe.setIngredient('G', Material.GUNPOWDER);
                recipe.setIngredient('R', Material.REDSTONE_BLOCK);
            }

            case PHASE_2 -> {
                recipe.shape("GTG", "TRT", "GTG");
                recipe.setIngredient('G', Material.GOLD_BLOCK);
                recipe.setIngredient('T', Material.TNT);
                recipe.setIngredient('R', Material.REDSTONE_BLOCK);
            }

            case PHASE_3 -> {
                recipe.shape("OBO", "BEB", "OBO");
                recipe.setIngredient('O', Material.OBSIDIAN);
                recipe.setIngredient('B', Material.BEACON);
                recipe.setIngredient('E', Material.ENDER_EYE);
            }

            default -> {
                return;
            }
        }

        Bukkit.addRecipe(recipe);
    }
}
