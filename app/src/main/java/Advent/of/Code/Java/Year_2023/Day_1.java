package Advent.of.Code.Java.Year_2023;

import Advent.of.Code.Java.Utility.DataUtilities;
import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.NumberUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;
import Advent.of.Code.Java.Utility.Structures.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Day_1 implements DayWithExecute {
    private static final List<String> lookForNumbers = DataUtilities.List("1", "2", "3", "4", "5", "6", "7", "8", "9");
    private static final List<String> lookForNumbersAndSpelledOutNumbers = DataUtilities.List(
            "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"
    );
    private static final Map<String, String> replacementNumbers = DataUtilities.Map(
            Pair.Create("one", "1"),
            Pair.Create("two", "2"),
            Pair.Create("three", "3"),
            Pair.Create("four", "4"),
            Pair.Create("five", "5"),
            Pair.Create("six", "6"),
            Pair.Create("seven", "7"),
            Pair.Create("eight", "8"),
            Pair.Create("nine", "9")
    );

    public void run() throws Exception {
        DayUtilities.run("input/2023/1/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        runSolution1(fileName);
        runSolution2(fileName);
    }

    private void runSolution1(final String fileName) throws Exception {
        final List<Long> numbers = new ArrayList<>();
        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);
        for (final String line : input) {
            final String firstNumber = StringUtilities.getFirstMatchingValue(line, lookForNumbers);
            final String secondNumber = StringUtilities.getLastMatchingValue(line, lookForNumbers);
            if (firstNumber != null && secondNumber != null) {
                numbers.add(Long.parseLong(firstNumber + secondNumber));
            }
        }

        LogUtilities.logGreen("Solution 1: " + NumberUtilities.sum(numbers));
    }
    private void runSolution2(final String fileName) throws Exception {
        final List<Long> numbers = new ArrayList<>();
        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);
        for (final String line : input) {
            String firstNumber = StringUtilities.getFirstMatchingValue(line, lookForNumbersAndSpelledOutNumbers);
            String secondNumber = StringUtilities.getLastMatchingValue(line, lookForNumbersAndSpelledOutNumbers);
            if (replacementNumbers.containsKey(firstNumber)) {
                firstNumber = replacementNumbers.get(firstNumber);
            }
            if (replacementNumbers.containsKey(secondNumber)) {
                secondNumber = replacementNumbers.get(secondNumber);
            }
            final long numberToAdd = Long.parseLong(firstNumber + secondNumber);
            numbers.add(numberToAdd);
        }

        LogUtilities.logGreen("Solution 2: " + NumberUtilities.sum(numbers));
    }
}
