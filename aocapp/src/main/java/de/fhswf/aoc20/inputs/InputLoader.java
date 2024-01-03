package de.fhswf.aoc20.inputs;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads the input from the given input file in the resources archive with the name defined in the {@link #INPUT_FILE_NAME file name variable}.
 */
public class InputLoader {

    private static final String ERROR_INPUT_FILE_NOT_FOUND = "The input file could not be found.";

    private static final String INPUT_FILE_NAME = "input.txt";

    /**
     * Loads all lines from the given input file. The file must exist, otherwise the rest of the program wouldn't work.
     *
     * @return the list of lines found in the input file
     */
    @Nonnull
    public List<String> loadInputs() {
        InputStream inputStream = fetchInputStream();
        return collectLinesFromStream(inputStream);
    }

    private InputStream fetchInputStream() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(INPUT_FILE_NAME);
        if (inputStream == null) {
            throw new IllegalStateException(ERROR_INPUT_FILE_NOT_FOUND);
        }
        return inputStream;
    }

    private List<String> collectLinesFromStream(InputStream inputStream) {
        List<String> foundLines = new ArrayList<>();

        try (InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {
            String line;
            while ((line = reader.readLine()) != null) {
                foundLines.add(line);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return foundLines;
    }
}
