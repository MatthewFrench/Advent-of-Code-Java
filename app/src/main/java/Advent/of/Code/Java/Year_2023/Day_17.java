package Advent.of.Code.Java.Year_2023;

import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;
import Advent.of.Code.Java.Utility.Structures.Pair;
import Advent.of.Code.Java.Utility.Structures.Quadruple;

import java.util.*;

public class Day_17 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2023/17/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        runSolution1(fileName);
        runSolution2(fileName);
    }

    private Integer[][] parseInput(final List<String> input, final int width, final int height) {
        final Integer[][] grid = new Integer[width][height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                final String character = StringUtilities.getStringChunk(input.get(y), x, 1);
                grid[x][y] = Integer.parseInt(character);
            }
        }
        return grid;
    }

    enum Direction {
        DOWN,
        UP,
        LEFT,
        RIGHT
    }

    class CurrentPossibility {
        int x;
        int y;
        long heatLoss;
        Direction direction;
        int currentDirectionCount = 0;
        //Set<Pair<Integer, Integer>> visited;
        //List<Pair<Integer, Integer>> path;
    }

    private void runSolution1(final String fileName) throws Exception {
        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);
        final int height = input.size();
        final int width = input.getFirst().length();
        final Integer[][] grid = parseInput(input, width, height);
        final Long[][] gridMinimumCountsFreshTurns = new Long[width][height];
        //final HashMap<Quadruple<Integer, Integer, Direction, Integer>, Long> gridMinimumCounts = new HashMap<Quadruple<Integer, Integer, Direction, Integer>, Long>();
        // Todo: Probably could do gridMinimumCounts for fresh turns
        // If
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                gridMinimumCountsFreshTurns[x][y] = -1L;
            }
        }

        final List<CurrentPossibility> possibilities = new ArrayList<>();
        {
            final CurrentPossibility startingPossibility = new CurrentPossibility();
            startingPossibility.heatLoss = 0;
            startingPossibility.direction = Direction.DOWN;
            startingPossibility.currentDirectionCount = 0;
            startingPossibility.x = 0;
            startingPossibility.y = 0;
            //startingPossibility.visited = new HashSet<>();
            //startingPossibility.path = new ArrayList<>();
            possibilities.add(startingPossibility);
        }

        long leastHeatLoss = 0;
        {
            int currentX = 0;
            int currentY = 0;
            for (int index = 0; index < Math.max(width, height); index++) {
                if (currentX < width - 1) {
                    currentX += 1;
                    leastHeatLoss += grid[currentX][currentY];
                }
                if (currentY < height - 1) {
                    currentY += 1;
                    leastHeatLoss += grid[currentX][currentY];
                }
            }
        }
        // 865 is too high
        //leastHeatLoss = Math.min(865, leastHeatLoss);
        while (!possibilities.isEmpty()) {
            final CurrentPossibility possibility = possibilities.removeLast();
            //final Quadruple<Integer, Integer, Direction, Integer> gridMinimumKey = new Quadruple<>(possibility.x, possibility.y, possibility.direction, possibility.currentDirectionCount);
            //long gridMinimumCount = -1;
            /*
            if (!gridMinimumCounts.containsKey(gridMinimumKey)) {
                gridMinimumCounts.put(gridMinimumKey, possibility.heatLoss);
            } else {
                gridMinimumCount = gridMinimumCounts.get(gridMinimumKey);
                if (possibility.heatLoss < gridMinimumCount) {
                    gridMinimumCounts.put(gridMinimumKey, possibility.heatLoss);
                }
            }
             */
            // Todo: This may not be correct or well optimized
            if (possibility.currentDirectionCount == 1) {
                long gridMinimumCount = gridMinimumCountsFreshTurns[possibility.x][possibility.y];
                if (gridMinimumCount == -1) {
                    gridMinimumCountsFreshTurns[possibility.x][possibility.y] = possibility.heatLoss;
                } else if (possibility.heatLoss >= gridMinimumCount) {
                    continue;
                }
            }
            if (possibility.x == width - 1 && possibility.y == height - 1) {
                if (possibility.heatLoss < leastHeatLoss) {
                    leastHeatLoss = possibility.heatLoss;
                    LogUtilities.logBlue("New least heat loss: " + leastHeatLoss);
                }
                continue;
            }
            if (possibility.heatLoss >= leastHeatLoss) {
                continue;
            }
            //if (gridMinimumCount != -1 && possibility.heatLoss >= gridMinimumCount) {
            //    continue;
            //}
            // Create path left
            if (possibility.direction != Direction.RIGHT
                    && (possibility.direction != Direction.LEFT || possibility.currentDirectionCount < 3)
                    && possibility.x > 0
                    //&& !possibility.visited.contains(new Pair<>(possibility.x - 1, possibility.y))
            ) {
                final CurrentPossibility newPossibility = new CurrentPossibility();
                newPossibility.x = possibility.x - 1;
                newPossibility.y = possibility.y;
                newPossibility.currentDirectionCount = possibility.currentDirectionCount;
                if (possibility.direction != Direction.LEFT) {
                    newPossibility.currentDirectionCount = 0;
                }
                newPossibility.currentDirectionCount += 1;
                newPossibility.heatLoss = possibility.heatLoss + grid[newPossibility.x][newPossibility.y];
                newPossibility.direction = Direction.LEFT;
                //newPossibility.visited = new HashSet<>(possibility.visited);
                //newPossibility.visited.add(new Pair<>(newPossibility.x, newPossibility.y));
                //newPossibility.path = new ArrayList<>(possibility.path);
                //newPossibility.path.add(new Pair<>(newPossibility.x, newPossibility.y));
                possibilities.add(newPossibility);
            }
            // Create path up
            if (possibility.direction != Direction.DOWN
                    && (possibility.direction != Direction.UP || possibility.currentDirectionCount < 3)
                    && possibility.y > 0
                    //&& !possibility.visited.contains(new Pair<>(possibility.x, possibility.y - 1))
            ) {
                final CurrentPossibility newPossibility = new CurrentPossibility();
                newPossibility.x = possibility.x;
                newPossibility.y = possibility.y - 1;
                newPossibility.currentDirectionCount = possibility.currentDirectionCount;
                if (possibility.direction != Direction.UP) {
                    newPossibility.currentDirectionCount = 0;
                }
                newPossibility.currentDirectionCount += 1;
                newPossibility.heatLoss = possibility.heatLoss + grid[newPossibility.x][newPossibility.y];
                newPossibility.direction = Direction.UP;
                //newPossibility.visited = new HashSet<>(possibility.visited);
                //newPossibility.visited.add(new Pair<>(newPossibility.x, newPossibility.y));
                //newPossibility.path = new ArrayList<>(possibility.path);
                //newPossibility.path.add(new Pair<>(newPossibility.x, newPossibility.y));
                possibilities.add(newPossibility);
            }
            // Create path right
            if (possibility.direction != Direction.LEFT
                    && (possibility.direction != Direction.RIGHT || possibility.currentDirectionCount < 3)
                    && possibility.x < width - 1
                    //&& !possibility.visited.contains(new Pair<>(possibility.x + 1, possibility.y))
            ) {
                final CurrentPossibility newPossibility = new CurrentPossibility();
                newPossibility.x = possibility.x + 1;
                newPossibility.y = possibility.y;
                newPossibility.currentDirectionCount = possibility.currentDirectionCount;
                if (possibility.direction != Direction.RIGHT) {
                    newPossibility.currentDirectionCount = 0;
                }
                newPossibility.currentDirectionCount += 1;
                newPossibility.heatLoss = possibility.heatLoss + grid[newPossibility.x][newPossibility.y];
                newPossibility.direction = Direction.RIGHT;
                //newPossibility.visited = new HashSet<>(possibility.visited);
                //newPossibility.visited.add(new Pair<>(newPossibility.x, newPossibility.y));
                //newPossibility.path = new ArrayList<>(possibility.path);
                //newPossibility.path.add(new Pair<>(newPossibility.x, newPossibility.y));
                possibilities.add(newPossibility);
            }
            // Create path down
            if (possibility.direction != Direction.UP
                    && (possibility.direction != Direction.DOWN || possibility.currentDirectionCount < 3)
                    && possibility.y < height - 1
                    //&& !possibility.visited.contains(new Pair<>(possibility.x, possibility.y + 1))
            ) {
                final CurrentPossibility newPossibility = new CurrentPossibility();
                newPossibility.x = possibility.x;
                newPossibility.y = possibility.y + 1;
                newPossibility.currentDirectionCount = possibility.currentDirectionCount;
                if (possibility.direction != Direction.DOWN) {
                    newPossibility.currentDirectionCount = 0;
                }
                newPossibility.currentDirectionCount += 1;
                newPossibility.heatLoss = possibility.heatLoss + grid[newPossibility.x][newPossibility.y];
                newPossibility.direction = Direction.DOWN;
                //newPossibility.visited = new HashSet<>(possibility.visited);
                //newPossibility.visited.add(new Pair<>(newPossibility.x, newPossibility.y));
                //newPossibility.path = new ArrayList<>(possibility.path);
                //newPossibility.path.add(new Pair<>(newPossibility.x, newPossibility.y));
                possibilities.add(newPossibility);
            }
        }

        LogUtilities.logGreen("Solution 1: " + leastHeatLoss);
    }

    private void runSolution2(final String fileName) throws Exception {
    }
}
