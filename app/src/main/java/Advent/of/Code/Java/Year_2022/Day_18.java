package Advent.of.Code.Java.Year_2022;

import Advent.of.Code.Java.Utility.DataUtilities;
import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;
import org.javatuples.Triplet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day_18 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2022/18/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        final List<String> cubeStrings = StringUtilities.splitStringIntoList(LoadUtilities.loadTextFileAsString(fileName), "\n");

        final Map<Long, Map<Long, Set<Long>>> cubeGrid = new HashMap<>();
        Long minX = null;
        Long maxX = null;
        Long minY = null;
        Long maxY = null;
        Long minZ = null;
        Long maxZ = null;
        for (final String cubeString : cubeStrings) {
            final List<String> cubeCoordinateStrings = StringUtilities.splitStringIntoList(cubeString, ",");
            final long x = Long.parseLong(cubeCoordinateStrings.get(0));
            final long y = Long.parseLong(cubeCoordinateStrings.get(1));
            final long z = Long.parseLong(cubeCoordinateStrings.get(2));
            cubeGrid.computeIfAbsent(x, (x1) -> new HashMap<>()).computeIfAbsent(y, (y1) -> new HashSet<>()).add(z);
            if (minX == null || x < minX) {
                minX = x;
            }
            if (maxX == null || x > maxX) {
                maxX = x;
            }
            if (minY == null || y < minY) {
                minY = y;
            }
            if (maxY == null || y > maxY) {
                maxY = y;
            }
            if (minZ == null || z < minZ) {
                minZ = z;
            }
            if (maxZ == null || z > maxZ) {
                maxZ = z;
            }
        }

        List<Triplet<Long, Long, Long>> checkCoordinates = DataUtilities.List(
                Triplet.with(-1L, 0L, 0L),
                Triplet.with(1L, 0L, 0L),
                Triplet.with(0L, -1L, 0L),
                Triplet.with(0L, 1L, 0L),
                Triplet.with(0L, 0L, -1L),
                Triplet.with(0L, 0L, 1L)
        );
        //Set<Triplet<Long, Long, Long>> surfaceAreaSet = new HashSet<>();
        long surfaceArea = 0;
        long surfaceCanReachOutside = 0;

        for (long x = minX; x <= maxX; x++) {
            for (long y = minY; y <= maxY; y++) {
                for (long z = minZ; z <= maxZ; z++) {
                    if (containsCoordinate(x, y, z, cubeGrid)) {
                        for (final Triplet<Long, Long, Long> coordinate : checkCoordinates) {
                            if (!containsCoordinate(x + coordinate.getValue0(), y + coordinate.getValue1(), z + coordinate.getValue2(), cubeGrid)) {
                                surfaceArea += 1;
                                if (coordinateCanReachOutside(x + coordinate.getValue0(), y + coordinate.getValue1(), z + coordinate.getValue2(), cubeGrid,
                                        minX, maxX, minY, maxY, minZ, maxZ)) {
                                    surfaceCanReachOutside += 1;
                                }
                            }
                        }
                    }
                }
            }
        }

        LogUtilities.logGreen("Solution: " + surfaceArea);
        LogUtilities.logGreen("Solution 2: " + surfaceCanReachOutside);
    }

    boolean containsCoordinate(long x, long y, long z, final Map<Long, Map<Long, Set<Long>>> cubeGrid) {
        final Map<Long, Set<Long>> yGrid = cubeGrid.getOrDefault(x, null);
        if (yGrid == null) {
            return false;
        }
        final Set<Long> zGrid = yGrid.getOrDefault(y, null);
        if (zGrid == null) {
            return false;
        }
        return zGrid.contains(z);
    }

    boolean coordinateCanReachOutside(
            long x,
            long y,
            long z,
            final Map<Long, Map<Long, Set<Long>>> cubeGrid,
            long minX,
            long maxX,
            long minY,
            long maxY,
            long minZ,
            long maxZ
    ) {
        List<Triplet<Long, Long, Long>> cubeSides = DataUtilities.List(
                Triplet.with(-1L, 0L, 0L),
                Triplet.with(1L, 0L, 0L),
                Triplet.with(0L, -1L, 0L),
                Triplet.with(0L, 1L, 0L),
                Triplet.with(0L, 0L, -1L),
                Triplet.with(0L, 0L, 1L)
        );

        Set<Triplet<Long, Long, Long>> checkedCoordinates = new HashSet<>();
        List<Triplet<Long, Long, Long>> checkCoordinates = new ArrayList<>();
        final Triplet<Long, Long, Long> startingCoordinate = new Triplet<>(x, y, z);
        checkCoordinates.add(new Triplet<>(x, y, z));
        checkedCoordinates.add(startingCoordinate);

        while (checkCoordinates.size() > 0) {
            final Triplet<Long, Long, Long> coordinateToCheck = checkCoordinates.remove(0);

            // If the coordinate is outside of our map, return true. We are outside
            if (coordinateToCheck.getValue0() < minX || coordinateToCheck.getValue1() > maxX ||
                    coordinateToCheck.getValue1() < minY || coordinateToCheck.getValue1() > maxY ||
                    coordinateToCheck.getValue2() < minZ || coordinateToCheck.getValue2() > maxZ) {
                return true;
            }

            if (!containsCoordinate(coordinateToCheck.getValue0(), coordinateToCheck.getValue1(), coordinateToCheck.getValue2(), cubeGrid)) {
                for (final Triplet<Long, Long, Long> cubeSide : cubeSides) {
                    final Triplet<Long, Long, Long> newCoordinate = new Triplet<>(
                            coordinateToCheck.getValue0() + cubeSide.getValue0(),
                            coordinateToCheck.getValue1() + cubeSide.getValue1(),
                            coordinateToCheck.getValue2() + cubeSide.getValue2()
                    );
                    if (!checkedCoordinates.contains(newCoordinate)) {
                        checkCoordinates.add(newCoordinate);
                        checkedCoordinates.add(newCoordinate);
                    }
                }
            }
        }

        return false;
    }
}
