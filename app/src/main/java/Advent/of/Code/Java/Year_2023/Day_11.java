package Advent.of.Code.Java.Year_2023;

import Advent.of.Code.Java.Utility.DataUtilities;
import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;
import Advent.of.Code.Java.Utility.Structures.Pair;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Day_11 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2023/11/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        runSolution1(fileName);
        runSolution2(fileName);
    }

    private void runSolution1(final String fileName) throws Exception {
        final List<List<String>> input = DataUtilities.transformData(LoadUtilities.loadTextFileAsList(fileName), s -> StringUtilities.splitStringIntoList(s, ""));
        LogUtilities.logGreen("Solution 1: " + getSumOfLengths(input, 1));
    }

    private void runSolution2(final String fileName) throws Exception {
        final List<List<String>> input = DataUtilities.transformData(LoadUtilities.loadTextFileAsList(fileName), s -> StringUtilities.splitStringIntoList(s, ""));
        // Part 2 is replacing the column with 1000000, so subtract one
        LogUtilities.logGreen("Solution 2: " + getSumOfLengths(input, 1000000 - 1));
    }

    private long getSumOfLengths(List<List<String>> input, long distanceForEmptyRows) {
        List<Pair<Long, Long>> galaxyLocations = new ArrayList<>();
        List<Long> expandedYLocations = new ArrayList<>();
        List<Long> expandedXLocations = new ArrayList<>();

        for (int y = 0; y < input.size(); y++) {
            List<String> rowItems = input.get(y);
            boolean hasItem = false;
            for (int x = 0; x < rowItems.size(); x++) {
                if (rowItems.get(x).equals("#")) {
                    galaxyLocations.add(new Pair<>((long)x, (long)y));
                    hasItem = true;
                }
            }
            if (!hasItem) {
                expandedYLocations.add((long)y);
            }
        }

        for (int x = 0; x < input.get(0).size(); x++) {
            boolean hasItem = false;
            for (int y = 0; y < input.size(); y++) {
                if (input.get(y).get(x).equals("#")) {
                    hasItem = true;
                }
            }
            if (!hasItem) {
                expandedXLocations.add((long)x);
            }
        }

        long sumOfShortestPaths = 0;
        for (int index1 = 0; index1 < galaxyLocations.size(); index1++) {
            final Pair<Long, Long> location1 = galaxyLocations.get(index1);
            for (int index2 = index1 + 1; index2 < galaxyLocations.size(); index2++) {
                final Pair<Long, Long> location2 = galaxyLocations.get(index2);
                long shortestX = Math.abs(location2.getKey() - location1.getKey());
                long shortestY = Math.abs(location2.getValue() - location1.getValue());
                for (final long expandedX : expandedXLocations) {
                    if ((expandedX > location1.getKey() && expandedX < location2.getKey()) || expandedX < location1.getKey() && expandedX > location2.getKey()) {
                        shortestX += distanceForEmptyRows;
                    }
                }
                for (final long expandedY : expandedYLocations) {
                    if ((expandedY > location1.getValue() && expandedY < location2.getValue()) || expandedY < location1.getValue() && expandedY > location2.getValue()) {
                        shortestY += distanceForEmptyRows;
                    }
                }
                sumOfShortestPaths += shortestX + shortestY;
            }
        }
        return sumOfShortestPaths;
    }
}
