package me.aaryan.evolvingworld.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

public class AbilityMasterCraftListener implements Listener {

    @EventHandler
    public void onCraft(CraftItemEvent e) {

        if (!(e.getWhoClicked() instanceof Player p)) return;

        CraftingInventory inv = (CraftingInventory) e.getInventory();

        ItemStack result = inv.getResult();
        if (result == null || !result.hasItemMeta()) return;

        String name = result.getItemMeta().getDisplayName();

        // AM-1 & AM-2
        if (name.contains("Ability Master §e1") || name.contains("Ability Master §e2")) {

            if (!check(inv, 32)) {
                e.setCancelled(true);
                p.sendMessage("§cYou need full stacks (32) in every slot!");
                return;
            }
        }

        // AM-3
        if (name.contains("Ability Master §e3")) {

            if (!checkDebris(inv)) {
                e.setCancelled(true);
                p.sendMessage("§cYou need 5 Ancient Debris in each slot!");
                return;
            }
        }
    }

    private boolean check(CraftingInventory inv, int amount) {
        for (ItemStack item : inv.getMatrix()) {
            if (item == null || item.getAmount() < amount) return false;
        }
        return true;
    }

    private boolean checkDebris(CraftingInventory inv) {
        for (ItemStack item : inv.getMatrix()) {
            if (item == null) return false;

            if (item.getType() == Material.ANCIENT_DEBRIS && item.getAmount() < 5)
                return false;
        }
        return true;
    }
}