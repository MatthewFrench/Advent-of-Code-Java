package Advent.of.Code.Java.Year_2022;

import Advent.of.Code.Java.Utility.DataUtilities;
import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;
import Advent.of.Code.Java.Utility.Structures.Pair;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day_17 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2022/17/", this);
    }
    static class FallingRock {
        final Map<Long, Map<Long, Boolean>> rockStructure = new HashMap<>();
        long width;
        long height;
        public FallingRock(List<Pair<Long, Long>> rockLocations) {
            for (final Pair<Long, Long> rockLocation : rockLocations) {
                placeRockAt(rockLocation.getKey(), rockLocation.getValue());
                width = Math.max(width, rockLocation.getKey() + 1);
                height = Math.max(height, rockLocation.getValue() + 1);
            }
        }

        void placeRockAt(long x, long y) {
            rockStructure.computeIfAbsent(x, (x1) -> new HashMap<>()).put(y, true);
        }

        public boolean hasRockAtLocation(long x, long y) {
            if (!rockStructure.containsKey(x)) {
                return false;
            }
            return rockStructure.get(x).getOrDefault(y, false);
        }

        public Long getWidth() {
            return width;
        }
        public Long getHeight() {
            return height;
        }
    }
    static class Arena {
        static long WIDTH = 7;
        final Map<Long, Map<Long, Boolean>> rockMap = new HashMap<>();
        long currentHeight = 0;

        public long getCurrentHeight() {
            return currentHeight;
        }
        public void overwriteCurrentHeight(final long newHeight, final List<Long> depths) {
            for (int x = 0; x < depths.size(); x++) {
                placeRockAt(x, depths.get(x) + newHeight);
            }
        }
        public boolean canFallingRockPushLeft(final long x, final long y, final FallingRock fallingRock) {
            final long newX = x - 1;
            if (newX < 0) {
                return false;
            }
            for (long checkX = 0; checkX < fallingRock.getWidth(); checkX++) {
                for (long checkY = 0; checkY < fallingRock.getHeight(); checkY++) {
                    if (fallingRock.hasRockAtLocation(checkX, checkY) &&
                            rockExistsAtLocation(checkX + newX, checkY + y)) {
                        return false;
                    }
                }
            }
            return true;
        }
        public boolean canFallingRockPushRight(final long x, final long y, final FallingRock fallingRock) {
            final long newX = x + 1;
            if (newX + fallingRock.width > WIDTH) {
                return false;
            }
            for (long checkX = 0; checkX < fallingRock.getWidth(); checkX++) {
                for (long checkY = 0; checkY < fallingRock.getHeight(); checkY++) {
                    if (fallingRock.hasRockAtLocation(checkX, checkY) &&
                            rockExistsAtLocation(checkX + newX, checkY + y)) {
                        return false;
                    }
                }
            }
            return true;
        }
        // Returns true if the rock came to rest
        public boolean isFallingRockAtRestWhenPushingDown(final long x, final long y, final FallingRock fallingRock) {
            boolean rockIsAtRest = false;
            final long newY = y - 1;
            if (newY < 0) {
                rockIsAtRest = true;
            } else {
                forStart: for (long checkX = 0; checkX < fallingRock.getWidth(); checkX++) {
                    for (long checkY = 0; checkY < fallingRock.getHeight(); checkY++) {
                        if (fallingRock.hasRockAtLocation(checkX, checkY) &&
                                rockExistsAtLocation(checkX + x, checkY + newY)) {
                            rockIsAtRest = true;
                            break forStart;
                        }
                    }
                }
            }
            if (rockIsAtRest) {
                // Add the rock to the arena map
                for (long checkX = 0; checkX < fallingRock.getWidth(); checkX++) {
                    for (long checkY = 0; checkY < fallingRock.getHeight(); checkY++) {
                        if (fallingRock.hasRockAtLocation(checkX, checkY)) {
                            placeRockAt(checkX + x, checkY + y);
                        }
                    }
                }
            }
            return rockIsAtRest;
        }

        void placeRockAt(long x, long y) {
            rockMap.computeIfAbsent(x, (x1) -> new HashMap<>()).put(y, true);
            currentHeight = Math.max(currentHeight, y+1);
        }

        boolean rockExistsAtLocation(long x, long y) {
            return rockMap.computeIfAbsent(x, (x1) -> new HashMap<>()).getOrDefault(y, false);
        }

        public List<Long> getFloorDeepnessLevelsFromCurrentHeight() {
            final List<Long> floorBlocks = new ArrayList<>();
            for (int x = 0; x < WIDTH; x++) {
                long deepness = 0;
                boolean stillChecking = true;
                while (stillChecking) {
                    deepness -= 1;
                    if (rockExistsAtLocation(x, currentHeight - deepness) || currentHeight + deepness <= 0) {
                        stillChecking = false;
                    }
                }
                floorBlocks.add(Math.max(deepness, -100));
                //floorBlocks.add(deepness);
            }
            return floorBlocks;
        }
    }
    @Data
    static class RockPattern {
        final int nextFallingRock;
        final int nextJetCommand;
        final List<Long> floorBlocks;
    }
    public void executeWithInput(final String fileName) throws Exception {
        final List<String> jetCommands = StringUtilities.splitStringIntoList(LoadUtilities.loadTextFileAsString(fileName), "");

        final List<FallingRock> fallingRocks = DataUtilities.List(
                // ####
                new FallingRock(DataUtilities.List(
                        new Pair<>(0L, 0L), new Pair<>(1L, 0L), new Pair<>(2L, 0L), new Pair<>(3L, 0L)
                )),
                // .#.
                // ###
                // .#.
                new FallingRock(DataUtilities.List(
                                          new Pair<>(1L, 2L),
                        new Pair<>(0L, 1L), new Pair<>(1L, 1L), new Pair<>(2L, 1L),
                                          new Pair<>(1L, 0L)
                )),
                // ..#
                // ..#
                // ###
                new FallingRock(DataUtilities.List(
                                                            new Pair<>(2L, 2L),
                                                            new Pair<>(2L, 1L),
                        new Pair<>(0L, 0L), new Pair<>(1L, 0L), new Pair<>(2L, 0L)
                )),
                // #
                // #
                // #
                // #
                new FallingRock(DataUtilities.List(
                        new Pair<>(0L, 3L),
                        new Pair<>(0L, 2L),
                        new Pair<>(0L, 1L),
                        new Pair<>(0L, 0L)
                )),
                // ##
                // ##
                new FallingRock(DataUtilities.List(
                        new Pair<>(0L, 1L), new Pair<>(1L, 1L),
                        new Pair<>(0L, 0L), new Pair<>(1L, 0L)
                ))
        );

        int currentFallingRockIndex = 0;
        int currentJetCommandIndex = 0;
        //long maxRocks = 2022;//1_000_000_000_000L;
        long maxRocks = 1_000_000_000_000L;

        // Chamber is 7 spaces wide
        // Left edge always appears two spaces from the left side
        //Bottom edge is three units above the highest rock or floor

        Arena arena = new Arena();
        FallingRock currentFallingRock = fallingRocks.get(currentFallingRockIndex);
        long currentFallingRockLeftEdge = 2;
        long currentFallingRockBottomEdge = 3;
        long totalFallenRocks = 0;
        final Set<RockPattern> rockPatterns = new HashSet<>();
        final RockPattern firstRockPattern = new RockPattern(currentFallingRockIndex, currentJetCommandIndex, DataUtilities.List(-1L, -1L, -1L, -1L, -1L, -1L, -1L));
        rockPatterns.add(firstRockPattern);
        final Map<RockPattern, Pair<Long, Long>> rockPatternLocations = new HashMap<>();
        rockPatternLocations.put(firstRockPattern, new Pair<>(totalFallenRocks, arena.getCurrentHeight()));
        while (totalFallenRocks < maxRocks) {
            final String jetCommand = jetCommands.get(currentJetCommandIndex);
            currentJetCommandIndex += 1;
            if (currentJetCommandIndex >= jetCommands.size()) {
                currentJetCommandIndex = 0;
            }
            if (jetCommand.equals(">")) {
                if (arena.canFallingRockPushRight(currentFallingRockLeftEdge, currentFallingRockBottomEdge, currentFallingRock)) {
                    currentFallingRockLeftEdge += 1;
                }
            } else if (jetCommand.equals("<")) {
                if (arena.canFallingRockPushLeft(currentFallingRockLeftEdge, currentFallingRockBottomEdge, currentFallingRock)) {
                    currentFallingRockLeftEdge -= 1;
                }
            }
            if (arena.isFallingRockAtRestWhenPushingDown(currentFallingRockLeftEdge, currentFallingRockBottomEdge, currentFallingRock)) {
                //arena.printArena();
                currentFallingRockIndex += 1;
                if (currentFallingRockIndex >= fallingRocks.size()) {
                    currentFallingRockIndex = 0;
                }
                currentFallingRock = fallingRocks.get(currentFallingRockIndex);
                currentFallingRockLeftEdge = 2;
                currentFallingRockBottomEdge = arena.getCurrentHeight() + 4 - 1;
                totalFallenRocks += 1;
                final RockPattern newRockPattern = new RockPattern(currentFallingRockIndex, currentJetCommandIndex, arena.getFloorDeepnessLevelsFromCurrentHeight());
                if (rockPatterns.contains(newRockPattern)) {
                    long previousPatternHeight = rockPatternLocations.get(newRockPattern).getValue();
                    long previousPatternFallenRocks = rockPatternLocations.get(newRockPattern).getKey();

                    long patternAddsFallenRocks = totalFallenRocks - previousPatternFallenRocks;
                    long patternAddsHeight = arena.currentHeight - previousPatternHeight;

                    long numberOfLoops = (maxRocks - totalFallenRocks) / patternAddsFallenRocks;
                    long newTotalFallenRocks = numberOfLoops * patternAddsFallenRocks + totalFallenRocks;

                    if (newTotalFallenRocks == maxRocks) {
                        totalFallenRocks = numberOfLoops * patternAddsFallenRocks + totalFallenRocks;
                        long newHeight = numberOfLoops * patternAddsHeight + arena.currentHeight;
                        arena.overwriteCurrentHeight(newHeight, firstRockPattern.floorBlocks);
                        rockPatterns.clear();
                        rockPatternLocations.clear();
                        LogUtilities.logPurple("Test");
                    }
                } else {
                    rockPatterns.add(newRockPattern);
                    rockPatternLocations.put(newRockPattern, new Pair<>(totalFallenRocks, arena.getCurrentHeight()));
                }
            } else {
                currentFallingRockBottomEdge -= 1;
            }
        }

        LogUtilities.logGreen("Solution: " + arena.getCurrentHeight());
    }
}
