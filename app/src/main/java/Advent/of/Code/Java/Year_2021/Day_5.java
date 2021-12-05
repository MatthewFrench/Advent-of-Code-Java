package Advent.of.Code.Java.Year_2021;

import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day_5 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2021/5/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        {
            final List<String> input = LoadUtilities.loadTextFileAsList(fileName);
            final Map<Point, Long> mapOverlap = new HashMap<>();

            for (final String inputLine : input) {
                final List<String> splitInput = StringUtilities.splitStringIntoList(inputLine, " -> ");
                final List<String> point1Input = StringUtilities.splitStringIntoList(splitInput.get(0), ",");
                final List<String> point2Input = StringUtilities.splitStringIntoList(splitInput.get(1), ",");
                final long point1X = Long.parseLong(point1Input.get(0));
                final long point1Y = Long.parseLong(point1Input.get(1));
                final long point2X = Long.parseLong(point2Input.get(0));
                final long point2Y = Long.parseLong(point2Input.get(1));
                if (point1X == point2X) {
                    for (long y = Math.min(point1Y, point2Y); y <= Math.max(point1Y, point2Y); y++) {
                        final Point position = new Point(point1X, y);
                        mapOverlap.put(position, mapOverlap.getOrDefault(position, 0L) + 1);
                    }
                } else if (point1Y == point2Y) {
                    for (long x = Math.min(point1X, point2X); x <= Math.max(point1X, point2X); x++) {
                        final Point position = new Point(x, point1Y);
                        mapOverlap.put(position, mapOverlap.getOrDefault(position, 0L) + 1);
                    }
                }
            }
            long twoOrGreater = 0;
            for (final Long overlap : mapOverlap.values()) {
                if (overlap >= 2) {
                    twoOrGreater += 1;
                }
            }

            LogUtilities.logGreen("Solution: " + twoOrGreater);
        }
        {
            final List<String> input = LoadUtilities.loadTextFileAsList(fileName);
            final Map<Point, Long> mapOverlap = new HashMap<>();

            for (final String inputLine : input) {
                final List<String> splitInput = StringUtilities.splitStringIntoList(inputLine, " -> ");
                final List<String> point1Input = StringUtilities.splitStringIntoList(splitInput.get(0), ",");
                final List<String> point2Input = StringUtilities.splitStringIntoList(splitInput.get(1), ",");
                final long point1X = Long.parseLong(point1Input.get(0));
                final long point1Y = Long.parseLong(point1Input.get(1));
                final long point2X = Long.parseLong(point2Input.get(0));
                final long point2Y = Long.parseLong(point2Input.get(1));
                if (point1X == point2X) {
                    for (long y = Math.min(point1Y, point2Y); y <= Math.max(point1Y, point2Y); y++) {
                        final Point position = new Point(point1X, y);
                        mapOverlap.put(position, mapOverlap.getOrDefault(position, 0L) + 1);
                    }
                } else if (point1Y == point2Y) {
                    for (long x = Math.min(point1X, point2X); x <= Math.max(point1X, point2X); x++) {
                        final Point position = new Point(x, point1Y);
                        mapOverlap.put(position, mapOverlap.getOrDefault(position, 0L) + 1);
                    }
                } else {
                    final long length = Math.abs(point2X - point1X);
                    final long moveX = point1X < point2X ? 1 : -1;
                    final long moveY = point1Y < point2Y ? 1 : -1;
                    for (long distance = 0L; distance <= length; distance+= 1) {
                        final long x = point1X + distance * moveX;
                        final long y = point1Y + distance * moveY;
                        final Point position = new Point(x, y);
                        mapOverlap.put(position, mapOverlap.getOrDefault(position, 0L) + 1);
                    }
                }
            }
            long twoOrGreater = 0;
            for (final Long overlap : mapOverlap.values()) {
                if (overlap >= 2) {
                    twoOrGreater += 1;
                }
            }

            LogUtilities.logGreen("Solution: " + twoOrGreater);
        }
    }

    @EqualsAndHashCode
    @AllArgsConstructor
    class Point {
        public long x;
        public long y;
    }
}
