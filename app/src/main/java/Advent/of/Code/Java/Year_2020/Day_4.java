package Advent.of.Code.Java.Year_2020;

import Advent.of.Code.Java.Utility.DataUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.Day;
import Advent.of.Code.Java.Utility.Structures.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day_4 implements Day {
    static Set<Character> VALID_HCL_CHARACTERS = Set.of('a', 'b', 'c', 'd', 'e', 'f', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
    static Set<String> VALID_ECL_VALUES = Set.of("amb", "blu", "brn", "gry", "grn", "hzl", "oth");
    static Map<String, Function<String, Boolean>> FIELD_VALIDATION = DataUtilities.Map(
            Pair.Create("byr", (value) -> {
                // idea: isNumber(value).ofLength(4).allowedRange(1920, 2002).isTrue()
                if (value.length() != 4) {
                    return false;
                }
                var num = Integer.parseInt(value);
                return num >= 1920 && num <= 2002;
            }),
            Pair.Create("iyr", (value) -> {
                // idea: isNumber(value).ofLength(4).allowedRange(2010, 2020).isTrue()
                if (value.length() != 4) {
                    return false;
                }
                var num = Integer.parseInt(value);
                return num >= 2010 && num <= 2020;
            }),
            Pair.Create("eyr", (value) -> {
                // idea: isNumber(value).ofLength(4).allowedRange(2020, 2030).isTrue()
                if (value.length() != 4) {
                    return false;
                }
                var num = Integer.parseInt(value);
                return num >= 2020 && num <= 2030;
            }),
            Pair.Create("hgt", (value) -> {
                /*
                return stringQuery(value).endsIn("cm").removeEnd("cm").toNumber().allowedRange(150, 193).isTrue()
                || stringQuery(value).query.endsIn("in").removeEnd("in").toNumber().allowedRange(59, 76).isTrue()
                */
                if (StringUtilities.chunkExistsAtEnd(value, "cm")) {
                    var value2 = StringUtilities.removeEndChunk(value, "cm");
                    var num = Integer.parseInt(value2);
                    return num >= 150 && num <= 193;
                } else if (StringUtilities.chunkExistsAtEnd(value, "in")) {
                    var value2 = StringUtilities.removeEndChunk(value, "in");
                    var num = Integer.parseInt(value2);
                    return num >= 59 && num <= 76;
                } else {
                    return false;
                }
            }),
            Pair.Create("hcl", (value) -> {
                /*
                return stringQuery(value).startsWith("#").removeStart("#").allCharactersMatch(VALID_HCL_CHARACTERS).isTrue()
                */
                if (StringUtilities.chunkExistsAtStart(value, "#")) {
                    var value2 = StringUtilities.removeStartChunk(value, "#");
                    if (value2.length() == 6) {
                        return value2.chars().mapToObj(c -> (char)c).allMatch(VALID_HCL_CHARACTERS::contains);
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }),
            Pair.Create("ecl", VALID_ECL_VALUES::contains),
            Pair.Create("pid", (value) -> {
                /*
                return stringQuery(value).ofLength(9).allCharactersMatch(Character::isDigit).isTrue()
                */
                if (value.length() == 9) {
                    return value.chars().allMatch(Character::isDigit);
                } else {
                    return false;
                }
            })
    );

    public void run() throws Exception {
        LogUtilities.log("Day 4");
        final String input = LoadUtilities.loadTextFileAsString("input/2020/4/input.txt");

        // Can you make a function that turns generic list data into a map?

        // Can this be made more concise and readable?
        // Split entrees by double new line
        List<Map<String, String>> credentials = Arrays.stream(input.split("\n\n"))
                // Change other whitespace to plain spaces
                .map(cred -> cred.replace("\n", " ").replace("\r", " "))
                // Split by whitespace
                .map(cred -> StringUtilities.splitStringIntoList(cred, " "))
                .map(credList -> credList.stream()
                        // Split by :
                        .map(piece -> StringUtilities.splitStringIntoList(piece, ":"))
                        // Change string list to map
                        .collect(Collectors.toMap(pieceSplit -> pieceSplit.get(0), pieceSplit -> pieceSplit.get(1).trim()))
                ).collect(Collectors.toList());

        // Can this be made more readable?
        var firstPassValidCredentials = credentials.stream()
                .filter(credential -> credential.keySet().containsAll(FIELD_VALIDATION.keySet()))
                .peek(credential -> credential.keySet().removeIf(piece -> !FIELD_VALIDATION.containsKey(piece)))
                .collect(Collectors.toList());

        LogUtilities.log("Part 1 valid: " + firstPassValidCredentials.size());

        // Can this be made more readable?
        var validCount = firstPassValidCredentials.stream().filter(credential ->
                credential.entrySet().stream().allMatch(entry ->
                        FIELD_VALIDATION.get(entry.getKey()).apply(entry.getValue())
                )
        ).count();
        LogUtilities.log("Part 2 valid: " + validCount);
    }
}