package Advent.of.Code.Java.Year_2023;

import Advent.of.Code.Java.Utility.DataUtilities;
import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.NumberUtilities;
import Advent.of.Code.Java.Utility.SimpleParallelism;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Day_6 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2023/6/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        runSolution1(fileName);
        runSolution2(fileName);
    }

    private void runSolution1(final String fileName) throws Exception {
        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);
        final List<Long> timeTable = DataUtilities.transformData(
                StringUtilities.splitStringByWhitespaceDeduplicated(
                        StringUtilities.removeStartChunk(input.get(0), "Time:")
                ), Long::parseLong);
        final List<Long> distanceTable = DataUtilities.transformData(
                StringUtilities.splitStringByWhitespaceDeduplicated(
                        StringUtilities.removeStartChunk(input.get(1), "Distance:")
                ), Long::parseLong);

        List<Long> solutions = new ArrayList<>();

        for (int index = 0; index < timeTable.size(); index++) {
            long time = timeTable.get(index);
            long distance = distanceTable.get(index);
            long numberOfSolutions = 0;
            for (long timeCount = 1; timeCount < time; timeCount++) {
                if (calculateMillimeters(time, timeCount) > distance) {
                    numberOfSolutions += 1;
                }
            }
            if (numberOfSolutions > 0) {
                solutions.add(numberOfSolutions);
            }
        }

        LogUtilities.logGreen("Solution 1: " + NumberUtilities.multiply(solutions));
    }

    private long calculateMillimeters(long timeAvailable, long holdForAmount) {
        return (timeAvailable - holdForAmount) * holdForAmount;
    }

    private void runSolution2(final String fileName) throws Exception {
        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);
        final long time = Long.parseLong(
                StringUtilities.removeWhitespaceFromString(
                        StringUtilities.removeStartChunk(input.get(0), "Time:")
                ));
        final long distance = Long.parseLong(
                StringUtilities.removeWhitespaceFromString(
                        StringUtilities.removeStartChunk(input.get(1), "Distance:")
                ));
        long numberOfSolutions = 0;
        for (long timeCount = 1; timeCount < time; timeCount++) {
            if (calculateMillimeters(time, timeCount) > distance) {
                numberOfSolutions += 1;
            }
        }

        LogUtilities.logGreen("Solution 2: " + numberOfSolutions);
    }
}
