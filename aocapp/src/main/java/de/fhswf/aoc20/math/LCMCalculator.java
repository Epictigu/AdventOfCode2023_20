package de.fhswf.aoc20.math;

import javax.annotation.Nonnull;

/**
 * Calculates the lowest common multiple.
 */
public class LCMCalculator {

    /**
     * Calculates the lowest common multiple for as many numbers as provided, by calling the lcm algorithm for 2 numbers over and over, until only
     * a single number is left.
     *
     * @param numbers the numbers that the LCM is to be calculated for
     * @return the lowest common multiple for all provided numbers
     */
    public long calculateLCMForMultipleNumbers(@Nonnull Integer... numbers) {
        if (numbers.length == 0) {
            return 0;
        }
        if (numbers.length == 1) {
            return numbers[0];
        }

        long lcmResult = calculateLCM(numbers[0], numbers[1]);
        for (int i = 2; i < numbers.length; i++) {
            lcmResult = calculateLCM(lcmResult, numbers[i]);
        }
        return lcmResult;
    }

    /**
     * Calculates the lowest common multiple for the two provided numbers, by using the GCD-algorithm provided in {@link #calculateGCD(long, long)}.
     *
     * @param firstNumber  the first number for the LCM calculation
     * @param secondNumber the second number for the LCM calculation
     * @return the lowest common multiple for the two numbers
     */
    public long calculateLCM(long firstNumber, long secondNumber) {
        return firstNumber * (secondNumber / calculateGCD(firstNumber, secondNumber));
    }

    private long calculateGCD(long firstNumber, long secondNumber) {
        while (secondNumber > 0) {
            long temp = secondNumber;
            secondNumber = firstNumber % secondNumber;
            firstNumber = temp;
        }
        return firstNumber;
    }
}
