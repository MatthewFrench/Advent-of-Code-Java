package Advent.of.Code.Java.Year_2023;

import Advent.of.Code.Java.Utility.DataUtilities;
import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.NumberUtilities;
import Advent.of.Code.Java.Utility.SimpleParallelism;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Day_5 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2023/5/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        runSolution1(fileName);
        runSolution2Efficient(fileName);
    }
    final static NumberFormat myFormat = NumberFormat.getInstance();

    private final List<String> MAPPING_ORDER = DataUtilities.List("seed-to-soil", "soil-to-fertilizer", "fertilizer-to-water", "water-to-light", "light-to-temperature", "temperature-to-humidity", "humidity-to-location");

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
        long minimumValue = NumberUtilities.min(seedToLocation.values());

        LogUtilities.logGreen("Solution 1: " + minimumValue);
    }

    class Range {
        long start;
        long end;
    }

    private void runSolution2Efficient(final String fileName) throws Exception {
        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);

        // Parse seeds
        List<Range> ranges = new ArrayList<>();
        {
            final List<String> seedValues = StringUtilities.splitStringIntoList(StringUtilities.splitStringIntoList(input.get(0), ": ").get(1), " ");
            for (int index = 0; index < seedValues.size(); index+=2) {
                final Range range = new Range();
                range.start = Long.parseLong(seedValues.get(index));
                range.end = range.start + Long.parseLong(seedValues.get(index+1));
                ranges.add(range);
            }
        }

        // Parse mappings
        final Map<String, Mapping> mappings = getMappings(input);

        // Now transform and subdivide each range with intersections on the mappings
        for (final String mapName : MAPPING_ORDER) {
            final Mapping mapping = mappings.get(mapName);

            final List<Range> newRanges = new ArrayList<>();

            for (final Range range : ranges) {
                // Note: The intersecting ranges should already be in sorted order
                final List<MappingRange> intersectingRanges = getIntersectingRanges(range.start, range.end, mapping.mappingRanges);
                long start = range.start;
                long end = range.end;
                for (final MappingRange mappingRange : intersectingRanges) {
                    if (start < mappingRange.sourceStart) {
                        // Cut the first part of the range off that doesn't intersect
                        final Range newRange = new Range();
                        newRange.start = start;
                        newRange.end = mappingRange.sourceStart;
                        start = mappingRange.sourceStart;
                        newRanges.add(newRange);
                    }
                    if (start < mappingRange.sourceEnd) {
                        if (end <= mappingRange.sourceEnd) {
                            // The rest of the range is entirely within the mapping range
                            final Range newRange = new Range();
                            newRange.start = start - mappingRange.sourceStart + mappingRange.destinationStart;
                            newRange.end = end - mappingRange.sourceStart + mappingRange.destinationStart;
                            newRanges.add(newRange);
                            // There is no more range for other mapping ranges to use, this should only happen on the last item in this loop
                            start = end;
                            break;
                        } else {
                            // The end is outside the mapping range
                            final Range newRange = new Range();
                            newRange.start = start - mappingRange.sourceStart + mappingRange.destinationStart;
                            newRange.end = mappingRange.destinationEnd;
                            // Move the start to be outside of the mapping range
                            start = mappingRange.sourceEnd;
                            newRanges.add(newRange);
                        }
                    }
                }
                if (start != end) {
                    final Range newRange = new Range();
                    newRange.start = start;
                    newRange.end = end;
                    newRanges.add(newRange);
                }
            }
            ranges = newRanges;
        }

        ranges.sort(Comparator.comparingLong(o -> o.start));

        LogUtilities.logGreen("Solution 2 efficient: " + ranges.get(0).start);
    }

    private List<MappingRange> getIntersectingRanges(final long start, final long end, final List<MappingRange> mappingRanges) {
        final List<MappingRange> intersectingRanges = new ArrayList<>();
        for (final MappingRange mappingRange : mappingRanges) {
            if ((start <= mappingRange.sourceStart && end >= mappingRange.sourceEnd)
                    || (start >= mappingRange.sourceStart && start < mappingRange.sourceEnd)
                    || (end > mappingRange.sourceStart && end <= mappingRange.sourceEnd)
            ) {
                intersectingRanges.add(mappingRange);
            }
        }
        return intersectingRanges;
    }

    private void runSolution2BruteForce(final String fileName) throws Exception {
        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);

        AtomicReference<Long> minimumSeedLocation = new AtomicReference<>(null);

        // Parse mappings
        final Map<String, Mapping> mappings = getMappings(input);

        // Parse and calculate seeds
        {
            final List<String> seedValues = StringUtilities.splitStringIntoList(StringUtilities.splitStringIntoList(input.get(0), ": ").get(1), " ");
            Long seedStart = null;
            int seedCount = 0;
            // Parallelize the processing
            final SimpleParallelism simpleParallelism = new SimpleParallelism(seedValues.size());

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
                    final long seedStartCopy = seedStart;
                    simpleParallelism.add(() -> {
                        Long currentLowest = null;
                        for (long toAdd = seedStartCopy; toAdd < seedEnd; toAdd++) {
                            final long seedLocation = calculateValue(toAdd, mappings);
                            if (currentLowest == null || seedLocation < currentLowest) {
                                currentLowest = seedLocation;
                            }
                        }
                        final long currentLowestCopy = currentLowest;
                        minimumSeedLocation.updateAndGet((currentValue)->{
                            if (currentValue == null || currentLowestCopy < currentValue) {
                                LogUtilities.logRed("New lowest seed: " + currentLowestCopy);
                                return currentLowestCopy;
                            }
                            return currentValue;
                        });
                    });
                    seedStart = null;
                    LogUtilities.unIndent();
                }
                LogUtilities.unIndent();
            }
            simpleParallelism.waitForCompletion();
        }

        LogUtilities.logGreen("Solution 2: " + minimumSeedLocation.get());
    }

    private long calculateValue(final long seed, final Map<String, Mapping> mappings) {
        long currentSeedValue = seed;
        for (final String mapName : MAPPING_ORDER) {
            final Mapping mapping = mappings.get(mapName);
            final long currentSeedValueCopy = currentSeedValue;
            final MappingRange range = DataUtilities.binarySearchSortedList(mapping.mappingRanges, (compareRange) -> {
                if (currentSeedValueCopy >= compareRange.sourceStart && currentSeedValueCopy < compareRange.sourceEnd) {
                    return 0;
                }
                if (currentSeedValueCopy < compareRange.sourceStart) {
                    return 1;
                }
                return -1;
            });
            if (range != null) {
                currentSeedValue = currentSeedValue - range.sourceStart + range.destinationStart;
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

    private static class Mapping {
        String mappingName;
        List<MappingRange> mappingRanges = new ArrayList<>();
    }

    private static class MappingRange {
        Long sourceStart;
        Long sourceEnd;
        Long destinationStart;
        Long destinationEnd;
    }
}
