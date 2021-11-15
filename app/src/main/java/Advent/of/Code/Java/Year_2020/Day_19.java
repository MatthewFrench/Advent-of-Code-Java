package Advent.of.Code.Java.Year_2020;

import Advent.of.Code.Java.Utility.DataUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.Day;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day_19 implements Day {
    public void run() throws Exception {
        LogUtilities.log(Day_19.class.getName());
        final String input = LoadUtilities.loadTextFileAsString("input/2020/19/input.txt");
        final List<String> rawRules = StringUtilities.splitStringIntoList(StringUtilities.splitStringIntoList(input, "\n\n").get(0), "\n");
        final List<String> messages = StringUtilities.splitStringIntoList(StringUtilities.splitStringIntoList(input, "\n\n").get(1), "\n");
        final Map<Integer, Rule> rules = new HashMap<>();
        for (var rawRule : rawRules) {
            var rule = new Rule(rawRule);
            rules.put(rule.index, rule);
        }

        // Count the invalid messages
        var matchingMessages = 0;
        for (var message : messages) {
            if (isValid(rules, DataUtilities.List(0), message)) {
                matchingMessages += 1;
            }
        }
        LogUtilities.log("Part 1/2 correct messages: " + matchingMessages);
    }
    // Returns true/false if matched
    static Boolean isValid(final Map<Integer, Rule> rules, final List<Integer> rulesToCompute, String message) {
        if (rulesToCompute.size() == 0) {
            if (message.length() == 0) {
                return true;
            } else {
                return false;
            }
        } else if (message.length() == 0) {
            return false;
        }
        var firstRuleNumber = rulesToCompute.remove(0);
        var firstRule = rules.get(firstRuleNumber);
        if (firstRule.ruleLists.size() == 0) {
            if (StringUtilities.chunkExistsAtStart(message, firstRule.value)) {
                message = StringUtilities.removeStartChunk(message, firstRule.value);
                return isValid(rules, new ArrayList<>(rulesToCompute), message);
            } else {
                return false;
            }
        }
        // There are branches to consider in rule lists
        for (var ruleList : firstRule.ruleLists) {
            var newRulesToCompute = new ArrayList<>(rulesToCompute);
            newRulesToCompute.addAll(0, ruleList);
            if (isValid(rules, newRulesToCompute, message)) {
                return true;
            }
        }
        return false;
    }
    static class Rule {
        public int index;
        public List<List<Integer>> ruleLists = new ArrayList<>();
        public String value = "";
        Rule(String rawRule) {
            var split = StringUtilities.splitStringIntoList(rawRule, ": ");
            index = Integer.parseInt(split.get(0));
            var rightHalf = split.get(1);
            if (StringUtilities.countStringInstanceInString(rightHalf, "\"") > 0) {
                var split2 = StringUtilities.splitStringIntoList(rightHalf, "\"");
                value = split2.get(1);
            } else {
                var split2 = StringUtilities.splitStringIntoList(split.get(1), " | ");
                for (var rawOrPath : split2) {
                    List<Integer> ruleList = new ArrayList<>();
                    var ruleNumbers = StringUtilities.splitStringIntoList(rawOrPath, " ");
                    for (var ruleNumber : ruleNumbers) {
                        var number = Integer.parseInt(ruleNumber);
                        ruleList.add(number);
                    }
                    ruleLists.add(ruleList);
                }
            }
        }
    }
}