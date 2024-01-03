package de.fhswf.aoc20.data;

import de.fhswf.aoc20.pulse.PulseManager;
import de.fhswf.aoc20.pulse.PulseSignal;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * An abstract module, which is to be extended by the various needed modules.
 */
public abstract class Module {

    private final String token;
    private List<Module> connectedModules = new ArrayList<>();
    private final List<Module> predecessors = new ArrayList<>();

    protected Module(@Nonnull String token) {
        this.token = token;
    }

    @Nonnull
    public String getToken() {
        return token;
    }

    /**
     * Sets the connected modules, but also adds the current module as predecessor to all given modules.
     *
     * @param connectedModules the connected modules that should be set for the current module
     */
    public void setConnectedModules(@Nonnull List<Module> connectedModules) {
        this.connectedModules = new ArrayList<>(connectedModules);
        this.connectedModules.forEach(module -> module.appendPredecessor(this));
    }

    public abstract void pulse(@Nonnull PulseSignal pulseSignal);

    public abstract void reset();

    /**
     * Adds the current module with the given {@link PulseSignal pulse signal} to the queue of pulses.
     *
     * @param pulseSignal the signal containing the source and the type
     */
    public void registerPulseForConnectedModules(@Nonnull PulseSignal pulseSignal) {
        connectedModules.forEach(module -> PulseManager.getInstance().registerModulePulse(module, pulseSignal));
    }

    public List<Module> getPredecessors() {
        return predecessors;
    }

    /**
     * Appends the given module to the list of predecessors.
     *
     * @param module the new predecessor
     */
    public void appendPredecessor(@Nonnull Module module) {
        predecessors.add(module);
    }

    /**
     * For the second task, we want to know if all predecessors are conjunctions, as we want to go down from the rx module to the predecessor
     * conjunctions, without relying on a flip-flop.
     *
     * @return {@code true} if all predecessors are conjunctions
     */
    public boolean areAllPredecessorsConjunctions() {
        return predecessors.stream()
                .allMatch(Conjunction.class::isInstance);
    }
}
