package me.aaryan.evolvingworld.items;

import me.aaryan.evolvingworld.EvolvingWorld;
import me.aaryan.evolvingworld.phase.Phase;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class PhaseBoosterItem {

    private static NamespacedKey PHASE_KEY;

    public static void init(EvolvingWorld plugin) {
        PHASE_KEY = new NamespacedKey(plugin, "phase_booster_level");
    }

    // ✅ Create booster for a specific phase
    public static ItemStack create(Phase phase) {
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("§dPhase " + phase.getLevel() + " Booster");
        meta.setLore(List.of(
                "§7Use this to advance",
                "§7from Phase " + phase.getLevel()
        ));

        meta.getPersistentDataContainer().set(
                PHASE_KEY,
                PersistentDataType.INTEGER,
                phase.getLevel()
        );

        item.setItemMeta(meta);
        return item;
    }

    // ✅ Identify booster
    public static boolean isPhaseBooster(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;

        return item.getItemMeta()
                .getPersistentDataContainer()
                .has(PHASE_KEY, PersistentDataType.INTEGER);
    }

    // ✅ THIS FIXES YOUR ERROR
    public static int getBoosterLevel(ItemStack item) {
        if (!isPhaseBooster(item)) return -1;

        return item.getItemMeta()
                .getPersistentDataContainer()
                .get(PHASE_KEY, PersistentDataType.INTEGER);
    }
}
