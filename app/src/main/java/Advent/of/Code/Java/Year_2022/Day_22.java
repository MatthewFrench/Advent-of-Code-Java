package Advent.of.Code.Java.Year_2022;

import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;
import Advent.of.Code.Java.Utility.Structures.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Day_22 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2022/22/", this);
    }

    enum Facing {
        DOWN,
        LEFT,
        UP,
        RIGHT
    }

    static class Cube {
        // X range (min x, max x), y range (min y, max y)
        Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> frontFace;
        Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> bottomFace;
        Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> backFace;
        Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> topFace;
        Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> leftFace;
        Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> rightFace;
        public Cube() {

        }
        //public
    }

    static class Person {
        int currentX = -1;
        int currentY = 1;
        Facing currentFacing = Facing.RIGHT;

        public void move(
                int value,
                Map<Integer, Map<Integer, String>> grid,
                Map<Integer, Pair<Integer, Integer>> rowYRange,
                Map<Integer, Pair<Integer, Integer>> columnXRange
        ) {
            int deltaX = 0;
            int deltaY = 0;
            if (currentFacing == Facing.UP) {
                deltaY = -1;
            } else if (currentFacing == Facing.DOWN) {
                deltaY = 1;
            } else if (currentFacing == Facing.LEFT) {
                deltaX = -1;
            } else if (currentFacing == Facing.RIGHT) {
                deltaX = 1;
            }
            for (int i = 0; i < value; i++) {
                int newX = currentX + deltaX;
                int newY = currentY + deltaY;
                if (deltaX != 0) {
                    // Wrap new X around
                    if (newX < rowYRange.get(newY).getKey()) {
                        newX = rowYRange.get(newY).getValue();
                    } else if (newX > rowYRange.get(newY).getValue()) {
                        newX = rowYRange.get(newY).getKey();
                    }
                } else if (deltaY != 0) {
                    // Wrap new Y around
                    if (newY < columnXRange.get(newX).getKey()) {
                        newY = columnXRange.get(newX).getValue();
                    } else if (newY > columnXRange.get(newX).getValue()) {
                        newY = columnXRange.get(newX).getKey();
                    }
                }
                if (grid.get(newX).get(newY).equals(".")) {
                    currentX = newX;
                    currentY = newY;
                } else {
                    break;
                }
            }
        }

        public void moveCube(
                int value,
                Map<Integer, Map<Integer, String>> grid,
                Map<Integer, Pair<Integer, Integer>> rowYRange,
                Map<Integer, Pair<Integer, Integer>> columnXRange
        ) {
            int deltaX = 0;
            int deltaY = 0;
            if (currentFacing == Facing.UP) {
                deltaY = -1;
            } else if (currentFacing == Facing.DOWN) {
                deltaY = 1;
            } else if (currentFacing == Facing.LEFT) {
                deltaX = -1;
            } else if (currentFacing == Facing.RIGHT) {
                deltaX = 1;
            }
            for (int i = 0; i < value; i++) {
                int newX = currentX + deltaX;
                int newY = currentY + deltaY;
                /*
                Fa
                 */
                if (deltaX != 0) {
                    // Wrap new X around
                    if (newX < rowYRange.get(newY).getKey()) {
                        newX = rowYRange.get(newY).getValue();
                    } else if (newX > rowYRange.get(newY).getValue()) {
                        newX = rowYRange.get(newY).getKey();
                    }
                } else if (deltaY != 0) {
                    // Wrap new Y around
                    if (newY < columnXRange.get(newX).getKey()) {
                        newY = columnXRange.get(newX).getValue();
                    } else if (newY > columnXRange.get(newX).getValue()) {
                        newY = columnXRange.get(newX).getKey();
                    }
                }
                if (grid.get(newX).get(newY).equals(".")) {
                    currentX = newX;
                    currentY = newY;
                } else {
                    break;
                }
            }
        }
    }

    public void executeWithInput(final String fileName) throws Exception {
        List<String> splitInput = StringUtilities.splitStringIntoList(LoadUtilities.loadTextFileAsString(fileName), "\n\n");
        List<String> mapRows = StringUtilities.splitStringIntoList(splitInput.get(0), "\n");
        Map<Integer, Map<Integer, String>> grid = new HashMap<>();
        Map<Integer, Pair<Integer, Integer>> rowYRange = new HashMap<>();
        Map<Integer, Pair<Integer, Integer>> columnXRange = new HashMap<>();
        for (int y = 1; y <= mapRows.size(); y++) {
            final String row = mapRows.get(y - 1);
            for (int x = 1; x <= row.length(); x++) {
                final String piece = StringUtilities.getStringChunk(row, x - 1, 1);
                if (!piece.equals(" ")) {
                    grid.computeIfAbsent(x, (xValue) -> new HashMap<>()).putIfAbsent(y, piece);

                    final int xValue = x;
                    Pair<Integer, Integer> rowRange = rowYRange.computeIfAbsent(y, (yValue)->new Pair<>(xValue, xValue));
                    rowYRange.put(y, new Pair<>(Math.min(rowRange.getKey(), x), Math.max(rowRange.getValue(), x)));

                    final int yValue = y;
                    Pair<Integer, Integer> columnRange = columnXRange.computeIfAbsent(x, (xValue2)->new Pair<>(yValue, yValue));
                    columnXRange.put(x, new Pair<>(Math.min(columnRange.getKey(), y), Math.max(columnRange.getValue(), y)));
                }
            }
        }

        {
            Person person = new Person();
            person.currentY = 1;
            person.currentX = rowYRange.get(1).getKey();
            final List<String> instructions = StringUtilities.splitStringIntoList(splitInput.get(1), "");
            String currentMoveValue = "";
            for (final String instructionCharacter : instructions) {
                if (instructionCharacter.equals("R") || instructionCharacter.equals("L")) {
                    if (currentMoveValue.length() > 0) {
                        int moveAmount = Integer.parseInt(currentMoveValue);
                        currentMoveValue = "";
                        person.move(moveAmount, grid, rowYRange, columnXRange);
                    }
                    if (instructionCharacter.equals("R")) {
                        if (person.currentFacing == Facing.UP) {
                            person.currentFacing = Facing.RIGHT;
                        } else if (person.currentFacing == Facing.DOWN) {
                            person.currentFacing = Facing.LEFT;
                        } else if (person.currentFacing == Facing.LEFT) {
                            person.currentFacing = Facing.UP;
                        } else if (person.currentFacing == Facing.RIGHT) {
                            person.currentFacing = Facing.DOWN;
                        }
                    } else {
                        if (person.currentFacing == Facing.UP) {
                            person.currentFacing = Facing.LEFT;
                        } else if (person.currentFacing == Facing.DOWN) {
                            person.currentFacing = Facing.RIGHT;
                        } else if (person.currentFacing == Facing.LEFT) {
                            person.currentFacing = Facing.DOWN;
                        } else if (person.currentFacing == Facing.RIGHT) {
                            person.currentFacing = Facing.UP;
                        }
                    }
                } else {
                    currentMoveValue += instructionCharacter;
                }
            }
            if (currentMoveValue.length() > 0) {
                int moveAmount = Integer.parseInt(currentMoveValue);
                person.move(moveAmount, grid, rowYRange, columnXRange);
            }

            long row = person.currentY;
            long column = person.currentX;
            long facing = 0;
            if (person.currentFacing == Facing.RIGHT) {
                facing = 0;
            } else if (person.currentFacing == Facing.DOWN) {
                facing = 1;
            } else if (person.currentFacing == Facing.LEFT) {
                facing = 2;
            } else if (person.currentFacing == Facing.UP) {
                facing = 3;
            }

            LogUtilities.logGreen("Solution: " + (1000 * row + 4 * column + facing));
        }

        {
            Person person = new Person();
            person.currentY = 1;
            person.currentX = rowYRange.get(1).getKey();
            final List<String> instructions = StringUtilities.splitStringIntoList(splitInput.get(1), "");
            String currentMoveValue = "";
            for (final String instructionCharacter : instructions) {
                if (instructionCharacter.equals("R") || instructionCharacter.equals("L")) {
                    if (currentMoveValue.length() > 0) {
                        int moveAmount = Integer.parseInt(currentMoveValue);
                        currentMoveValue = "";
                        person.moveCube(moveAmount, grid, rowYRange, columnXRange);
                    }
                    if (instructionCharacter.equals("R")) {
                        if (person.currentFacing == Facing.UP) {
                            person.currentFacing = Facing.RIGHT;
                        } else if (person.currentFacing == Facing.DOWN) {
                            person.currentFacing = Facing.LEFT;
                        } else if (person.currentFacing == Facing.LEFT) {
                            person.currentFacing = Facing.UP;
                        } else if (person.currentFacing == Facing.RIGHT) {
                            person.currentFacing = Facing.DOWN;
                        }
                    } else {
                        if (person.currentFacing == Facing.UP) {
                            person.currentFacing = Facing.LEFT;
                        } else if (person.currentFacing == Facing.DOWN) {
                            person.currentFacing = Facing.RIGHT;
                        } else if (person.currentFacing == Facing.LEFT) {
                            person.currentFacing = Facing.DOWN;
                        } else if (person.currentFacing == Facing.RIGHT) {
                            person.currentFacing = Facing.UP;
                        }
                    }
                } else {
                    currentMoveValue += instructionCharacter;
                }
            }
            if (currentMoveValue.length() > 0) {
                int moveAmount = Integer.parseInt(currentMoveValue);
                person.move(moveAmount, grid, rowYRange, columnXRange);
            }

            long row = person.currentY;
            long column = person.currentX;
            long facing = 0;
            if (person.currentFacing == Facing.RIGHT) {
                facing = 0;
            } else if (person.currentFacing == Facing.DOWN) {
                facing = 1;
            } else if (person.currentFacing == Facing.LEFT) {
                facing = 2;
            } else if (person.currentFacing == Facing.UP) {
                facing = 3;
            }

            LogUtilities.logGreen("Solution 2: " + (1000 * row + 4 * column + facing));
        }
    }
}
