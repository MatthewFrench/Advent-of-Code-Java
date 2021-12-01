package Advent.of.Code.Java.Year_2021;

import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.NumberUtilities;
import Advent.of.Code.Java.Utility.Structures.Day;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

public class Day_1 implements Day {
    public void run() throws Exception {
        // Solution 1
        {
            long last = -1;
            int increased = 0;
            final List<Long> input = LoadUtilities.loadTextFileAsTypeList("input/2021/1/input.txt", Long::parseLong);
            for (final Long depth : input) {
                if (last != -1 && depth > last) {
                    increased += 1;
                }
                last = depth;
            }

            LogUtilities.logGreen("Solution: " + increased);
        }
        // Solution 2
        {
            final List<Long> pastElements = new ArrayList<>();
            int increased = 0;
            final List<Long> input = LoadUtilities.loadTextFileAsTypeList("input/2021/1/input.txt", Long::parseLong);
            for (final Long depth : input) {
                if (pastElements.size() >= 3) {
                    final List<Long> newDepths = new ArrayList<>(pastElements);
                    newDepths.add(depth);
                    newDepths.remove(0);
                    if (NumberUtilities.sum(newDepths) > NumberUtilities.sum(pastElements)) {
                        increased += 1;
                    }
                }
                pastElements.add(depth);
                if (pastElements.size() > 3) {
                    pastElements.remove(0);
                }
            }

            LogUtilities.logGreen("Solution: " + increased);
        }
    }
}
