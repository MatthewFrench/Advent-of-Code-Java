package Advent.of.Code.Java.Year_2022;

import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Day_6 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2022/6/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        {
            final List<String> input = StringUtilities.splitStringIntoList(LoadUtilities.loadTextFileAsString(fileName), "");

            LogUtilities.logGreen("Solution: " + getCountUntilUniqueCharacterSequence(input, 4));
        }
        {
            final List<String> input = StringUtilities.splitStringIntoList(LoadUtilities.loadTextFileAsString(fileName), "");

            LogUtilities.logGreen("Solution 2: " + getCountUntilUniqueCharacterSequence(input, 14));
        }
    }

    public long getCountUntilUniqueCharacterSequence(List<String> input, final long sequenceLength) {
        // Is using a list performance enough? Would another data structure be better? Static array? Linked list?
        final List<String> rowCharacters = new ArrayList<>();
        long count = 0;
        for (final String character : input) {
            rowCharacters.add(character);
            if (rowCharacters.size() > sequenceLength) {
                rowCharacters.remove(0);
            }
            count += 1;
            // This feels dirty, would this be more performant? .stream().distinct().count();
            if (new HashSet<>(rowCharacters).size() >= sequenceLength) {
                break;
            }
        }
        return count;
    }
}
