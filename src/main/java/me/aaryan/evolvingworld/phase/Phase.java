package me.aaryan.evolvingworld.phase;

public enum Phase {

    PHASE_1(1),
    PHASE_2(2),
    PHASE_3(3),
    PHASE_4(4);

    private final int level;

    Phase(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public static Phase fromLevel(int level) {
        for (Phase phase : values()) {
            if (phase.level == level) {
                return phase;
            }
        }
        return PHASE_1;
    }

    public Phase next() {
        return switch (this) {
            case PHASE_1 -> PHASE_2;
            case PHASE_2 -> PHASE_3;
            case PHASE_3 -> PHASE_4;
            default -> PHASE_4;
        };
    }

}
