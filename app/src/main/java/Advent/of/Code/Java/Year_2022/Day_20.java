package Advent.of.Code.Java.Year_2022;

import Advent.of.Code.Java.Utility.DataUtilities;
import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day_20 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2022/20/", this);
    }

    @ToString
    @AllArgsConstructor
    static class Number {
        long value;
    }

    public void executeWithInput(final String fileName) throws Exception {
        {
            List<Number> originalInput = new ArrayList<>(
                    StringUtilities.splitStringIntoList(LoadUtilities.loadTextFileAsString(fileName), "\n").stream().map((value) -> new Number(Long.parseLong(value))).collect(Collectors.toList()
                    )
            );
            List<Number> transformedInput = new ArrayList<>(originalInput);
            for (final Number number : originalInput) {
                long index = transformedInput.indexOf(number);
                transformedInput.remove(number);
                index += number.value;
                if (index < 0) {
                    index += transformedInput.size() * ((transformedInput.size()-index)/transformedInput.size());
                }
                if (index >= transformedInput.size()) {
                    index -= transformedInput.size() * (index/transformedInput.size());
                }
                if (index == 0) {
                    index = transformedInput.size();
                }
                transformedInput.add((int) index, new Number(number.value));
            }
            long indexOfValueZero = 0;
            for (Number number : transformedInput) {
                if (number.value != 0) {
                    indexOfValueZero += 1;
                } else {
                    break;
                }
            }
            final List<Long> lookUpIndexes = DataUtilities.List(1000L + indexOfValueZero, 2000L + indexOfValueZero, 3000L + indexOfValueZero);
            long total = 0;
            for (long index : lookUpIndexes) {
                while (index < 0) {
                    index += originalInput.size();
                }
                while (index >= originalInput.size()) {
                    index -= originalInput.size();
                }
                total += transformedInput.get((int) index).value;
            }
            LogUtilities.logGreen("Solution: " + total);
        }
        {
            List<Number> originalInput = new ArrayList<>(
                    StringUtilities.splitStringIntoList(LoadUtilities.loadTextFileAsString(fileName), "\n").stream().map((value) -> new Number(Long.parseLong(value)*811589153)).collect(Collectors.toList()
                    )
            );
            List<Number> transformedInput = new ArrayList<>(originalInput);
            for (int i = 0; i < 10; i++) {
                for (final Number number : originalInput) {
                    long index = transformedInput.indexOf(number);
                    transformedInput.remove(number);
                    index += number.value;
                    if (index < 0) {
                        index += transformedInput.size() * ((transformedInput.size()-index)/transformedInput.size());
                    }
                    if (index >= transformedInput.size()) {
                        index -= transformedInput.size() * (index/transformedInput.size());
                    }
                    transformedInput.add((int) index, number);
                }
            }
            long indexOfValueZero = 0;
            for (Number number : transformedInput) {
                if (number.value != 0) {
                    indexOfValueZero += 1;
                } else {
                    break;
                }
            }
            final List<Long> lookUpIndexes = DataUtilities.List(1000L + indexOfValueZero, 2000L + indexOfValueZero, 3000L + indexOfValueZero);
            long total = 0;
            for (long index : lookUpIndexes) {
                while (index < 0) {
                    index += originalInput.size();
                }
                while (index >= originalInput.size()) {
                    index -= originalInput.size();
                }
                total += transformedInput.get((int) index).value;
            }
            LogUtilities.logGreen("Solution 2: " + total);
        }
    }
}
