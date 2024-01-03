package de.fhswf.aoc20.pulse;

/**
 * The pulse type, to show whether a pulse is either low or high.
 */
public enum PulseType {

    LOW,
    HIGH;

    public PulseType getInvertedPulseType() {
        return switch (this) {
            case HIGH -> LOW;
            case LOW -> HIGH;
        };
    }
}
