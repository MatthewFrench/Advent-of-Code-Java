package Advent.of.Code.Java.Year_2023;

import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;
import Advent.of.Code.Java.Utility.Structures.Pair;
import Advent.of.Code.Java.Utility.Structures.Quadruple;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class Day_17 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2023/17/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        //runSolution1(fileName);
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
        List<Pair<Integer, Integer>> path;
    }

    private void runSolution1(final String fileName) throws Exception {
        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);
        final int height = input.size();
        final int width = input.getFirst().length();
        final Integer[][] grid = parseInput(input, width, height);

        LogUtilities.logGreen("Solution 1: " + getLeastHeatLoss(grid, 0, 3));
    }

    private long getLeastHeatLoss(final Integer[][] grid, final int minimumTravelCount, final int maxTravelCount) {
        final int width = grid.length;
        final int height = grid[0].length;
        final HashMap<Quadruple<Integer, Integer, Direction, Integer>, Long> gridMinimumCounts = new HashMap<>();

        final PriorityQueue<CurrentPossibility> possibilities = new PriorityQueue<>(Comparator.comparingLong(value -> value.heatLoss));
        {
            final CurrentPossibility startingPossibility = new CurrentPossibility();
            startingPossibility.heatLoss = 0;
            startingPossibility.direction = Direction.DOWN;
            startingPossibility.currentDirectionCount = 0;
            startingPossibility.x = 0;
            startingPossibility.y = 0;
            //startingPossibility.visited = new HashSet<>();
            possibilities.add(startingPossibility);
        }
        {
            final CurrentPossibility startingPossibility = new CurrentPossibility();
            startingPossibility.heatLoss = 0;
            startingPossibility.direction = Direction.RIGHT;
            startingPossibility.currentDirectionCount = 0;
            startingPossibility.x = 0;
            startingPossibility.y = 0;
            //startingPossibility.visited = new HashSet<>();
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
        while (!possibilities.isEmpty()) {
            final CurrentPossibility possibility = possibilities.poll();

            final Quadruple<Integer, Integer, Direction, Integer> gridMinimumKey = new Quadruple<>(possibility.x, possibility.y, possibility.direction, possibility.currentDirectionCount);
            long gridMinimumCount = -1;

            if (possibility.heatLoss != 0) {
                if (!gridMinimumCounts.containsKey(gridMinimumKey)) {
                    gridMinimumCounts.put(gridMinimumKey, possibility.heatLoss);
                } else {
                    gridMinimumCount = gridMinimumCounts.get(gridMinimumKey);
                    if (possibility.heatLoss < gridMinimumCount) {
                        gridMinimumCounts.put(gridMinimumKey, possibility.heatLoss);
                    }
                }
            }
            if (possibility.x == width - 1 && possibility.y == height - 1 && possibility.currentDirectionCount >= minimumTravelCount) {
                if (possibility.heatLoss < leastHeatLoss) {
                    leastHeatLoss = possibility.heatLoss;
                    LogUtilities.logBlue("New least heat loss: " + leastHeatLoss);
                }
                continue;
            }
            if (possibility.heatLoss >= leastHeatLoss) {
                continue;
            }
            if (gridMinimumCount != -1 && possibility.heatLoss >= gridMinimumCount) {
                continue;
            }
            // Create path left
            if (possibility.direction != Direction.RIGHT
                    && possibility.x > 0
                    && ((possibility.direction != Direction.LEFT && possibility.currentDirectionCount >= minimumTravelCount) || (possibility.direction == Direction.LEFT && possibility.currentDirectionCount < maxTravelCount))
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
                possibilities.add(newPossibility);
            }
            // Create path up
            if (possibility.direction != Direction.DOWN
                    && possibility.y > 0
                    && ((possibility.direction != Direction.UP && possibility.currentDirectionCount >= minimumTravelCount) || (possibility.direction == Direction.UP && possibility.currentDirectionCount < maxTravelCount))
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
                possibilities.add(newPossibility);
            }
            // Create path right
            if (possibility.direction != Direction.LEFT
                    && possibility.x < width - 1
                    && ((possibility.direction != Direction.RIGHT && possibility.currentDirectionCount >= minimumTravelCount) || (possibility.direction == Direction.RIGHT && possibility.currentDirectionCount < maxTravelCount))
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
                possibilities.add(newPossibility);
            }
            // Create path down
            if (possibility.direction != Direction.UP
                    && possibility.y < height - 1
                    && ((possibility.direction != Direction.DOWN && possibility.currentDirectionCount >= minimumTravelCount) || (possibility.direction == Direction.DOWN && possibility.currentDirectionCount < maxTravelCount))
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
                possibilities.add(newPossibility);
            }
        }
        return leastHeatLoss;
    }

    private void runSolution2(final String fileName) throws Exception {
        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);
        final int height = input.size();
        final int width = input.getFirst().length();
        final Integer[][] grid = parseInput(input, width, height);

        LogUtilities.logGreen("Solution 2: " + getLeastHeatLoss(grid, 4, 10));
    }
}
