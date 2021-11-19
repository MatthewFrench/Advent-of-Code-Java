package Advent.of.Code.Java.Year_2018;

import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.Structures.Day;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Day_2 implements Day {
    public void run() throws Exception {
        final String originalInput = LoadUtilities.loadTextFileAsString("input/2018/2/input.txt");

        Map<Integer, Integer> numberMap = new HashMap<>();

        String[] splitInput = originalInput.split("\n");

        for (String piece : splitInput) {
            Map<Character, Integer> letterMap = new HashMap<Character, Integer>();
            for (char c: piece.toCharArray()) {
                if (!letterMap.containsKey(c)) {
                    letterMap.put(c, 1);
                } else {
                    letterMap.put(c, letterMap.get(c) + 1);
                }
            }

            Set<Integer> pieceNumberSet = new HashSet<>();
            for (Map.Entry<Character, Integer> e : letterMap.entrySet()) {
                if (e.getValue() != 1  && e.getValue() < 4) {
                    if (!pieceNumberSet.contains(e.getValue())) {
                        pieceNumberSet.add(e.getValue());
                    }
                }
            }

            for (Integer number : pieceNumberSet) {
                if (!numberMap.containsKey(number)) {
                    numberMap.put(number, 1);
                } else {
                    numberMap.put(number, numberMap.get(number) + 1);
                }
            }
        }

        int amount = 1;
        for (Map.Entry<Integer, Integer> e : numberMap.entrySet()) {
            amount = amount * e.getValue();
        }

        LogUtilities.log("Checksum: " + amount);

        //Part 2
        for (int index1 = 0; index1 < splitInput.length; index1++) {
            for (int index2 = index1 + 1; index2 < splitInput.length; index2++) {
                int diffCount = 0;
                char[] piece1Array = splitInput[index1].toCharArray();
                char[] piece2Array = splitInput[index2].toCharArray();
                String sameLetters = splitInput[index1] + "";
                for (int index3 = 0; index3 < piece1Array.length; index3++) {
                    if (piece1Array[index3] != piece2Array[index3]) {
                        diffCount++;
                    }
                }
                if (diffCount <= 1) {
                    LogUtilities.log("Found " + splitInput[index1] + ", " + splitInput[index2] + " with diff " + diffCount);

                    String endResult = "";
                    for (int index3 = 0; index3 < piece1Array.length; index3++) {
                        if (piece1Array[index3] == piece2Array[index3]) {
                            endResult += piece1Array[index3];
                        }
                    }
                    LogUtilities.log(endResult);
                }
            }
        }
    }
}