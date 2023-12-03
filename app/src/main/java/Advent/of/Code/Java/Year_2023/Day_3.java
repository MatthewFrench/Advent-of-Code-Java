package Advent.of.Code.Java.Year_2023;

import Advent.of.Code.Java.Utility.DataUtilities;
import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.NumberUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;
import Advent.of.Code.Java.Utility.Structures.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day_3 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2023/3/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        runSolution1(fileName);
        runSolution2(fileName);
    }

    class PartNumber {
        public String numberString="";
        public long number;
        public List<Pair<Integer, Integer>> locations = new ArrayList<>();
    }

    static Set<String> VALID_NUMBERS = new HashSet<>(DataUtilities.List("0", "1", "2", "3", "4", "5", "6", "7", "8", "9"));

    private void runSolution1(final String fileName) throws Exception {
        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);
        final int width = input.get(0).length();
        final int height = input.size();
        final String[][] grid = new String[width][height];
        int buildY = 0;
        for (final String row : input) {
            final List<String> splitRow = StringUtilities.splitStringIntoList(row, "");
            int buildX = 0;
            for (final String character : splitRow) {
                grid[buildX][buildY] = character;
                buildX += 1;
            }
            buildY += 1;
        }

        final Set<Pair<Integer, Integer>> symbolLocations = new HashSet<>();
        final List<PartNumber> partNumbers = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            PartNumber currentPartNumberConstruct = new PartNumber();
            for (int x = 0; x < width; x++) {
                final String currentValue = grid[x][y];
                if (currentValue.equals(".") || !VALID_NUMBERS.contains(currentValue)) {
                    if (!currentPartNumberConstruct.numberString.isEmpty()) {
                        currentPartNumberConstruct.number = Long.parseLong(currentPartNumberConstruct.numberString);
                        partNumbers.add(currentPartNumberConstruct);
                        currentPartNumberConstruct = new PartNumber();
                    }
                }
                if (VALID_NUMBERS.contains(currentValue)) {
                    currentPartNumberConstruct.numberString += currentValue;
                    currentPartNumberConstruct.locations.add(new Pair<>(x, y));
                } else if (!currentValue.equals(".")) {
                    symbolLocations.add(new Pair<>(x, y));
                }
            }
            if (!currentPartNumberConstruct.numberString.isEmpty()) {
                currentPartNumberConstruct.number = Long.parseLong(currentPartNumberConstruct.numberString);
                partNumbers.add(currentPartNumberConstruct);
            }
        }

        long sum = 0;
        partNumberLoop:
        for (final PartNumber partNumber : partNumbers) {
            for (final Pair<Integer, Integer> location : partNumber.locations) {
                for (int x = -1; x <= 1; x++) {
                    for (int y = -1; y <= 1; y++) {
                        if (symbolLocations.contains(new Pair<>(location.getKey() + x, location.getValue() + y))) {
                            sum += partNumber.number;
                            continue partNumberLoop;
                        }
                    }
                }
            }
        }

        LogUtilities.logGreen("Solution 1: " + sum);
    }

    class Gear {
        public Pair<Integer, Integer> location;
        public Set<PartNumber> nearbyPartNumbers = new HashSet<>();
    }

    private void runSolution2(final String fileName) throws Exception {
        final String GEAR_VALUE = "*";

        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);
        final int width = input.get(0).length();
        final int height = input.size();
        final String[][] grid = new String[width][height];
        int buildY = 0;
        for (final String row : input) {
            final List<String> splitRow = StringUtilities.splitStringIntoList(row, "");
            int buildX = 0;
            for (final String character : splitRow) {
                grid[buildX][buildY] = character;
                buildX += 1;
            }
            buildY += 1;
        }

        final Map<Pair<Integer, Integer>, Gear> gears = new HashMap<>();
        final List<PartNumber> partNumbers = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            PartNumber currentPartNumberConstruct = new PartNumber();
            for (int x = 0; x < width; x++) {
                final String currentValue = grid[x][y];
                if (currentValue.equals(".") || !VALID_NUMBERS.contains(currentValue)) {
                    if (!currentPartNumberConstruct.numberString.isEmpty()) {
                        currentPartNumberConstruct.number = Long.parseLong(currentPartNumberConstruct.numberString);
                        partNumbers.add(currentPartNumberConstruct);
                        currentPartNumberConstruct = new PartNumber();
                    }
                }
                if (VALID_NUMBERS.contains(currentValue)) {
                    currentPartNumberConstruct.numberString += currentValue;
                    currentPartNumberConstruct.locations.add(new Pair<>(x, y));
                } else if (!currentValue.equals(".") && currentValue.equals(GEAR_VALUE)) {
                    final Gear newGear = new Gear();
                    newGear.location = new Pair<>(x, y);
                    gears.put(new Pair<>(x, y), newGear);
                }
            }
            if (!currentPartNumberConstruct.numberString.isEmpty()) {
                currentPartNumberConstruct.number = Long.parseLong(currentPartNumberConstruct.numberString);
                partNumbers.add(currentPartNumberConstruct);
            }
        }

        for (final PartNumber partNumber : partNumbers) {
            for (final Pair<Integer, Integer> location : partNumber.locations) {
                for (int x = -1; x <= 1; x++) {
                    for (int y = -1; y <= 1; y++) {
                        if (gears.containsKey(new Pair<>(location.getKey() + x, location.getValue() + y))) {
                            final Gear gear = gears.get(new Pair<>(location.getKey() + x, location.getValue() + y));
                            gear.nearbyPartNumbers.add(partNumber);
                        }
                    }
                }
            }
        }

        long sum = 0;
        for (final Gear gear : gears.values()) {
            if (gear.nearbyPartNumbers.size() == 2) {
                final List<PartNumber> parts = new ArrayList<>(gear.nearbyPartNumbers);
                sum += parts.get(0).number * parts.get(1).number;
            }
        }

        LogUtilities.logGreen("Solution 2: " + sum);
    }
}
