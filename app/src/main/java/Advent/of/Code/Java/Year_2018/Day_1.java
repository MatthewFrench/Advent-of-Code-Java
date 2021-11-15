package Advent.of.Code.Java.Year_2018;

import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.Structures.Day;

import java.util.HashSet;
import java.util.Set;

public class Day_1 implements Day {
    public void run() throws Exception {
        final String originalInput = LoadUtilities.loadTextFileAsString("input/2018/1/input.txt");

        Set<Integer> frequencySet = new HashSet<>();

        int amount = 0;
        boolean found = false;

        String[] splitInput = originalInput.split("\n");
        while (!found) {
            for (int index = 0; index < splitInput.length; index++) {
                String piece = splitInput[index];
                char sign = piece.charAt(0);
                int number = Integer.parseInt(piece.substring(1));
                if (sign == '-') {
                    amount -= number;
                } else if (sign == '+') {
                    amount += number;
                } else {
                    LogUtilities.log("Uh oh " + piece);
                }

                if (frequencySet.contains(amount)) {
                    found = true;
                    break;
                }
                frequencySet.add(amount);
            }
        }

        LogUtilities.log("Set length: " + frequencySet.size());
        LogUtilities.log("Frequency: " + amount);
    }
}