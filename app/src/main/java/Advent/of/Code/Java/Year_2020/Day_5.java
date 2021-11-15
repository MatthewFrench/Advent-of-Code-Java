package Advent.of.Code.Java.Year_2020;

import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.Structures.Day;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day_5 implements Day {
    public void run() throws Exception {
        LogUtilities.log(Day_5.class.getName());
        final List<String> input = LoadUtilities.loadTextFileAsList("input/2020/5/input.txt");
        var largestSeatId = 0;
        List<Integer> seatIds = new ArrayList<>();
        for (var pass : input) {
            var rowLower = 0;
            var rowUpper = 127;
            var columnLower = 0;
            var columnUpper = 7;
            var row = 0;
            var column = 0;
            var rowCount = 0;
            var columnCount = 0;
            for (var c : pass.toCharArray()) {
                if (c == 'F') {
                    rowCount++;
                    rowUpper = halveRangeLower(rowUpper, rowLower);
                    if (rowCount == 7) {
                        row = rowLower;
                    }
                } else if (c == 'B') {
                    rowCount++;
                    rowLower = halveRangeUpper(rowUpper, rowLower);
                    if (rowCount == 7) {
                        row = rowUpper;
                    }
                } else if (c == 'L') {
                    columnCount++;
                    columnUpper = halveRangeLower(columnUpper, columnLower);
                    if (columnCount == 3) {
                        column = columnLower;
                    }
                } else if (c == 'R') {
                    columnCount++;
                    columnLower = halveRangeUpper(columnUpper, columnLower);
                    if (columnCount == 3) {
                        column = columnUpper;
                    }
                }
            }
            var seatId = row * 8 + column;
            seatIds.add(seatId);
            largestSeatId = Math.max(largestSeatId, seatId);
        }
        LogUtilities.log("Largest seat id: " + largestSeatId);

        Collections.sort(seatIds);
        var lastId = seatIds.get(0);
        for (var id : seatIds) {
            if (id - lastId > 1) {
                LogUtilities.log("Middle seat id: " + halveRangeLower(id, lastId));
            }
            lastId = id;
        }
    }
    static int halveRangeLower(int upper, int lower) {
        return (int) Math.floor((upper - lower) / 2.0 + lower);
    }
    static int halveRangeUpper(int upper, int lower) {
        return (int) Math.ceil((upper - lower) / 2.0 + lower);
    }
}