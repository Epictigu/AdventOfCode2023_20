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

/**
 * Manages the pulse process, by providing a queue for modules to be pulsed and tracking the amount of cycles, as well as the amount of low and high
 * pulses. It also contains start methods for both the tasks, which primarily differentiate themselves in their stopping condition.
 */
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

    /**
     * Adds the given {@link PulseSignal pulse signal} for the given module to the end of the current queue. The added elements are gone through in
     * the order of the queue, since it is required by the logic provided in the task.
     *
     * @param module      the module that should be added to the queue
     * @param pulseSignal the pulse signal that should be added for the model to the queue
     */
    public void registerModulePulse(@Nonnull Module module, @Nonnull PulseSignal pulseSignal) {
        Module sourceModule = pulseSignal.getSource();
        if (expectedPulseTypeForRx.containsKey(sourceModule) && expectedPulseTypeForRx.get(sourceModule).equals(pulseSignal.getPulseType())) {
            leastAmountOfCyclesNeededForExpectedPulse.putIfAbsent(sourceModule, amountOfProcessedCycles);
            announcePulsedRxModelIfConditionsAreMatched();
        }
        this.modulesWaitingForPulse.add(Map.entry(module, pulseSignal));
    }

    /**
     * Starts the first task by pulsing the broadcaster module the provided amount of cycles. The low and high pulses are tracked, since the task
     * requires the multiplied value of the two as a result.
     *
     * @param broadcaster    the broadcaster module that should be pulsed
     * @param amountOfCycles the amount of cycles that the module should be pulsed
     * @return the multiplied value of high and low pulses
     */
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

    /**
     * Starts the second task by pulsing the broadcaster module until the solution has been found. Since searching for the amount of cycles required
     * to pulse the rxModule low, instead we search until all its predecessor conjunctions have had the wanted pulse at least once. Then we can
     * calculate the lowest common multiple from the conjunctions, to find the solution for when the rx module is pulsed low.
     *
     * @param broadcaster the broadcaster module for the initial pulses
     * @param modules     the list of all available pulses
     */
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

    /**
     * If the entries in the {@link #leastAmountOfCyclesNeededForExpectedPulse least cycles needed map} is as long as the amount of provided
     * conjunctions for the rx module, the result can be calculated using the {@link LCMCalculator lcm calculator}.
     *
     * <p>The found solution is then printed to the user, and the algorithm is stopped, since no further actions are required.
     */
    public void announcePulsedRxModelIfConditionsAreMatched() {
        if (!announcedRcModule && expectedPulseTypeForRx.size() == leastAmountOfCyclesNeededForExpectedPulse.size()) {
            long rxResult = lcmCalculator.calculateLCMForMultipleNumbers(leastAmountOfCyclesNeededForExpectedPulse.values().toArray(new Integer[0]));
            log.info(String.format("Lowest amount of button presses for RX module found: '%d'.", rxResult));
            announcedRcModule = true;
        }
    }
}
