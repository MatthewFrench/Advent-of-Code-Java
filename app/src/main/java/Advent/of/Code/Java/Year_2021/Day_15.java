package Advent.of.Code.Java.Year_2021;

import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Day_15 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2021/15/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);
        final int width = input.get(0).length();
        final int height = input.size();
        final Position[][] grid = new Position[width][height];
        int currentY = 0;
        for (final String line : input) {
            for (int x = 0; x < line.length(); x++) {
                grid[x][currentY] = new Position(x, currentY, Integer.parseInt(line.charAt(x) + ""), Integer.MAX_VALUE);
            }
            currentY += 1;
        }

        solveGridForLowestRiskAlgorithm2(width, height, grid);

        // Now generate the 5x bigger grid
        int newWidth = width * 5;
        int newHeight = height * 5;
        final Position[][] newGrid = new Position[newWidth][newHeight];
        for (int gridX = 0; gridX < 5; gridX++) {
            for (int gridY = 0; gridY < 5; gridY++) {
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        int originalValue = grid[x][y].value;
                        int newX = gridX * width + x;
                        int newY = gridY * height + y;
                        int newValue = originalValue + gridX + gridY;
                        while (newValue > 9) {
                            newValue -= 9;
                        }
                        newGrid[newX][newY] = new Position(newX, newY, newValue, Integer.MAX_VALUE);
                    }
                }
            }
        }
        solveGridForLowestRiskAlgorithm2(newWidth, newHeight, newGrid);
    }

    public static void solveGridForLowestRisk(final int width, final int height, final Position[][] grid) {
        int maxPathCount = 0;
        // Make first path
        int firstPathX = 0;
        int firstPathY = 0;
        while (firstPathX != width - 1 || firstPathY != height - 1) {
            if (firstPathX == width - 1) {
                firstPathY += 1;
            } else if (firstPathY == height - 1) {
                firstPathX += 1;
            } else {
                if (grid[firstPathX+1][firstPathY].value > grid[firstPathX][firstPathY+1].value) {
                    firstPathY += 1;
                } else  {
                    firstPathX += 1;
                }
            }
            maxPathCount += grid[firstPathX][firstPathY].value;
            grid[firstPathX][firstPathY].smallestCountForGrid = maxPathCount;
        }
        LogUtilities.logBlue("Current path count: " + maxPathCount);
        final LinkedList<Path> pathsToProcess = new LinkedList<>();
        final LinkedList<Path> sparePaths = new LinkedList<>();
        final Position startPosition = grid[0][0];
        pathsToProcess.add(new Path(startPosition, 0));
        while (pathsToProcess.size() > 0) {
            final Path currentPath = pathsToProcess.removeLast();
            if (currentPath.currentCount >= maxPathCount) {
                sparePaths.add(currentPath);
                continue;
            }
            final Position currentPosition = currentPath.currentPosition;
            if (currentPath.currentCount < currentPosition.smallestCountForGrid) {
                currentPosition.smallestCountForGrid = currentPath.currentCount;
            } else if (currentPath.currentCount > currentPosition.smallestCountForGrid) {
                sparePaths.add(currentPath);
                continue;
            }
            final int x = currentPosition.x;
            final int y = currentPosition.y;
            if (x == width - 1 && y == height - 1) {
                LogUtilities.logBlue("Current in progress lowest: " + currentPath.currentCount + " vs previous: " + maxPathCount + " paths left: " + pathsToProcess.size());
                maxPathCount = currentPath.currentCount;
                continue;
            }
            Position leftPosition = null;
            Position rightPosition = null;
            Position upPosition = null;
            Position downPosition = null;
            if (currentPosition.x > 0) {
                leftPosition = grid[x-1][y];
                if (currentPath.currentCount + leftPosition.value > maxPathCount || currentPath.currentCount + leftPosition.value > leftPosition.smallestCountForGrid) {
                    leftPosition = null;
                } else {
                    leftPosition.smallestCountForGrid = currentPath.currentCount + leftPosition.value;
                }
            }
            if (currentPosition.y > 0) {
                downPosition = grid[x][y-1];
                if (currentPath.currentCount + downPosition.value > maxPathCount || currentPath.currentCount + downPosition.value > downPosition.smallestCountForGrid) {
                    downPosition = null;
                } else {
                    downPosition.smallestCountForGrid = currentPath.currentCount + downPosition.value;
                }
            }
            if (currentPosition.x < width - 1) {
                rightPosition = grid[x+1][y];
                if (currentPath.currentCount + rightPosition.value > maxPathCount || currentPath.currentCount + rightPosition.value > rightPosition.smallestCountForGrid) {
                    rightPosition = null;
                } else {
                    rightPosition.smallestCountForGrid = currentPath.currentCount + rightPosition.value;
                }
            }
            if (currentPosition.y < height - 1) {
                upPosition = grid[x][y+1];
                if (currentPath.currentCount + upPosition.value > maxPathCount || currentPath.currentCount + upPosition.value > upPosition.smallestCountForGrid) {
                    upPosition = null;
                } else {
                    upPosition.smallestCountForGrid = currentPath.currentCount + upPosition.value;
                }
            }

            Position smallestValue = leftPosition;
            if (downPosition != null && (smallestValue == null || smallestValue.value > downPosition.value)) {
                smallestValue = downPosition;
            }
            if (rightPosition != null && (smallestValue == null || smallestValue.value > rightPosition.value)) {
                smallestValue = rightPosition;
            }
            if (upPosition != null && (smallestValue == null || smallestValue.value > upPosition.value)) {
                smallestValue = upPosition;
            }
            if (smallestValue == leftPosition) {
                leftPosition = null;
            }
            if (smallestValue == rightPosition) {
                rightPosition = null;
            }
            if (smallestValue == upPosition) {
                upPosition = null;
            }
            if (smallestValue == downPosition) {
                downPosition = null;
            }
            if (leftPosition != null) {
                final Path newPath;
                if (sparePaths.size() > 0) {
                    newPath = sparePaths.removeLast();
                    newPath.currentPosition = leftPosition;
                    newPath.currentCount = currentPath.currentCount + leftPosition.value;
                } else {
                    newPath = new Path(leftPosition, currentPath.currentCount + leftPosition.value);
                }
                pathsToProcess.add(newPath);
            }
            if (downPosition != null) {
                final Path newPath;
                if (sparePaths.size() > 0) {
                    newPath = sparePaths.removeLast();
                    newPath.currentPosition = downPosition;
                    newPath.currentCount = currentPath.currentCount + downPosition.value;
                } else {
                    newPath = new Path(downPosition, currentPath.currentCount + downPosition.value);
                }
                pathsToProcess.add(newPath);
            }
            if (rightPosition != null) {
                final Path newPath;
                if (sparePaths.size() > 0) {
                    newPath = sparePaths.removeLast();
                    newPath.currentPosition = rightPosition;
                    newPath.currentCount = currentPath.currentCount + rightPosition.value;
                } else {
                    newPath = new Path(rightPosition, currentPath.currentCount + rightPosition.value);
                }
                pathsToProcess.add(newPath);
            }
            if (upPosition != null) {
                final Path newPath;
                if (sparePaths.size() > 0) {
                    newPath = sparePaths.removeLast();
                    newPath.currentPosition = upPosition;
                    newPath.currentCount = currentPath.currentCount + upPosition.value;
                } else {
                    newPath = new Path(upPosition, currentPath.currentCount + upPosition.value);
                }
                pathsToProcess.add(newPath);
            }
            if (smallestValue != null) {
                final Path newPath;
                if (sparePaths.size() > 0) {
                    newPath = sparePaths.removeLast();
                    newPath.currentPosition = smallestValue;
                    newPath.currentCount = currentPath.currentCount + smallestValue.value;
                } else {
                    newPath = new Path(smallestValue, currentPath.currentCount + smallestValue.value);
                }
                pathsToProcess.add(newPath);
            }

            sparePaths.add(currentPath);
        }

        LogUtilities.logGreen("Solution: " + maxPathCount);
    }

    public static void solveGridForLowestRiskAlgorithm2(final int width, final int height, final Position[][] grid) {
        grid[0][0].smallestCountForGrid = 0;
        int amountChanged = -1;
        while (amountChanged != 0) {
            amountChanged = 0;
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (x == 0 && y == 0) {
                        continue;
                    }
                    final Position position = grid[x][y];
                    final int originalSmallestCount = position.smallestCountForGrid;
                    if (x > 0) {
                        final Position leftPosition = grid[x - 1][y];
                        final int leftSmallestCount = leftPosition.smallestCountForGrid;
                        if (leftSmallestCount != Integer.MAX_VALUE) {
                            if (position.smallestCountForGrid == Integer.MAX_VALUE || position.smallestCountForGrid > leftSmallestCount + position.value) {
                                position.smallestCountForGrid = leftSmallestCount + position.value;
                            }
                        }
                    }
                    if (y > 0) {
                        final Position downPosition = grid[x][y-1];
                        final int downSmallestCount = downPosition.smallestCountForGrid;
                        if (downSmallestCount != Integer.MAX_VALUE) {
                            if (position.smallestCountForGrid == Integer.MAX_VALUE || position.smallestCountForGrid > downSmallestCount + position.value) {
                                position.smallestCountForGrid = downSmallestCount + position.value;
                            }
                        }
                    }
                    if (x < width - 1) {
                        final Position rightPosition = grid[x + 1][y];
                        final int rightSmallestCount = rightPosition.smallestCountForGrid;
                        if (rightSmallestCount != Integer.MAX_VALUE) {
                            if (position.smallestCountForGrid == Integer.MAX_VALUE || position.smallestCountForGrid > rightSmallestCount + position.value) {
                                position.smallestCountForGrid = rightSmallestCount + position.value;
                            }
                        }
                    }
                    if (y < height - 1) {
                        final Position downPosition = grid[x][y+1];
                        final int downSmallestCount = downPosition.smallestCountForGrid;
                        if (downSmallestCount != Integer.MAX_VALUE) {
                            if (position.smallestCountForGrid == Integer.MAX_VALUE || position.smallestCountForGrid > downSmallestCount + position.value) {
                                position.smallestCountForGrid = downSmallestCount + position.value;
                            }
                        }
                    }
                    if (position.smallestCountForGrid != originalSmallestCount) {
                        amountChanged += 1;
                    }
                }
            }
            LogUtilities.logBlue("Amount changed: " + amountChanged);
        }

        LogUtilities.logGreen("Solution: " + grid[width-1][height-1].smallestCountForGrid);
    }

    @AllArgsConstructor
    @EqualsAndHashCode
    static class Position {
        final int x;
        final int y;
        final int value;
        int smallestCountForGrid;
    }
    @AllArgsConstructor
    @EqualsAndHashCode
    static class Path {
        Position currentPosition;
        int currentCount;
    }
}
