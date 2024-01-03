package de.fhswf.aoc20.data;

import de.fhswf.aoc20.pulse.PulseManager;
import de.fhswf.aoc20.pulse.PulseSignal;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

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

    public void setConnectedModules(@Nonnull List<Module> connectedModules) {
        this.connectedModules = new ArrayList<>(connectedModules);
        this.connectedModules.stream()
                .filter(Conjunction.class::isInstance)
                .map(Conjunction.class::cast)
                .forEach(conjunction -> conjunction.registerInitialPulseType(this));
        this.connectedModules.forEach(module -> module.appendPredecessor(this));
    }

    public abstract void pulse(@Nonnull PulseSignal pulseSignal);

    public abstract void reset();

    public void registerPulseForConnectedModules(@Nonnull PulseSignal pulseSignal) {
        connectedModules.forEach(module -> PulseManager.getInstance().registerModulePulse(module, pulseSignal));
    }

    public List<Module> getPredecessors() {
        return predecessors;
    }

    public void appendPredecessor(@Nonnull Module module) {
        predecessors.add(module);
    }

    public boolean areAllPredecessorsConjunctions() {
        return predecessors.stream()
                .allMatch(Conjunction.class::isInstance);
    }
}
