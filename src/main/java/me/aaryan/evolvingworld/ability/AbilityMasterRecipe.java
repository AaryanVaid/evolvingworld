package me.aaryan.evolvingworld.ability;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public class AbilityMasterRecipe {

    public static void register(JavaPlugin plugin) {

        // AM-1
        ShapedRecipe am1 = new ShapedRecipe(new NamespacedKey(plugin, "am1"),
                AbilityMasterItem.create(1));

        am1.shape("AEA","IGI","AEA");
        am1.setIngredient('A', Material.AMETHYST_BLOCK);
        am1.setIngredient('E', Material.EMERALD_BLOCK);
        am1.setIngredient('I', Material.IRON_BLOCK);
        am1.setIngredient('G', Material.GUNPOWDER);

        plugin.getServer().addRecipe(am1);

        // AM-2
        ShapedRecipe am2 = new ShapedRecipe(new NamespacedKey(plugin, "am2"),
                AbilityMasterItem.create(2));

        am2.shape("DRD","ETE","DRD");
        am2.setIngredient('D', Material.DIAMOND_BLOCK);
        am2.setIngredient('R', Material.REDSTONE_BLOCK);
        am2.setIngredient('E', Material.EMERALD_BLOCK);
        am2.setIngredient('T', Material.TNT);

        plugin.getServer().addRecipe(am2);

        // AM-3
        ShapedRecipe am3 = new ShapedRecipe(new NamespacedKey(plugin, "am3"),
                AbilityMasterItem.create(3));

        am3.shape("NAN","ANA","NAN");
        am3.setIngredient('N', Material.NETHER_STAR);
        am3.setIngredient('A', Material.ANCIENT_DEBRIS);

        plugin.getServer().addRecipe(am3);
    }
}