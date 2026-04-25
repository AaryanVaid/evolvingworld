package me.aaryan.evolvingworld.listeners;

import me.aaryan.evolvingworld.ability.AbilityMasterGUI;
import me.aaryan.evolvingworld.ability.AbilityMasterItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class AbilityMasterListener implements Listener {

    @EventHandler
    public void onUse(PlayerInteractEvent e) {

        if (e.getHand() != EquipmentSlot.HAND) return;

        Player p = e.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();

        int tier = AbilityMasterItem.getTier(item);
        if (tier == -1) return;

        e.setCancelled(true);

        AbilityMasterGUI.open(p, tier);

        item.setAmount(item.getAmount() - 1);
    }
}