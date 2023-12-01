package Advent.of.Code.Java.Year_2022;

import Advent.of.Code.Java.Utility.DataUtilities;
import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;
import Advent.of.Code.Java.Utility.Structures.Pair;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Day_24 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2022/24/", this);
    }

    enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    class Blizzard {
        int x;
        int y;
        Direction direction;
    }

    @EqualsAndHashCode
    class PersonScenario {
        int x;
        int y;
    }

    public void executeWithInput(final String fileName) throws Exception {
        List<String> splitInput = StringUtilities.splitStringIntoList(LoadUtilities.loadTextFileAsString(fileName), "\n");

        // True means wall, false means open space, not existing means non-accessible
        Map<Integer, Map<Integer, Boolean>> terraignGrid = new HashMap<>();
        Map<Integer, Map<Integer, HashSet<Blizzard>>> blizzardGrid = new HashMap<>();
        List<Blizzard> blizzards = new ArrayList<>();

        {
            int y = 0;
            for (final String row : splitInput) {
                final List<String> pieces = StringUtilities.splitStringIntoList(row, "");
                int x = 0;
                for (final String piece : pieces) {
                    terraignGrid.computeIfAbsent(x, (xValue) -> new HashMap<>()).put(y, piece.equals("#"));
                    if (piece.equals(">") || piece.equals("^") || piece.equals("v") || piece.equals("<")) {
                        final Blizzard blizzard = new Blizzard();
                        blizzard.x = x;
                        blizzard.y = y;
                        if (piece.equals(">")) {
                            blizzard.direction = Direction.RIGHT;
                        } else if (piece.equals("<")) {
                            blizzard.direction = Direction.LEFT;
                        } else if (piece.equals("v")) {
                            blizzard.direction = Direction.DOWN;
                        } else if (piece.equals("^")) {
                            blizzard.direction = Direction.UP;
                        }
                        blizzards.add(blizzard);
                        blizzardGrid.computeIfAbsent(x, (xValue) -> new HashMap<>()).computeIfAbsent(y, (yValue) -> new HashSet<>()).add(blizzard);
                    }
                    x += 1;
                }
                y += 1;
            }
        }

        int gridWidth = splitInput.get(0).length();
        int gridHeight = splitInput.size();
        int startX = 1;
        int startY = 0;
        int endX = gridWidth - 2;
        int endY = gridHeight - 1;

        List<PersonScenario> personScenarios = new ArrayList<>();
        final PersonScenario startingPersonScenario = new PersonScenario();
        startingPersonScenario.x = startX;
        startingPersonScenario.y = startY;
        personScenarios.add(startingPersonScenario);
        int currentMinute = 0;
        scenarioLoop: while (personScenarios.size() > 0) {
            for (final Blizzard blizzard : blizzards) {
                blizzardGrid.get(blizzard.x).get(blizzard.y).remove(blizzard);
                int newX = blizzard.x;
                int newY = blizzard.y;
                if (blizzard.direction == Direction.UP) {
                    newY -= 1;
                } else if (blizzard.direction == Direction.DOWN) {
                    newY += 1;
                } else if (blizzard.direction == Direction.LEFT) {
                    newX -= 1;
                } else if (blizzard.direction == Direction.RIGHT) {
                    newX += 1;
                }
                if (newX >= gridWidth - 1) {
                    newX = 1;
                }
                if (newX <= 0) {
                    newX = gridWidth - 2;
                }
                if (newY >= gridHeight - 1) {
                    newY = 1;
                }
                if (newY <= 0) {
                    newY = gridHeight - 2;
                }
                blizzard.x = newX;
                blizzard.y = newY;
                blizzardGrid.computeIfAbsent(blizzard.x, (xValue) -> new HashMap<>()).computeIfAbsent(blizzard.y, (yValue) -> new HashSet<>()).add(blizzard);
            }
            final HashSet<PersonScenario> scenariosToAdd = new HashSet<>();
            while (personScenarios.size() > 0) {
                final PersonScenario personScenario = personScenarios.remove(personScenarios.size() - 1);
                final int[][] positionDeltasToCheck = { { 0, 0 }, { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
                // Check if the person can stand still or move in any direction, add it to scenarios to add
                for (final int[] delta : positionDeltasToCheck) {
                    int newX = personScenario.x + delta[0];
                    int newY = personScenario.y + delta[1];
                    if (terraignGrid.containsKey(newX) && terraignGrid.get(newX).containsKey(newY) && !terraignGrid.get(newX).get(newY)) {
                        if (!blizzardGrid.containsKey(newX) || !blizzardGrid.get(newX).containsKey(newY) || blizzardGrid.get(newX).get(newY).size() == 0) {
                            final PersonScenario newScenario = new PersonScenario();
                            newScenario.x = newX;
                            newScenario.y = newY;
                            scenariosToAdd.add(newScenario);
                        }
                    }
                }
            }
            // Add all scenarios
            personScenarios.addAll(scenariosToAdd);
            currentMinute += 1;
            // Now check if any scenario reached the goal
            for (final PersonScenario personScenario : personScenarios) {
                if (personScenario.x == endX && personScenario.y == endY) {
                    break scenarioLoop;
                }
            }
        }

        LogUtilities.logGreen("Solution: " + currentMinute);



        personScenarios = new ArrayList<>();
        final PersonScenario endingPersonScenario = new PersonScenario();
        endingPersonScenario.x = endX;
        endingPersonScenario.y = endY;
        personScenarios.add(endingPersonScenario);
        scenarioLoop: while (personScenarios.size() > 0) {
            for (final Blizzard blizzard : blizzards) {
                blizzardGrid.get(blizzard.x).get(blizzard.y).remove(blizzard);
                int newX = blizzard.x;
                int newY = blizzard.y;
                if (blizzard.direction == Direction.UP) {
                    newY -= 1;
                } else if (blizzard.direction == Direction.DOWN) {
                    newY += 1;
                } else if (blizzard.direction == Direction.LEFT) {
                    newX -= 1;
                } else if (blizzard.direction == Direction.RIGHT) {
                    newX += 1;
                }
                if (newX >= gridWidth - 1) {
                    newX = 1;
                }
                if (newX <= 0) {
                    newX = gridWidth - 2;
                }
                if (newY >= gridHeight - 1) {
                    newY = 1;
                }
                if (newY <= 0) {
                    newY = gridHeight - 2;
                }
                blizzard.x = newX;
                blizzard.y = newY;
                blizzardGrid.computeIfAbsent(blizzard.x, (xValue) -> new HashMap<>()).computeIfAbsent(blizzard.y, (yValue) -> new HashSet<>()).add(blizzard);
            }
            final HashSet<PersonScenario> scenariosToAdd = new HashSet<>();
            while (personScenarios.size() > 0) {
                final PersonScenario personScenario = personScenarios.remove(personScenarios.size() - 1);
                final int[][] positionDeltasToCheck = { { 0, 0 }, { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
                // Check if the person can stand still or move in any direction, add it to scenarios to add
                for (final int[] delta : positionDeltasToCheck) {
                    int newX = personScenario.x + delta[0];
                    int newY = personScenario.y + delta[1];
                    if (terraignGrid.containsKey(newX) && terraignGrid.get(newX).containsKey(newY) && !terraignGrid.get(newX).get(newY)) {
                        if (!blizzardGrid.containsKey(newX) || !blizzardGrid.get(newX).containsKey(newY) || blizzardGrid.get(newX).get(newY).size() == 0) {
                            final PersonScenario newScenario = new PersonScenario();
                            newScenario.x = newX;
                            newScenario.y = newY;
                            scenariosToAdd.add(newScenario);
                        }
                    }
                }
            }
            // Add all scenarios
            personScenarios.addAll(scenariosToAdd);
            currentMinute += 1;
            // Now check if any scenario reached the goal
            for (final PersonScenario personScenario : personScenarios) {
                if (personScenario.x == startX && personScenario.y == startY) {
                    break scenarioLoop;
                }
            }
        }



        personScenarios = new ArrayList<>();
        final PersonScenario starting2PersonScenario = new PersonScenario();
        starting2PersonScenario.x = startX;
        starting2PersonScenario.y = startY;
        personScenarios.add(starting2PersonScenario);
        scenarioLoop: while (personScenarios.size() > 0) {
            for (final Blizzard blizzard : blizzards) {
                blizzardGrid.get(blizzard.x).get(blizzard.y).remove(blizzard);
                int newX = blizzard.x;
                int newY = blizzard.y;
                if (blizzard.direction == Direction.UP) {
                    newY -= 1;
                } else if (blizzard.direction == Direction.DOWN) {
                    newY += 1;
                } else if (blizzard.direction == Direction.LEFT) {
                    newX -= 1;
                } else if (blizzard.direction == Direction.RIGHT) {
                    newX += 1;
                }
                if (newX >= gridWidth - 1) {
                    newX = 1;
                }
                if (newX <= 0) {
                    newX = gridWidth - 2;
                }
                if (newY >= gridHeight - 1) {
                    newY = 1;
                }
                if (newY <= 0) {
                    newY = gridHeight - 2;
                }
                blizzard.x = newX;
                blizzard.y = newY;
                blizzardGrid.computeIfAbsent(blizzard.x, (xValue) -> new HashMap<>()).computeIfAbsent(blizzard.y, (yValue) -> new HashSet<>()).add(blizzard);
            }
            final HashSet<PersonScenario> scenariosToAdd = new HashSet<>();
            while (personScenarios.size() > 0) {
                final PersonScenario personScenario = personScenarios.remove(personScenarios.size() - 1);
                final int[][] positionDeltasToCheck = { { 0, 0 }, { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
                // Check if the person can stand still or move in any direction, add it to scenarios to add
                for (final int[] delta : positionDeltasToCheck) {
                    int newX = personScenario.x + delta[0];
                    int newY = personScenario.y + delta[1];
                    if (terraignGrid.containsKey(newX) && terraignGrid.get(newX).containsKey(newY) && !terraignGrid.get(newX).get(newY)) {
                        if (!blizzardGrid.containsKey(newX) || !blizzardGrid.get(newX).containsKey(newY) || blizzardGrid.get(newX).get(newY).size() == 0) {
                            final PersonScenario newScenario = new PersonScenario();
                            newScenario.x = newX;
                            newScenario.y = newY;
                            scenariosToAdd.add(newScenario);
                        }
                    }
                }
            }
            // Add all scenarios
            personScenarios.addAll(scenariosToAdd);
            currentMinute += 1;
            // Now check if any scenario reached the goal
            for (final PersonScenario personScenario : personScenarios) {
                if (personScenario.x == endX && personScenario.y == endY) {
                    break scenarioLoop;
                }
            }
        }

        LogUtilities.logGreen("Solution 2: " + currentMinute);
    }
}
