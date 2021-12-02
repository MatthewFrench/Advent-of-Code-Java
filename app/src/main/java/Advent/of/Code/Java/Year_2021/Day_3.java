package Advent.of.Code.Java.Year_2021;

import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;

import java.util.List;

public class Day_3 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2021/3/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);

        LogUtilities.logGreen("Solution: " + input);
    }
}
