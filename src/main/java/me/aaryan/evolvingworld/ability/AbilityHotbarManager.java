package me.aaryan.evolvingworld.ability;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AbilityHotbarManager {

    public static void giveAbility(Player p, ItemStack item) {

        item = item.clone();

        if (p.getInventory().getItem(6) == null) {
            p.getInventory().setItem(6, item);
            return;
        }

        if (p.getInventory().getItem(7) == null) {
            p.getInventory().setItem(7, item);
            return;
        }

        if (p.getInventory().getItem(8) == null) {
            p.getInventory().setItem(8, item);
            return;
        }

        p.sendMessage("§cYou already have 3 abilities.");
    }
}