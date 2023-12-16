package Advent.of.Code.Java.Year_2023;

import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class Day_16 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2023/16/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        runSolution1(fileName);
        runSolution2(fileName);
    }

    enum GridType {
        EMPTY,
        UP_DOWN_SPLITTER,
        LEFT_RIGHT_SPLITTER,
        DIAGONAL_BOTTOM_LEFT_TO_TOP_RIGHT,
        DIAGONAL_BOTTOM_RIGHT_TO_TOP_LEFT
    }
    enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }
    class GridObject {
        GridType gridType = GridType.EMPTY;
        boolean energized = false;
        Set<Direction> passedBeamDirections = new HashSet<>();
    }
    class BeamObject {
        int x;
        int y;
        Direction direction;
    }

    private GridObject[][] parseInput(final List<String> input, final int width, final int height) {
        final GridObject[][] grid = new GridObject[width][height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                final String character = StringUtilities.getStringChunk(input.get(y), x, 1);
                final GridObject gridObject = new GridObject();
                grid[x][y] = gridObject;
                if (character.equals("|")) {
                    gridObject.gridType = GridType.UP_DOWN_SPLITTER;
                } else if (character.equals("/")) {
                    gridObject.gridType = GridType.DIAGONAL_BOTTOM_LEFT_TO_TOP_RIGHT;
                } else if (character.equals("\\")) {
                    gridObject.gridType = GridType.DIAGONAL_BOTTOM_RIGHT_TO_TOP_LEFT;
                } else if (character.equals("-")) {
                    gridObject.gridType = GridType.LEFT_RIGHT_SPLITTER;
                } else {
                    gridObject.gridType = GridType.EMPTY;
                }
            }
        }
        return grid;
    }

    private void runSolution1(final String fileName) throws Exception {
        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);
        final int height = input.size();
        final int width = input.getFirst().length();
        LogUtilities.logGreen("Solution 1: " + getEnergizedCount(input, 0, 0, Direction.RIGHT));
    }
    private void runSolution2(final String fileName) throws Exception {
        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);
        final int height = input.size();
        final int width = input.getFirst().length();
        long maxEnergized = 0;
        for (int x = 0; x < width; x++) {
            maxEnergized = Math.max(maxEnergized, getEnergizedCount(input, x, 0, Direction.DOWN));
            maxEnergized = Math.max(maxEnergized, getEnergizedCount(input, x, height - 1, Direction.UP));
        }
        for (int y = 0; y < height; y++) {
            maxEnergized = Math.max(maxEnergized, getEnergizedCount(input, 0, y, Direction.RIGHT));
            maxEnergized = Math.max(maxEnergized, getEnergizedCount(input, width - 1, y, Direction.LEFT));
        }
        LogUtilities.logGreen("Solution 2: " + maxEnergized);
    }

    private long getEnergizedCount(final List<String> input, final int startingX, final int startingY, final Direction startingDirection) {
        final int height = input.size();
        final int width = input.getFirst().length();
        final GridObject[][] grid = parseInput(input, width, height);
        final BeamObject startingBeam = new BeamObject();
        startingBeam.direction = startingDirection;
        startingBeam.x = startingX + (startingDirection == Direction.RIGHT ? -1 : (startingDirection == Direction.LEFT ? 1 : 0));
        startingBeam.y = startingY + (startingDirection == Direction.DOWN ? -1 : (startingDirection == Direction.UP ? 1 : 0));
        final List<BeamObject> bouncingBeams = new ArrayList<>();
        bouncingBeams.add(startingBeam);
        while (bouncingBeams.size() > 0) {
            final BeamObject beamObject = bouncingBeams.removeFirst();
            int newX = beamObject.x + (beamObject.direction == Direction.RIGHT ? 1 : (beamObject.direction == Direction.LEFT ? -1 : 0));
            int newY = beamObject.y + (beamObject.direction == Direction.DOWN ? 1 : (beamObject.direction == Direction.UP ? -1 : 0));
            if (newX >= 0 && newY >= 0 && newX < width && newY < height) {
                final GridObject intersectingGridObject = grid[newX][newY];
                intersectingGridObject.energized = true;
                if (!intersectingGridObject.passedBeamDirections.contains(beamObject.direction)) {
                    intersectingGridObject.passedBeamDirections.add(beamObject.direction);
                    beamObject.x = newX;
                    beamObject.y = newY;
                    if (intersectingGridObject.gridType == GridType.DIAGONAL_BOTTOM_LEFT_TO_TOP_RIGHT) {
                        if (beamObject.direction == Direction.RIGHT) {
                            beamObject.direction = Direction.UP;
                        } else if (beamObject.direction == Direction.DOWN) {
                            beamObject.direction = Direction.LEFT;
                        } else if (beamObject.direction == Direction.UP) {
                            beamObject.direction = Direction.RIGHT;
                        } else if (beamObject.direction == Direction.LEFT) {
                            beamObject.direction = Direction.DOWN;
                        }
                        bouncingBeams.add(beamObject);
                    } else if (intersectingGridObject.gridType == GridType.DIAGONAL_BOTTOM_RIGHT_TO_TOP_LEFT) {
                        if (beamObject.direction == Direction.RIGHT) {
                            beamObject.direction = Direction.DOWN;
                        } else if (beamObject.direction == Direction.DOWN) {
                            beamObject.direction = Direction.RIGHT;
                        } else if (beamObject.direction == Direction.UP) {
                            beamObject.direction = Direction.LEFT;
                        } else if (beamObject.direction == Direction.LEFT) {
                            beamObject.direction = Direction.UP;
                        }
                        bouncingBeams.add(beamObject);
                    } else if (intersectingGridObject.gridType == GridType.LEFT_RIGHT_SPLITTER) {
                        if (beamObject.direction == Direction.DOWN || beamObject.direction == Direction.UP) {
                            final BeamObject leftBeamObject = new BeamObject();
                            leftBeamObject.x = newX;
                            leftBeamObject.y = newY;
                            leftBeamObject.direction = Direction.LEFT;
                            bouncingBeams.add(leftBeamObject);
                            final BeamObject rightBeamObject = new BeamObject();
                            rightBeamObject.x = newX;
                            rightBeamObject.y = newY;
                            rightBeamObject.direction = Direction.RIGHT;
                            bouncingBeams.add(rightBeamObject);
                        } else {
                            bouncingBeams.add(beamObject);
                        }
                    } else if (intersectingGridObject.gridType == GridType.UP_DOWN_SPLITTER) {
                        if (beamObject.direction == Direction.LEFT || beamObject.direction == Direction.RIGHT) {
                            final BeamObject upBeamObject = new BeamObject();
                            upBeamObject.x = newX;
                            upBeamObject.y = newY;
                            upBeamObject.direction = Direction.UP;
                            bouncingBeams.add(upBeamObject);
                            final BeamObject downBeamObject = new BeamObject();
                            downBeamObject.x = newX;
                            downBeamObject.y = newY;
                            downBeamObject.direction = Direction.DOWN;
                            bouncingBeams.add(downBeamObject);
                        } else {
                            bouncingBeams.add(beamObject);
                        }
                    } else {
                        bouncingBeams.add(beamObject);
                    }
                }
            }
        }

        long energizedCount = 0;

        for (int y = 0; y < height; y++) {
            String row = "";
            for (int x = 0; x < width; x++) {
                if (grid[x][y].gridType == GridType.DIAGONAL_BOTTOM_LEFT_TO_TOP_RIGHT) {
                    row += "/";
                } else if (grid[x][y].gridType == GridType.DIAGONAL_BOTTOM_RIGHT_TO_TOP_LEFT) {
                    row += "\\";
                } else if (grid[x][y].gridType == GridType.UP_DOWN_SPLITTER) {
                    row += "|";
                } else if (grid[x][y].gridType == GridType.LEFT_RIGHT_SPLITTER) {
                    row += "-";
                } else {
                    row += ".";
                }
            }
            //LogUtilities.logPurple(row);
        }
        for (int y = 0; y < height; y++) {
            String row = "";
            for (int x = 0; x < width; x++) {
                if (grid[x][y].energized) {
                    energizedCount += 1;
                    row += "#";
                } else {
                    row += ".";
                }
            }
            //LogUtilities.logPurple(row);
        }

        return energizedCount;
    }
}
