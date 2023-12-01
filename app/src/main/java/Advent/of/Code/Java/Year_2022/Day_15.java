package Advent.of.Code.Java.Year_2022;

import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day_15 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2022/15/", this);
    }
    public void executeWithInput(final String fileName) throws Exception {
        final List<String> lines = StringUtilities.splitStringIntoList(LoadUtilities.loadTextFileAsString(fileName), "\n");
        final List<String> values = StringUtilities.splitStringIntoList(lines.get(1), " ");
        final Set<Integer> childIDs = new HashSet<>();
        for (final String childIDString : values) {
            final int childID = Integer.parseInt(childIDString);
            if (childIDs.contains(childID)) {
                childIDs.remove(childID);
            } else {
                childIDs.add(childID);
            }
        }

        LogUtilities.logGreen("Solution: " + childIDs.stream().map(String::valueOf).collect(Collectors.joining(",")));
    }
}
