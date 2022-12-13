package Advent.of.Code.Java.Year_2022;

import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;
import com.google.common.collect.Comparators;
import com.google.common.collect.ImmutableList;
import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Day_13 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2022/13/", this);
    }

    class ParsedItem {
        final String originalInput;
        List<ParsedItem> items = new ArrayList<>();
        Integer value = null;
        public ParsedItem(final String parse) {
            originalInput = parse;
            if (parse.charAt(0) != '[') {
                value = Integer.parseInt(parse);
            } else {
                int bracketCount = 0;
                StringBuilder currentInnerItem = new StringBuilder();
                for (int i = 0; i < parse.length(); i++) {
                    if (bracketCount > 0) {
                        if (bracketCount == 1 && parse.charAt(i) == ',') {
                            items.add(new ParsedItem(currentInnerItem.toString()));
                            currentInnerItem = new StringBuilder();
                        } else if (bracketCount > 1 || parse.charAt(i) != ']') {
                            currentInnerItem.append(parse.charAt(i));
                        }
                    }
                    if (parse.charAt(i) == '[') {
                        bracketCount += 1;
                    } else if (parse.charAt(i) == ']') {
                        bracketCount -= 1;
                    }
                }
                if (currentInnerItem.length() > 0) {
                    items.add(new ParsedItem(currentInnerItem.toString()));
                }
            }
        }
    }

    public void executeWithInput(final String fileName) throws Exception {
        final List<String> inputRows = StringUtilities.splitStringIntoList(LoadUtilities.loadTextFileAsString(fileName), "\n\n");

        int count = 0;
        int rightSum = 0;
        List<ParsedItem> parsedItems = new ArrayList<>();
        for (final String input : inputRows) {
            count += 1;
            final List<String> inputSplit = StringUtilities.splitStringIntoList(input, "\n");
            final ParsedItem leftItem = new ParsedItem(inputSplit.get(0));
            final ParsedItem rightItem = new ParsedItem(inputSplit.get(1));
            if (leftCompare(leftItem, rightItem) > 0) {
                rightSum += count;
            }
            parsedItems.add(leftItem);
            parsedItems.add(rightItem);
        }

        LogUtilities.logGreen("Solution: " + rightSum);

        final ParsedItem item2 = new ParsedItem("[[2]]");
        final ParsedItem item6 = new ParsedItem("[[6]]");
        parsedItems.add(item2);
        parsedItems.add(item6);

        // Now organize all the packages
        parsedItems.sort((o1, o2) -> -leftCompare(o1, o2));
        LogUtilities.logGreen("Solution 2: " + ((parsedItems.indexOf(item2)+1) * (parsedItems.indexOf(item6)+1)));
    }

    // 1 if left is smaller, 0 is same, -1 is left is bigger
    public int leftCompare(final ParsedItem leftItem, final ParsedItem rightItem) {
        if (leftItem.items.size() == 0 && rightItem.items.size() == 0) {
            if (leftItem.value == null && rightItem.value == null) {
                return 0;
            } else if (leftItem.value == null) {
                return 1;
            } else if (rightItem.value == null) {
                return -1;
            }
            if (leftItem.value.equals(rightItem.value)) {
                return 0;
            }
            return leftItem.value < rightItem.value ? 1 : -1;
        } else {
            List<ParsedItem> leftItems = leftItem.items.size() == 0 ? ImmutableList.of(leftItem) : leftItem.items;
            List<ParsedItem> rightItems = rightItem.items.size() == 0 ? ImmutableList.of(rightItem) : rightItem.items;
            for (int i = 0; i < leftItems.size(); i++) {
                if (rightItems.size() <= i) {
                    return -1;
                }
                final int compare = leftCompare(leftItems.get(i), rightItems.get(i));
                if (compare != 0) {
                    return compare;
                }
            }
            if (leftItems.size() < rightItems.size()) {
                return 1;
            }
        }
        return 0;
    }
}
