package Advent.of.Code.Java.Utility;

import Advent.of.Code.Java.Utility.Structures.DayWithExecute;

public class DayUtilities {
    public static void run(final String prefix, final DayWithExecute day) throws Exception {
        if (!LoadUtilities.isFileEmpty(prefix + "sample.txt")) {
            LogUtilities.logBlue("Sample");
            LogUtilities.indent();
            LogUtilities.startTiming("Sample");
            day.executeWithInput(prefix + "sample.txt");
            LogUtilities.endTiming("Sample");
            LogUtilities.unIndent();
        }
        LogUtilities.logPurple("Input");
        LogUtilities.indent();
        LogUtilities.startTiming("Input");
        day.executeWithInput(prefix + "input.txt");
        LogUtilities.endTiming("Input");
        LogUtilities.unIndent();
    }
}
