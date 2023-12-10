package Advent.of.Code.Java.Year_2023;

import Advent.of.Code.Java.Utility.DataUtilities;
import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;

import java.util.ArrayList;
import java.util.List;

public class Day_10 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2023/10/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        runSolution1(fileName);
        runSolution2(fileName);
    }

    enum PipeType {
        VERTICAL,
        HORIZONTAL,
        TOP_TO_RIGHT,
        TOP_TO_LEFT,
        BOTTOM_TO_LEFT,
        BOTTOM_TO_RIGHT,
        EMPTY
    }

    class GridItem {
        int x;
        int y;
        long distance;
        PipeType pipeType;
        boolean mainLoop = false;
    }

    private void runSolution1(final String fileName) throws Exception {
        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);

        GridItem startingItem = null;
        final GridItem[][] grid = new GridItem[input.get(0).length()][input.size()];
        {
            int y = 0;
            for (final String rowInput : input) {
                final List<String> row = StringUtilities.splitStringIntoList(rowInput, "");
                int x = 0;
                for (final String piece : row) {
                    final GridItem gridItem = new GridItem();
                    gridItem.x = x;
                    gridItem.y = y;
                    gridItem.distance = -1;
                    if (piece.equals("S")) {
                        startingItem = gridItem;
                        gridItem.distance = 0;
                    } else if (piece.equals("|")) {
                        gridItem.pipeType = PipeType.VERTICAL;
                    } else if (piece.equals("-")) {
                        gridItem.pipeType = PipeType.HORIZONTAL;
                    } else if (piece.equals("L")) {
                        gridItem.pipeType = PipeType.TOP_TO_RIGHT;
                    } else if (piece.equals("J")) {
                        gridItem.pipeType = PipeType.TOP_TO_LEFT;
                    } else if (piece.equals("7")) {
                        gridItem.pipeType = PipeType.BOTTOM_TO_LEFT;
                    } else if (piece.equals("F")) {
                        gridItem.pipeType = PipeType.BOTTOM_TO_RIGHT;
                    } else if (piece.equals(".")) {
                        gridItem.pipeType = PipeType.EMPTY;
                    }
                    grid[x][y] = gridItem;
                    x++;
                }
                y++;
            }
        }

        // Fill in the S type
        if (aboveConnected(startingItem.x, startingItem.y, grid) && belowConnected(startingItem.x, startingItem.y, grid)) {
            startingItem.pipeType = PipeType.VERTICAL;
        } else if (leftConnected(startingItem.x, startingItem.y, grid) && rightConnected(startingItem.x, startingItem.y, grid)) {
            startingItem.pipeType = PipeType.HORIZONTAL;
        } else if (aboveConnected(startingItem.x, startingItem.y, grid) && rightConnected(startingItem.x, startingItem.y, grid)) {
            startingItem.pipeType = PipeType.TOP_TO_RIGHT;
        } else if (aboveConnected(startingItem.x, startingItem.y, grid) && leftConnected(startingItem.x, startingItem.y, grid)) {
            startingItem.pipeType = PipeType.TOP_TO_LEFT;
        } else if (belowConnected(startingItem.x, startingItem.y, grid) && leftConnected(startingItem.x, startingItem.y, grid)) {
            startingItem.pipeType = PipeType.BOTTOM_TO_LEFT;
        } else if (belowConnected(startingItem.x, startingItem.y, grid) && rightConnected(startingItem.x, startingItem.y, grid)) {
            startingItem.pipeType = PipeType.BOTTOM_TO_RIGHT;
        } else {
            startingItem.pipeType = PipeType.EMPTY;
        }

        // Now flood fill continually to find the farthest spot
        final List<GridItem> floodFillItems = new ArrayList<>();
        floodFillItems.add(startingItem);
        long largestDistance = 0;
        while (!floodFillItems.isEmpty()) {
            final GridItem item = floodFillItems.remove(0);
            final int x = item.x;
            final int y = item.y;
            if (canMoveUp(item, grid, false)) {
                final GridItem nextItem = grid[x][y - 1];
                if (nextItem.distance == -1 || nextItem.distance > item.distance + 1) {
                    nextItem.distance = item.distance + 1;
                    largestDistance = Math.max(largestDistance, nextItem.distance);
                    floodFillItems.add(nextItem);
                }
            }
            if (canMoveDown(item, grid, false)) {
                final GridItem nextItem = grid[x][y + 1];
                if (nextItem.distance == -1 || nextItem.distance > item.distance + 1) {
                    nextItem.distance = item.distance + 1;
                    largestDistance = Math.max(largestDistance, nextItem.distance);
                    floodFillItems.add(nextItem);
                }
            }
            if (canMoveLeft(item, grid, false)) {
                final GridItem nextItem = grid[x - 1][y];
                if (nextItem.distance == -1 || nextItem.distance > item.distance + 1) {
                    nextItem.distance = item.distance + 1;
                    largestDistance = Math.max(largestDistance, nextItem.distance);
                    floodFillItems.add(nextItem);
                }
            }
            if (canMoveRight(item, grid, false)) {
                final GridItem nextItem = grid[x + 1][y];
                if (nextItem.distance == -1 || nextItem.distance > item.distance + 1) {
                    nextItem.distance = item.distance + 1;
                    largestDistance = Math.max(largestDistance, nextItem.distance);
                    floodFillItems.add(nextItem);
                }
            }
        }

        LogUtilities.logGreen("Solution 1: " + largestDistance);
    }

    private boolean canMoveUp(final GridItem item, final GridItem[][] grid, boolean includeEmpty) {
        if (item.y == 0) {
            return false;
        }
        final GridItem checkItem = grid[item.x][item.y - 1];
        return (checkItem.pipeType == PipeType.VERTICAL || checkItem.pipeType == PipeType.BOTTOM_TO_LEFT || checkItem.pipeType == PipeType.BOTTOM_TO_RIGHT)
                && (item.pipeType == PipeType.VERTICAL || item.pipeType == PipeType.TOP_TO_LEFT || item.pipeType == PipeType.TOP_TO_RIGHT || (item.pipeType == PipeType.EMPTY && includeEmpty));
    }

    private boolean canMoveDown(final GridItem item, final GridItem[][] grid, boolean includeEmpty) {
        final int height = grid[0].length;
        if (item.y == height - 1) {
            return false;
        }
        final GridItem checkItem = grid[item.x][item.y + 1];
        return (checkItem.pipeType == PipeType.VERTICAL || checkItem.pipeType == PipeType.TOP_TO_LEFT || checkItem.pipeType == PipeType.TOP_TO_RIGHT)
                && (item.pipeType == PipeType.VERTICAL || item.pipeType == PipeType.BOTTOM_TO_LEFT || item.pipeType == PipeType.BOTTOM_TO_RIGHT || (item.pipeType == PipeType.EMPTY && includeEmpty));
    }

    private boolean canMoveLeft(final GridItem item, final GridItem[][] grid, boolean includeEmpty) {
        if (item.x == 0) {
            return false;
        }
        final GridItem checkItem = grid[item.x - 1][item.y];
        return (checkItem.pipeType == PipeType.HORIZONTAL || checkItem.pipeType == PipeType.BOTTOM_TO_RIGHT || checkItem.pipeType == PipeType.TOP_TO_RIGHT)
                && (item.pipeType == PipeType.HORIZONTAL || item.pipeType == PipeType.BOTTOM_TO_LEFT || item.pipeType == PipeType.TOP_TO_LEFT || (item.pipeType == PipeType.EMPTY && includeEmpty));
    }

    private boolean canMoveRight(final GridItem item, final GridItem[][] grid, boolean includeEmpty) {
        final int width = grid.length;
        if (item.x == width - 1) {
            return false;
        }
        final GridItem checkItem = grid[item.x + 1][item.y];
        return (checkItem.pipeType == PipeType.HORIZONTAL || checkItem.pipeType == PipeType.BOTTOM_TO_LEFT || checkItem.pipeType == PipeType.TOP_TO_LEFT)
                && (item.pipeType == PipeType.HORIZONTAL || item.pipeType == PipeType.BOTTOM_TO_RIGHT || item.pipeType == PipeType.TOP_TO_RIGHT || (item.pipeType == PipeType.EMPTY && includeEmpty));
    }

    private boolean aboveConnected(int x, int y, final GridItem[][] grid) {
        if (y <= 0) {
            return false;
        }
        final GridItem checkItem = grid[x][y - 1];
        return checkItem.pipeType == PipeType.VERTICAL || checkItem.pipeType == PipeType.BOTTOM_TO_LEFT || checkItem.pipeType == PipeType.BOTTOM_TO_RIGHT;
    }

    private boolean belowConnected(int x, int y, final GridItem[][] grid) {
        final int height = grid[0].length;
        if (y >= height - 1) {
            return false;
        }
        final GridItem checkItem = grid[x][y + 1];
        return checkItem.pipeType == PipeType.VERTICAL || checkItem.pipeType == PipeType.TOP_TO_LEFT || checkItem.pipeType == PipeType.TOP_TO_RIGHT;
    }

    private boolean leftConnected(int x, int y, final GridItem[][] grid) {
        if (x <= 0) {
            return false;
        }
        final GridItem checkItem = grid[x - 1][y];
        return checkItem.pipeType == PipeType.HORIZONTAL || checkItem.pipeType == PipeType.TOP_TO_RIGHT || checkItem.pipeType == PipeType.BOTTOM_TO_RIGHT;
    }

    private boolean rightConnected(int x, int y, final GridItem[][] grid) {
        final int width = grid.length;
        if (x >= width - 1) {
            return false;
        }
        final GridItem checkItem = grid[x + 1][y];
        return checkItem.pipeType == PipeType.HORIZONTAL || checkItem.pipeType == PipeType.TOP_TO_LEFT || checkItem.pipeType == PipeType.BOTTOM_TO_LEFT;
    }

    private boolean isEmpty(int x, int y, final GridItem[][] grid) {
        if (x < 0 || y < 0 || x >= grid.length || y >= grid[0].length) {
            return false;
        }
        final GridItem checkItem = grid[x][y];
        return checkItem.pipeType == PipeType.EMPTY;
    }

    private boolean isNotMainLoop(int x, int y, final GridItem[][] grid) {
        if (x < 0 || y < 0 || x >= grid.length || y >= grid[0].length) {
            return false;
        }
        final GridItem checkItem = grid[x][y];
        return checkItem.mainLoop == false;
    }

    private void runSolution2(final String fileName) throws Exception {
        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);

        GridItem startingItem = null;
        final GridItem[][] grid = new GridItem[input.get(0).length() * 2 - 1][input.size() * 2 - 1];
        {
            int y = 0;
            for (final String rowInput : input) {
                final List<String> row = StringUtilities.splitStringIntoList(rowInput, "");
                int x = 0;
                for (final String piece : row) {
                    final GridItem gridItem = new GridItem();
                    gridItem.x = x * 2;
                    gridItem.y = y * 2;
                    gridItem.distance = -1;
                    if (piece.equals("S")) {
                        startingItem = gridItem;
                        gridItem.distance = 0;
                    } else if (piece.equals("|")) {
                        gridItem.pipeType = PipeType.VERTICAL;
                    } else if (piece.equals("-")) {
                        gridItem.pipeType = PipeType.HORIZONTAL;
                    } else if (piece.equals("L")) {
                        gridItem.pipeType = PipeType.TOP_TO_RIGHT;
                    } else if (piece.equals("J")) {
                        gridItem.pipeType = PipeType.TOP_TO_LEFT;
                    } else if (piece.equals("7")) {
                        gridItem.pipeType = PipeType.BOTTOM_TO_LEFT;
                    } else if (piece.equals("F")) {
                        gridItem.pipeType = PipeType.BOTTOM_TO_RIGHT;
                    } else if (piece.equals(".")) {
                        gridItem.pipeType = PipeType.EMPTY;
                    }
                    grid[x * 2][y * 2] = gridItem;
                    x++;
                }
                y++;
            }
        }

        // Fill in the S type
        if (aboveConnected(startingItem.x, startingItem.y - 1, grid) && belowConnected(startingItem.x, startingItem.y + 1, grid)) {
            startingItem.pipeType = PipeType.VERTICAL;
        } else if (leftConnected(startingItem.x - 1, startingItem.y, grid) && rightConnected(startingItem.x + 1, startingItem.y, grid)) {
            startingItem.pipeType = PipeType.HORIZONTAL;
        } else if (aboveConnected(startingItem.x, startingItem.y - 1, grid) && rightConnected(startingItem.x + 1, startingItem.y, grid)) {
            startingItem.pipeType = PipeType.TOP_TO_RIGHT;
        } else if (aboveConnected(startingItem.x, startingItem.y - 1, grid) && leftConnected(startingItem.x - 1, startingItem.y, grid)) {
            startingItem.pipeType = PipeType.TOP_TO_LEFT;
        } else if (belowConnected(startingItem.x, startingItem.y + 1, grid) && leftConnected(startingItem.x - 1, startingItem.y, grid)) {
            startingItem.pipeType = PipeType.BOTTOM_TO_LEFT;
        } else if (belowConnected(startingItem.x, startingItem.y + 1, grid) && rightConnected(startingItem.x + 1, startingItem.y, grid)) {
            startingItem.pipeType = PipeType.BOTTOM_TO_RIGHT;
        } else {
            startingItem.pipeType = PipeType.EMPTY;
        }

        // Now fill the odd grid items in
        {
            // Certain spots are always empty
            for (int x = 1; x < grid.length; x += 2) {
                for (int y = 1; y < grid[0].length; y += 2) {
                    final GridItem newGridItem = new GridItem();
                    newGridItem.x = x;
                    newGridItem.y = y;
                    newGridItem.distance = -1;
                    newGridItem.pipeType = PipeType.EMPTY;
                    grid[x][y] = newGridItem;
                }
            }
            // Fill in up and down, this will either be vertical connector or empty
            for (int x = 0; x < grid.length; x += 2) {
                for (int y = 1; y < grid[0].length; y += 2) {
                    final GridItem newGridItem = new GridItem();
                    newGridItem.x = x;
                    newGridItem.y = y;
                    newGridItem.distance = -1;
                    // Extrapolate
                    if (aboveConnected(newGridItem.x, newGridItem.y, grid) && belowConnected(newGridItem.x, newGridItem.y, grid)) {
                        newGridItem.pipeType = PipeType.VERTICAL;
                    } else {
                        newGridItem.pipeType = PipeType.EMPTY;
                    }
                    grid[x][y] = newGridItem;
                }
            }
            // Fill in left and right, this will either be horizontal connector or empty
            for (int x = 1; x < grid.length; x += 2) {
                for (int y = 0; y < grid[0].length; y += 2) {
                    final GridItem newGridItem = new GridItem();
                    newGridItem.x = x;
                    newGridItem.y = y;
                    newGridItem.distance = -1;
                    // Extrapolate
                    if (leftConnected(newGridItem.x, newGridItem.y, grid) && rightConnected(newGridItem.x, newGridItem.y, grid)) {
                        newGridItem.pipeType = PipeType.HORIZONTAL;
                    } else {
                        newGridItem.pipeType = PipeType.EMPTY;
                    }
                    grid[x][y] = newGridItem;
                }
            }
        }

        // Now flood fill continually to find the farthest spot
        final List<GridItem> floodFillItems = new ArrayList<>();
        floodFillItems.add(startingItem);
        startingItem.mainLoop = true;
        while (!floodFillItems.isEmpty()) {
            final GridItem item = floodFillItems.remove(0);
            final int x = item.x;
            final int y = item.y;
            if (canMoveUp(item, grid, false)) {
                final GridItem nextItem = grid[x][y - 1];
                if (nextItem.distance == -1 || nextItem.distance > item.distance + 1) {
                    nextItem.distance = item.distance + 1;
                    nextItem.mainLoop = true;
                    floodFillItems.add(nextItem);
                }
            }
            if (canMoveDown(item, grid, false)) {
                final GridItem nextItem = grid[x][y + 1];
                if (nextItem.distance == -1 || nextItem.distance > item.distance + 1) {
                    nextItem.distance = item.distance + 1;
                    nextItem.mainLoop = true;
                    floodFillItems.add(nextItem);
                }
            }
            if (canMoveLeft(item, grid, false)) {
                final GridItem nextItem = grid[x - 1][y];
                if (nextItem.distance == -1 || nextItem.distance > item.distance + 1) {
                    nextItem.distance = item.distance + 1;
                    nextItem.mainLoop = true;
                    floodFillItems.add(nextItem);
                }
            }
            if (canMoveRight(item, grid, false)) {
                final GridItem nextItem = grid[x + 1][y];
                if (nextItem.distance == -1 || nextItem.distance > item.distance + 1) {
                    nextItem.distance = item.distance + 1;
                    nextItem.mainLoop = true;
                    floodFillItems.add(nextItem);
                }
            }
        }
        // Now flood fill the empty items
        for (int x = 0; x < grid.length; x++) {
            if (grid[x][0].pipeType == PipeType.EMPTY || grid[x][0].mainLoop == false) {
                grid[x][0].distance = 0;
                floodFillItems.add(grid[x][0]);
            }
            if (grid[x][grid[0].length - 1].pipeType == PipeType.EMPTY || grid[x][grid[0].length - 1].mainLoop == false) {
                grid[x][grid[0].length - 1].distance = 0;
                floodFillItems.add(grid[x][grid[0].length - 1]);
            }
        }
        for (int y = 0; y < grid[0].length; y++) {
            if (grid[0][y].pipeType == PipeType.EMPTY || grid[0][y].mainLoop == false) {
                grid[0][y].distance = 0;
                floodFillItems.add(grid[0][y]);
            }
            if (grid[grid.length - 1][y].pipeType == PipeType.EMPTY || grid[grid.length - 1][y].mainLoop == false) {
                grid[grid.length - 1][y].distance = 0;
                floodFillItems.add(grid[grid.length - 1][y]);
            }
        }
        while (!floodFillItems.isEmpty()) {
            final GridItem item = floodFillItems.remove(0);
            final int x = item.x;
            final int y = item.y;
            if (canMoveUp(item, grid, true)) {
                final GridItem nextItem = grid[x][y - 1];
                if (nextItem.distance == -1 || nextItem.distance > item.distance + 1) {
                    nextItem.distance = item.distance + 1;
                    floodFillItems.add(nextItem);
                }
            } else if (isEmpty(x, y - 1, grid) || isNotMainLoop(x, y - 1, grid)) {
                final GridItem nextItem = grid[x][y - 1];
                if (nextItem.distance == -1 || nextItem.distance > item.distance + 1) {
                    nextItem.distance = item.distance + 1;
                    floodFillItems.add(nextItem);
                }
            }
            if (canMoveDown(item, grid, true)) {
                final GridItem nextItem = grid[x][y + 1];
                if (nextItem.distance == -1 || nextItem.distance > item.distance + 1) {
                    nextItem.distance = item.distance + 1;
                    floodFillItems.add(nextItem);
                }
            } else if (isEmpty(x, y + 1, grid) || isNotMainLoop(x, y + 1, grid)) {
                final GridItem nextItem = grid[x][y + 1];
                if (nextItem.distance == -1 || nextItem.distance > item.distance + 1) {
                    nextItem.distance = item.distance + 1;
                    floodFillItems.add(nextItem);
                }
            }
            if (canMoveLeft(item, grid, true)) {
                final GridItem nextItem = grid[x - 1][y];
                if (nextItem.distance == -1 || nextItem.distance > item.distance + 1) {
                    nextItem.distance = item.distance + 1;
                    floodFillItems.add(nextItem);
                }
            } else if (isEmpty(x - 1, y, grid) || isNotMainLoop(x - 1, y, grid)) {
                final GridItem nextItem = grid[x - 1][y];
                if (nextItem.distance == -1 || nextItem.distance > item.distance + 1) {
                    nextItem.distance = item.distance + 1;
                    floodFillItems.add(nextItem);
                }
            }
            if (canMoveRight(item, grid, true)) {
                final GridItem nextItem = grid[x + 1][y];
                if (nextItem.distance == -1 || nextItem.distance > item.distance + 1) {
                    nextItem.distance = item.distance + 1;
                    floodFillItems.add(nextItem);
                }
            } else if (isEmpty(x + 1, y, grid) || isNotMainLoop(x + 1, y, grid)) {
                final GridItem nextItem = grid[x + 1][y];
                if (nextItem.distance == -1 || nextItem.distance > item.distance + 1) {
                    nextItem.distance = item.distance + 1;
                    floodFillItems.add(nextItem);
                }
            }
        }

        long isolatedCount = 0;
        for (int x = 0; x < grid.length; x+=1) {
            for (int y = 0; y < grid[0].length; y+=1) {
                if (grid[x][y].distance == -1 && x % 2 == 0 && y % 2 == 0) {
                    isolatedCount += 1;
                }
            }
        }

        // Print out for debugging
        for (int y = 0; y < grid[0].length; y += 2) {
            for (int x = 0; x < grid.length; x += 2) {
                final GridItem item = grid[x][y];
                System.out.print(LogUtilities.ANSI_WHITE);
                if (item.mainLoop) {
                    System.out.print(LogUtilities.ANSI_RED);
                } else {
                    if (item.distance == -1 && x % 2 == 0 && y % 2 == 0) {
                        System.out.print(LogUtilities.ANSI_PURPLE);
                    }
                    if (item.distance != -1) {
                        System.out.print(LogUtilities.ANSI_GREEN);
                    }
                }
                if (item.pipeType == PipeType.HORIZONTAL) {
                    System.out.print("-");
                } else if (item.pipeType == PipeType.VERTICAL) {
                    System.out.print("|");
                } else if (item.pipeType == PipeType.EMPTY) {
                    System.out.print(".");
                } else if (item.pipeType == PipeType.TOP_TO_LEFT) {
                    System.out.print("J");
                } else if (item.pipeType == PipeType.TOP_TO_RIGHT) {
                    System.out.print("L");
                } else if (item.pipeType == PipeType.BOTTOM_TO_RIGHT) {
                    System.out.print("F");
                } else if (item.pipeType == PipeType.BOTTOM_TO_LEFT) {
                    System.out.print("7");
                }
                System.out.print(LogUtilities.ANSI_WHITE);
            }
            System.out.println();
        }

        LogUtilities.logGreen("Solution 2: " + isolatedCount);
    }
}
