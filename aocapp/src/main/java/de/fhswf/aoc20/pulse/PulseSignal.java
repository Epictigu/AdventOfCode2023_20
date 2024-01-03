package de.fhswf.aoc20.pulse;

import de.fhswf.aoc20.data.Module;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class PulseSignal {

    private final PulseType pulseType;
    private final Module source;
}
