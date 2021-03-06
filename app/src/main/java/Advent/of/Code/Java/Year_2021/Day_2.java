package Advent.of.Code.Java.Year_2021;

import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.Day;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;

import java.util.List;

public class Day_2 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2021/2/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        {
            final List<String> input = LoadUtilities.loadTextFileAsList(fileName);
            long x = 0;
            long y = 0;
            for (final String instruction : input) {
                List<String> fields = StringUtilities.splitStringIntoList(instruction, " ");
                final String direction = fields.get(0);
                final long amount = Long.parseLong(fields.get(1));
                if (direction.equals("forward")) {
                    x += amount;
                } else if (direction.equals("up")) {
                    y -= amount;
                } else if (direction.equals("down")) {
                    y += amount;
                } else {
                    LogUtilities.log(instruction);
                }
            }

            LogUtilities.logGreen("Solution: " + (x * y));
        }

        {
            final List<String> input = LoadUtilities.loadTextFileAsList(fileName);
            long x = 0;
            long y = 0;
            long aim = 0;
            for (final String instruction : input) {
                List<String> fields = StringUtilities.splitStringIntoList(instruction, " ");
                final String direction = fields.get(0);
                final long amount = Long.parseLong(fields.get(1));
                if (direction.equals("forward")) {
                    x += amount;
                    y += aim * amount;
                } else if (direction.equals("up")) {
                    aim -= amount;
                } else if (direction.equals("down")) {
                    aim += amount;
                } else {
                    LogUtilities.log(instruction);
                }
            }

            LogUtilities.logGreen("Solution: " + (x * y));
        }
    }
}
