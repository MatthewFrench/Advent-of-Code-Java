package Advent.of.Code.Java.Year_2023;

import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day_4 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2023/4/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        runSolution1(fileName);
        runSolution2(fileName);
    }

    private void runSolution1(final String fileName) throws Exception {
        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);

        long totalSum = 0;

        for (final String line : input) {
            List<String> numbers = StringUtilities.splitStringIntoList(StringUtilities.splitStringIntoList(line, ": ").get(1), " | ");
            List<String> winningNumberStrings = StringUtilities.splitStringIntoList(numbers.get(0), " ");
            List<String> pulledNumberStrings = StringUtilities.splitStringIntoList(numbers.get(1), " ");
            final Set<Long> winningNumbers = new HashSet<>();
            final List<Long> pulledNumbers = new ArrayList<>();
            for (final String winningNumber : winningNumberStrings) {
                if (!winningNumber.isEmpty()) {
                    winningNumbers.add(Long.parseLong(winningNumber));
                }
            }
            for (final String pulledNumber : pulledNumberStrings) {
                if (!pulledNumber.isEmpty()) {
                    pulledNumbers.add(Long.parseLong(pulledNumber));
                }
            }
            long count = 0;
            for (final Long pulledNumber : pulledNumbers) {
                if (winningNumbers.contains(pulledNumber)) {
                    if (count == 0) {
                        count += 1;
                    } else {
                        count = count * 2;
                    }
                }
            }
            totalSum += count;
        }

        LogUtilities.logGreen("Solution 1: " + totalSum);
    }

    private void runSolution2(final String fileName) throws Exception {
        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);

        final Map<Integer, Integer> cards = new HashMap<>();
        int cardNumber = 0;
        for (final String line : input) {
            List<String> numbers = StringUtilities.splitStringIntoList(StringUtilities.splitStringIntoList(line, ": ").get(1), " | ");
            List<String> winningNumberStrings = StringUtilities.splitStringIntoList(numbers.get(0), " ");
            List<String> pulledNumberStrings = StringUtilities.splitStringIntoList(numbers.get(1), " ");
            final Set<Long> winningNumbers = new HashSet<>();
            final List<Long> pulledNumbers = new ArrayList<>();
            for (final String winningNumber : winningNumberStrings) {
                if (!winningNumber.isEmpty()) {
                    winningNumbers.add(Long.parseLong(winningNumber));
                }
            }
            for (final String pulledNumber : pulledNumberStrings) {
                if (!pulledNumber.isEmpty()) {
                    pulledNumbers.add(Long.parseLong(pulledNumber));
                }
            }
            int count = 0;
            for (final Long pulledNumber : pulledNumbers) {
                if (winningNumbers.contains(pulledNumber)) {
                    count += 1;
                }
            }
            cardNumber += 1;
            cards.put(cardNumber, count);
        }

        final Map<Integer, Long> cardCounts = new HashMap<>();
        for (int currentCardNumber = cardNumber; currentCardNumber > 0; currentCardNumber--) {
            final int cardCount = cards.get(currentCardNumber);
            long totalCards = 1;
            for (int cardOffset = 1; cardOffset <= cardCount; cardOffset++) {
                final int nextCardNumber = currentCardNumber + cardOffset;
                totalCards += cardCounts.get(nextCardNumber);
            }
            cardCounts.put(currentCardNumber, totalCards);
        }

        long numberOfCards = 0;
        for (final Long value : cardCounts.values()) {
            numberOfCards += value;
        }

        LogUtilities.logGreen("Solution 2: " + numberOfCards);
    }
}
