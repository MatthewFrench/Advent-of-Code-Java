package Advent.of.Code.Java.Year_2022;

import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;

import java.util.ArrayList;
import java.util.List;

public class Day_25 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2022/25/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        List<String> splitInput = StringUtilities.splitStringIntoList(LoadUtilities.loadTextFileAsString(fileName), "\n");
        List<Long> decimalNumbers = new ArrayList<>();
        for (final String input : splitInput) {
            decimalNumbers.add(parseSnafuStringToDecimal(input));
        }

        long decimalSum = 0;
        for (final long decimalNumber : decimalNumbers) {
            decimalSum += decimalNumber;
        }

        LogUtilities.logGreen("Solution: " + parseDecimalToSnafuString(decimalSum));
    }
    long parseSnafuStringToDecimal(final String input) {
        long multiplier = 1;
        long sum = 0;
        for (int i = 0; i < input.length(); i++) {
            final String character = StringUtilities.getStringChunk(input, input.length() - i - 1, 1);
            if (character.equals("1")) {
                sum += multiplier;
            } else if (character.equals("2")) {
                sum += 2 * multiplier;
            } else if (character.equals("-")) {
                sum -= multiplier;
            } else if (character.equals("=")) {
                sum -= 2 * multiplier;
            }
            multiplier *= 5;
        }
        return sum;
    }

    String parseDecimalToSnafuString(final long decimal) {
        // Calculate base 5 multipliers from decimal
        long maxPower = (long) (Math.log(decimal)/Math.log(5));
        long runningSum = decimal;
        List<Long> snafuNumbers = new ArrayList<>();
        for (long power = maxPower; power >= 0; power -= 1) {
            long powerAmount = (long) Math.pow(5, power);
            long multiplier = runningSum / powerAmount;
            snafuNumbers.add(multiplier);
            runningSum -= powerAmount * multiplier;
        }
        // Go through the list and reduce any numbers > 2 by making
        // them negative and increasing the size of the prior number
        for (int i = snafuNumbers.size() - 1; i >= 0; i--) {
            long currentNumber = snafuNumbers.get(i);
            if (currentNumber > 2) {
                currentNumber = currentNumber - 5;
                snafuNumbers.set(i, currentNumber);
                if (i > 0) {
                    snafuNumbers.set(i-1, snafuNumbers.get(i-1)+1);
                } else {
                    snafuNumbers.add(0, 1L);
                }
            }
        }
        // Parse the number array into a string
        StringBuilder output = new StringBuilder();
        for (final long value : snafuNumbers) {
            if (value == -2) {
                output.append("=");
            } else if (value == -1) {
                output.append("-");
            } else {
                output.append(value);
            }
        }

        return output.toString();
    }
}
