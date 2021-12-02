package Advent.of.Code.Java.Year_2021;

import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.NumberUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;

import java.util.ArrayList;
import java.util.List;

public class Day_1 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2021/1/", this);
    }
    public void executeWithInput(final String fileName) throws Exception {
        // Solution 1
        {
            long last = -1;
            int increased = 0;
            final List<Long> input = LoadUtilities.loadTextFileAsTypeList(fileName, Long::parseLong);
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
            final List<Long> input = LoadUtilities.loadTextFileAsTypeList(fileName, Long::parseLong);
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
