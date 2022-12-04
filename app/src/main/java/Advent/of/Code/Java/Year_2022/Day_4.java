package Advent.of.Code.Java.Year_2022;

import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;

import java.util.List;

public class Day_4 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2022/4/", this);
    }

    class AssignedRange {
        public long min;
        public long max;
        public AssignedRange(final String input) {
            final List<String> numberStrings = StringUtilities.splitStringIntoList(input, "-");
            min = Long.parseLong(numberStrings.get(0));
            max = Long.parseLong(numberStrings.get(1));
        }
    }

    public void executeWithInput(final String fileName) throws Exception {
        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);
        {
            long numberOfContainedPairs = 0;
            for (final String line : input) {
                final List<String> commaSplit = StringUtilities.splitStringIntoList(line, ",");
                final AssignedRange range1 = new AssignedRange(commaSplit.get(0));
                final AssignedRange range2 = new AssignedRange(commaSplit.get(1));
                if (rangeWithinRange(range1, range2)) {
                    numberOfContainedPairs += 1;
                }
            }

            LogUtilities.logGreen("Solution: " + numberOfContainedPairs);
        }
        {
            long numberOfOverlapPairs = 0;
            for (final String line : input) {
                final List<String> commaSplit = StringUtilities.splitStringIntoList(line, ",");
                final AssignedRange range1 = new AssignedRange(commaSplit.get(0));
                final AssignedRange range2 = new AssignedRange(commaSplit.get(1));
                if (rangeOverlapRange(range1, range2)) {
                    numberOfOverlapPairs += 1;
                }
            }

            LogUtilities.logGreen("Solution 2: " + numberOfOverlapPairs);
        }
    }

    public boolean rangeWithinRange(final AssignedRange range1, final AssignedRange range2) {
        if (range1.min >= range2.min && range1.max <= range2.max) {
            return true;
        }
        if (range2.min >= range1.min && range2.max <= range1.max) {
            return true;
        }
        return false;
    }
    public boolean rangeOverlapRange(final AssignedRange range1, final AssignedRange range2) {
        if (range1.min <= range2.max && range1.max >= range2.min) {
            return true;
        }
        if (range2.min <= range1.max && range2.max >= range1.min) {
            return true;
        }
        return false;
    }
}
