package de.fhswf.aoc20.data;

import de.fhswf.aoc20.pulse.PulseSignal;

import javax.annotation.Nonnull;

public class UnknownModule extends Module {

    public UnknownModule(@Nonnull String token) {
        super(token);
    }

    @Override
    public void pulse(@Nonnull PulseSignal pulseSignal) {
        //No action needed
    }

    @Override
    public void reset() {
        //No reset needed
    }
}
