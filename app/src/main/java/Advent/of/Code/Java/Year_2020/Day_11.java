package Advent.of.Code.Java.Year_2020;

import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.Structures.Day;

import java.util.List;

public class Day_11 implements Day {
    static final int SEAT_EMPTY = 1;
    static final int FLOOR = 0;
    static final int SEAT_TAKEN = 2;

    public void run() throws Exception {
        LogUtilities.log(Day_11.class.getName());
        final List<String> input = LoadUtilities.loadTextFileAsList("input/2020/11/input.txt");
        int width = input.get(0).length();
        int height = input.size();
        int[][] originalSeating = new int[width][height];

        for (var y = 0; y < input.size(); y++) {
            var line = input.get(y);
            var x = 0;
            for (char c : line.toCharArray()) {
                int number = FLOOR;
                if (c == 'L') {
                    number = SEAT_EMPTY;
                } else if (c == '#') {
                    number = SEAT_TAKEN;
                }
                originalSeating[x][y] = number;
                x += 1;
            }
        }
        {
            var seating = deepCopyIntMatrix(originalSeating);
            var isChanging = true;
            var rounds = 0;
            while (isChanging) {
                // Uncomment to view rounds
                // LogUtilities.log("Round: " + rounds);
                // printSeats(seating, width, height);
                var result = stepSeatingPart1(seating, width, height);
                seating = result.seating;
                isChanging = result.changed;
                rounds += 1;
            }
            LogUtilities.log("Rounds: " + rounds);
            LogUtilities.log("Occupied seats: " + countOccupiedSeats(seating, width, height));

        }
        LogUtilities.log("Part 2");
        {
            var seating = deepCopyIntMatrix(originalSeating);
            var isChanging = true;
            var rounds = 0;
            while (isChanging) {
                // Uncomment to view rounds
                // LogUtilities.log("Round: " + rounds);
                // printSeats(seating, width, height);
                var result = stepSeatingPart2(seating, width, height);
                seating = result.seating;
                isChanging = result.changed;
                rounds += 1;
            }
            LogUtilities.log("Rounds: " + rounds);
            LogUtilities.log("Occupied seats: " + countOccupiedSeats(seating, width, height));
        }
    }

    public static void printSeats(int[][] seating, int width, int height) {
        for (var y = 0; y < height; y++) {
            var line = "";
            for (var x = 0; x < width; x++) {
                var target = seating[x][y];
                if (target == SEAT_EMPTY) {
                    line += "L";
                } else if (target == SEAT_TAKEN) {
                    line += "#";
                } else if (target == FLOOR) {
                    line += ".";
                }
            }
            LogUtilities.log(line);
        }
    }
    public static int[][] deepCopyIntMatrix(int[][] input) {
        if (input == null)
            return null;
        int[][] result = new int[input.length][];
        for (int r = 0; r < input.length; r++) {
            result[r] = input[r].clone();
        }
        return result;
    }
    public static int countOccupiedSeats(int[][] seating, int width, int height) {
        var occupiedSeats = 0;
        for (var x = 0; x < width; x++) {
            for (var y = 0; y < height; y++) {
                if (seating[x][y] == SEAT_TAKEN) {
                    occupiedSeats += 1;
                }
            }
        }
        return occupiedSeats;
    }
    public static Result stepSeatingPart2(int[][] seating, int width, int height) {
        var seatingChanged = false;
        var newSeating = deepCopyIntMatrix(seating);
        for (var x = 0; x < width; x++) {
            for (var y = 0; y < height; y++) {
                var occupiedAdjacentSeats = numberOfOccupiedAdjacentSeatsPart2(seating, width, height, x, y);
                var target = seating[x][y];
                var newSeat = target;
                if (target == SEAT_EMPTY && occupiedAdjacentSeats == 0) {
                    newSeat = SEAT_TAKEN;
                } else if (target == SEAT_TAKEN && occupiedAdjacentSeats >= 5) {
                    newSeat = SEAT_EMPTY;
                } else {
                    // No change
                }
                if (target != newSeat) {
                    seatingChanged = true;
                }
                newSeating[x][y] = newSeat;
            }
        }
        var result = new Result();
        result.seating = newSeating;
        result.changed = seatingChanged;
        return result;
    }

    public static Result stepSeatingPart1(int[][] seating, int width, int height) {
        var seatingChanged = false;
        var newSeating = deepCopyIntMatrix(seating);
        for (var x = 0; x < width; x++) {
            for (var y = 0; y < height; y++) {
                var occupiedAdjacentSeats = numberOfOccupiedAdjacentSeatsPart1(seating, width, height, x, y);
                var target = seating[x][y];
                var newSeat = target;
                if (target == SEAT_EMPTY && occupiedAdjacentSeats == 0) {
                    newSeat = SEAT_TAKEN;
                } else if (target == SEAT_TAKEN && occupiedAdjacentSeats >= 4) {
                    newSeat = SEAT_EMPTY;
                } else {
                    // No change
                }
                if (target != newSeat) {
                    seatingChanged = true;
                }
                newSeating[x][y] = newSeat;
            }
        }
        var result = new Result();
        result.seating = newSeating;
        result.changed = seatingChanged;
        return result;
    }
    public static boolean hasOccupiedSeatInDirection(int[][] seating, int width, int height, int x, int y, int dirX, int dirY) {
        var positionX = x + dirX;
        var positionY = y + dirY;
        while (positionY >= 0 && positionY < height && positionX >= 0 && positionX < width) {
            if (seating[positionX][positionY] == SEAT_TAKEN) {
                return true;
            }
            if (seating[positionX][positionY] == SEAT_EMPTY) {
                return false;
            }
            positionX += dirX;
            positionY += dirY;
        }
        return false;
    }
    public static int numberOfOccupiedAdjacentSeatsPart2(int[][] seating, int width, int height, int x, int y) {
        var occupiedSeats = 0;
        if (hasOccupiedSeatInDirection(seating, width, height, x, y, 0, 1)) {
            occupiedSeats += 1;
        }
        if (hasOccupiedSeatInDirection(seating, width, height, x, y, 0, -1)) {
            occupiedSeats += 1;
        }
        if (hasOccupiedSeatInDirection(seating, width, height, x, y, 1, 0)) {
            occupiedSeats += 1;
        }
        if (hasOccupiedSeatInDirection(seating, width, height, x, y, -1, 0)) {
            occupiedSeats += 1;
        }
        if (hasOccupiedSeatInDirection(seating, width, height, x, y, 1, 1)) {
            occupiedSeats += 1;
        }
        if (hasOccupiedSeatInDirection(seating, width, height, x, y, 1, -1)) {
            occupiedSeats += 1;
        }
        if (hasOccupiedSeatInDirection(seating, width, height, x, y, -1, 1)) {
            occupiedSeats += 1;
        }
        if (hasOccupiedSeatInDirection(seating, width, height, x, y, -1, -1)) {
            occupiedSeats += 1;
        }
        return occupiedSeats;
    }
    public static class Result {
        public int[][] seating;
        public boolean changed;
    }
    public static int numberOfOccupiedAdjacentSeatsPart1(int[][] seating, int width, int height, int x, int y) {
        var occupiedSeats = 0;
        // Left side
        if (x > 0) {
            if (seating[x-1][y] == SEAT_TAKEN) {
                occupiedSeats += 1;
            }
            //Left top
            if (y > 0) {
                if (seating[x-1][y-1] == SEAT_TAKEN) {
                    occupiedSeats += 1;
                }
            }
            //Left bottom
            if (y < height - 1) {
                if (seating[x-1][y+1] == SEAT_TAKEN) {
                    occupiedSeats += 1;
                }
            }
        }
        // Right side
        if (x < width - 1) {
            if (seating[x+1][y] == SEAT_TAKEN) {
                occupiedSeats += 1;
            }
            //Left top
            if (y > 0) {
                if (seating[x+1][y-1] == SEAT_TAKEN) {
                    occupiedSeats += 1;
                }
            }
            //Left bottom
            if (y < height - 1) {
                if (seating[x+1][y+1] == SEAT_TAKEN) {
                    occupiedSeats += 1;
                }
            }
        }
        // Top and Bottom
        if (y > 0) {
            if (seating[x][y-1] == SEAT_TAKEN) {
                occupiedSeats += 1;
            }
        }
        if (y < height - 1) {
            if (seating[x][y+1] == SEAT_TAKEN) {
                occupiedSeats += 1;
            }
        }
        return occupiedSeats;
    }
}