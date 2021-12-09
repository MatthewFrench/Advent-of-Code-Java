package Advent.of.Code.Java.Year_2021;

import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day_9 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2021/9/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        {
            final List<String> input = LoadUtilities.loadTextFileAsList(fileName);

            int height = input.size();
            int width = input.get(0).length();

            long[][] grid = new long[width][height];
            int currentY = 0;
            for (final String row : input) {
                final List<String> letters = StringUtilities.splitStringIntoList(row, "");
                for (int x = 0; x < letters.size(); x++) {
                    final long value = Long.parseLong(letters.get(x));
                    grid[x][currentY] = value;
                }
                currentY += 1;
            }

            final List<Long> lowPoints = new ArrayList<>();
            long totalCount = 0;
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    long value = grid[x][y];
                    if (x > 0 && value >= grid[x - 1][y]) {
                        continue;
                    }
                    if (x < width - 1 && value >= grid[x + 1][y]) {
                        continue;
                    }
                    if (y > 0 && value >= grid[x][y - 1]) {
                        continue;
                    }
                    if (y < height - 1 && value >= grid[x][y + 1]) {
                        continue;
                    }
                    totalCount += value + 1;
                }
            }

            LogUtilities.logGreen("Solution: " + totalCount);
        }
        {

            final List<String> input = LoadUtilities.loadTextFileAsList(fileName);

            int height = input.size();
            int width = input.get(0).length();

            long[][] grid = new long[width][height];
            boolean[][] basinGrid = new boolean[width][height];
            int currentY = 0;
            for (final String row : input) {
                final List<String> letters = StringUtilities.splitStringIntoList(row, "");
                for (int x = 0; x < letters.size(); x++) {
                    final long value = Long.parseLong(letters.get(x));
                    grid[x][currentY] = value;
                    basinGrid[x][currentY] = false;
                }
                currentY += 1;
            }

            final List<Long> basinCounts = new ArrayList<>();
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    long value = grid[x][y];
                    if (x > 0 && value >= grid[x-1][y]) {
                        continue;
                    }
                    if (x < width-1 && value >= grid[x+1][y]) {
                        continue;
                    }
                    if (y > 0 && value >= grid[x][y-1]) {
                        continue;
                    }
                    if (y < height-1 && value >= grid[x][y+1]) {
                        continue;
                    }
                    // This is a low point, do a basin count
                    basinCounts.add(getBasinCount(x, y, width, height, grid, basinGrid));
                }
            }

            basinCounts.sort(Long::compareTo);
            basinCounts.sort(Collections.reverseOrder());
            LogUtilities.logGreen("Solution: " + (basinCounts.get(0) * basinCounts.get(1) * basinCounts.get(2)));
        }
    }
    static long getBasinCount(int x, int y, int width, int height, long[][] grid, boolean[][] basinGrid) {
        if (basinGrid[x][y] || grid[x][y] == 9) {
            return 0;
        }
        long count = 0;
        count += 1;
        basinGrid[x][y] = true;
        if (x > 0) {
            count += getBasinCount(x-1, y, width, height, grid, basinGrid);
        }
        if (y > 0) {
            count += getBasinCount(x, y-1, width, height, grid, basinGrid);
        }
        if (x < width - 1) {
            count += getBasinCount(x+1, y, width, height, grid, basinGrid);
        }
        if (y < height - 1) {
            count += getBasinCount(x, y+1, width, height, grid, basinGrid);
        }
        return count;
    }
}
