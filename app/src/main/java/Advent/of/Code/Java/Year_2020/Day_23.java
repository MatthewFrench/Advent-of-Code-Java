package Advent.of.Code.Java.Year_2020;

import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.Day;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Day_23 implements Day {
    public void run() throws Exception {
        final String input = LoadUtilities.loadTextFileAsString("input/2020/23/input.txt");
        List<String> rawCups = StringUtilities.splitStringIntoList(input, "");
        ArrayList<Integer> originalCups = new ArrayList<>();
        for (String rawCup : rawCups) {
            Integer parseInt = Integer.parseInt(rawCup);
            originalCups.add(parseInt);
        }
        var max = originalCups.get(0);
        for (var cup : originalCups) {
            max = Math.max(cup, max);
        }
        while (originalCups.size() < 1_000_000) {
            max += 1;
            originalCups.add(max);
        }
        HashMap<Integer, CircularLinkedListNode> cupsMap = new HashMap<>();
        CircularLinkedListNode currentCupNode = new CircularLinkedListNode(originalCups.get(0));
        cupsMap.put(currentCupNode.getValue(), currentCupNode);
        for (var i = 1; i < originalCups.size(); i++) {
            var cup = originalCups.get(i);
            currentCupNode = currentCupNode.addValueAsNodeAfter(cup);
            cupsMap.put(currentCupNode.getValue(), currentCupNode);
        }
        // Now circle the linked list back to origin
        currentCupNode = currentCupNode.getNextNode();

        var round = 0;
        var maxRound = 10_000_000;
        int originalMinCup = 1;
        int originalMaxCup = max;
        int minCup = originalMinCup;
        int maxCup = originalMaxCup;
        while (round < maxRound) {
            // Grab the 3 cups to the right of the current cup
            var nextCupNode1 = currentCupNode.removeNextNode();
            var nextCupNode2 = currentCupNode.removeNextNode();
            var nextCupNode3 = currentCupNode.removeNextNode();

            while (minCup == nextCupNode1.getValue() || minCup == nextCupNode2.getValue() || minCup == nextCupNode3.getValue()) {
                minCup += 1;
            }
            while (maxCup == nextCupNode1.getValue() || maxCup == nextCupNode2.getValue() || maxCup == nextCupNode3.getValue()) {
                maxCup -= 1;
            }
            // remove the 3 cups
            cupsMap.remove(nextCupNode1.getValue());
            cupsMap.remove(nextCupNode2.getValue());
            cupsMap.remove(nextCupNode3.getValue());
            var destinationCupNode = getDestinationCupNode(cupsMap, minCup, maxCup, currentCupNode.getValue());

            destinationCupNode.insertNodeAfter(nextCupNode3);
            destinationCupNode.insertNodeAfter(nextCupNode2);
            destinationCupNode.insertNodeAfter(nextCupNode1);
            cupsMap.put(nextCupNode1.getValue(), nextCupNode1);
            cupsMap.put(nextCupNode2.getValue(), nextCupNode2);
            cupsMap.put(nextCupNode3.getValue(), nextCupNode3);
            minCup = originalMinCup;
            maxCup = originalMaxCup;
            currentCupNode = currentCupNode.getNextNode();
            round += 1;
        }
        /*
        var index = cups.indexOf(1);
        var output = "";
        while (output.length() < cups.size() - 1) {
            index += 1;
            if (index >= cups.size()) {
                index = 0;
            }
            output += cups.get(index);
        }
        log("part 1: " + output);
         */

        var number1Node = cupsMap.get(1).getNextNode();
        var number2Node = number1Node.getNextNode();
        LogUtilities.log("Part 2: " + ((long)number1Node.getValue() * (long)number2Node.getValue()));
    }

    static CircularLinkedListNode getDestinationCupNode(final HashMap<Integer, CircularLinkedListNode> cupsMap, final int minCup, final int maxCup, final int currentCupValue) {
        var value = currentCupValue;
        while (true) {
            value -= 1;
            //Check if value is less than all cups
            if (value < minCup) {
                value = maxCup;
            }
            var node = cupsMap.get(value);
            if (node != null) {
                return node;
            }
        }
    }

    /* Linked list Node*/
    static class CircularLinkedListNode {
        private final int data;
        private CircularLinkedListNode previous;
        private CircularLinkedListNode next;

        CircularLinkedListNode(final int value) {
            data = value;
            next = this;
            previous = this;
        }
        CircularLinkedListNode(final int value, final CircularLinkedListNode previousNode, final CircularLinkedListNode nextNode) {
            data = value;
            next = nextNode;
            previous = previousNode;
        }

        public CircularLinkedListNode addValueAsNodeAfter(final int value) {
            var newNode = new CircularLinkedListNode(value, this, next);
            next.previous = newNode;
            next = newNode;
            return newNode;
        }

        public void insertNodeAfter(final CircularLinkedListNode node) {
            node.next = next;
            next.previous = node;
            node.previous = this;
            next = node;
        }

        public CircularLinkedListNode removeNextNode() {
            var node = next;
            node.next.previous = this;
            this.next = node.next;
            node.next = null;
            node.previous = null;
            return node;
        }

        public CircularLinkedListNode getNextNode() {
            return next;
        }

        public int getValue() {
            return data;
        }
    }
}