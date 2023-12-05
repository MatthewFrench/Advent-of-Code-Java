package Advent.of.Code.Java.Year_2023;

import Advent.of.Code.Java.Utility.DataUtilities;
import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.NumberUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day_5 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2023/5/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        runSolution1(fileName);
        runSolution2(fileName);
    }
    final static NumberFormat myFormat = NumberFormat.getInstance();

    private List<String> MAPPING_ORDER = DataUtilities.List("seed-to-soil", "soil-to-fertilizer", "fertilizer-to-water", "water-to-light", "light-to-temperature", "temperature-to-humidity", "humidity-to-location");

    private void runSolution1(final String fileName) throws Exception {
        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);

        // Parse seeds
        final List<Long> seeds = new ArrayList<>();
        {
            final List<String> seedValues = StringUtilities.splitStringIntoList(StringUtilities.splitStringIntoList(input.get(0), ": ").get(1), " ");
            for (final String seedValue : seedValues) {
                seeds.add(Long.parseLong(seedValue));
            }
        }

        // Parse mappings
        final Map<String, Mapping> mappings = getMappings(input);

        // Now take each seed and calculate the final value from the mapping order
        final Map<Long, Long> seedToLocation = new HashMap<>();
        for (final Long seed : seeds) {
            seedToLocation.put(seed, calculateValue(seed, mappings));
        }
        // Todo add get minimum from list function
        long minimumValue = seedToLocation.values().stream().min(Long::compareTo).get();

        LogUtilities.logGreen("Solution 1: " + minimumValue);
    }

    private void runSolution2(final String fileName) throws Exception {
        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);

        Long minimumSeedLocation = null;

        // Parse mappings
        final Map<String, Mapping> mappings = getMappings(input);

        // Parse and calculate seeds
        {
            final List<String> seedValues = StringUtilities.splitStringIntoList(StringUtilities.splitStringIntoList(input.get(0), ": ").get(1), " ");
            Long seedStart = null;
            int seedCount = 0;
            // Todo: Add an easy way to parallelize calculating over a list of values
            for (final String seedValue : seedValues) {
                LogUtilities.indent();
                LogUtilities.logBlue("Seed range progress: " + (seedCount) + " / " + (seedValues.size()));
                seedCount += 1;
                if (seedStart == null) {
                    seedStart = Long.parseLong(seedValue);
                } else {
                    long seedEnd = seedStart + Long.parseLong(seedValue);
                    LogUtilities.indent();
                    LogUtilities.logPurple("Number in range: " + myFormat.format(seedEnd - seedStart));
                    for (long toAdd = seedStart; toAdd < seedEnd; toAdd++) {
                        final long seedLocation = calculateValue(toAdd, mappings);
                        if (minimumSeedLocation == null || seedLocation < minimumSeedLocation) {
                            minimumSeedLocation = seedLocation;
                            LogUtilities.logRed("New lowest seed: " + minimumSeedLocation);
                        }
                    }
                    seedStart = null;
                    LogUtilities.unIndent();
                }
                LogUtilities.unIndent();
            }
        }

        LogUtilities.logGreen("Solution 2: " + minimumSeedLocation);
        // Todo: Add a number utility for getting the minimum number
    }

    private long calculateValue(final long seed, final Map<String, Mapping> mappings) {
        long currentSeedValue = seed;
        mappingLoop:
        for (final String mapName : MAPPING_ORDER) {
            final Mapping mapping = mappings.get(mapName);
            // Todo: Add a binary search list function that should make this very fast based on a comparator
            // assuming the list is already sorted
            //Collections.binarySearch(mapping.mappingRanges, currentSeedValue);
            for (final MappingRange range : mapping.mappingRanges) {
                if (currentSeedValue >= range.sourceStart && currentSeedValue < range.sourceEnd) {
                    currentSeedValue = currentSeedValue - range.sourceStart + range.destinationStart;
                    continue mappingLoop;
                }
            }
        }
        return currentSeedValue;
    }

    private Map<String, Mapping> getMappings(final List<String> input) {
        final Map<String, Mapping> mappings = new HashMap<>();

        Mapping currentMapping = null;
        for (final String line : input) {
            if (line.isEmpty()) {
                if (currentMapping != null) {
                    mappings.put(currentMapping.mappingName, currentMapping);
                }
                currentMapping = null;
            } else if (line.contains(" map:")) {
                currentMapping = new Mapping();
                currentMapping.mappingName = StringUtilities.removeEndChunk(line, " map:");
            } else if (!StringUtilities.chunkExistsAtStart(line, "seeds: ")) {
                final List<String> numberStrings = StringUtilities.splitStringIntoList(line, " ");
                long destinationRangeStart = Long.parseLong(numberStrings.get(0));
                long sourceRangeStart = Long.parseLong(numberStrings.get(1));
                long rangeLength = Long.parseLong(numberStrings.get(2));
                final MappingRange newMappingRange = new MappingRange();
                newMappingRange.sourceStart = sourceRangeStart;
                newMappingRange.destinationStart = destinationRangeStart;
                newMappingRange.sourceEnd = sourceRangeStart + rangeLength;
                newMappingRange.destinationEnd = destinationRangeStart + rangeLength;
                currentMapping.mappingRanges.add(newMappingRange);
            }
        }
        if (currentMapping != null) {
            mappings.put(currentMapping.mappingName, currentMapping);
        }

        for (final Mapping mapping : mappings.values()) {
            mapping.mappingRanges.sort(Comparator.comparingLong(o -> o.sourceStart));
        }

        return mappings;
    }

    private class Mapping {
        String mappingName;
        List<MappingRange> mappingRanges = new ArrayList<>();
    }

    private class MappingRange {
        Long sourceStart;
        Long sourceEnd;
        Long destinationStart;
        Long destinationEnd;
    }
}
