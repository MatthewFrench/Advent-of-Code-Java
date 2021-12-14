package Advent.of.Code.Java.Year_2021;

import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day_14 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2021/14/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        {

            final String input = LoadUtilities.loadTextFileAsString(fileName);
            final List<String> splitInput = StringUtilities.splitStringIntoList(input, "\n\n");
            final String sequenceInput = splitInput.get(0);
            final List<String> sequence = new ArrayList<>(StringUtilities.splitStringIntoList(sequenceInput, ""));
            final List<String> patternInput = StringUtilities.splitStringIntoList(splitInput.get(1), "\n");
            final List<Pattern> patterns = new ArrayList<>();
            final Map<String, Map<String, Pattern>> patternMap = new HashMap();
            for (final String pattern : patternInput) {
                final List<String> pieces = StringUtilities.splitStringIntoList(pattern, " -> ");
                final String firstLetter = pieces.get(0).charAt(0) + "";
                final String secondLetter = pieces.get(0).charAt(1) + "";
                final String insertLetter = pieces.get(1);
                final Pattern newPattern = new Pattern(firstLetter, secondLetter, insertLetter);
                patterns.add(newPattern);
                patternMap.computeIfAbsent(firstLetter, (f) -> new HashMap<>()).put(secondLetter, newPattern);
            }

            // Now if we see this as a graph, we can travel down it and count the final elements without storing them
            // Just tons of travelling
            final Map<Pattern, Map<Integer, Map<String, Long>>> patternCountMap = new HashMap<>();
            final Map<String, Long> totalElementCount = new HashMap<>();
            for (int index = 0; index < sequence.size() - 1; index++) {
                final Map<String, Long> elementCount = travel(10, sequence.get(index), sequence.get(index+1), patternMap, patternCountMap);
                for (final Map.Entry<String, Long> element : elementCount.entrySet()) {
                    totalElementCount.put(element.getKey(), totalElementCount.getOrDefault(element.getKey(), 0L) + element.getValue());
                }
            }
            totalElementCount.put(sequence.get(sequence.size() - 1), totalElementCount.getOrDefault(sequence.get(sequence.size() - 1), 0L) + 1);

            String lowestElement = sequence.get(0);
            long lowestElementCount = totalElementCount.get(sequence.get(0));
            String highestElement = sequence.get(0);
            long highestElementCount = totalElementCount.get(sequence.get(0));
            for (final Map.Entry<String, Long> element : totalElementCount.entrySet()) {
                if (element.getValue() < lowestElementCount) {
                    lowestElement = element.getKey();
                    lowestElementCount = element.getValue();
                }
                if (element.getValue() > highestElementCount) {
                    highestElement = element.getKey();
                    highestElementCount = element.getValue();
                }
            }

            LogUtilities.logGreen("Most common: " + highestElement + ", with: " + highestElementCount);
            LogUtilities.logGreen("Least common: " + lowestElement + ", with: " + lowestElementCount);
            LogUtilities.logGreen("Solution: " + (highestElementCount - lowestElementCount));
        }


        {

            final String input = LoadUtilities.loadTextFileAsString(fileName);
            final List<String> splitInput = StringUtilities.splitStringIntoList(input, "\n\n");
            final String sequenceInput = splitInput.get(0);
            final List<String> sequence = new ArrayList<>(StringUtilities.splitStringIntoList(sequenceInput, ""));
            final List<String> patternInput = StringUtilities.splitStringIntoList(splitInput.get(1), "\n");
            final List<Pattern> patterns = new ArrayList<>();
            final Map<String, Map<String, Pattern>> patternMap = new HashMap();
            for (final String pattern : patternInput) {
                final List<String> pieces = StringUtilities.splitStringIntoList(pattern, " -> ");
                final String firstLetter = pieces.get(0).charAt(0) + "";
                final String secondLetter = pieces.get(0).charAt(1) + "";
                final String insertLetter = pieces.get(1);
                final Pattern newPattern = new Pattern(firstLetter, secondLetter, insertLetter);
                patterns.add(newPattern);
                patternMap.computeIfAbsent(firstLetter, (f) -> new HashMap<>()).put(secondLetter, newPattern);
            }

            // Now if we see this as a graph, we can travel down it and count the final elements without storing them
            // Just tons of travelling
            final Map<Pattern, Map<Integer, Map<String, Long>>> patternCountMap = new HashMap<>();
            final Map<String, Long> totalElementCount = new HashMap<>();
            for (int index = 0; index < sequence.size() - 1; index++) {
                final Map<String, Long> elementCount = travel(40, sequence.get(index), sequence.get(index+1), patternMap, patternCountMap);
                for (final Map.Entry<String, Long> element : elementCount.entrySet()) {
                    totalElementCount.put(element.getKey(), totalElementCount.getOrDefault(element.getKey(), 0L) + element.getValue());
                }
            }
            totalElementCount.put(sequence.get(sequence.size() - 1), totalElementCount.getOrDefault(sequence.get(sequence.size() - 1), 0L) + 1);

            String lowestElement = sequence.get(0);
            long lowestElementCount = totalElementCount.get(sequence.get(0));
            String highestElement = sequence.get(0);
            long highestElementCount = totalElementCount.get(sequence.get(0));
            for (final Map.Entry<String, Long> element : totalElementCount.entrySet()) {
                if (element.getValue() < lowestElementCount) {
                    lowestElement = element.getKey();
                    lowestElementCount = element.getValue();
                }
                if (element.getValue() > highestElementCount) {
                    highestElement = element.getKey();
                    highestElementCount = element.getValue();
                }
            }

            LogUtilities.logGreen("Most common: " + highestElement + ", with: " + highestElementCount);
            LogUtilities.logGreen("Least common: " + lowestElement + ", with: " + lowestElementCount);
            LogUtilities.logGreen("Solution: " + (highestElementCount - lowestElementCount));
        }
    }

    public static Map<String, Long> travel(
            final int remainingSteps,
            final String left,
            final String right,
            final Map<String, Map<String, Pattern>> patternMap, // Element -> Element -> Pattern
            final Map<Pattern, Map<Integer, Map<String, Long>>> patternCountMap // Pattern -> Steps left -> Element -> Count
    ) {
        final Map<String, Pattern> patternPart = patternMap.get(left);
        if (patternPart != null) {
            final Pattern pattern = patternPart.get(right);
            if (pattern != null) {
                final Map<Integer, Map<String, Long>> patternStepsLeftMap = patternCountMap.computeIfAbsent(pattern, (a) -> new HashMap<>());
                if (patternStepsLeftMap.containsKey(remainingSteps)) {
                    return patternStepsLeftMap.get(remainingSteps);
                }
                final Map<String, Long> elementCount = new HashMap<>();
                if (remainingSteps > 0) {
                    final Map<String, Long> leftElementCount = travel(remainingSteps - 1, left, pattern.insert, patternMap, patternCountMap);
                    final Map<String, Long> rightElementCount = travel(remainingSteps - 1, pattern.insert, right, patternMap, patternCountMap);
                    for (final Map.Entry<String, Long> element : leftElementCount.entrySet()) {
                        elementCount.put(element.getKey(), elementCount.getOrDefault(element.getKey(), 0L) + element.getValue());
                    }
                    for (final Map.Entry<String, Long> element : rightElementCount.entrySet()) {
                        elementCount.put(element.getKey(), elementCount.getOrDefault(element.getKey(), 0L) + element.getValue());
                    }
                } else {
                    elementCount.put(left, 1L);
                    //elementCount.put(right, 1L);
                }
                patternStepsLeftMap.put(remainingSteps, elementCount);
                return elementCount;
            }
        }

        final Map<String, Long> elementCount = new HashMap<>();
        elementCount.put(left, 1L);
        elementCount.put(right, 1L);
        return elementCount;
    }

    @AllArgsConstructor
    @EqualsAndHashCode
    class Pattern {
        final String first;
        final String second;
        final String insert;
    }
}
