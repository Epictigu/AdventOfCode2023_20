package de.fhswf.aoc20.data;

import de.fhswf.aoc20.pulse.PulseSignal;
import de.fhswf.aoc20.pulse.PulseType;

import javax.annotation.Nonnull;

/**
 * The flip-flop module, which ignored high pulses and flips its current state and pulse value every time it receives a low pulse.
 */
public class FlipFlop extends Module {

    private boolean onState = false;

    public FlipFlop(@Nonnull String token) {
        super(token);
    }

    @Override
    public void pulse(@Nonnull PulseSignal pulseSignal) {
        if (pulseSignal.getPulseType() == PulseType.HIGH) {
            return;
        }

        if (onState) {
            onState = false;
            registerPulseForConnectedModules(new PulseSignal(PulseType.LOW, this));
        } else {
            onState = true;
            registerPulseForConnectedModules(new PulseSignal(PulseType.HIGH, this));
        }
    }

    @Override
    public void reset() {
        this.onState = false;
    }
}
