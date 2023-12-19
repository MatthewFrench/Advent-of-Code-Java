package Advent.of.Code.Java.Year_2023;

import Advent.of.Code.Java.Utility.DataUtilities;
import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.NumberUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;
import Advent.of.Code.Java.Utility.Structures.Pair;
import com.google.common.base.Strings;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day_19 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2023/19/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        runSolution1(fileName);
        runSolution2(fileName);
    }
    enum Condition {
        GREATER_THAN,
        LESS_THAN,
        NONE
    }
    class Instruction {
        String leftName;
        Condition condition;
        Long rightValue;
        String result;
        public boolean isSatisfied(final Part part) {
            if (condition == Condition.NONE) {
                return true;
            }
            long compareValue = 0;
            if (leftName.equals("x")) {
                compareValue = part.x;
            }
            if (leftName.equals("m")) {
                compareValue = part.m;
            }
            if (leftName.equals("a")) {
                compareValue = part.a;
            }
            if (leftName.equals("s")) {
                compareValue = part.s;
            }
            if (condition == Condition.GREATER_THAN) {
                if (compareValue > rightValue) {
                    return true;
                }
            }
            if (condition == Condition.LESS_THAN) {
                if (compareValue < rightValue) {
                    return true;
                }
            }
            return false;
        }
    }
    class InstructionSet {
        String name;
        List<Instruction> instructions = new ArrayList<>();

        public String getResult(final Part part) {
            for (final Instruction instruction : instructions) {
                if (instruction.isSatisfied(part)) {
                    return instruction.result;
                }
            }
            return null;
        }
    }
    class Part {
        long x;
        long m;
        long a;
        long s;
    }

    private Map<String, InstructionSet> getInstructionSetMap(final String input) {
        final List<String> halves = StringUtilities.splitStringIntoList(input, "\n\n");
        final List<String> instructionLines = StringUtilities.splitStringIntoList(halves.get(0), "\n");
        final Map<String, InstructionSet> instructionSetMap = new HashMap<>();
        for (final String instructionLine : instructionLines) {
            final InstructionSet instructionSet = new InstructionSet();
            instructionSet.name = StringUtilities.splitStringIntoList(instructionLine, "{").get(0);
            final List<String> instructions = StringUtilities.splitStringIntoList(StringUtilities.removeEndChunk(StringUtilities.splitStringIntoList(instructionLine, "{").get(1), "}"), ",");
            for (final String instructionString : instructions) {
                final Instruction instruction = new Instruction();
                if (instructionString.contains(">")) {
                    instruction.leftName = StringUtilities.splitStringIntoList(instructionString, ">").get(0);
                    instruction.rightValue = Long.parseLong(StringUtilities.splitStringIntoList(StringUtilities.splitStringIntoList(instructionString, ">").get(1), ":").get(0));
                    instruction.result = StringUtilities.splitStringIntoList(StringUtilities.splitStringIntoList(instructionString, ">").get(1), ":").get(1);
                    instruction.condition = Condition.GREATER_THAN;
                } else if (instructionString.contains("<")) {
                    instruction.leftName = StringUtilities.splitStringIntoList(instructionString, "<").get(0);
                    instruction.rightValue = Long.parseLong(StringUtilities.splitStringIntoList(StringUtilities.splitStringIntoList(instructionString, "<").get(1), ":").get(0));
                    instruction.result = StringUtilities.splitStringIntoList(StringUtilities.splitStringIntoList(instructionString, "<").get(1), ":").get(1);
                    instruction.condition = Condition.LESS_THAN;
                } else {
                    instruction.condition = Condition.NONE;
                    instruction.result = instructionString;
                }
                instructionSet.instructions.add(instruction);
            }
            instructionSetMap.put(instructionSet.name, instructionSet);
        }
        return instructionSetMap;
    }
    private List<Part> getParts(final String input) {
        final List<String> halves = StringUtilities.splitStringIntoList(input, "\n\n");
        final List<String> partLines = StringUtilities.splitStringIntoList(halves.get(1), "\n");
        final List<Part> parts = new ArrayList<>();
        for (final String partValue : partLines) {
            List<String> values = StringUtilities.splitStringIntoList(StringUtilities.removeStartChunk(StringUtilities.removeEndChunk(partValue, "}"), "{"), ",");
            final Part newPart = new Part();
            newPart.x = Long.parseLong(StringUtilities.splitStringIntoList(values.get(0), "=").get(1));
            newPart.m = Long.parseLong(StringUtilities.splitStringIntoList(values.get(1), "=").get(1));
            newPart.a = Long.parseLong(StringUtilities.splitStringIntoList(values.get(2), "=").get(1));
            newPart.s = Long.parseLong(StringUtilities.splitStringIntoList(values.get(3), "=").get(1));
            parts.add(newPart);
        }
        return parts;
    }

    private void runSolution1(final String fileName) throws Exception {
        final String input = LoadUtilities.loadTextFileAsString(fileName);
        final Map<String, InstructionSet> instructionSetMap = getInstructionSetMap(input);
        final List<Part> parts = getParts(input);

        final List<Part> acceptedParts = new ArrayList<>();
        for (final Part part : parts) {
            String currentInstruction = "in";
            while (!currentInstruction.equals("A") && !currentInstruction.equals("R")) {
                final InstructionSet instructionSet = instructionSetMap.get(currentInstruction);
                currentInstruction = instructionSet.getResult(part);
            }
            if (currentInstruction.equals("A")) {
                acceptedParts.add(part);
            }
        }

        LogUtilities.logGreen("Solution 1: " + NumberUtilities.sum(DataUtilities.transformData(acceptedParts, part -> part.x + part.m + part.a + part.s)));
    }

    static class Range {
        long minimum;
        long maximum;
        public Range copy() {
            final Range range = new Range();
            range.minimum = minimum;
            range.maximum = maximum;
            return range;
        }
        public long getAmount() {
            return maximum - minimum + 1;
        }
    }
    class PartRanges {
        List<Range> xRanges = new ArrayList<>();
        List<Range> mRanges = new ArrayList<>();
        List<Range> aRanges = new ArrayList<>();
        List<Range> sRanges = new ArrayList<>();
        public PartRanges copy() {
            final PartRanges partRanges = new PartRanges();
            for (final Range range : xRanges) {
                partRanges.xRanges.add(range.copy());
            }
            for (final Range range : mRanges) {
                partRanges.mRanges.add(range.copy());
            }
            for (final Range range : aRanges) {
                partRanges.aRanges.add(range.copy());
            }
            for (final Range range : sRanges) {
                partRanges.sRanges.add(range.copy());
            }
            return partRanges;
        }
        public long getCombinationValue() {
            return NumberUtilities.sum(DataUtilities.transformData(xRanges, Range::getAmount))
                    * NumberUtilities.sum(DataUtilities.transformData(mRanges, Range::getAmount))
                    * NumberUtilities.sum(DataUtilities.transformData(aRanges, Range::getAmount))
                    * NumberUtilities.sum(DataUtilities.transformData(sRanges, Range::getAmount));
        }
        public static List<Range> splitRangeOnGreaterThan(final long greaterThanValue, final List<Range> ranges) {
            final List<Range> newRanges = new ArrayList<>();
            for (final Range range : ranges) {
                if (range.maximum <= greaterThanValue) {
                    continue;
                }
                if (range.minimum > greaterThanValue) {
                    newRanges.add(range.copy());
                    continue;
                }
                // Greater than value needs to split the range
                final Range newRange = new Range();
                newRange.minimum = greaterThanValue + 1;
                newRange.maximum = range.maximum;
                newRanges.add(newRange);
            }
            return newRanges;
        }
        public static List<Range> splitRangeOnLessThan(final long lessThanValue, final List<Range> ranges) {
            final List<Range> newRanges = new ArrayList<>();
            for (final Range range : ranges) {
                if (range.minimum >= lessThanValue) {
                    continue;
                }
                if (range.maximum < lessThanValue) {
                    newRanges.add(range.copy());
                    continue;
                }
                // Greater than value needs to split the range
                final Range newRange = new Range();
                newRange.minimum = range.minimum;
                newRange.maximum = lessThanValue - 1;
                newRanges.add(newRange);
            }
            return newRanges;
        }
    }

    private long calculateAcceptedCombinations(
            final String instructionName,
            final PartRanges partRanges,
            final Map<String, InstructionSet> instructionSetMap,
            final List<String> instructionPath
    ) {
        if (instructionName.equals("R")) {
            return 0;
        }
        if (instructionName.equals("A")) {
            return partRanges.getCombinationValue();
        }
        final List<Long> values = new ArrayList<>();
        PartRanges currentPartRanges = partRanges.copy();
        for (final Instruction instruction : instructionSetMap.get(instructionName).instructions) {
            if (instruction.condition == Condition.GREATER_THAN) {
                final PartRanges sendOffPartRanges = currentPartRanges.copy();
                final long mustBeGreaterThanValue = instruction.rightValue;
                if (instruction.leftName.equals("x")) {
                    sendOffPartRanges.xRanges = PartRanges.splitRangeOnGreaterThan(mustBeGreaterThanValue, sendOffPartRanges.xRanges);
                    currentPartRanges.xRanges = PartRanges.splitRangeOnLessThan(mustBeGreaterThanValue+1, currentPartRanges.xRanges);
                }
                if (instruction.leftName.equals("m")) {
                    sendOffPartRanges.mRanges = PartRanges.splitRangeOnGreaterThan(mustBeGreaterThanValue, sendOffPartRanges.mRanges);
                    currentPartRanges.mRanges = PartRanges.splitRangeOnLessThan(mustBeGreaterThanValue+1, currentPartRanges.mRanges);
                }
                if (instruction.leftName.equals("a")) {
                    sendOffPartRanges.aRanges = PartRanges.splitRangeOnGreaterThan(mustBeGreaterThanValue, sendOffPartRanges.aRanges);
                    currentPartRanges.aRanges = PartRanges.splitRangeOnLessThan(mustBeGreaterThanValue+1, currentPartRanges.aRanges);
                }
                if (instruction.leftName.equals("s")) {
                    sendOffPartRanges.sRanges = PartRanges.splitRangeOnGreaterThan(mustBeGreaterThanValue, sendOffPartRanges.sRanges);
                    currentPartRanges.sRanges = PartRanges.splitRangeOnLessThan(mustBeGreaterThanValue+1, currentPartRanges.sRanges);
                }
                final List<String> newInstructionPath = new ArrayList<>(instructionPath);
                newInstructionPath.add(instruction.result);
                values.add(calculateAcceptedCombinations(instruction.result, sendOffPartRanges, instructionSetMap, newInstructionPath));
            } else if (instruction.condition == Condition.LESS_THAN) {
                final PartRanges sendOffPartRanges = currentPartRanges.copy();
                final long mustBeLessThanValue = instruction.rightValue;
                if (instruction.leftName.equals("x")) {
                    sendOffPartRanges.xRanges = PartRanges.splitRangeOnLessThan(mustBeLessThanValue, sendOffPartRanges.xRanges);
                    currentPartRanges.xRanges = PartRanges.splitRangeOnGreaterThan(mustBeLessThanValue - 1, currentPartRanges.xRanges);
                }
                if (instruction.leftName.equals("m")) {
                    sendOffPartRanges.mRanges = PartRanges.splitRangeOnLessThan(mustBeLessThanValue, sendOffPartRanges.mRanges);
                    currentPartRanges.mRanges = PartRanges.splitRangeOnGreaterThan(mustBeLessThanValue - 1, currentPartRanges.mRanges);
                }
                if (instruction.leftName.equals("a")) {
                    sendOffPartRanges.aRanges = PartRanges.splitRangeOnLessThan(mustBeLessThanValue, sendOffPartRanges.aRanges);
                    currentPartRanges.aRanges = PartRanges.splitRangeOnGreaterThan(mustBeLessThanValue - 1, currentPartRanges.aRanges);
                }
                if (instruction.leftName.equals("s")) {
                    sendOffPartRanges.sRanges = PartRanges.splitRangeOnLessThan(mustBeLessThanValue, sendOffPartRanges.sRanges);
                    currentPartRanges.sRanges = PartRanges.splitRangeOnGreaterThan(mustBeLessThanValue - 1, currentPartRanges.sRanges);
                }
                final List<String> newInstructionPath = new ArrayList<>(instructionPath);
                newInstructionPath.add(instruction.result);
                values.add(calculateAcceptedCombinations(instruction.result, sendOffPartRanges, instructionSetMap, newInstructionPath));
            } else if (instruction.condition == Condition.NONE) {
                final List<String> newInstructionPath = new ArrayList<>(instructionPath);
                newInstructionPath.add(instruction.result);
                values.add(calculateAcceptedCombinations(instruction.result, currentPartRanges, instructionSetMap, newInstructionPath));
            }
        }
        return NumberUtilities.sum(values);
    }

    private void runSolution2(final String fileName) throws Exception {
        final String input = LoadUtilities.loadTextFileAsString(fileName);
        final Map<String, InstructionSet> instructionSetMap = getInstructionSetMap(input);

        final PartRanges partRanges = new PartRanges();
        final Range xRange = new Range();
        xRange.minimum = 1;
        xRange.maximum = 4000;
        partRanges.xRanges.add(xRange);
        final Range mRange = new Range();
        mRange.minimum = 1;
        mRange.maximum = 4000;
        partRanges.mRanges.add(mRange);
        final Range aRange = new Range();
        aRange.minimum = 1;
        aRange.maximum = 4000;
        partRanges.aRanges.add(aRange);
        final Range sRange = new Range();
        sRange.minimum = 1;
        sRange.maximum = 4000;
        partRanges.sRanges.add(sRange);

        final List<String> instructionPath = new ArrayList<>();
        instructionPath.add("in");
        LogUtilities.logGreen("Solution 2: " + calculateAcceptedCombinations("in", partRanges, instructionSetMap, instructionPath));
    }
}
