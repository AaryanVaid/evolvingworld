package me.aaryan.evolvingworld.ability;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import me.aaryan.evolvingworld.ability.AbilityKeys;

public class AbilityItemUtil {

    private static JavaPlugin plugin;

    public static void init(JavaPlugin pl) {
        plugin = pl;
    }

    public static ItemStack createAbilityItem(String abilityName) {

        if (plugin == null) {
            throw new IllegalStateException("AbilityItemUtil not initialized! Call init() in onEnable()");
        }

        ItemStack item = new ItemStack(Material.CARROT_ON_A_STICK);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§b" + abilityName);

        meta.getPersistentDataContainer().set(
                new NamespacedKey(plugin, "ability_name"),
                PersistentDataType.STRING,
                abilityName
        );

        item.setItemMeta(meta);
        AbilityKeys.tag(item);

        return item;
    }
}