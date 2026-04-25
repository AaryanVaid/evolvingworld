package me.aaryan.evolvingworld.ability;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class AbilityKeys {

    private static NamespacedKey KEY;

    public static void init(JavaPlugin plugin) {
        KEY = new NamespacedKey(plugin, "ability_item");
    }

    public static boolean isAbilityItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;

        return item.getItemMeta()
                .getPersistentDataContainer()
                .has(KEY, PersistentDataType.STRING);
    }

    public static void tag(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return;

        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(KEY, PersistentDataType.STRING, "true");
        item.setItemMeta(meta);
    }
}