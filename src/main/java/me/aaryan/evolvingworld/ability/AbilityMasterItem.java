package me.aaryan.evolvingworld.ability;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class AbilityMasterItem {

    public static ItemStack create(int tier) {
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.GOLD + "Ability Master §e" + tier);
        meta.setLore(Arrays.asList(
                ChatColor.GRAY + "Right-click to roll an ability.",
                ChatColor.DARK_GRAY + "Tier: " + tier
        ));

        item.setItemMeta(meta);
        return item;
    }

    public static int getTier(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return -1;
        String name = item.getItemMeta().getDisplayName();

        if (name.contains("§e1")) return 1;
        if (name.contains("§e2")) return 2;
        if (name.contains("§e3")) return 3;

        return -1;
    }
}