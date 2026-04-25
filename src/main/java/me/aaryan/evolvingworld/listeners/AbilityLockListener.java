package me.aaryan.evolvingworld.listeners;

import me.aaryan.evolvingworld.ability.AbilityKeys;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class AbilityLockListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player)) return;

        Inventory inv = e.getClickedInventory();
        if (inv == null) return;

        if (!inv.equals(e.getWhoClicked().getInventory())) return;

        ItemStack current = e.getCurrentItem();
        ItemStack cursor = e.getCursor();

        if (AbilityKeys.isAbilityItem(current)) {
            e.setCancelled(true);
            return;
        }

        if (AbilityKeys.isAbilityItem(cursor)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e) {

        if (!(e.getWhoClicked() instanceof Player)) return;

        Inventory inv = e.getInventory();
        if (!inv.equals(e.getWhoClicked().getInventory())) return;

        for (ItemStack item : e.getNewItems().values()) {

            if (AbilityKeys.isAbilityItem(item)) {
                e.setCancelled(true);
                return;
            }
        }
    }
}