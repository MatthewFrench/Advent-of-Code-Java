package Advent.of.Code.Java.Year_2020.Day_1;

import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.Structures.Day;

import java.util.List;

public class Day_1 implements Day {
    public void run() throws Exception {
        final int targetValue = 2020;
        final List<Integer> input = LoadUtilities.loadTextFileAsTypeList("input/2020/1/input.txt", Integer::parseInt);
        for (var index1 = 0; index1 < input.size(); index1++) {
            var number1 = input.get(index1);
            for (var index2 = index1 + 1; index2 < input.size(); index2++) {
                var number2 = input.get(index2);
                if (number1 + number2 == targetValue) {
                    LogUtilities.log("Solution 1");
                    LogUtilities.log("Number 1: " + number1);
                    LogUtilities.log("Number 2: " + number2);
                    LogUtilities.log("Multiplied: " + (number1 * number2));
                }
                for (var index3 = index2 + 1; index3 < input.size(); index3++) {
                    var number3 = input.get(index3);
                    if (number1 + number2 + number3 == targetValue) {
                        LogUtilities.log("Solution 2");
                        LogUtilities.log("Number 1: " + number1);
                        LogUtilities.log("Number 2: " + number2);
                        LogUtilities.log("Number 3: " + number3);
                        LogUtilities.log("Multiplied: " + (number1 * number2 * number3));
                    }
                }
            }
        }
    }
}