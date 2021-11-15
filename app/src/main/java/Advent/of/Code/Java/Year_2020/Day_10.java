package Advent.of.Code.Java.Year_2020;

import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.Structures.Day;

import java.util.*;

public class Day_10 implements Day {
    public void run() throws Exception {
        LogUtilities.log(Day_10.class.getName());
        final List<Long> input = LoadUtilities.loadTextFileAsTypeList("input/2020/10/input.txt", Long::parseLong);
        Collections.sort(input);
        {
            long current = 0;
            long ending = input.get(input.size() - 1) + 3;
            Map<Long, Integer> joltDifferences = new HashMap<>();
            for (var i = 0; i < input.size(); i++) {
                long jolt = input.get(i);
                long difference = jolt - current;
                joltDifferences.put(difference, joltDifferences.getOrDefault(difference, 0) + 1);
                current = jolt;
            }
            long difference = ending - current;
            joltDifferences.put(difference, joltDifferences.getOrDefault(difference, 0) + 1);
            LogUtilities.log("Part 1: " + (joltDifferences.get(1L) * joltDifferences.get(3L)));
        }

        // Add start and end
        input.add(0, 0L);
        input.add(input.get(input.size() - 1) + 3);
        List<Long> builtInput = new ArrayList<>();
        builtInput.add(input.get(0));
        Map<Long, Long> cachedPaths = new HashMap<>();
        var count = countAllPaths(input, builtInput, 0, 0, cachedPaths);

        LogUtilities.log("Part 2 total number of ways to connect adapters: " + count);
    }
    static public long countAllPaths(List<Long> input, List<Long> builtInput, int currentInputIndex, int currentBuildIndex, Map<Long, Long> cachedPaths) {
        long totalCount = 0;
        if (currentInputIndex == input.size() - 1) {
            return 1;
        }
        long jolt = input.get(currentInputIndex);
        if (cachedPaths.containsKey(jolt)) {
            return cachedPaths.get(jolt);
        }
        List<Long> nextPossibleJolts = getAvailableJoltsSublist(input, currentInputIndex, jolt);
        // For these, recursively find the next ones

        for (var i = 0; i < nextPossibleJolts.size(); i++) {
            var nextJolt = nextPossibleJolts.get(i);
            List<Long> newBuilt = new ArrayList<>(builtInput);
            newBuilt.add(nextJolt);
            totalCount += countAllPaths(input, newBuilt, currentInputIndex + i + 1, currentBuildIndex + 1, cachedPaths);
        }
        cachedPaths.put(jolt, totalCount);
        return totalCount;
    }
    static public List<Long> getAvailableJoltsSublist(List<Long> input, int index, long jolt) {
        var max = jolt + 3;
        int endIndex = index;
        for (var i = index + 1; i < input.size(); i++) {
            var newJolt = input.get(i);
            if (newJolt <= max) {
                endIndex = i;
            } else {
                break;
            }
        }
        return input.subList(index + 1, endIndex+1);
    }
}