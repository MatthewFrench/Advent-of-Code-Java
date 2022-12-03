package Advent.of.Code.Java.Year_2022;

import Advent.of.Code.Java.Utility.DataUtilities;
import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day_3 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2022/3/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        {
            final List<List<List<String>>> input = LoadUtilities.loadTextFileAsTypeList(fileName, (line) ->
            {
                List<String> firstHalf = StringUtilities.splitStringIntoList(line.substring(0, (line.length()/2)), "");
                List<String> secondHalf = StringUtilities.splitStringIntoList(line.substring(line.length()/2), "");
                return List.of(firstHalf, secondHalf);
            });

            long score = 0;
            for (final List<List<String>> rucksack : input) {
                final List<String> component1 = rucksack.get(0);
                final List<String> component2 = rucksack.get(1);
                final Set<String> commonElements = DataUtilities.getCommonElements(component1, component2);
                for (final String commonElement : commonElements) {
                    score += getValueOfType(commonElement.charAt(0));
                }
            }

            LogUtilities.logGreen("Solution: " + score);
        }

        {
            final List<List<String>> input = LoadUtilities.loadTextFileAsTypeList(fileName, (line) ->
                    StringUtilities.splitStringIntoList(line, ""));

            long score = 0;
            for (int index = 0; index < input.size(); index += 3) {
                final List<String> rucksack1 = input.get(index);
                final List<String> rucksack2 = input.get(index+1);
                final List<String> rucksack3 = input.get(index+2);
                final Set<String> commonElements = DataUtilities.getCommonElements(rucksack1, rucksack2, rucksack3);
                for (final String commonElement : commonElements) {
                    score += getValueOfType(commonElement.charAt(0));
                }
            }

            LogUtilities.logGreen("Solution 2: " + score);
        }
    }

    /*
    Lowercase item types a through z have priorities 1 through 26.
    Uppercase item types A through Z have priorities 27 through 52.
     */
    private long getValueOfType(final char type) {
        final char lowercaseA = 'a';
        final char uppercaseA = 'A';
        if (type >= lowercaseA) {
            return type - lowercaseA + 1;
        } else {
            return type - uppercaseA + 27;
        }
    }
}
