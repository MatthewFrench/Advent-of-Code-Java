package Advent.of.Code.Java.Year_2022;

import Advent.of.Code.Java.Utility.DataUtilities;
import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;
import Advent.of.Code.Java.Utility.Structures.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day_23 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2022/23/", this);
    }

    enum Direction {
        North,
        South,
        East,
        West
    }

    static class Elf {
        long x;
        long y;
        List<Direction> directionPriority = DataUtilities.List(Direction.North, Direction.South, Direction.West, Direction.East);

        public Elf(long x, long y) {
            this.x = x;
            this.y = y;
        }
    }

    public void executeWithInput(final String fileName) throws Exception {
        List<String> splitInput = StringUtilities.splitStringIntoList(LoadUtilities.loadTextFileAsString(fileName), "\n");
        Map<Long, Map<Long, Elf>> elvesMap = new HashMap<>();
        final List<Elf> elves = new ArrayList<>();
        {
            long y = 0;
            for (final String row : splitInput) {
                final List<String> pieces = StringUtilities.splitStringIntoList(row, "");
                long x = 0;
                for (final String piece : pieces) {
                    if (piece.equals("#")) {
                        final Elf elf = new Elf(x, y);
                        elves.add(elf);
                        elvesMap.computeIfAbsent(x, (xValue) -> new HashMap<>()).put(y, elf);
                    }
                    x += 1;
                }
                y += 1;
            }
        }

        boolean continueMoving = true;
        int round = 0;
        while (continueMoving) {
            round += 1;
            //if (round > 10) {
            //    break;
            //}
            continueMoving = false;
            Map<Pair<Long, Long>, List<Elf>> elvesMovingToPosition = new HashMap<>();
            // Do movement
            for (final Elf elf : elves) {
                if (areElvesAroundPosition(elf.x, elf.y, elvesMap)) {
                    // Propose movements
                    for (final Direction direction : elf.directionPriority) {
                        if (canMoveInDirection(direction, elf.x, elf.y, elvesMap)) {
                            elvesMovingToPosition.computeIfAbsent(new Pair<>(getDirectionX(direction, elf.x), getDirectionY(direction, elf.y)), (position) -> new ArrayList<>()).add(elf);
                            break;
                        }
                    }
                }
            }
            for (final Pair<Long, Long> moveToLocation : elvesMovingToPosition.keySet()) {
                List<Elf> elvesToMove = elvesMovingToPosition.get(moveToLocation);
                if (elvesToMove.size() == 1) {
                    final Elf elfToMove = elvesToMove.get(0);
                    elvesMap.get(elfToMove.x).remove(elfToMove.y);
                    elfToMove.x = moveToLocation.getKey();
                    elfToMove.y = moveToLocation.getValue();
                    elvesMap.computeIfAbsent(elfToMove.x, (xValue) -> new HashMap<>()).put(elfToMove.y, elfToMove);
                    continueMoving = true;
                }
            }
            for (final Elf elf : elves) {
                elf.directionPriority.add(elf.directionPriority.remove(0));
            }
        }
/*
        long minX = elves.get(0).x;
        long minY = elves.get(0).y;
        long maxX = elves.get(0).x;
        long maxY = elves.get(0).y;
        for (final Elf elf : elves) {
            minX = Math.min(minX, elf.x);
            minY = Math.min(minY, elf.y);
            maxX = Math.max(maxX, elf.x);
            maxY = Math.max(maxY, elf.y);
        }
        long emptySpots = 0;
        for (long x = minX; x <= maxX; x++) {
            for (long y = minY; y <= maxY; y++) {
                if (!elvesMap.containsKey(x) || !elvesMap.get(x).containsKey(y)) {
                    emptySpots += 1;
                }
            }
        }

        LogUtilities.logGreen("Solution: " + emptySpots);

 */
        LogUtilities.logGreen("Solution 2: " + round);
    }

    void printMap(final List<Elf> elves, Map<Long, Map<Long, Elf>> elvesMap) {
        long minX = elves.get(0).x;
        long minY = elves.get(0).y;
        long maxX = elves.get(0).x;
        long maxY = elves.get(0).y;
        for (final Elf elf : elves) {
            minX = Math.min(minX, elf.x);
            minY = Math.min(minY, elf.y);
            maxX = Math.max(maxX, elf.x);
            maxY = Math.max(maxY, elf.y);
        }
        String output = "";
        for (long y = minY; y <= maxY; y++) {
            for (long x = minX; x <= maxX; x++) {
                if (!elvesMap.containsKey(x) || !elvesMap.get(x).containsKey(y)) {
                    output += ".";
                } else {
                    output += "#";
                }
            }
            output += "\n";
        }
        LogUtilities.logPurple("Map: \n" + output);
    }

    long getDirectionX(Direction direction, long x) {
        if (direction == Direction.North) {
            return x;
        } else if (direction == Direction.South) {
            return x;
        } else if (direction == Direction.East) {
            return x + 1;
        } else if (direction == Direction.West) {
            return x - 1;
        }
        throw new RuntimeException("Invalid direction");
    }

    long getDirectionY(Direction direction, long y) {
        if (direction == Direction.North) {
            return y - 1;
        } else if (direction == Direction.South) {
            return y + 1;
        } else if (direction == Direction.East) {
            return y;
        } else if (direction == Direction.West) {
            return y;
        }
        throw new RuntimeException("Invalid direction");
    }

    boolean areElvesAroundPosition(long x, long y, Map<Long, Map<Long, Elf>> elvesMap) {
        for (long newX = x - 1; newX <= x + 1; newX++) {
            for (long newY = y - 1; newY <= y + 1; newY++) {
                if (newX != x || newY != y) {
                    if (elvesMap.containsKey(newX)) {
                        if (elvesMap.get(newX).containsKey(newY)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    boolean canMoveInDirection(Direction direction, long x, long y, Map<Long, Map<Long, Elf>> elvesMap) {
        if (direction == Direction.North) {
            return !areElvesAbove(x, y, elvesMap);
        } else if (direction == Direction.South) {
            return !areElvesBelow(x, y, elvesMap);
        } else if (direction == Direction.East) {
            return !areElvesRight(x, y, elvesMap);
        } else if (direction == Direction.West) {
            return !areElvesLeft(x, y, elvesMap);
        }
        throw new RuntimeException("Invalid direction");
    }

    boolean areElvesAbove(long x, long y, Map<Long, Map<Long, Elf>> elvesMap) {
        for (long newX = x - 1; newX <= x + 1; newX++) {
            if (elvesMap.containsKey(newX)) {
                if (elvesMap.get(newX).containsKey(y - 1)) {
                    return true;
                }
            }
        }
        return false;
    }

    boolean areElvesBelow(long x, long y, Map<Long, Map<Long, Elf>> elvesMap) {
        for (long newX = x - 1; newX <= x + 1; newX++) {
            if (elvesMap.containsKey(newX)) {
                if (elvesMap.get(newX).containsKey(y + 1)) {
                    return true;
                }
            }
        }
        return false;
    }

    boolean areElvesLeft(long x, long y, Map<Long, Map<Long, Elf>> elvesMap) {
        for (long newY = y - 1; newY <= y + 1; newY++) {
            if (elvesMap.containsKey(x - 1)) {
                if (elvesMap.get(x - 1).containsKey(newY)) {
                    return true;
                }
            }
        }
        return false;
    }

    boolean areElvesRight(long x, long y, Map<Long, Map<Long, Elf>> elvesMap) {
        for (long newY = y - 1; newY <= y + 1; newY++) {
            if (elvesMap.containsKey(x + 1)) {
                if (elvesMap.get(x + 1).containsKey(newY)) {
                    return true;
                }
            }
        }
        return false;
    }
}
