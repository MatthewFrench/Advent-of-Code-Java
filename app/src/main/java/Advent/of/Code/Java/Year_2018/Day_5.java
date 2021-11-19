package Advent.of.Code.Java.Year_2018;

import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.Structures.Day;

public class Day_5 implements Day {
    public void run() throws Exception {
        final String originalInput = LoadUtilities.loadTextFileAsString("input/2018/5/input.txt");

        int shortestLength = -1;
        String[] alphabet = "abcdefghijklmnopqrstuvwxyz".split("");

        for (int removeLetter = 0; removeLetter < alphabet.length; removeLetter++) {
            String input = originalInput + "";
            String letterToRemove = alphabet[removeLetter];
            input = String.join("", input.split(letterToRemove));
            input = String.join("", input.split(letterToRemove.toUpperCase()));

            int length = 0;
            while (input.length() != length) {
                length = input.length();
                for (int i = 0; i < alphabet.length; i++) {
                    String lower = alphabet[i];
                    String upper = lower.toUpperCase();
                    input = input.replace(lower+upper, "");
                    input = input.replace(upper+lower, "");
                }
            }
            if (length < shortestLength || shortestLength < 0) {
                shortestLength = length;
            }
        }

        LogUtilities.log("Shortest length: " + shortestLength);
    }
}