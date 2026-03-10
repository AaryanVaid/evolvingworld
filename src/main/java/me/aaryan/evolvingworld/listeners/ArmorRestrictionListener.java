package me.aaryan.evolvingworld.listeners;

import me.aaryan.evolvingworld.EvolvingWorld;
import me.aaryan.evolvingworld.phase.Phase;
import me.aaryan.evolvingworld.restrictions.ToolPhase;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class ArmorRestrictionListener implements Listener {

    private final EvolvingWorld plugin;

    public ArmorRestrictionListener(EvolvingWorld plugin) {
        this.plugin = plugin;
    }

    /* ===============================
       INVENTORY / SHIFT / HOTBAR
       =============================== */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player player)) return;

        ItemStack item = event.getCurrentItem();
        if (item == null) return;

        if (!isArmor(item)) return;

        Phase required = ToolPhase.requiredPhase(item.getType());
        Phase playerPhase = plugin.getPlayerPhaseManager().getPlayerPhase(player);

        if (playerPhase.getLevel() < required.getLevel()) {
            event.setCancelled(true);
            player.sendMessage("§cYou are not evolved enough to wear this armor.");
        }
    }

    /* ===============================
       RIGHT-CLICK EQUIP
       =============================== */
    @EventHandler
    public void onRightClickEquip(PlayerInteractEvent event) {

        if (event.getHand() != EquipmentSlot.HAND) return;

        ItemStack item = event.getItem();
        if (item == null) return;

        if (!isArmor(item)) return;

        Player player = event.getPlayer();

        Phase required = ToolPhase.requiredPhase(item.getType());
        Phase playerPhase = plugin.getPlayerPhaseManager().getPlayerPhase(player);

        if (playerPhase.getLevel() < required.getLevel()) {
            event.setCancelled(true);
            player.sendMessage("§cYou are not evolved enough to wear this armor.");
        }
    }

    /* ===============================
       ARMOR TYPE CHECK
       =============================== */
    private boolean isArmor(ItemStack item) {
        String name = item.getType().name();
        return name.endsWith("_HELMET")
                || name.endsWith("_CHESTPLATE")
                || name.endsWith("_LEGGINGS")
                || name.endsWith("_BOOTS");
    }
}
