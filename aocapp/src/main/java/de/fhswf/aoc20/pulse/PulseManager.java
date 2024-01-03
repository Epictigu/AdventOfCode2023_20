package de.fhswf.aoc20.pulse;

import de.fhswf.aoc20.data.Broadcaster;
import de.fhswf.aoc20.data.Module;
import de.fhswf.aoc20.math.LCMCalculator;
import lombok.extern.java.Log;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log
public class PulseManager {

    private static final String ERROR_RX_MODULE_NOT_FOUND = "The rx module could not be found.";

    private static final PulseManager instance = new PulseManager();

    private final LCMCalculator lcmCalculator;

    private final List<Map.Entry<Module, PulseSignal>> modulesWaitingForPulse = new ArrayList<>();

    private int amountOfLowPulses = 0;
    private int amountOfHighPulses = 0;
    private int amountOfProcessedCycles = 0;

    private final Map<Module, PulseType> expectedPulseTypeForRx = new HashMap<>();
    private final Map<Module, Integer> leastAmountOfCyclesNeededForExpectedPulse = new HashMap<>();
    private boolean announcedRcModule = false;

    @Nonnull
    public static PulseManager getInstance() {
        return instance;
    }

    PulseManager() {
        this.lcmCalculator = new LCMCalculator();
    }

    public void registerModulePulse(@Nonnull Module module, @Nonnull PulseSignal pulseSignal) {
        Module sourceModule = pulseSignal.getSource();
        if (expectedPulseTypeForRx.containsKey(sourceModule) && expectedPulseTypeForRx.get(sourceModule).equals(pulseSignal.getPulseType())) {
            leastAmountOfCyclesNeededForExpectedPulse.putIfAbsent(sourceModule, amountOfProcessedCycles);
            announcePulsedRxModelIfConditionsAreMatched();
        }
        this.modulesWaitingForPulse.add(Map.entry(module, pulseSignal));
    }

    public int startCycles(@Nonnull Broadcaster broadcaster, int amountOfCycles) {
        amountOfProcessedCycles = 0;
        while (amountOfProcessedCycles < amountOfCycles || (amountOfCycles == -1 && !announcedRcModule)) {
            amountOfProcessedCycles++;
            startCycle(broadcaster);
        }
        return amountOfHighPulses * amountOfLowPulses;
    }

    private void startCycle(Broadcaster broadcaster) {
        broadcaster.pulse(new PulseSignal(PulseType.LOW, null));
        amountOfLowPulses++;

        while (!modulesWaitingForPulse.isEmpty()) {
            Map.Entry<Module, PulseSignal> firstEntry = modulesWaitingForPulse.remove(0);
            Module module = firstEntry.getKey();
            PulseSignal pulseSignal = firstEntry.getValue();
            if (pulseSignal.getPulseType() == PulseType.LOW) {
                amountOfLowPulses++;
            } else {
                amountOfHighPulses++;
            }
            module.pulse(pulseSignal);
        }
    }

    public void searchForRxModulePulse(@Nonnull Broadcaster broadcaster, @Nonnull List<Module> modules) {
        Module rxModule = modules.stream()
                .filter(module -> module.getToken().equals("rx"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(ERROR_RX_MODULE_NOT_FOUND));
        addModuleToRxChain(rxModule, PulseType.HIGH);

        startCycles(broadcaster, -1);
    }

    private void addModuleToRxChain(Module module, PulseType pulseType) {
        if (module.areAllPredecessorsConjunctions()) {
            module.getPredecessors().forEach(predecessor -> addModuleToRxChain(predecessor, pulseType.getInvertedPulseType()));
            return;
        }

        expectedPulseTypeForRx.put(module, pulseType);
    }

    public void announcePulsedRxModelIfConditionsAreMatched() {
        if (!announcedRcModule && expectedPulseTypeForRx.size() == leastAmountOfCyclesNeededForExpectedPulse.size()) {
            long rxResult = lcmCalculator.calculateLCMForMultipleNumbers(leastAmountOfCyclesNeededForExpectedPulse.values().toArray(new Integer[0]));
            log.info(String.format("Lowest amount of button presses for RX module found: '%d'.", rxResult));
            announcedRcModule = true;
        }
    }
}
