package Advent.of.Code.Java.Year_2023;

import Advent.of.Code.Java.Utility.DataUtilities;
import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.NumberUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;
import Advent.of.Code.Java.Utility.Structures.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Day_2 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2023/2/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        runSolution1(fileName);
        runSolution2(fileName);
    }

    private void runSolution1(final String fileName) throws Exception {
        final long maxRed = 12;
        final long maxGreen = 13;
        final long maxBlue = 14;

        final List<Long> validGameIDs = new ArrayList<>();

        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);
        allGames:
        for (final String allGames : input) {
            final long gameID = Long.parseLong(StringUtilities.splitStringIntoList(StringUtilities.splitStringIntoList(allGames, ": ").get(0), " ").get(1));
            final List<String> gameSets = StringUtilities.splitStringIntoList(StringUtilities.splitStringIntoList(allGames, ": ").get(1), "; ");
            for (final String gameSet : gameSets) {
                final List<String> diceSets = StringUtilities.splitStringIntoList(gameSet, ", ");
                for (final String diceSet : diceSets) {
                    final List<String> diceInfo = StringUtilities.splitStringIntoList(diceSet, " ");
                    final long diceNumber = Long.parseLong(diceInfo.get(0));
                    final String diceType = diceInfo.get(1);
                    if (diceType.equals("blue") && diceNumber > maxBlue) {
                        continue allGames;
                    }
                    if (diceType.equals("red") && diceNumber > maxRed) {
                        continue allGames;
                    }
                    if (diceType.equals("green") && diceNumber > maxGreen) {
                        continue allGames;
                    }
                }
            }

            validGameIDs.add(gameID);
        }

        LogUtilities.logGreen("Solution 1: " + NumberUtilities.sum(validGameIDs));
    }
    private void runSolution2(final String fileName) throws Exception {
        final List<Long> gamePowers = new ArrayList<>();

        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);
        for (final String allGames : input) {
            final List<String> gameSets = StringUtilities.splitStringIntoList(StringUtilities.splitStringIntoList(allGames, ": ").get(1), "; ");
            long minimumBlue = 0;
            long minimumRed = 0;
            long minimumGreen = 0;
            for (final String gameSet : gameSets) {
                final List<String> diceSets = StringUtilities.splitStringIntoList(gameSet, ", ");
                for (final String diceSet : diceSets) {
                    final List<String> diceInfo = StringUtilities.splitStringIntoList(diceSet, " ");
                    final long diceNumber = Long.parseLong(diceInfo.get(0));
                    final String diceType = diceInfo.get(1);
                    if (diceType.equals("blue")) {
                        minimumBlue = Math.max(minimumBlue, diceNumber);
                    }
                    if (diceType.equals("red")) {
                        minimumRed = Math.max(minimumRed, diceNumber);
                    }
                    if (diceType.equals("green")) {
                        minimumGreen = Math.max(minimumGreen, diceNumber);
                    }
                }
            }

            gamePowers.add(minimumBlue * minimumRed * minimumGreen);
        }

        LogUtilities.logGreen("Solution 2: " + NumberUtilities.sum(gamePowers));
    }
}
