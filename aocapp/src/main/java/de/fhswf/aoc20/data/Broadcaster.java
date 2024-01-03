package de.fhswf.aoc20.data;

import de.fhswf.aoc20.pulse.PulseSignal;

import javax.annotation.Nonnull;

public class Broadcaster extends Module {

    public Broadcaster(@Nonnull String token) {
        super(token);
    }

    @Override
    public void pulse(@Nonnull PulseSignal pulseSignal) {
        registerPulseForConnectedModules(new PulseSignal(pulseSignal.getPulseType(), this));
    }

    @Override
    public void reset() {
        //No reset needed
    }
}
