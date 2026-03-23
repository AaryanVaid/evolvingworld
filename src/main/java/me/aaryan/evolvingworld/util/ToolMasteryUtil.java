package me.aaryan.evolvingworld.util;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ToolMasteryUtil {

    public static void applyMastery(ItemStack item, NamespacedKey key) {

        ItemMeta meta = item.getItemMeta();

        // mark mastered
        meta.getPersistentDataContainer().set(
                key,
                PersistentDataType.INTEGER,
                1
        );

        // ✨ glow
        meta.addEnchant(Enchantment.UNBREAKING, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        // 🧾 lore
        List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();

        lore.remove("§6✦ Mastered");
        lore.add("§6✦ Mastered");

        meta.setLore(lore);

        item.setItemMeta(meta);
    }

    public static boolean isMastered(ItemStack item, NamespacedKey key) {

        if (item == null || !item.hasItemMeta()) return false;

        return item.getItemMeta()
                .getPersistentDataContainer()
                .has(key, PersistentDataType.INTEGER);
    }
}