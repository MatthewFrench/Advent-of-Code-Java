package Advent.of.Code.Java.Year_2023;

import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;
import Advent.of.Code.Java.Utility.Structures.Pair;
import Advent.of.Code.Java.Utility.Structures.Quadruple;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class Day_18 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2023/18/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        runSolution1(fileName);
        runSolution2(fileName);
    }

    enum GridObject {
        UNKNOWN,
        OUTSIDE,
        PATH
    }

    private void runSolution1(final String fileName) throws Exception {
        /*
        final List<String> lines = LoadUtilities.loadTextFileAsList(fileName);

        int minX = 0;
        int minY = 0;
        int maxX = 0;
        int maxY = 0;
        int currentX = 0;
        int currentY = 0;
        // Todo: Make a function for calculating the area of a list of connected points
        List<Pair<Integer, Integer>> positions = new ArrayList<>();
        positions.add(new Pair<>(0, 0));
        for (final String line : lines) {
            final List<String> splitLine = StringUtilities.splitStringIntoList(line, " ");
            final String direction = splitLine.get(0);
            final int moveCount = Integer.parseInt(splitLine.get(1));
            final int priorX = currentX;
            final int priorY = currentY;
            if (direction.equals("R")) {
                currentX += moveCount;
            } else if (direction.equals("L")) {
                currentX -= moveCount;
            } else if (direction.equals("U")) {
                currentY -= moveCount;
            } else if (direction.equals("D")) {
                currentY += moveCount;
            }
            if (priorX != currentX) {
                for (int index = Math.min(priorX, currentX); index <= Math.max(priorX, currentX); index++) {
                    positions.add(new Pair<>(index, currentY));
                }
            } else if (priorY != currentY) {
                for (int index = Math.min(priorY, currentY); index <= Math.max(priorY, currentY); index++) {
                    positions.add(new Pair<>(currentX, index));
                }
            }
            minX = Math.min(minX, currentX);
            minY = Math.min(minY, currentY);
            maxX = Math.max(maxX, currentX);
            maxY = Math.max(maxY, currentY);
        }
        final int width = (maxX - minX) + 1;
        final int height = (maxY - minY) + 1;
        final int offsetX = -minX + 1;
        final int offsetY = -minY + 1;
        final GridObject[][] grid = new GridObject[width + 2][height + 2];
        for (int x = 0; x < width + 2; x++) {
            for (int y = 0; y < height + 2; y++) {
                if (x == 0 || y == 0 || x == width + 1 || y == height + 1) {
                    grid[x][y] = GridObject.OUTSIDE;
                } else {
                    grid[x][y] = GridObject.UNKNOWN;
                }
            }
        }
        for (final Pair<Integer, Integer> position : positions) {
            grid[position.getKey() + offsetX][position.getValue() + offsetY] = GridObject.PATH;
        }
        boolean stillChanging = true;
        while (stillChanging) {
            stillChanging = false;
            for (int x = 0; x < width + 2; x++) {
                for (int y = 0; y < height + 2; y++) {
                    if (grid[x][y] == GridObject.OUTSIDE) {
                        if (x > 0 && grid[x - 1][y] == GridObject.UNKNOWN) {
                            grid[x - 1][y] = GridObject.OUTSIDE;
                            stillChanging = true;
                        }
                        if (y > 0 && grid[x][y - 1] == GridObject.UNKNOWN) {
                            grid[x][y - 1] = GridObject.OUTSIDE;
                            stillChanging = true;
                        }
                        if (x < width + 1 && grid[x + 1][y] == GridObject.UNKNOWN) {
                            grid[x + 1][y] = GridObject.OUTSIDE;
                            stillChanging = true;
                        }
                        if (y < height + 1 && grid[x][y + 1] == GridObject.UNKNOWN) {
                            grid[x][y + 1] = GridObject.OUTSIDE;
                            stillChanging = true;
                        }
                    }
                }
            }
        }
        // Count the unknowns and paths total
        long total = 0;
        for (int y = 0; y < height + 2; y++) {
            String row = "";
            for (int x = 0; x < width + 2; x++) {
                if (grid[x][y] == GridObject.PATH || grid[x][y] == GridObject.UNKNOWN) {
                    total += 1;
                }
                if (grid[x][y] == GridObject.OUTSIDE) {
                    row += ".";
                } else if (grid[x][y] == GridObject.PATH) {
                    row += "X";
                } else if (grid[x][y] == GridObject.UNKNOWN) {
                    row += "#";
                }
            }
            //LogUtilities.logPurple(row);
        }

        LogUtilities.logGreen("Solution 1: " + total);

         */

        final List<String> lines = LoadUtilities.loadTextFileAsList(fileName);

        long currentX = 0;
        long currentY = 0;
        long additionalX = 0;
        long additionalY = 0;
        // Todo: Make a function for calculating the area of a list of connected points
        List<Pair<Long, Long>> points = new ArrayList<>();
        points.add(new Pair<>(0L, 0L));
        for (final String line : lines) {
            final List<String> splitLine = StringUtilities.splitStringIntoList(line, " ");
            final String direction = splitLine.get(0);
            final int moveCount = Integer.parseInt(splitLine.get(1));

            long priorX = currentX;
            long priorY = currentY;
            if (direction.equals("R")) {
                currentX += moveCount;
            } else if (direction.equals("L")) {
                currentX -= moveCount;
            } else if (direction.equals("U")) {
                currentY -= moveCount;
            } else if (direction.equals("D")) {
                currentY += moveCount;
            }
            additionalX += Math.abs(priorX - currentX);
            additionalY += Math.abs(priorY - currentY);
            points.add(new Pair<>(currentX, currentY));
        }
        long area = (long) area(points);
        long areaTotal = area + (additionalX + additionalY) / 2 + 1;
        LogUtilities.logGreen("Solution 1: " + areaTotal);
    }

    private void runSolution2(final String fileName) throws Exception {
        final List<String> lines = LoadUtilities.loadTextFileAsList(fileName);

        long currentX = 0;
        long currentY = 0;
        long additionalX = 0;
        long additionalY = 0;
        // Todo: Make a function for calculating the area of a list of connected points
        List<Pair<Long, Long>> points = new ArrayList<>();
        points.add(new Pair<>(0L, 0L));
        for (final String line : lines) {
            final List<String> splitLine = StringUtilities.splitStringIntoList(line, "#");
            final String hexadecimal = StringUtilities.removeEndChunkIfExists(splitLine.get(1), ")");
            final long moveCount = Long.parseLong(hexadecimal.substring(0, 5), 16);
            final String direction = hexadecimal.substring(5);

            long priorX = currentX;
            long priorY = currentY;
            if (direction.equals("0")) {
                currentX += moveCount;
            } else if (direction.equals("2")) {
                currentX -= moveCount;
            } else if (direction.equals("3")) {
                currentY -= moveCount;
            } else if (direction.equals("1")) {
                currentY += moveCount;
            }
            additionalX += Math.abs(priorX - currentX);
            additionalY += Math.abs(priorY - currentY);
            points.add(new Pair<>(currentX, currentY));
        }
        // Add outside fence
        // I don't know why this is correct. I just saw the pattern and made this calculation.
        long area = (long) area(points);
        long areaTotal = area + (additionalX + additionalY) / 2 + 1;
        LogUtilities.logGreen("Solution 2: " + areaTotal);
    }

    // This was pulled from https://stackoverflow.com/questions/25987465/computing-area-of-a-polygon-in-java
    public double area(final List<Pair<Long, Long>> vertices)
    {
        double sum = 0;
        for (int i = 0; i < vertices.size() ; i++)
        {
            if (i == 0)
            {
                //System.out.println(vertices.get(i).getKey() + "x" + (vertices.get(i + 1).y + "-" + vertices.get(vertices.size() - 1).y));
                sum += vertices.get(i).getKey() * (vertices.get(i + 1).getValue() - vertices.get(vertices.size() - 1).getValue());
            }
            else if (i == vertices.size() - 1)
            {
                //System.out.println(vertices.get(i).getKey() + "x" + (vertices.get(0).y + "-" + vertices.get(i - 1).y));
                sum += vertices.get(i).getKey() * (vertices.get(0).getValue() - vertices.get(i - 1).getValue());
            }
            else
            {
                //System.out.println(vertices.get(i).x + "x" + (vertices.get(i + 1).y + "-" + vertices.get(i - 1).y));
                sum += vertices.get(i).getKey() * (vertices.get(i + 1).getValue() - vertices.get(i - 1).getValue());
            }
        }

        double area = 0.5 * Math.abs(sum);
        return area;

    }
}
