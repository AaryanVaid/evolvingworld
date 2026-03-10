package me.aaryan.evolvingworld.restrictions;

import me.aaryan.evolvingworld.phase.Phase;
import org.bukkit.Material;
import org.bukkit.Tag;

import java.util.EnumSet;
import java.util.Set;

public enum BlockPhase {

    PHASE_1(EnumSet.of(
            // Terrain
            Material.STONE,
            Material.COBBLESTONE,
            Material.DIRT,
            Material.GRASS_BLOCK,
            Material.SAND,
            Material.GRAVEL,
            Material.CLAY,

            // Ores
            Material.COAL_ORE,
            Material.IRON_ORE,

            // 🔑 INGOTS (critical fix)
            Material.IRON_INGOT,

            // Wood
            Material.OAK_LOG,
            Material.SPRUCE_LOG,
            Material.BIRCH_LOG,
            Material.OAK_PLANKS,

            // Utility
            Material.CRAFTING_TABLE,
            Material.FURNACE,
            Material.CHEST
    )),

    PHASE_2(EnumSet.of(
            // Industrial
            Material.GOLD_ORE,
            Material.DEEPSLATE,
            Material.DEEPSLATE_COAL_ORE,
            Material.DEEPSLATE_IRON_ORE,
            Material.DEEPSLATE_GOLD_ORE,

            // 🔑 INGOTS
            Material.GOLD_INGOT,

            // Deep Dark
            Material.SCULK,
            Material.SCULK_SENSOR,
            Material.SCULK_SHRIEKER
    )),

    PHASE_3(EnumSet.of(
            // Diamonds & Nether
            Material.DIAMOND_ORE,
            Material.DIAMOND,
            Material.DIAMOND_BLOCK,
            Material.NETHERRACK,
            Material.NETHER_BRICKS,
            Material.ANCIENT_DEBRIS
    ));

    private final Set<Material> blocks;

    BlockPhase(Set<Material> blocks) {
        this.blocks = blocks;
    }

    public static Phase requiredPhase(Material material) {

        // Dynamic tags
        if (Tag.LEAVES.isTagged(material)) return Phase.PHASE_1;
        if (Tag.LOGS.isTagged(material)) return Phase.PHASE_1;

        for (BlockPhase bp : values()) {
            if (bp.blocks.contains(material)) {
                return Phase.valueOf(bp.name());
            }
        }

        return Phase.PHASE_1; // safe default
    }
}
