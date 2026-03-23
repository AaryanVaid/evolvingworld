package me.aaryan.evolvingworld.listeners;

import me.aaryan.evolvingworld.util.ToolMasteryUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class ArmorBuffListener implements Listener {

    private final NamespacedKey KEY;

    public ArmorBuffListener(NamespacedKey key) {
        this.KEY = key;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {

        if (!(event.getEntity() instanceof Player player)) return;

        double reduction = 0;

        for (ItemStack armor : player.getInventory().getArmorContents()) {

            if (armor == null) continue;

            if (ToolMasteryUtil.isMastered(armor, KEY)) {
                reduction += 0.08; // 🔥 8% per piece
            }
        }

        if (reduction > 0) {
            event.setDamage(event.getDamage() * (1 - reduction));
        }
    }
}