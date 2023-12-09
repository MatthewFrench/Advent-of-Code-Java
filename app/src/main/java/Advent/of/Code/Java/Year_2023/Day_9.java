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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Day_9 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2023/9/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        runSolution1(fileName);
        runSolution2(fileName);
    }

    private void runSolution1(final String fileName) throws Exception {
        final List<List<Long>> sequences = DataUtilities.transformData(LoadUtilities.loadTextFileAsList(fileName), s -> DataUtilities.transformData(StringUtilities.splitStringIntoList(s, " "), Long::parseLong));
        long sumOfExtrapolatedValues = 0;

        for (final List<Long> sequence : sequences) {
            final List<List<Long>> differences = new ArrayList<>();
            differences.add(sequence);
            while (!isAllZeros(differences.get(differences.size() - 1))) {
                final List<Long> checkSequence = differences.get(differences.size() - 1);
                // Add a new list that is the differences of the prior
                final List<Long> newDifference = new ArrayList<>();
                for (int index = 1; index < checkSequence.size(); index++) {
                    newDifference.add(checkSequence.get(index) - checkSequence.get(index - 1));
                }
                differences.add(newDifference);
            }
            // Now add to the differences
            List<Long> currentDifference = differences.get(differences.size() - 2);
            // Add another number to the difference
            currentDifference.add(currentDifference.get(currentDifference.size() - 1));
            for (int index = differences.size() - 3; index >= 0; index--) {
                final List<Long> nextDifference = differences.get(index);
                nextDifference.add(nextDifference.get(nextDifference.size() - 1) + currentDifference.get(currentDifference.size() - 1));
                currentDifference = nextDifference;
            }
            sumOfExtrapolatedValues += currentDifference.get(currentDifference.size() - 1);
        }

        LogUtilities.logGreen("Solution 1: " + sumOfExtrapolatedValues);
    }

    private boolean isAllZeros(final List<Long> sequence) {
        for (final long number : sequence) {
            if (number != 0) {
                return false;
            }
        }
        return true;
    }

    private void runSolution2(final String fileName) throws Exception {
        final List<List<Long>> sequences = DataUtilities.transformData(
                LoadUtilities.loadTextFileAsList(fileName),
                s -> DataUtilities.transformData(StringUtilities.splitStringIntoList(s, " "), Long::parseLong)
        );
        long sumOfExtrapolatedValues = 0;

        for (final List<Long> sequence : sequences) {
            // Reverse it to handle the differences at the other end
            Collections.reverse(sequence);
            final List<List<Long>> differences = new ArrayList<>();
            differences.add(sequence);
            while (!isAllZeros(differences.get(differences.size() - 1))) {
                final List<Long> checkSequence = differences.get(differences.size() - 1);
                // Add a new list that is the differences of the prior
                final List<Long> newDifference = new ArrayList<>();
                for (int index = 1; index < checkSequence.size(); index++) {
                    newDifference.add(checkSequence.get(index) - checkSequence.get(index - 1));
                }
                differences.add(newDifference);
            }
            // Now add to the differences
            List<Long> currentDifference = differences.get(differences.size() - 2);
            // Add another number to the difference
            currentDifference.add(currentDifference.get(currentDifference.size() - 1));
            for (int index = differences.size() - 3; index >= 0; index--) {
                final List<Long> nextDifference = differences.get(index);
                nextDifference.add(nextDifference.get(nextDifference.size() - 1) + currentDifference.get(currentDifference.size() - 1));
                currentDifference = nextDifference;
            }
            sumOfExtrapolatedValues += currentDifference.get(currentDifference.size() - 1);
        }

        LogUtilities.logGreen("Solution 2: " + sumOfExtrapolatedValues);
    }
}
