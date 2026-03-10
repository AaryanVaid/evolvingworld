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

        int nextLevel = this.level + 1;

        for (Phase phase : values()) {
            if (phase.level == nextLevel) {
                return phase;
            }
        }

}
