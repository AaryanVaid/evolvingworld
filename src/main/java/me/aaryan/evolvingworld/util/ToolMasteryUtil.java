package me.aaryan.evolvingworld.util;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class ToolMasteryUtil {

    public static void applyMastery(ItemStack item, NamespacedKey key) {

        ItemMeta meta = item.getItemMeta();

        meta.getPersistentDataContainer().set(
                key,
                PersistentDataType.INTEGER,
                1
        );

        item.setItemMeta(meta);
    }

    public static boolean isMastered(ItemStack item, NamespacedKey key) {

        if (item == null || !item.hasItemMeta()) return false;

        return item.getItemMeta()
                .getPersistentDataContainer()
                .has(key, PersistentDataType.INTEGER);
    }
}