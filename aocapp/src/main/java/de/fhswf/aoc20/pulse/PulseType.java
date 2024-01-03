package de.fhswf.aoc20.pulse;

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
