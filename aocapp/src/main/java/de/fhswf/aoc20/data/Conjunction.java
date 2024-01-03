package de.fhswf.aoc20.data;

import de.fhswf.aoc20.pulse.PulseSignal;
import de.fhswf.aoc20.pulse.PulseType;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class Conjunction extends Module {

    private final Map<Module, PulseType> lastReceivedSignals = new HashMap<>();

    public Conjunction(@Nonnull String token) {
        super(token);
    }

    @Override
    public void pulse(@Nonnull PulseSignal pulseSignal) {
        lastReceivedSignals.put(pulseSignal.getSource(), pulseSignal.getPulseType());
        PulseType pulseType = haveRecentPulsesAllBeenHigh() ? PulseType.LOW : PulseType.HIGH;
        registerPulseForConnectedModules(new PulseSignal(pulseType, this));
    }

    @Override
    public void reset() {
        lastReceivedSignals.keySet().forEach(module -> lastReceivedSignals.put(module, PulseType.LOW));
    }

    private boolean haveRecentPulsesAllBeenHigh() {
        return lastReceivedSignals.values().stream()
                .allMatch(PulseType.HIGH::equals);
    }

    public void registerInitialPulseType(@Nonnull Module module) {
        lastReceivedSignals.put(module, PulseType.LOW);
    }
}
