package me.aaryan.evolvingworld.restrictions;

import me.aaryan.evolvingworld.phase.Phase;
import org.bukkit.World;

public class DimensionPhase {

    public static Phase required(World world) {

        return switch (world.getEnvironment()) {
            case NORMAL -> Phase.PHASE_1;
            case NETHER -> Phase.PHASE_3;
            case THE_END -> Phase.PHASE_4;
            default -> Phase.PHASE_4;
        };
    }
}
