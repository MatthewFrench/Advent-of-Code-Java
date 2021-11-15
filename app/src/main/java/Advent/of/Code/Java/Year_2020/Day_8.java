package Advent.of.Code.Java.Year_2020;

import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.Day;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day_8 implements Day {
    public void run() throws Exception {
        LogUtilities.log(Day_8.class.getName());
        final List<String> input = LoadUtilities.loadTextFileAsList("input/2020/8/input.txt");
        List<Instruction> instructions = new ArrayList<>();
        for (var line : input) {
            var lineSplit1 = StringUtilities.splitStringIntoList(line, " ");
            var instructionName = lineSplit1.get(0);
            var secondHalf = lineSplit1.get(1);
            var sign = StringUtilities.chunkExistsAtStart(secondHalf, "+");
            var value = StringUtilities.removeStartChunk(secondHalf, sign ? "+" : "-");
            instructions.add(new Instruction(instructionName, sign, Integer.parseInt(value)));
        }
        {
            var accumulator = 0;
            var instructionNumber = 0;
            Set<Instruction> hasRan = new HashSet<>();
            while (instructionNumber < instructions.size()) {
                var instruction = instructions.get(instructionNumber);
                if (hasRan.contains(instruction)) {
                    LogUtilities.log("Breaking out");
                    break;
                }
                hasRan.add(instruction);
                var dontIncrement = false;
                if (instruction.instructionName.equals("nop")) {
                    //Do nothing
                } else if (instruction.instructionName.equals("acc")) {
                    if (instruction.isPlus) {
                        accumulator += instruction.value;
                    } else {
                        accumulator -= instruction.value;
                    }
                } else if (instruction.instructionName.equals("jmp")) {
                    if (instruction.isPlus) {
                        instructionNumber += instruction.value;
                    } else {
                        instructionNumber -= instruction.value;
                    }
                    dontIncrement = true;
                }

                if (!dontIncrement) {
                    instructionNumber += 1;
                }
            }
            LogUtilities.log("Accumulator: " + accumulator);
            LogUtilities.log("Instruction: " + instructionNumber);
        }
        LogUtilities.log("Part 2");


        var accumulator = 0;
        var instructionNumber = 0;
        Set<Instruction> hasRan = new HashSet<>();
        boolean didntBreakOut = false;
        int instructionToChange = -1;
        var originalInstructions = instructions;
        while (!didntBreakOut) {
            accumulator = 0;
            instructionNumber = 0;
            instructions = cloneInstructions(originalInstructions);
            instructionToChange = findNextInstructionToFlip(instructions, instructionToChange);
            if (instructionToChange == -1) {
                LogUtilities.log("FAILED TO FIND INSTRUCTION TO CHANGE");
                break;
            }
            var instructionBeingChanged = instructions.get(instructionToChange);
            if (instructionBeingChanged.instructionName.equals("nop")) {
                instructionBeingChanged.instructionName = "jmp";
            }
            if (instructionBeingChanged.instructionName.equals("jmp")) {
                instructionBeingChanged.instructionName = "nop";
            }
            var failedToRunSuccessfully = false;
            while (instructionNumber < instructions.size()) {
                var instruction = instructions.get(instructionNumber);
                if (hasRan.contains(instruction)) {
                    LogUtilities.log("Breaking out");
                    failedToRunSuccessfully = true;
                    break;
                }
                hasRan.add(instruction);
                var dontIncrement = false;
                if (instruction.instructionName.equals("nop")) {
                    //Do nothing
                } else if (instruction.instructionName.equals("acc")) {
                    if (instruction.isPlus) {
                        accumulator += instruction.value;
                    } else {
                        accumulator -= instruction.value;
                    }
                } else if (instruction.instructionName.equals("jmp")) {
                    if (instruction.isPlus) {
                        instructionNumber += instruction.value;
                    } else {
                        instructionNumber -= instruction.value;
                    }
                    dontIncrement = true;
                }

                if (!dontIncrement) {
                    instructionNumber += 1;
                }
            }
            if (!failedToRunSuccessfully) {
                didntBreakOut = true;
            }
            LogUtilities.log("Accumulator: " + accumulator);
            LogUtilities.log("Instruction: " + instructionNumber);
        }
        LogUtilities.log("Broke out successfully! " + instructionNumber + " / " + instructions.size());
        LogUtilities.log("Accumulator: " + accumulator);
    }

    static int findNextInstructionToFlip(List<Instruction> instructions, int instructionNumber) {
        for (var i = instructionNumber + 1; i < instructions.size(); i++) {
            if (instructions.get(i).instructionName.equals("nop") || instructions.get(i).instructionName.equals("jmp")) {
                return i;
            }
        }
        return -1;
    }

    static List<Instruction> cloneInstructions(List<Instruction> instructions) {
        var list = new ArrayList<Instruction>();
        for (var instruction : instructions) {
            list.add(instruction.clone());
        }
        return list;
    }

    static class Instruction {
        public String instructionName;
        public boolean isPlus;
        public int value;

        Instruction(String name, boolean plus, int v) {
            instructionName = name;
            isPlus = plus;
            value = v;
        }

        public Instruction clone() {
            return new Instruction(instructionName, isPlus, value);
        }
    }
}