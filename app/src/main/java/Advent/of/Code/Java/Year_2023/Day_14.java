package Advent.of.Code.Java.Year_2023;

import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.NumberUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Day_14 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2023/14/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        runSolution1(fileName);
        runSolution2(fileName);
    }

    enum GridObject {
        EMPTY,
        FOUNDATION,
        ROCK
    }

    private void runSolution1(final String fileName) throws Exception {
        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);
        final int height = input.size();
        final int width = input.getFirst().length();
        final GridObject[][] grid = parseInput(input, width, height);

        shiftRocks(grid, 0, -1, width, height);

        LogUtilities.logGreen("Solution 1: " + calculateSum(grid, height));
    }

    private GridObject[][] parseInput(final List<String> input, final int width, final int height) {
        final GridObject[][] grid = new GridObject[width][height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                final String character = StringUtilities.getStringChunk(input.get(y), x, 1);
                if (character.equals("O")) {
                    grid[x][y] = GridObject.ROCK;
                } else if (character.equals("#")) {
                    grid[x][y] = GridObject.FOUNDATION;
                } else {
                    grid[x][y] = GridObject.EMPTY;
                }
            }
        }
        return grid;
    }

    private void shiftRocks(final GridObject[][] grid, final int xDirection, final int yDirection, final int width, final int height) {
        boolean rocksShifting = true;
        while (rocksShifting) {
            rocksShifting = false;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < height; x++) {
                    if (grid[x][y] == GridObject.ROCK) {
                        if (x + xDirection >= 0 && x + xDirection < width && y + yDirection >= 0 && y + yDirection < height && grid[x + xDirection][y  + yDirection] == GridObject.EMPTY) {
                            grid[x + xDirection][y + yDirection] = GridObject.ROCK;
                            grid[x][y] = GridObject.EMPTY;
                            rocksShifting = true;
                        }
                    }
                }
            }
        }
    }

    private long calculateSum(final GridObject[][] grid, final int height) {
        long sum = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < height; x++) {
                if (grid[x][y] == GridObject.ROCK) {
                    sum += height - y;
                }
            }
        }
        return sum;
    }

    private void runSolution2(final String fileName) throws Exception {
        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);
        final int height = input.size();
        final int width = input.getFirst().length();
        final GridObject[][] grid = parseInput(input, width, height);

        final long limitCycles = 2000;
        final long cycles = 1000000000;
        final HashMap<Long, List<Long>> sumHits = new HashMap<>();
        for (long index = 0; index < limitCycles; index++) {
            shiftRocks(grid, 0, -1, width, height);
            shiftRocks(grid, -1, 0, width, height);
            shiftRocks(grid, 0, 1, width, height);
            shiftRocks(grid, 1, 0, width, height);
            if (index > 1000) {
                final long sum = calculateSum(grid, height);
                final List<Long> hitIndexes = sumHits.getOrDefault(sum, new ArrayList<>());
                hitIndexes.add(index+1);
                sumHits.put(sum, hitIndexes);
                if (index % 1000 == 0) {
                    LogUtilities.logPurple(NumberUtilities.formatNumber(index) + " / " + NumberUtilities.formatNumber(cycles) + " - " + NumberUtilities.formatNumber(sum));
                }
            }
        }

        long winningSum = -1;
        for (final long sumKey : sumHits.keySet()) {
            final List<Long> sumList = sumHits.get(sumKey);
            long start = sumList.get(0);
            long increaseBy = sumList.get(1) - start;
            while (start < cycles) {
                start += increaseBy;
            }
            if (start == cycles) {
                winningSum = sumKey;
                break;
            }
        }

        LogUtilities.logGreen("Solution 2: " + winningSum);
    }
}
