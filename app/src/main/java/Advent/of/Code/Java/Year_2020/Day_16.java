package Advent.of.Code.Java.Year_2020;

import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.Day;

import java.util.*;

public class Day_16 implements Day {
    public void run() throws Exception {
        LogUtilities.log(Day_16.class.getName());
        final String input = LoadUtilities.loadTextFileAsString("input/2020/16/input.txt");
        final List<String> section = StringUtilities.splitStringIntoList(input, "\n\n");
        List<String> rawDefinitions = StringUtilities.splitStringIntoList(section.get(0), "\n");
        String yourTicket = section.get(1);
        List<String> otherTickets = StringUtilities.splitStringIntoList(section.get(2), "\n");
        List<Definition> definitions = new ArrayList<>();
        for (var rawDefinition : rawDefinitions) {
            definitions.add(new Definition(rawDefinition));
        }
        List<Integer> invalidValues = new ArrayList<>();
        List<String> validTickets = new ArrayList<>();
        for (var i = 1; i < otherTickets.size(); i++) {
            var rawTicket = otherTickets.get(i);
            List<String> rawValues = StringUtilities.splitStringIntoList(rawTicket, ",");
            var isValidTicket = true;
            for (var rawValue : rawValues) {
                var value = Integer.parseInt(rawValue);
                var isValid = false;
                for (var definition : definitions) {
                    if (definition.isValid(value)) {
                        isValid = true;
                        break;
                    }
                }
                if (!isValid) {
                    invalidValues.add(value);
                    isValidTicket = false;
                }
            }
            if (isValidTicket) {
                validTickets.add(rawTicket);
            }
        }
        var totalErrorRate = 0;
        for (var value : invalidValues) {
            totalErrorRate += value;
        }
        LogUtilities.log("Part 1 error rate: " + totalErrorRate);

        Map<Integer, List<Definition>> definitionsForRow = new HashMap<>();
        for (var i = 0; i < StringUtilities.splitStringIntoList(validTickets.get(0), ",").size(); i++) {
            definitionsForRow.put(i, new ArrayList<>(definitions));
        }
        validTickets.add(StringUtilities.splitStringIntoList(yourTicket, "\n").get(1));
        for (var i = 0; i < validTickets.size(); i++) {
            var rawTicket = validTickets.get(i);
            List<String> rawValues = StringUtilities.splitStringIntoList(rawTicket, ",");
            int row = 0;
            for (var rawValue : rawValues) {
                List<Definition> applicableDefinitions = definitionsForRow.get(row);
                var value = Integer.parseInt(rawValue);
                applicableDefinitions.removeIf(definition -> !definition.isValid(value));
                row += 1;
            }
        }
        // Prune definitions now
        Set<Definition> prunedDefinitions = new HashSet<>();
        var isChanging = true;
        while (isChanging) {
            isChanging = false;
            for (var row : definitionsForRow.keySet()) {
                List<Definition> definitionList = definitionsForRow.get(row);
                if (definitionList.size() == 1) {
                    var definition = definitionList.get(0);
                    if (!prunedDefinitions.contains(definition)) {
                        prunedDefinitions.add(definition);
                        isChanging = true;
                        // remove definition from all other rows
                        for (var row2 : definitionsForRow.keySet()) {
                            if (!row.equals(row2)) {
                                definitionsForRow.get(row2).remove(definition);
                            }
                        }
                    }
                } else {
                    // Check each definition to see if all other rows do not contain it
                    // If they don't, it is only for this row
                    for (var definition : definitionList) {
                        var existsInOtherRow = false;
                        for (var row2 : definitionsForRow.keySet()) {
                            if (!row.equals(row2)) {
                                if (definitionsForRow.get(row2).contains(definition)) {
                                    existsInOtherRow = true;
                                    break;
                                }
                            }
                        }
                        if (!existsInOtherRow) {
                            // Remove from all others
                            prunedDefinitions.add(definition);
                            // remove definition from all other rows
                            for (var row2 : definitionsForRow.keySet()) {
                                if (!row.equals(row2)) {
                                    if (definitionsForRow.get(row2).remove(definition)) {
                                        isChanging = true;
                                    }
                                }
                            }
                        }
                    }
                    //if (definitionList.size() > 1) {
                    //    definitionList.removeAll(prunedDefinitions);
                    //}
                }
            }
        }


        for (var row : definitionsForRow.keySet()) {
            List<Definition> definitionSet = definitionsForRow.get(row);
            if (definitionSet.size() > 1 || definitionSet.size() == 0) {
                LogUtilities.log("Uh oh on row: " + row);
                LogUtilities.logObject(definitionSet);
            }
        }


        List<String> yourTicketSplit = StringUtilities.splitStringIntoList(StringUtilities.splitStringIntoList(yourTicket, "\n").get(1), ",");
        long part2Value = 1;
        for (var row : definitionsForRow.keySet()) {
            Definition definition = definitionsForRow.get(row).get(0);
            if (definition.name.contains("departure")) {
                part2Value *= Long.parseLong(yourTicketSplit.get(row));
            }
        }

        LogUtilities.log("Part 2: " + part2Value);
    }
    public static class Definition {
        public String name;
        public List<Range> ranges;
        public Definition(String input) {
            var split = StringUtilities.splitStringIntoList(input, ": ");
            name = split.get(0);
            var ranges = StringUtilities.splitStringIntoList(split.get(1), " or ");
            this.ranges = new ArrayList<>();
            for (var range : ranges) {
                var splitRange = StringUtilities.splitStringIntoList(range, "-");
                var newRange = new Range();
                newRange.start = Integer.parseInt(splitRange.get(0));
                newRange.end = Integer.parseInt(splitRange.get(1));
                this.ranges.add(newRange);
            }
        }
        public boolean isValid(int value) {
            for (var range : ranges) {
                if (value >= range.start && value <= range.end) {
                    return true;
                }
            }
            return false;
        }
    }
    public static class Range {
        public int start;
        public int end;
    }
}