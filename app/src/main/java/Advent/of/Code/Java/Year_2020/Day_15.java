package Advent.of.Code.Java.Year_2020;

import Advent.of.Code.Java.Utility.DataUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.Day;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day_15 implements Day {
    public void run() throws Exception {
        LogUtilities.log(Day_15.class.getName());
        final String input = LoadUtilities.loadTextFileAsString("input/2020/15/input.txt");
        List<Long> inputNumbers = StringUtilities.splitStringIntoList(input, ",").stream().map(Long::parseLong).collect(Collectors.toList());
        var max = 30000000;
        Map<Long, List<Long>> spokenMap = new HashMap<>();
        for (long i = 0; i < inputNumbers.size(); i++) {
            spokenMap.put(inputNumbers.get((int) i), DataUtilities.List(i+1));
        }
        for (long i = inputNumbers.size() - 1; i < max - 1; i++) {
            long newNumber = 0;
            long lastNumber = inputNumbers.get((int) i);
            if (spokenMap.get(lastNumber).size() > 1) {
                var list = spokenMap.get(lastNumber);
                var last = list.get(list.size() - 1);
                var beforeLast = list.get(list.size() - 2);
                newNumber = last - beforeLast;
            }
            if (!spokenMap.containsKey(newNumber)) {
                spokenMap.put(newNumber, DataUtilities.List());
            }
            spokenMap.get(newNumber).add(i+2);
            inputNumbers.add(newNumber);
        }
        LogUtilities.log("Result: " + inputNumbers.get(inputNumbers.size() - 1));
    }
}