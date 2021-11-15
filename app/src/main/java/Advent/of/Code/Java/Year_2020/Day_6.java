package Advent.of.Code.Java.Year_2020;

import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.Day;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class Day_6 implements Day {
    public void run() throws Exception {
        LogUtilities.log(Day_6.class.getName());
        final String input = LoadUtilities.loadTextFileAsString("input/2020/6/input.txt");
        {
            // Todo split into StringUtil
            List<String> groups = StringUtilities.splitStringIntoList(input, "\n\n");
            int sum = 0;
            for (var group : groups) {
                // Todo make an easy function
                group = group.replaceAll("\\s+", "");
                // Todo make an easy function
                var set = new HashSet<>(group.chars().mapToObj(c -> (char) c).collect(Collectors.toList()));
                sum += set.size();
            }
            LogUtilities.log("Sum: " + sum);
        }
        List<String> groups = StringUtilities.splitStringIntoList(input,"\n\n");
        int sum = 0;
        for (var group : groups) {
            // Todo determine if there is a way to union sets or make this more succinct
            var map = new HashMap<Character, Integer>();
            var count = 0;
            for (var person : StringUtilities.splitStringIntoList(group, "\n")) {
                count += 1;
                // Todo, make an easy way to loop through letters, it is a pain using chars
                for (char c : person.toCharArray()) {
                    map.put(c, map.getOrDefault(c, 0)+1);
                }
            }
            for (var key : map.keySet()) {
                if (map.get(key) == count) {
                    sum += 1;
                }
            }
        }
        LogUtilities.log("Sum: "+sum);
    }
}