package Advent.of.Code.Java.Year_2022;

import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;

import java.util.List;

public class Day_8 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2022/8/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        // Find all of the directories with a total size of at most 100,000. What is the sum of the total sizes of those directories?
        final List<String> inputRows = StringUtilities.splitStringIntoList(LoadUtilities.loadTextFileAsString(fileName), "\n");

        final Integer[][] treeArray = new Integer[inputRows.size()][inputRows.get(0).length()];
        final int height = inputRows.size();
        final int width = inputRows.get(0).length();
        for (int y = 0; y < height; y++) {
            final List<String> row = StringUtilities.splitStringIntoList(inputRows.get(y),"");
            for (int x = 0; x < width; x++) {
                treeArray[x][y] = Integer.parseInt(row.get(x));
            }
        }

        {
            long numberOfVisibleTrees = 0;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    final int currentHeight = treeArray[x][y];
                    if (y == 0 || x == 0 || y == height - 1 || x == width - 1) {
                        numberOfVisibleTrees += 1;
                        continue;
                    }
                    if (!treeNotVisibleAbove(treeArray, currentHeight, x, y) ||
                            !treeNotVisibleLeft(treeArray, currentHeight, x, y) ||
                            !treeNotVisibleBelow(treeArray, currentHeight, x, y, height) ||
                            !treeNotVisibleRight(treeArray, currentHeight, x, y, width)) {
                        numberOfVisibleTrees += 1;
                    }
                }
            }

            LogUtilities.logGreen("Solution: " + numberOfVisibleTrees);
        }

        {
            long currentScenicScore = 0;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    final int currentHeight = treeArray[x][y];
                    long scenicScore = getViewingDistanceAbove(treeArray, currentHeight, x, y) *
                            getViewingDistanceLeft(treeArray, currentHeight, x, y) *
                            getViewingDistanceBelow(treeArray, currentHeight, x, y, height) *
                            getViewingDistanceRight(treeArray, currentHeight, x, y, width);
                    if (scenicScore > currentScenicScore) {
                        currentScenicScore = scenicScore;
                    }
                }
            }

            LogUtilities.logGreen("Solution 2: " + currentScenicScore);
        }
    }
    public boolean treeNotVisibleAbove(final Integer[][] treeArray, final int treeHeight, final int startX, final int startY) {
        for (int y = startY - 1; y >= 0; y--) {
            if (treeArray[startX][y] >= treeHeight) {
                return true;
            }
        }
        return false;
    }
    public boolean treeNotVisibleBelow(final Integer[][] treeArray, final int treeHeight, final int startX, final int startY, final int height) {
        for (int y = startY + 1; y < height; y++) {
            if (treeArray[startX][y] >= treeHeight) {
                return true;
            }
        }
        return false;
    }
    public boolean treeNotVisibleLeft(final Integer[][] treeArray, final int treeHeight, final int startX, final int startY) {
        for (int x = startX - 1; x >= 0; x--) {
            if (treeArray[x][startY] >= treeHeight) {
                return true;
            }
        }
        return false;
    }
    public boolean treeNotVisibleRight(final Integer[][] treeArray, final int treeHeight, final int startX, final int startY, final int width) {
        for (int x = startX + 1; x < width; x++) {
            if (treeArray[x][startY] >= treeHeight) {
                return true;
            }
        }
        return false;
    }

    public long getViewingDistanceAbove(final Integer[][] treeArray, final int treeHeight, final int startX, final int startY) {
        long viewingDistance = 0;
        for (int y = startY - 1; y >= 0; y--) {
            viewingDistance += 1;
            if (treeArray[startX][y] >= treeHeight) {
                return viewingDistance;
            }
        }
        return viewingDistance;
    }
    public long getViewingDistanceBelow(final Integer[][] treeArray, final int treeHeight, final int startX, final int startY, final int height) {
        long viewingDistance = 0;
        for (int y = startY + 1; y < height; y++) {
            viewingDistance += 1;
            if (treeArray[startX][y] >= treeHeight) {
                return viewingDistance;
            }
        }
        return viewingDistance;
    }
    public long getViewingDistanceLeft(final Integer[][] treeArray, final int treeHeight, final int startX, final int startY) {
        long viewingDistance = 0;
        for (int x = startX - 1; x >= 0; x--) {
            viewingDistance += 1;
            if (treeArray[x][startY] >= treeHeight) {
                return viewingDistance;
            }
        }
        return viewingDistance;
    }
    public long getViewingDistanceRight(final Integer[][] treeArray, final int treeHeight, final int startX, final int startY, final int width) {
        long viewingDistance = 0;
        for (int x = startX + 1; x < width; x++) {
            viewingDistance += 1;
            if (treeArray[x][startY] >= treeHeight) {
                return viewingDistance;
            }
        }
        return viewingDistance;
    }
}
