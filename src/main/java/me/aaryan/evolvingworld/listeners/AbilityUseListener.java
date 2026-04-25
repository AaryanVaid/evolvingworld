package me.aaryan.evolvingworld.listeners;

import me.aaryan.evolvingworld.EvolvingWorld;
import me.aaryan.evolvingworld.ability.*;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class AbilityUseListener implements Listener {

    private final AbilityManager abilityManager;
    private final CooldownManager cooldownManager;
    private final NamespacedKey abilityKey;

    public AbilityUseListener(EvolvingWorld plugin) {
        this.abilityManager = plugin.getAbilityManager();
        this.cooldownManager = plugin.getCooldownManager();
        this.abilityKey = new NamespacedKey(plugin, "ability_name");
    }

    @EventHandler
    public void onUse(PlayerInteractEvent e) {

        Player p = e.getPlayer();
        ItemStack item = e.getItem();

        if (item == null || !item.hasItemMeta()) return;

        int slot = p.getInventory().getHeldItemSlot();

        if (slot < 6 || slot > 8) return;

        String abilityName = item.getItemMeta()
                .getPersistentDataContainer()
                .get(abilityKey, PersistentDataType.STRING);

        if (abilityName == null) return;

        Ability ability = abilityManager.get(abilityName);
        if (ability == null) return;

        // ✅ Cooldown check (slot-based)
        if (!cooldownManager.canUse(p, slot)) return;

        ability.use(p);

        cooldownManager.apply(p, slot, ability.getCooldown());

        CooldownVisual.start(p, item, ability.getCooldown());
    }
}