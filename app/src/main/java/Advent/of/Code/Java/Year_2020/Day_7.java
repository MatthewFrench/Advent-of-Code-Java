package Advent.of.Code.Java.Year_2020;

import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.Day;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day_7 implements Day {
    public void run() throws Exception {
        // Needs lots of cleanup still
        LogUtilities.log(Day_7.class.getName());
        final List<String> input = LoadUtilities.loadTextFileAsList("input/2020/7/input.txt");
        HashMap<String, Bag> bagMap = new HashMap<>();
        for (var line : input) {
            var splitLine = StringUtilities.splitStringIntoList(line, " contain ");
            var bagName = splitLine.get(0);
            if (StringUtilities.chunkExistsAtEnd(bagName, "s")) {
                bagName = StringUtilities.removeEndChunk(bagName, "s");
            }
            var contentsUnparsed = splitLine.get(1);
            contentsUnparsed = StringUtilities.removeEndChunk(contentsUnparsed, ".");
            if (contentsUnparsed.equals("no other bags")) {
                bagMap.put(bagName, new Bag(bagName, new ArrayList<String>()));
            } else {
                var contents = StringUtilities.splitStringIntoList(contentsUnparsed, ", ");
                bagMap.put(bagName, new Bag(bagName, contents));
            }
        }
        var targetBag = "shiny gold bag";
        long numberContainingTarget = 0;
        for (var bagName : bagMap.keySet()) {
            if (getNumberOfBagTypeInBag(bagMap, bagName, targetBag) > 0) {
                numberContainingTarget += 1;
            }
        }
        LogUtilities.log("Result: " + numberContainingTarget);

        LogUtilities.log("Result: " + (countTotalBagsInBag(bagMap, targetBag)));
    }

    public static long getNumberOfBagTypeInBag(HashMap<String, Bag> bagMap, final String bagName, final String lookupBag) {
        var bag = bagMap.get(bagName);
        var contains = 0;
        for (String innerBagName : bag.containsBags.keySet()) {
            long numberOfBags = bag.containsBags.get(innerBagName);
            if (innerBagName.equals(lookupBag)) {
                contains += numberOfBags;
            } else {
                contains += getNumberOfBagTypeInBag(bagMap, innerBagName, lookupBag) * numberOfBags;
            }
        }
        return contains;
    }
    public static long countTotalBagsInBag(HashMap<String, Bag> bagMap, final String bagName) {
        var bag = bagMap.get(bagName);
        var contains = 0;
        for (String innerBagName : bag.containsBags.keySet()) {
            long numberOfBags = bag.containsBags.get(innerBagName);
            contains += numberOfBags * ( 1 + countTotalBagsInBag(bagMap, innerBagName));
        }
        return contains;
    }
    public static class Bag {
        public String bagName;
        public Map<String, Integer> containsBags;
        public Bag(String name, List<String> containsContents) {
            bagName = name;
            containsBags = new HashMap<>();
            for (var content : containsContents) {
                var amountString = StringUtilities.splitStringIntoList(content, " ").get(0);
                var amount = Integer.parseInt(amountString);
                var innerName = content.substring(amountString.length() + 1);
                if (StringUtilities.chunkExistsAtEnd(innerName, "s")) {
                    innerName = StringUtilities.removeEndChunk(innerName, "s");
                }
                containsBags.put(innerName, amount);
            }
        }
    }
}