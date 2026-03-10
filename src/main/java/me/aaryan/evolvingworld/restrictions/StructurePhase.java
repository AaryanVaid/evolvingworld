package me.aaryan.evolvingworld.restrictions;

import me.aaryan.evolvingworld.phase.Phase;
import org.bukkit.generator.structure.Structure;

import java.util.Map;

public class StructurePhase {

    private static final Map<Structure, Phase> MAP = Map.of(
            Structure.VILLAGE_PLAINS, Phase.PHASE_1,
            Structure.MINESHAFT, Phase.PHASE_1,

            Structure.ANCIENT_CITY, Phase.PHASE_2,

            Structure.STRONGHOLD, Phase.PHASE_3,
            Structure.BASTION_REMNANT, Phase.PHASE_3,
            Structure.FORTRESS, Phase.PHASE_3,

            Structure.END_CITY, Phase.PHASE_4
    );

    public static Phase required(Structure structure) {
        return MAP.get(structure);
    }
}
