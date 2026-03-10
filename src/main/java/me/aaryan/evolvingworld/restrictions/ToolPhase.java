package me.aaryan.evolvingworld.restrictions;

import me.aaryan.evolvingworld.phase.Phase;
import org.bukkit.Material;

public class ToolPhase {

    public static Phase requiredPhase(Material material) {

        // 🛡️ Absolute safety: never restrict non-equipment
        if (!isRestrictedEquipment(material)) {
            return Phase.PHASE_1;
        }

        String name = material.name();

        if (name.contains("NETHERITE")) return Phase.PHASE_4;
        if (name.contains("DIAMOND")) return Phase.PHASE_3;
        if (name.contains("IRON") || name.contains("GOLD")) return Phase.PHASE_2;

        return Phase.PHASE_1;
    }

    /**
     * ONLY tools, weapons, and armor should ever be restricted.
     * Everything else (ingots, crafting items, boosters, etc.) is always allowed.
     */
    private static boolean isRestrictedEquipment(Material material) {

        if (!material.isItem()) return false;

        String name = material.name();

        return name.endsWith("_SWORD")
                || name.endsWith("_AXE")
                || name.endsWith("_PICKAXE")
                || name.endsWith("_SHOVEL")
                || name.endsWith("_HOE")
                || name.endsWith("_HELMET")
                || name.endsWith("_CHESTPLATE")
                || name.endsWith("_LEGGINGS")
                || name.endsWith("_BOOTS");
    }
}
