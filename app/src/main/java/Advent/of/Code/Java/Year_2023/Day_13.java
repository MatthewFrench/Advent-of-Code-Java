package Advent.of.Code.Java.Year_2023;

import Advent.of.Code.Java.Utility.DataUtilities;
import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.NumberUtilities;
import Advent.of.Code.Java.Utility.SimpleParallelism;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;
import Advent.of.Code.Java.Utility.TimeUtilities;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Day_13 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2023/13/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        runSolution1(fileName);
        runSolution2(fileName);
    }

    private void runSolution1(final String fileName) throws Exception {
        final List<String> chunks = StringUtilities.splitStringIntoList(LoadUtilities.loadTextFileAsString(fileName), "\n\n");

        long totalSum = 0;
        for (final String chunk : chunks) {
            // Todo: Make a way of splitting a text grid into a grid array object easily
            final List<String> rows = StringUtilities.splitStringIntoList(chunk, "\n");
            final int height = rows.size();
            final int width = rows.getFirst().length();
            final Boolean[][] grid = new Boolean[width][height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    grid[x][y] = StringUtilities.getStringChunk(rows.get(y), x, 1).equals("#");
                }
            }
            // Check first for reflection across horizontal line ---
            int rowsAboveHorizontalMirror = 0;
            yLoop:
            for (int y = 0; y < height-1; y++) {
                int maxYDistance = Math.min(y+1, (height-1) - y);
                for (int distance = 0; distance < maxYDistance; distance++) {
                    for (int x = 0; x < width; x++) {
                        if (grid[x][y - distance] != grid[x][y + distance + 1]) {
                            continue yLoop;
                        }
                    }
                }
                rowsAboveHorizontalMirror = y + 1;
            }
            totalSum += rowsAboveHorizontalMirror * 100L;

            // Check for reflection across vertical line |
            int columnsLeftOfVerticalMirror = 0;
            xLoop:
            for (int x = 0; x < width-1; x++) {
                int maxXDistance = Math.min(x+1, (width-1) - x);
                for (int distance = 0; distance < maxXDistance; distance++) {
                    for (int y = 0; y < height; y++) {
                        if (grid[x-distance][y] != grid[x + distance + 1][y]) {
                            continue xLoop;
                        }
                    }
                }
                columnsLeftOfVerticalMirror = x + 1;
            }
            totalSum += columnsLeftOfVerticalMirror;
        }

        LogUtilities.logGreen("Solution 1: " + totalSum);
    }

    private void runSolution2(final String fileName) throws Exception {

        final List<String> chunks = StringUtilities.splitStringIntoList(LoadUtilities.loadTextFileAsString(fileName), "\n\n");

        long totalSum = 0;
        for (final String chunk : chunks) {
            // Todo: Make a way of splitting a text grid into a grid array object easily
            final List<String> rows = StringUtilities.splitStringIntoList(chunk, "\n");
            final int height = rows.size();
            final int width = rows.getFirst().length();
            final Boolean[][] grid = new Boolean[width][height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    grid[x][y] = StringUtilities.getStringChunk(rows.get(y), x, 1).equals("#");
                }
            }
            // Check first for reflection across horizontal line ---
            int rowsAboveHorizontalMirror = 0;
            yLoop:
            for (int y = 0; y < height-1; y++) {
                int maxYDistance = Math.min(y+1, (height-1) - y);
                int mistakes = 0;
                for (int distance = 0; distance < maxYDistance; distance++) {
                    for (int x = 0; x < width; x++) {
                        if (grid[x][y - distance] != grid[x][y + distance + 1]) {
                            mistakes += 1;
                        }
                        if (mistakes > 1) {
                            continue yLoop;
                        }
                    }
                }
                if (mistakes == 1) {
                    rowsAboveHorizontalMirror = y + 1;
                }
            }
            totalSum += rowsAboveHorizontalMirror * 100L;

            // Check for reflection across vertical line |
            int columnsLeftOfVerticalMirror = 0;
            xLoop:
            for (int x = 0; x < width-1; x++) {
                int maxXDistance = Math.min(x+1, (width-1) - x);
                int mistakes = 0;
                for (int distance = 0; distance < maxXDistance; distance++) {
                    for (int y = 0; y < height; y++) {
                        if (grid[x-distance][y] != grid[x + distance + 1][y]) {
                            mistakes += 1;
                        }
                        if (mistakes > 1) {
                            continue xLoop;
                        }
                    }
                }
                if (mistakes == 1) {
                    columnsLeftOfVerticalMirror = x + 1;
                }
            }
            totalSum += columnsLeftOfVerticalMirror;
        }

        LogUtilities.logGreen("Solution 2: " + totalSum);
    }
}
