package Advent.of.Code.Java.Year_2023;

import Advent.of.Code.Java.Utility.DataUtilities;
import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.NumberUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day_7 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2023/7/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        runSolution1(fileName);
        runSolution2(fileName);
    }

    final List<String> ORDERED_CARDS = DataUtilities.List("A", "K", "Q", "J", "T", "9", "8", "7", "6", "5", "4", "3", "2", "J");
    final List<String> ORDERED_CARDS_WILDCARD = DataUtilities.List("A", "K", "Q", "T", "9", "8", "7", "6", "5", "4", "3", "2", "J");
    enum HAND_TYPE {
        FIVE_OF_A_KIND(1),
        FOUR_OF_A_KIND(2),
        FULL_HOUSE(3),
        THREE_OF_A_KIND(4),
        TWO_PAIR(5),
        ONE_PAIR(6),
        HIGH_CARD(7);

        public final int value;

        HAND_TYPE(int value) {
            this.value = value;
        }
    }

    class Hand {
        List<String> characters = new ArrayList<>();
        long bid;
        public HAND_TYPE getHand() {
            final Set<String> handSet = new HashSet<>(characters);
            if (handSet.size() == 5) {
                return HAND_TYPE.HIGH_CARD;
            } else if (handSet.size() == 4) {
                return HAND_TYPE.ONE_PAIR;
            } else if (handSet.size() == 3) {
                for (final String character : handSet) {
                    if (characters.stream().filter(s -> s.equals(character)).count() == 3) {
                        return HAND_TYPE.THREE_OF_A_KIND;
                    }
                }
                return HAND_TYPE.TWO_PAIR;
            } else if (handSet.size() == 2) {
                for (final String character : handSet) {
                    if (characters.stream().filter(s -> s.equals(character)).count() == 4) {
                        return HAND_TYPE.FOUR_OF_A_KIND;
                    }
                }
                return HAND_TYPE.FULL_HOUSE;
            }
            return HAND_TYPE.FIVE_OF_A_KIND;
        }
        public HAND_TYPE getHandWildcard() {
            final Set<String> handSet = new HashSet<>(characters);
            handSet.remove("J");
            long jCount = characters.stream().filter(s -> s.equals("J")).count();
            if (handSet.size() == 5) {
                return HAND_TYPE.HIGH_CARD;
            } else if (handSet.size() == 4) {
                return HAND_TYPE.ONE_PAIR;
            } else if (handSet.size() == 3) {
                for (final String character : handSet) {
                    if (characters.stream().filter(s -> s.equals(character)).count() >= 3 - jCount) {
                        return HAND_TYPE.THREE_OF_A_KIND;
                    }
                }
                return HAND_TYPE.TWO_PAIR;
            } else if (handSet.size() == 2) {
                for (final String character : handSet) {
                    if (characters.stream().filter(s -> s.equals(character)).count() >= 4 -  jCount) {
                        return HAND_TYPE.FOUR_OF_A_KIND;
                    }
                }
                return HAND_TYPE.FULL_HOUSE;
            }
            return HAND_TYPE.FIVE_OF_A_KIND;
        }
    }

    private void runSolution1(final String fileName) throws Exception {
        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);
        final List<Hand> hands = new ArrayList<>();
        for (final String line : input) {
            final Hand hand = new Hand();
            hand.characters = StringUtilities.splitStringIntoList(StringUtilities.splitStringIntoList(line, " ").get(0), "");
            hand.bid = Long.parseLong(StringUtilities.splitStringIntoList(line, " ").get(1));
            hands.add(hand);
        }
        hands.sort((o1, o2) -> {
            if (o1.getHand().value < o2.getHand().value) {
                return 1;
            } else if (o1.getHand().value > o2.getHand().value) {
                return -1;
            } else {
                for (int index = 0; index < o1.characters.size(); index++) {
                    if (ORDERED_CARDS.indexOf(o1.characters.get(index)) < ORDERED_CARDS.indexOf(o2.characters.get(index))) {
                        return 1;
                    } else if (ORDERED_CARDS.indexOf(o1.characters.get(index)) > ORDERED_CARDS.indexOf(o2.characters.get(index))) {
                        return -1;
                    }
                }
            }
            return 0;
        });

        long totalWinnings = 0;
        long current = 1;
        for (final Hand hand : hands) {
            totalWinnings += hand.bid * current;
            current += 1;
        }

        LogUtilities.logGreen("Solution 1: " + totalWinnings);
    }

    private void runSolution2(final String fileName) throws Exception {
        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);
        final List<Hand> hands = new ArrayList<>();
        for (final String line : input) {
            final Hand hand = new Hand();
            hand.characters = StringUtilities.splitStringIntoList(StringUtilities.splitStringIntoList(line, " ").get(0), "");
            hand.bid = Long.parseLong(StringUtilities.splitStringIntoList(line, " ").get(1));
            hands.add(hand);
        }
        hands.sort((o1, o2) -> {
            if (o1.getHandWildcard().value < o2.getHandWildcard().value) {
                return 1;
            } else if (o1.getHandWildcard().value > o2.getHandWildcard().value) {
                return -1;
            } else {
                for (int index = 0; index < o1.characters.size(); index++) {
                    if (ORDERED_CARDS_WILDCARD.indexOf(o1.characters.get(index)) < ORDERED_CARDS_WILDCARD.indexOf(o2.characters.get(index))) {
                        return 1;
                    } else if (ORDERED_CARDS_WILDCARD.indexOf(o1.characters.get(index)) > ORDERED_CARDS_WILDCARD.indexOf(o2.characters.get(index))) {
                        return -1;
                    }
                }
            }
            return 0;
        });

        long totalWinnings = 0;
        long current = 1;
        for (final Hand hand : hands) {
            totalWinnings += hand.bid * current;
            current += 1;
        }

        LogUtilities.logGreen("Solution 2: " + totalWinnings);
    }
}
