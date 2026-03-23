package me.aaryan.evolvingworld.items;

import me.aaryan.evolvingworld.EvolvingWorld;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class ToolShard {

    private static NamespacedKey KEY;

    public static void init(EvolvingWorld plugin) {
        KEY = new NamespacedKey(plugin, "tool_shard");
    }

    public static ItemStack create() {

        ItemStack item = new ItemStack(Material.PRISMARINE_CRYSTALS);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("§bMastery Shard");
        meta.setLore(List.of(
                "§7Apply to a tool or armor",
                "§7to permanently master it"
        ));

        meta.getPersistentDataContainer().set(
                KEY,
                PersistentDataType.INTEGER,
                1
        );

        item.setItemMeta(meta);
        return item;
    }

    public static boolean isShard(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;

        return item.getItemMeta()
                .getPersistentDataContainer()
                .has(KEY, PersistentDataType.INTEGER);
    }
}