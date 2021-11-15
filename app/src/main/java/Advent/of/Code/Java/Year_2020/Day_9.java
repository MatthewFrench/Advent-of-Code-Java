package Advent.of.Code.Java.Year_2020;

import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.Structures.Day;

import java.util.List;

public class Day_9 implements Day {
    public void run() throws Exception {
        LogUtilities.log(Day_9.class.getName());
        // Tripped here on making it load as a type. Then erroneously chose int over long
        final List<Long> input = LoadUtilities.loadTextFileAsTypeList("input/2020/9/input.txt", Long::parseLong);

        // Tripped on setting the preamble, had to switch from 5 to 25 to 5 to 25. Forgot once, got invalid output
        var preamble = 25;
        long part1Answer = -1;
        // Tripped here, starting at 0 instead of preamble
        for (var i = preamble; i < input.size(); i++) {
            // Tripped here, remembered sublist from Zhenpeng's solution after
            var preambleList = input.subList(Math.max(i - preamble, 0), i);
            var number = input.get(i);
            var noNumber = true;
            inner:
            for (var j = 0; j < preambleList.size(); j++) {
                var number2 = preambleList.get(j);
                for (var k = j + 1; k < preambleList.size(); k++) {
                    var number3 = preambleList.get(k);
                    // Tripped here, put if they equaled to exit, actually opposite the case
                    if (!number2.equals(number3) && number2 + number3 == number) {
                        noNumber = false;
                        break inner;
                    }
                }
            }
            if (noNumber) {
                LogUtilities.log("Part 1 answer: " + number);
                part1Answer = number;
                break;
            }
        }

        // Had to re-arrange variables here. I suppose due to a lack of understanding of what I was doing.
        // Didn't stop to think enough.
        for (var i = 0; i < input.size(); i++) {
            var number = input.get(i);
            var total = number;
            var largestNumber = number;
            var smallestNumber = number;
            for (var j = i + 1; j < input.size(); j++) {
                var number2 = input.get(j);
                // Tripped on smallest/largest number, thought it was first/last
                largestNumber = Math.max(largestNumber, number2);
                smallestNumber = Math.min(smallestNumber, number2);
                total += number2;
                if (total == part1Answer) {
                    LogUtilities.log("Part 2 answer: " + (largestNumber + smallestNumber));
                } else if (total > part1Answer) {
                    break;
                }
            }
        }
    }
}