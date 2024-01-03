package de.fhswf.aoc20.data;

import de.fhswf.aoc20.pulse.PulseSignal;

import javax.annotation.Nonnull;

/**
 * The broadcaster module which is meant to represent the start point, from which the pulses are sent.
 */
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
