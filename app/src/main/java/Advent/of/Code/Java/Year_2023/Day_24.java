package Advent.of.Code.Java.Year_2023;

import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LineUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.Coordinate2D;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;

import java.util.ArrayList;
import java.util.List;

public class Day_24 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2023/24/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        runSolution1(fileName);
    }

    class Hailstone {
        public final Coordinate2D<Double> coordinate;
        public final Coordinate2D<Double> velocity;
        public Hailstone(final long x, final long y, final long velocityX, final long velocityY) {
            this.coordinate = new Coordinate2D<>((double)x, (double)y);
            this.velocity = new Coordinate2D<>((double)velocityX, (double)velocityY);
        }
    }

    private void runSolution1(final String fileName) throws Exception {
        long minimum = 7;
        long maximum = 27;
        if (!fileName.contains("sample")) {
            minimum = 200000000000000L;
            maximum = 400000000000000L;
        }
        LogUtilities.logGreen("Solution 1: " + getNumberOfIntersections(fileName, minimum, maximum));
    }

    public long getNumberOfIntersections(final String fileName, long minimum, long maximum) throws Exception {
        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);
        final List<Hailstone> hailstones = new ArrayList<>();
        for (final String line : input) {
            final List<String> halves = StringUtilities.splitStringIntoList(line, "@");
            final List<String> coordinates = StringUtilities.splitStringIntoList(halves.get(0), ",");
            final List<String> velocities = StringUtilities.splitStringIntoList(halves.get(1), ",");
            // Ignore Z
            hailstones.add(new Hailstone(Long.parseLong(StringUtilities.removeWhitespaceFromString(coordinates.get(0))), Long.parseLong(StringUtilities.removeWhitespaceFromString(coordinates.get(1))),
                    Long.parseLong(StringUtilities.removeWhitespaceFromString(velocities.get(0))), Long.parseLong(StringUtilities.removeWhitespaceFromString(velocities.get(1)))));
        }
        long validIntersections = 0;
        for (int index1 = 0; index1 < hailstones.size(); index1++) {
            final Hailstone hailstone1 = hailstones.get(index1);
            for (int index2 = index1 + 1; index2 < hailstones.size(); index2++) {
                final Hailstone hailstone2 = hailstones.get(index2);
                final LineUtilities.IntersectionInformation intersectionInformation = LineUtilities.getCoordinateIntersection(hailstone1.coordinate, hailstone1.velocity, hailstone2.coordinate, hailstone2.velocity);
                if (intersectionInformation.getIntersectionType() == LineUtilities.IntersectionType.INTERSECTION) {
                    if (LineUtilities.isPointInFront(intersectionInformation.getIntersection(), hailstone1.coordinate, hailstone1.velocity)
                    && LineUtilities.isPointInFront(intersectionInformation.getIntersection(), hailstone2.coordinate, hailstone2.velocity)) {
                        if (intersectionInformation.getIntersection().getX() >= minimum
                                && intersectionInformation.getIntersection().getX() <= maximum
                                && intersectionInformation.getIntersection().getY() >= minimum
                                && intersectionInformation.getIntersection().getY() <= maximum) {
                            validIntersections += 1;
                        }
                    }
                }
            }
        }
        return validIntersections;
    }
}
