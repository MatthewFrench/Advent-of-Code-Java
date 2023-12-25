package Advent.of.Code.Java.Utility;

import Advent.of.Code.Java.Utility.Structures.Coordinate2D;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Objects;

public class LineUtilities {
    public enum IntersectionType {
        NO_INTERSECTION_PARALLEL_NEVER_TOUCH,
        PARALLEL_ALWAYS_TOUCH,
        INTERSECTION
    }
    @AllArgsConstructor
    @Data
    public static class IntersectionInformation {
        final IntersectionType intersectionType;
        final Coordinate2D<Double> intersection;
    }
    // Todo: Add unit tests for this function to make sure it is correct
    public static IntersectionInformation getCoordinateIntersection(final Coordinate2D<Double> coordinate1, final Coordinate2D<Double> slope1, final Coordinate2D<Double> coordinate2, final Coordinate2D<Double> slope2) {
        if ((Objects.equals(slope1.getX(), slope2.getX()) && Objects.equals(slope1.getY(), slope2.getY()))
                // Negative slope is also parallel
                || (Objects.equals(slope1.getX(), -slope2.getX()) && Objects.equals(slope1.getY(), -slope2.getY()))) {
            // Parallel always touch
            // x == 0, y == 0, velX = 1, velY = 2
            // x == 2, y == 4, velX = 1, velY = 2
            // If coordinates are the same, they are always touching
            if (Objects.equals(coordinate1.getX(), coordinate2.getX()) && Objects.equals(coordinate1.getY(), coordinate2.getY())) {
                return new IntersectionInformation(IntersectionType.PARALLEL_ALWAYS_TOUCH, null);
            }
            // Move coordinate Y to be the same on both, if X is the same, they are always touching
            // Todo: Do I need to account for if slopes are pointing in the opposite directions?
            double stepsToAlign = (coordinate2.getY() - coordinate1.getY()) / slope1.getY();
            double newX = coordinate1.getX() + (slope1.getX() * stepsToAlign);
            if (newX == coordinate2.getX()) {
                return new IntersectionInformation(IntersectionType.PARALLEL_ALWAYS_TOUCH, null);
            }

            // Parallel never touch
            // x == 0, y == 0, velX = 1, velY = 1
            // x == 2, y == 0, velX = 1, velY = 1
            return new IntersectionInformation(IntersectionType.NO_INTERSECTION_PARALLEL_NEVER_TOUCH, null);
        }
        // Intersection point is where y and x are the same
        // y = mx + b
        // x == 0, y == 0, velX = 1, velY = 1
        //      y = 1x + 0
        // x = 5, y == 5, velX = -2, velY = 3
        //      y = (3/-2)x + 25/2
        // 1x + 0 = (3/-2)x + 25/2
        //      1x = (3/-2)x + 25/2
        //      1x - (3/-2)x = 25/2
        //      2.5x = 25/2
        //      x = 5
        // Intersection point: 5, 5
        // x == -2, y == -1, velX = 1, velY = 1
        //      y = 1x + 1
        //          Slope = 1 / 1
        //          b = y - (velX / velY) * x
        //              b = y - (1 / 1)x
        //              b = (-1) - 1*(-2)
        //              b = 1
        // x = 5, y == 5, velX = -2, velY = 3
        //      y = (3/-2)x + 25/2
        //          Slope = 3/-2
        //          b = y - (velY / velX) * x
        //              b = y - (3 / -2)x
        //              b = (5) - (3 / -2) * 5
        //              b = 12.5
        //      (3/-2)x + 12.5 = 1x + 1
        //          (3/-2)x - 1x = 1 - 12.5
        //          x = -11.5 / -2.5
        //          x = 4.6
        // Intersection point: 4.6, 5.6
        // Todo: Do I need to account for when slope x is 0?
        final double slope1Value = slope1.getY() / slope1.getX();
        final double slope2Value = slope2.getY() / slope2.getX();
        final double coordinate1B = coordinate1.getY() - (slope1Value * coordinate1.getX());
        final double coordinate2B = coordinate2.getY() - (slope2Value * coordinate2.getX());
        // Formula: slope1 * x + b1 = slope2 * x + b2
        //          slope1 * x - slope2 * x = b2 - b1
        //          (slope1 - slope2) * x = (b2 - b1)
        //          x = (b2 - b1) / (slope1 - slope2)
        // Todo: Do I need to account for when (slope1 - slope2) is 0?
        final double xIntersect = (coordinate2B - coordinate1B) / (slope1Value - slope2Value);
        final double yIntersect = slope1Value * xIntersect + coordinate1B;

        return new IntersectionInformation(IntersectionType.INTERSECTION, new Coordinate2D<>(xIntersect, yIntersect));
    }
    /*
    A line has a starting point and velocity. This checks if a point is in front of that starting point and velocity.
     */
    public static boolean isPointInFront(final Coordinate2D<Double> pointInFront,  final Coordinate2D<Double> point, final Coordinate2D<Double> velocity) {
        if (velocity.getX() < 0 && pointInFront.getX() > point.getX()) {
            return false;
        }
        if (velocity.getX() > 0 && pointInFront.getX() < point.getX()) {
            return false;
        }
        if (velocity.getY() < 0 && pointInFront.getY() > point.getY()) {
            return false;
        }
        if (velocity.getY() > 0 && pointInFront.getY() < point.getY()) {
            return false;
        }
        return true;
    }
}
