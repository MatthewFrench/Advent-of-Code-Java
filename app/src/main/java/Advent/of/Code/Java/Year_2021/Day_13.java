package Advent.of.Code.Java.Year_2021;

import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day_13 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2021/13/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        final String input = LoadUtilities.loadTextFileAsString(fileName);
        final List<String> inputSplit = StringUtilities.splitStringIntoList(input, "\n\n");
        final Set<Point> points = new HashSet<>();

        final List<String> lines = StringUtilities.splitStringIntoList(inputSplit.get(0), "\n");
        for (final String line : lines) {
            final List<String> lineSplit = StringUtilities.splitStringIntoList(line, ",");
            points.add(new Point(Integer.parseInt(lineSplit.get(0)), Integer.parseInt(lineSplit.get(1))));
        }

        final List<String> folds = StringUtilities.splitStringIntoList(inputSplit.get(1), "\n");
        int round = 1;
        for (final String fold : folds) {
            final String foldPieces = StringUtilities.removeStartChunk(fold, "fold along ");
            final List<String> foldSplit = StringUtilities.splitStringIntoList(foldPieces, "=");
            final int foldValue = Integer.parseInt(foldSplit.get(1));
            final String axis = foldSplit.get(0);
            if (axis.equals("x")) { // fold left
                final Set<Point> previousPoints = new HashSet<>(points);
                for (final Point point : previousPoints) {
                    if (point.x > foldValue) {
                        points.remove(point);
                        point.x = foldValue - (point.x - foldValue);
                        points.add(new Point(point.x, point.y));
                    }
                }
            } else if (axis.equals("y")) { // fold up
                final Set<Point> previousPoints = new HashSet<>(points);
                for (final Point point : previousPoints) {
                    if (point.y > foldValue) {
                        points.remove(point);
                        point.y = foldValue - (point.y - foldValue);
                        points.add(new Point(point.x, point.y));
                    }
                }
            }
            LogUtilities.logGreen("For fold: " + round + ": " + points.size());
            round += 1;

            List<Point> pointList = new ArrayList<>(points);
            int minX = pointList.get(0).x;
            int maxX = pointList.get(0).x;
            int minY = pointList.get(0).y;
            int maxY = pointList.get(0).y;
            for (final Point point : points) {
                minX = Math.min(minX, point.x);
                minY = Math.min(minY, point.y);
                maxX = Math.max(maxX, point.x);
                maxY = Math.max(maxY, point.y);
            }
            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\n");
            for (int y = minY; y <= maxY; y++) {
                for (int x = minX; x <= maxX; x++) {
                    final Point point = new Point(x, y);
                    if (points.contains(point)) {
                        stringBuilder.append("#");
                    } else {
                        stringBuilder.append(".");
                    }
                }
                stringBuilder.append("\n");
            }
            LogUtilities.logGreen("Printout: ");
            LogUtilities.logGreen(stringBuilder.toString());
        }

        LogUtilities.logGreen("Solution: " + points.size());
    }

    @AllArgsConstructor
    @EqualsAndHashCode
    class Point {
        int x;
        int y;
    }
}
