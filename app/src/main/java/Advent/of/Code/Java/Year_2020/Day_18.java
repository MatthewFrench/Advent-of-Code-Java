package Advent.of.Code.Java.Year_2020;

import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.Day;

import java.util.List;

public class Day_18 implements Day {
    public void run() throws Exception {
        {
            LogUtilities.log(Day_18.class.getName());
            final List<String> input = LoadUtilities.loadTextFileAsList("input/2020/18/input.txt");

            long totalSum = 0;
            for (var line : input) {
                totalSum += evaluateInstruction(line.replaceAll("\\s+", ""));
            }
            LogUtilities.log("Part 1 total sum: " + totalSum);
        }
        {
            final List<String> input = LoadUtilities.loadTextFileAsList("input/2020/18/input.txt");

            long totalSum = 0;
            for (var line : input) {
                totalSum += evaluateInstruction2(line.replaceAll("\\s+",""));
            }
            LogUtilities.log("Part 2 total sum: " + totalSum);
        }
    }
    public static long evaluateInstruction(String line) {
        if (StringUtilities.countStringInstanceInString(line, "(") > 0) {
            // First split instructions by top level parenthesis
            var newEquation = "";
            var currentLine = "";
            var parenthesis = 0;
            for (var i = 0; i < line.length(); i++) {
                var character = StringUtilities.getStringChunk(line, i, 1);
                if (parenthesis == 0 && character.equals("(")) {
                    parenthesis += 1;
                    if (currentLine.length() > 0) {
                        newEquation += currentLine;
                    }
                    currentLine = "";
                } else if (character.equals("(")) {
                    parenthesis += 1;
                    currentLine += character;
                } else if (character.equals(")") && parenthesis == 1) {
                    parenthesis = 0;
                    newEquation += evaluateInstruction(currentLine);
                    currentLine = "";
                } else if (character.equals(")")) {
                    parenthesis -= 1;
                    currentLine += character;
                } else {
                    currentLine += character;
                }
            }
            if (currentLine.length() > 0) {
                newEquation += currentLine;
            }
            line = newEquation;
        }
        var firstValueString = getNextValue(line, 0);
        var currentValue = Long.parseLong(firstValueString);
        var index = firstValueString.length();
        while (index < line.length()) {
            var character = StringUtilities.getStringChunk(line, index, 1);
            var value2 = getNextValue(line, index + 1);
            if (character.equals("+")) {
                currentValue = currentValue + Long.parseLong(value2);
            } else if (character.equals("*")) {
                currentValue = currentValue * Long.parseLong(value2);
            } else {
                LogUtilities.log("Uh oh mistake in: " + line);
            }
            index += character.length() + value2.length();
        }
        return currentValue;
    }
    static String getNextValue(String line, int index) {
        String value = "";
        for (var i = index; i < line.length(); i++) {
            var character = StringUtilities.getStringChunk(line, i, 1);
            if (!character.equals("+") && !character.equals("*")) {
                value += character;
            } else {
                break;
            }
        }
        return value;
    }

    // Here and below experimenting with a different way
    interface EquationInterface {
        long solve();
    }
    static class EquationValue implements EquationInterface {
        long value;
        public EquationValue(String stringValue) {
            value = Long.parseLong(stringValue);
        }
        public long solve() {
            return value;
        }
    }
    static class Equation implements EquationInterface {
        Equation leftSide;
        String operator;
        Equation rightSide;
        public Equation(String line) {
            // Get left side, can be number or parenthesis
            // Unfinished
        }
        public long solve() {
            long left = leftSide.solve();
            long right = rightSide.solve();
            if (operator.equals("+")) {
                return left + right;
            } else if (operator.equals("*")) {
                return left * right;
            } else {
                LogUtilities.log("Unknown operator");
            }
            return 0;
        }
    }
    public static long evaluateInstruction2(String line) {
        if (StringUtilities.countStringInstanceInString(line, "(") > 0) {
            // First split instructions by top level parenthesis
            var newEquation = "";
            var currentLine = "";
            var parenthesis = 0;
            for (var i = 0; i < line.length(); i++) {
                var character = StringUtilities.getStringChunk(line, i, 1);
                if (parenthesis == 0 && character.equals("(")) {
                    parenthesis += 1;
                    if (currentLine.length() > 0) {
                        newEquation += currentLine;
                    }
                    currentLine = "";
                } else if (character.equals("(")) {
                    parenthesis += 1;
                    currentLine += character;
                } else if (character.equals(")") && parenthesis == 1) {
                    parenthesis = 0;
                    newEquation += evaluateInstruction2(currentLine);
                    currentLine = "";
                } else if (character.equals(")")) {
                    parenthesis -= 1;
                    currentLine += character;
                } else {
                    currentLine += character;
                }
            }
            if (currentLine.length() > 0) {
                newEquation += currentLine;
            }
            line = newEquation;
        }
        // Process any addition
        if (StringUtilities.countStringInstanceInString(line, "+") > 0) {
            var firstValueString = getNextValue2(line, 0);
            var index = firstValueString.length();
            var newEquation = "";
            while (index < line.length()) {
                var character = StringUtilities.getStringChunk(line, index, 1);
                var secondValueString = getNextValue2(line, index + 1);
                if (character.equals("+")) {
                    firstValueString = Long.toString(Long.parseLong(firstValueString) + Long.parseLong(secondValueString));
                } else if (character.equals("*")) {
                    newEquation += firstValueString + character;
                    firstValueString = secondValueString;
                } else {
                    LogUtilities.log("Uh oh mistake in: " + line);
                }
                index += character.length() + secondValueString.length();
            }
            newEquation += firstValueString;
            line = newEquation;
        }
        {
            var firstValueString = getNextValue2(line, 0);
            if (firstValueString.length() == line.length()) {
                return Long.parseLong(firstValueString);
            }
            var currentValue = Long.parseLong(firstValueString);
            var index = firstValueString.length();
            while (index < line.length()) {
                var character = StringUtilities.getStringChunk(line, index, 1);
                var value2 = getNextValue2(line, index + 1);
                if (character.equals("*")) {
                    currentValue = currentValue * Long.parseLong(value2);
                } else {
                    LogUtilities.log("Uh oh mistake in: " + line);
                }
                index += character.length() + value2.length();
            }
            return currentValue;
        }
    }
    static String getNextValue2(String line, int index) {
        String value = "";
        for (var i = index; i < line.length(); i++) {
            var character = StringUtilities.getStringChunk(line, i, 1);
            if (!character.equals("+") && !character.equals("*")) {
                value += character;
            } else {
                break;
            }
        }
        return value;
    }
}