/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package de.fhswf.aoc20;

import de.fhswf.aoc20.data.Broadcaster;
import de.fhswf.aoc20.data.Module;
import de.fhswf.aoc20.inputs.ModuleLoader;
import de.fhswf.aoc20.pulse.PulseManager;
import lombok.extern.java.Log;

import javax.annotation.Nullable;
import java.util.List;

/**
 * The actual app, which connects the module loading, as well as the start of the two tasks.
 */
@Log
public class AOCApp {

    public static void main(@Nullable String[] args) {
        ModuleLoader moduleLoader = new ModuleLoader();
        List<Module> modules = moduleLoader.loadModules();

        Broadcaster broadcaster = modules.stream()
                .filter(Broadcaster.class::isInstance)
                .map(Broadcaster.class::cast)
                .findFirst()
                .orElseThrow(IllegalStateException::new);

        searchForTheMultipliedHighAndLowPulses(broadcaster);
        modules.forEach(Module::reset);
        searchForTheRxPulse(broadcaster, modules);
    }

    private static void searchForTheMultipliedHighAndLowPulses(Broadcaster broadcaster) {
        int result = PulseManager.getInstance().startCycles(broadcaster, 1000);
        log.info(String.format("The multiplied amount of high and low pulses is '%d'.", result));
    }

    private static void searchForTheRxPulse(Broadcaster broadcaster, List<Module> modules) {
        PulseManager.getInstance().searchForRxModulePulse(broadcaster, modules);
    }
}
