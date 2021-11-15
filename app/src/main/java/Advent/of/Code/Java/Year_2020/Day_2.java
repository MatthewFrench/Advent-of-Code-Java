package Advent.of.Code.Java.Year_2020;

import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.Day;

import java.util.List;

public class Day_2 implements Day {
    public void run() throws Exception {
        LogUtilities.log("Day 2");
        final List<String> input = LoadUtilities.loadTextFileAsList("input/2020/2/input.txt");
        int validPart1 = 0;
        int validPart2 = 0;
        for (String value : input) {
            var sections = StringUtilities.splitStringIntoList(value, " ");
            var rangeNumbers = StringUtilities.splitStringIntoList(sections.get(0), "-");
            var startingNumber = Integer.parseInt(rangeNumbers.get(0));
            var endingNumber = Integer.parseInt(rangeNumbers.get(1));
            var letter = StringUtilities.getStringChunk(sections.get(1), 0, 1);
            var password = sections.get(2);
            int count = StringUtilities.countStringInstanceInString(password, letter);
            if (count >= startingNumber && count <= endingNumber) {
                validPart1 += 1;
            }

            // Part 2
            var firstLocation = false;
            var secondLocation = false;
            if (StringUtilities.chunkExistsAtLocation(password, letter, startingNumber - 1)) {
                firstLocation = true;
            }
            if (StringUtilities.chunkExistsAtLocation(password, letter, endingNumber - 1)) {
                secondLocation = true;
            }
            if (firstLocation != secondLocation) {
                validPart2 += 1;
            }
        }
        LogUtilities.log("Part 1");
        LogUtilities.log("Valid passwords: " + validPart1);
        LogUtilities.log("Total passwords: " + input.size());
        LogUtilities.log("Part 2");
        LogUtilities.log("Valid passwords: " + validPart2);
        LogUtilities.log("Total passwords: " + input.size());
    }
}