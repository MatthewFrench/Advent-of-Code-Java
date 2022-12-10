package Advent.of.Code.Java.Year_2022;

import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;

import java.util.ArrayList;
import java.util.List;

public class Day_10 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2022/10/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        final List<String> inputRows = StringUtilities.splitStringIntoList(LoadUtilities.loadTextFileAsString(fileName), "\n");
        final List<String> pixels = new ArrayList<>();

        final int width = 40;
        int cycleCount = 0;
        long valueX = 1;
        long totalCycleSum = 0;
        for (final String input : inputRows) {
            final List<String> commandSplit = StringUtilities.splitStringIntoList(input, " ");
            final String command = commandSplit.get(0);
            long runCycles;
            long addToValue = 0;
            if (command.equals("addx")) {
                addToValue = Long.parseLong(commandSplit.get(1));
                runCycles = 2;
            } else {
                runCycles = 1;
            }
            for (int i = 0; i < runCycles; i++) {
                int cyclePosition = cycleCount - ((cycleCount/width) * width);
                if (cyclePosition <= valueX + 1 && cyclePosition >= valueX - 1) {
                    pixels.add("#");
                } else {
                    pixels.add(" ");
                }
                cycleCount += 1;
                if (cycleCount == 20 || cycleCount == 60 || cycleCount == 100 || cycleCount == 140 || cycleCount == 180 || cycleCount == 220) {
                    totalCycleSum += valueX * cycleCount;
                }
            }
            valueX += addToValue;
        }

        LogUtilities.logGreen("Solution: " + totalCycleSum);

        StringBuilder output = new StringBuilder();
        int count = 0;
        for (final String pixel : pixels) {
            output.append(pixel);
            count += 1;
            if (count >= width) {
                count -= width;
                output.append("\n");
            }
        }
        LogUtilities.logGreen("Solution 2: \n" + output);
    }
}
