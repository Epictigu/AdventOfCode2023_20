package de.fhswf.aoc20.pulse;

import de.fhswf.aoc20.data.Module;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * A pulse signal containing both a {@link PulseType type} and a {@link Module source module}.
 */
@RequiredArgsConstructor
@Getter
public class PulseSignal {

    private final PulseType pulseType;
    private final Module source;
}
