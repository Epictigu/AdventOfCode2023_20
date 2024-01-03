package de.fhswf.aoc20.math;

public class LCMCalculator {

    public long calculateLCMForMultipleNumbers(Integer... numbers) {
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

    public long calculateLCM(long firstNumber, long secondNumber) {
        return firstNumber * (secondNumber / calculateGCD(firstNumber, secondNumber));
    }

    public long calculateGCD(long firstNumber, long secondNumber) {
        while (secondNumber > 0) {
            long temp = secondNumber;
            secondNumber = firstNumber % secondNumber;
            firstNumber = temp;
        }
        return firstNumber;
    }
}
