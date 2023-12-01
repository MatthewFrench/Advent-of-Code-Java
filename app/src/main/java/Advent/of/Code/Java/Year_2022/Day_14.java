package Advent.of.Code.Java.Year_2022;

import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;
import com.google.common.collect.Comparators;
import com.google.common.collect.ImmutableList;
import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day_14 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2022/14/", this);
    }

    @Data
    public static class Sand {
        boolean isAtRest = false;
        long x;
        long y;
        final boolean isRock;
    }

    public void executeWithInput(final String fileName) throws Exception {
        final List<String> inputRows = StringUtilities.splitStringIntoList(LoadUtilities.loadTextFileAsString(fileName), "\n");

        // Sand pours one unit at a time
        // Next unit of sand is not produced until the previous unit comes to rest
        // Sand always falls down one step if possible
        //  If the tile below is blocked, sand will move one step down and to the left
        //      If that tile is blocked, the sand moves one step down and to the right
        // Sand keeps moving as long as it is able to do so.
        // If all movement is blocked, the sand becomes at rest
        // Then next unit of sand is created
        // Simulate falling sand, how many units of sand come to rest before sand starts flowing into the abyss
        {
            final List<Sand> totalSand = new ArrayList<>();
            Map<Long, Map<Long, Sand>> sandMap = new HashMap<>();
            long largestY = 0;

            long sandPourX = 500;
            long sandPourY = 0;
            long minRestingX = sandPourX;
            long maxRestingX = sandPourX;
            long minRestingY = sandPourY;
            long maxRestingY = sandPourY;
            // Place the rock
            for (final String row : inputRows) {
                final List<String> points = new ArrayList<>(StringUtilities.splitStringIntoList(row, " -> "));
                final List<String> startingPoint = StringUtilities.splitStringIntoList(points.remove(0), ",");
                long startingLineX = Long.parseLong(startingPoint.get(0));
                long startingLineY = Long.parseLong(startingPoint.get(1));
                for (final String pointString : points) {
                    final List<String> point = StringUtilities.splitStringIntoList(pointString, ",");
                    long nextLineX = Long.parseLong(point.get(0));
                    long nextLineY = Long.parseLong(point.get(1));

                    long drawLineStartingX = startingLineX;
                    long drawLineStartingY = startingLineY;
                    long drawLineEndingX = nextLineX;
                    long drawLineEndingY = nextLineY;
                    if (drawLineEndingX < drawLineStartingX || drawLineEndingY < drawLineStartingY) {
                        long temp = drawLineStartingX;
                        drawLineStartingX = drawLineEndingX;
                        drawLineEndingX = temp;

                        long temp2 = drawLineStartingY;
                        drawLineStartingY = drawLineEndingY;
                        drawLineEndingY = temp2;
                    }

                    for (long x = drawLineStartingX; x <= drawLineEndingX; x++) {
                        for (long y = drawLineStartingY; y <= drawLineEndingY; y++) {
                            largestY = Math.max(largestY, y);
                            if (getSandAtPoint(x, y, sandMap) == null) {
                                putSandAtPoint(x, y, new Sand(true), sandMap);

                                minRestingX = Math.min(x, minRestingX);
                                maxRestingX = Math.max(x, maxRestingX);
                                minRestingY = Math.min(y, minRestingY);
                                maxRestingY = Math.max(y, maxRestingY);
                            }
                        }
                    }

                    startingLineX = nextLineX;
                    startingLineY = nextLineY;
                }
            }

            long floor = 2 + largestY;

            // Now simulate sand falling
            Sand currentSand = new Sand(false);
            putSandAtPoint(sandPourX, sandPourY, currentSand, sandMap);
            totalSand.add(currentSand);

            while (currentSand.y <= largestY + 1000 || currentSand.isAtRest) {
                if (currentSand.isAtRest) {
                    currentSand = new Sand(false);
                    putSandAtPoint(sandPourX, sandPourY, currentSand, sandMap);
                    totalSand.add(currentSand);
                } else {
                    final Sand downSand = getSandAtPoint(currentSand.x, currentSand.y + 1, sandMap);
                    if (downSand == null) {
                        putSandAtPoint(currentSand.x, currentSand.y + 1, currentSand, sandMap);
                    } else {
                        final Sand leftSand = getSandAtPoint(currentSand.x - 1, currentSand.y + 1, sandMap);
                        if (leftSand == null) {
                            putSandAtPoint(currentSand.x - 1, currentSand.y + 1, currentSand, sandMap);
                        } else {
                            final Sand rightSand = getSandAtPoint(currentSand.x + 1, currentSand.y + 1, sandMap);
                            if (rightSand == null) {
                                putSandAtPoint(currentSand.x + 1, currentSand.y + 1, currentSand, sandMap);
                            } else {
                                currentSand.isAtRest = true;
                                minRestingX = Math.min(currentSand.x, minRestingX);
                                maxRestingX = Math.max(currentSand.x, maxRestingX);
                                minRestingY = Math.min(currentSand.y, minRestingY);
                                maxRestingY = Math.max(currentSand.y, maxRestingY);
                            }
                        }
                    }
                }
            }

            // Display the output for debugging
            String output = "";
            for (long y = minRestingY; y <= Math.max(maxRestingY, floor); y++) {
                for (long x = minRestingX; x <= maxRestingX; x++) {
                    final Sand sand = getSandAtPoint(x, y, sandMap);
                    if (sand == null) {
                        output += ".";
                    } else if (sand.isRock) {
                        output += "#";
                    } else if (sand.isAtRest) {
                        output += "o";
                    } else {
                        output += "~";
                    }
                }
                output += "\n";
            }
            LogUtilities.logPurple("Debug Map: \n" + output);


            long restingSand = totalSand.stream().filter(Sand::isAtRest).count();
            LogUtilities.logGreen("Solution: " + restingSand);
        }

        // Solution 2
        {
            final List<Sand> totalSand = new ArrayList<>();
            Map<Long, Map<Long, Sand>> sandMap = new HashMap<>();
            long largestY = 0;

            long sandPourX = 500;
            long sandPourY = 0;
            long minRestingX = sandPourX;
            long maxRestingX = sandPourX;
            long minRestingY = sandPourY;
            long maxRestingY = sandPourY;
            // Place the rock
            for (final String row : inputRows) {
                final List<String> points = new ArrayList<>(StringUtilities.splitStringIntoList(row, " -> "));
                final List<String> startingPoint = StringUtilities.splitStringIntoList(points.remove(0), ",");
                long startingLineX = Long.parseLong(startingPoint.get(0));
                long startingLineY = Long.parseLong(startingPoint.get(1));
                for (final String pointString : points) {
                    final List<String> point = StringUtilities.splitStringIntoList(pointString, ",");
                    long nextLineX = Long.parseLong(point.get(0));
                    long nextLineY = Long.parseLong(point.get(1));

                    long drawLineStartingX = startingLineX;
                    long drawLineStartingY = startingLineY;
                    long drawLineEndingX = nextLineX;
                    long drawLineEndingY = nextLineY;
                    if (drawLineEndingX < drawLineStartingX || drawLineEndingY < drawLineStartingY) {
                        long temp = drawLineStartingX;
                        drawLineStartingX = drawLineEndingX;
                        drawLineEndingX = temp;

                        long temp2 = drawLineStartingY;
                        drawLineStartingY = drawLineEndingY;
                        drawLineEndingY = temp2;
                    }

                    for (long x = drawLineStartingX; x <= drawLineEndingX; x++) {
                        for (long y = drawLineStartingY; y <= drawLineEndingY; y++) {
                            largestY = Math.max(largestY, y);
                            if (getSandAtPoint(x, y, sandMap) == null) {
                                putSandAtPoint(x, y, new Sand(true), sandMap);

                                minRestingX = Math.min(x, minRestingX);
                                maxRestingX = Math.max(x, maxRestingX);
                                minRestingY = Math.min(y, minRestingY);
                                maxRestingY = Math.max(y, maxRestingY);
                            }
                        }
                    }

                    startingLineX = nextLineX;
                    startingLineY = nextLineY;
                }
            }

            long floor = 2 + largestY;

            // Now simulate sand falling
            Sand currentSand = new Sand(false);
            putSandAtPoint(sandPourX, sandPourY, currentSand, sandMap);
            totalSand.add(currentSand);

            while (currentSand.y <= largestY + 1000 || currentSand.isAtRest) {
                if (currentSand.isAtRest) {
                    if (getSandAtPoint(sandPourX, sandPourY, sandMap) != null) {
                        break;
                    }
                    currentSand = new Sand(false);
                    putSandAtPoint(sandPourX, sandPourY, currentSand, sandMap);
                    totalSand.add(currentSand);
                } else {
                    final Sand downSand = getSandAtPoint(currentSand.x, currentSand.y + 1, sandMap);
                    if (downSand == null && currentSand.y + 1 < floor) {
                        putSandAtPoint(currentSand.x, currentSand.y + 1, currentSand, sandMap);
                    } else {
                        final Sand leftSand = getSandAtPoint(currentSand.x - 1, currentSand.y + 1, sandMap);
                        if (leftSand == null && currentSand.y + 1 < floor) {
                            putSandAtPoint(currentSand.x - 1, currentSand.y + 1, currentSand, sandMap);
                        } else {
                            final Sand rightSand = getSandAtPoint(currentSand.x + 1, currentSand.y + 1, sandMap);
                            if (rightSand == null && currentSand.y + 1 < floor) {
                                putSandAtPoint(currentSand.x + 1, currentSand.y + 1, currentSand, sandMap);
                            } else {
                                currentSand.isAtRest = true;
                                minRestingX = Math.min(currentSand.x, minRestingX);
                                maxRestingX = Math.max(currentSand.x, maxRestingX);
                                minRestingY = Math.min(currentSand.y, minRestingY);
                                maxRestingY = Math.max(currentSand.y, maxRestingY);
                            }
                        }
                    }
                }
            }

            // Display the output for debugging
            String output = "";
            for (long y = minRestingY; y <= Math.max(maxRestingY, floor); y++) {
                for (long x = minRestingX; x <= maxRestingX; x++) {
                    final Sand sand = getSandAtPoint(x, y, sandMap);
                    if (sand == null) {
                        output += ".";
                    } else if (sand.isRock) {
                        output += "#";
                    } else if (sand.isAtRest) {
                        output += "o";
                    } else {
                        output += "~";
                    }
                }
                output += "\n";
            }
            LogUtilities.logPurple("Debug Map: \n" + output);


            long restingSand = totalSand.stream().filter(Sand::isAtRest).count();
            LogUtilities.logGreen("Solution: " + restingSand);
        }
    }

    Sand getSandAtPoint(final long x, final long y, Map<Long, Map<Long, Sand>> sandMap) {
        return sandMap.computeIfAbsent(x, (insertX) -> new HashMap<>())
                .getOrDefault(y, null);
    }

    void putSandAtPoint(final long x, final long y, final Sand sand, Map<Long, Map<Long, Sand>> sandMap) {
        removeSand(sand, sandMap);
        sand.x = x;
        sand.y = y;
        sandMap.computeIfAbsent(x, (insertX) -> new HashMap<>())
                .putIfAbsent(y, sand);
    }

    void removeSand(final Sand sand, Map<Long, Map<Long, Sand>> sandMap) {
        final Map<Long, Sand> sandColumn = sandMap.getOrDefault(sand.x, null);
        if (sandColumn != null) {
            sandColumn.remove(sand.y);
        }
    }

}
