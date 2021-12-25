package Advent.of.Code.Java.Year_2021;

import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;

import java.util.List;

public class Day_25 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2021/25/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);
        int height = input.size();
        int width = input.get(0).length();

        final int EMPTY = 0;
        final int RIGHT = 1;
        final int DOWN = 2;

        int[][] grid = new int[width][height];
        int[][] temporaryGrid = new int[width][height];
        int currentY = 0;
        for (final String item : input) {
            List<String> pieces = StringUtilities.splitStringIntoList(item, "");
            for (int x = 0; x < pieces.size(); x++) {
                final String piece = pieces.get(x);
                int value = EMPTY;
                if (piece.equals(">")) {
                    value = RIGHT;
                } else if (piece.equals("v")) {
                    value = DOWN;
                }
                grid[x][currentY] = value;
                temporaryGrid[x][currentY] = value;
            }
            currentY += 1;
        }

        boolean changes = true;
        int step = 0;
        while (changes) {
            changes = false;
            step += 1;

            // Right
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int value = grid[x][y];
                    if (value == RIGHT) {
                        int nextX = x + 1;
                        if (nextX >= width) {
                            nextX -= width;
                        }
                        int nextY = y;
                        int nextValue = grid[nextX][nextY];
                        if (nextValue == EMPTY) {
                            temporaryGrid[x][y] = EMPTY;
                            temporaryGrid[nextX][nextY] = value;
                            changes = true;
                        }
                    }
                }
            }
            // Copy values to grid
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    grid[x][y] = temporaryGrid[x][y];
                }
            }
            // Down
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int value = grid[x][y];
                    if (value == DOWN) {
                        int nextX = x;
                        int nextY = y + 1;
                        if (nextY >= height) {
                            nextY -= height;
                        }
                        int nextValue = grid[nextX][nextY];
                        if (nextValue == EMPTY) {
                            temporaryGrid[x][y] = EMPTY;
                            temporaryGrid[nextX][nextY] = value;
                            changes = true;
                        }
                    }
                }
            }
            // Copy values to grid
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    grid[x][y] = temporaryGrid[x][y];
                }
            }
        }

        LogUtilities.logGreen("Solution: " + step);
    }
}
