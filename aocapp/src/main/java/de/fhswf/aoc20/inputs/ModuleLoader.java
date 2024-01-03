package de.fhswf.aoc20.inputs;

import de.fhswf.aoc20.data.Module;
import de.fhswf.aoc20.data.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class ModuleLoader {

    private static final String ERROR_UNKNOWN_MODULE_TYPE = "The module '%s' has an unknown module type.";

    private static final String RX_MODULE_TOKEN = "rx";

    private final InputLoader inputLoader;

    public ModuleLoader() {
        this.inputLoader = new InputLoader();
    }

    public List<Module> loadModules() {
        List<Map.Entry<Module, String>> entries = inputLoader.loadInputs().stream()
                .map(this::convertLineToModuleEntry)
                .toList();
        List<Module> modules = entries.stream()
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        modules.add(new InvalidModule(RX_MODULE_TOKEN));
        entries.forEach(entry -> fillInConnectedModules(entry, modules));
        return modules;
    }

    private Map.Entry<Module, String> convertLineToModuleEntry(String line) {
        String[] prefixSuffix = line.split(" -> ");
        String prefix = prefixSuffix[0];
        Module module = determineModuleFromPrefix(prefix);
        return Map.entry(module, prefixSuffix[1]);
    }

    private Module determineModuleFromPrefix(String prefix) {
        return switch (prefix.charAt(0)) {
            case '%' -> new FlipFlop(prefix.substring(1));
            case '&' -> new Conjunction(prefix.substring(1));
            case 'b' -> new Broadcaster(prefix);
            default -> throw new IllegalStateException(String.format(ERROR_UNKNOWN_MODULE_TYPE, prefix));
        };
    }

    private void fillInConnectedModules(Map.Entry<Module, String> entry, List<Module> availableModules) {
        Module module = entry.getKey();
        List<Module> connectedModules = Arrays.stream(entry.getValue().split(", "))
                .map(token -> determineModuleForToken(token, availableModules))
                .filter(Objects::nonNull)
                .toList();
        module.setConnectedModules(connectedModules);
    }

    private Module determineModuleForToken(String token, List<Module> modules) {
        return modules.stream()
                .filter(module -> module.getToken().equals(token))
                .findFirst()
                .orElseGet(() -> new InvalidModule(token));
    }
}
