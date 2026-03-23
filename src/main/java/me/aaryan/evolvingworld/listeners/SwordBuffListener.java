package me.aaryan.evolvingworld.listeners;

import me.aaryan.evolvingworld.util.ToolMasteryUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class SwordBuffListener implements Listener {

    private final NamespacedKey KEY;

    public SwordBuffListener(NamespacedKey key) {
        this.KEY = key;
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {

        if (!(event.getDamager() instanceof Player player)) return;

        ItemStack item = player.getInventory().getItemInMainHand();

        if (item == null) return;

        if (!item.getType().toString().contains("SWORD")) return;

        if (!ToolMasteryUtil.isMastered(item, KEY)) return;

        // 🔥 +25% damage
        event.setDamage(event.getDamage() * 1.25);
    }
}