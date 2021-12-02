package Advent.of.Code.Java.Utility;

import Advent.of.Code.Java.Utility.Structures.DayWithExecute;

public class DayUtilities {
    public static void run(final String prefix, final DayWithExecute day) throws Exception {
        if (!LoadUtilities.isFileEmpty(prefix + "sample.txt")) {
            LogUtilities.logBlue("Sample");
            LogUtilities.indent();
            day.executeWithInput(prefix + "sample.txt");
            LogUtilities.unIndent();
        }
        LogUtilities.logPurple("Input");
        LogUtilities.indent();
        day.executeWithInput(prefix + "input.txt");
        LogUtilities.unIndent();
    }
}
