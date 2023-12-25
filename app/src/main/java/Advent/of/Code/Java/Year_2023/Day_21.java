package Advent.of.Code.Java.Year_2023;

import Advent.of.Code.Java.Utility.DataUtilities;
import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.NumberUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Day_21 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2023/21/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        runSolution1(fileName);
        runSolution2(fileName);
    }

    private void runSolution1(final String fileName) throws Exception {
        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);
        LogUtilities.logGreen("Solution 1: " + getReachableGardenPlots(input, fileName.contains("sample") ? 6 : 64));
    }

    private long getReachableGardenPlots(final List<String> input, final int stepCount) {
        final int height = input.size();
        final int width = input.getFirst().length();
        int startX = -1;
        int startY = -1;
        final Boolean[][] grid = new Boolean[width][height];
        final Long[][] reachDistance = new Long[width][height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                reachDistance[x][y] = -1L;
                final String character = StringUtilities.getStringChunk(input.get(y), x, 1);
                grid[x][y] = character.equals("#");
                if (character.equals("S")) {
                    startX = x;
                    startY = y;
                }
            }
        }
        reachDistance[startX][startY] = 0L;

        boolean isChanging = true;
        while (isChanging) {
            isChanging = false;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    long distanceAway = reachDistance[x][y];
                    if (reachDistance[x][y] != -1 && distanceAway + 1 <= stepCount) {
                        if (x > 0 && !grid[x - 1][y] && (reachDistance[x - 1][y] == -1 || reachDistance[x - 1][y] > distanceAway + 1)) {
                            reachDistance[x - 1][y] = distanceAway + 1;
                            isChanging = true;
                        } else if (x <= 0 && !grid[(x - 1) + width][y] && (reachDistance[(x - 1) + width][y] == -1 || reachDistance[(x - 1) + width][y] > distanceAway + 1)) {
                            reachDistance[(x - 1) + width][y] = distanceAway + 1;
                            isChanging = true;
                        }
                        if (y > 0 && !grid[x][y - 1] && (reachDistance[x][y - 1] == -1 || reachDistance[x][y - 1] > distanceAway + 1)) {
                            reachDistance[x][y - 1] = distanceAway + 1;
                            isChanging = true;
                        } else if (y <= 0 && !grid[x][(y - 1) + height] && (reachDistance[x][(y - 1) + height] == -1 || reachDistance[x][(y - 1) + height] > distanceAway + 1)) {
                            reachDistance[x][(y - 1) + height] = distanceAway + 1;
                            isChanging = true;
                        }
                        if (x < width - 1 && !grid[x + 1][y] && (reachDistance[x + 1][y] == -1 || reachDistance[x + 1][y] > distanceAway + 1)) {
                            reachDistance[x + 1][y] = distanceAway + 1;
                            isChanging = true;
                        } else if (x >= width - 1 && !grid[(x + 1) - width][y] && (reachDistance[(x + 1) - width][y] == -1 || reachDistance[(x + 1) - width][y] > distanceAway + 1)) {
                            reachDistance[(x + 1) - width][y] = distanceAway + 1;
                            isChanging = true;
                        }
                        if (y < height - 1 && !grid[x][y + 1] && (reachDistance[x][y + 1] == -1 || reachDistance[x][y + 1] > distanceAway + 1)) {
                            reachDistance[x][y + 1] = distanceAway + 1;
                            isChanging = true;
                        } else if (y >= height - 1 && !grid[x][(y + 1) - height] && (reachDistance[x][(y + 1) - height] == -1 || reachDistance[x][(y + 1) - height] > distanceAway + 1)) {
                            reachDistance[x][(y + 1) - height] = distanceAway + 1;
                            isChanging = true;
                        }
                    }
                }
            }
        }

        long steppableCount = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                long distance = reachDistance[x][y];
                if (distance != -1) {
                    boolean stepCountIsEven = stepCount % 2 == 0;
                    boolean distanceIsEven = distance % 2 == 0;
                    if (stepCountIsEven == distanceIsEven && !grid[x][y]) {
                        steppableCount += 1;
                    }
                }
            }
        }
        return steppableCount;
    }

    private void runSolution2(final String fileName) throws Exception {
        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);
        LogUtilities.logGreen("Solution 2: " + getReachableGardenPlots(input, fileName.contains("sample") ? 50 : 26501365));
    }
}
