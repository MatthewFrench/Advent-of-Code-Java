package Advent.of.Code.Java.Year_2022;

import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;

import java.util.List;

public class Day_2 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2022/2/", this);
    }

    enum Shape {
        Rock,
        Paper,
        Scissors
    }

    public void executeWithInput(final String fileName) throws Exception {
        final List<List<String>> input = LoadUtilities.loadTextFileAsTypeList(fileName, (line) -> StringUtilities.splitStringIntoList(line, " "));
        {
            long yourScore = 0;
            for (final List<String> round : input) {
                final Shape opponentHand = getOpponentShape(round.get(0));
                final Shape yourHand = getYourShape(round.get(1));
                if (opponentHand == yourHand) {
                    yourScore += 3;
                } else if (opponentHand == Shape.Rock) {
                    if (yourHand == Shape.Paper) {
                        yourScore += 6;
                    }
                } else if (opponentHand == Shape.Paper) {
                    if (yourHand == Shape.Scissors) {
                        yourScore += 6;
                    }
                } else if (opponentHand == Shape.Scissors) {
                    if (yourHand == Shape.Rock) {
                        yourScore += 6;
                    }
                }
                yourScore += getScore(yourHand);
            }

            LogUtilities.logGreen("Solution: " + yourScore);
        }
        {
            long yourScore = 0;
            for (final List<String> round : input) {
                final Shape opponentHand = getOpponentShape(round.get(0));
                final Shape yourHand = getExpectedShape(opponentHand, round.get(1));
                // Tie
                if (opponentHand == yourHand) {
                    yourScore += 3;
                } else if (opponentHand == Shape.Rock) {
                    if (yourHand == Shape.Paper) {
                        yourScore += 6;
                    }
                } else if (opponentHand == Shape.Paper) {
                    if (yourHand == Shape.Scissors) {
                        yourScore += 6;
                    }
                } else if (opponentHand == Shape.Scissors) {
                    if (yourHand == Shape.Rock) {
                        yourScore += 6;
                    }
                }
                yourScore += getScore(yourHand);
            }

            LogUtilities.logGreen("Solution 2: " + yourScore);
        }
    }

    public Shape getOpponentShape(final String hand) {
        if (hand.equals("A")) {
            return Shape.Rock;
        }
        if (hand.equals("B")) {
            return Shape.Paper;
        }
        if (hand.equals("C")) {
            return Shape.Scissors;
        }
        throw new RuntimeException("Invalid opponent shape: " + hand);
    }

    public Shape getYourShape(final String hand) {
        if (hand.equals("X")) {
            return Shape.Rock;
        }
        if (hand.equals("Y")) {
            return Shape.Paper;
        }
        if (hand.equals("Z")) {
            return Shape.Scissors;
        }
        throw new RuntimeException("Invalid your shape: " + hand);
    }

    // X = lose, Y = draw, Z = win
    public Shape getExpectedShape(final Shape opponentHand, final String hand) {
        if (opponentHand == Shape.Rock) {
            if (hand.equals("X")) {
                return Shape.Scissors;
            }
            if (hand.equals("Y")) {
                return Shape.Rock;
            }
            if (hand.equals("Z")) {
                return Shape.Paper;
            }
        } else if (opponentHand == Shape.Paper) {
            if (hand.equals("X")) {
                return Shape.Rock;
            }
            if (hand.equals("Y")) {
                return Shape.Paper;
            }
            if (hand.equals("Z")) {
                return Shape.Scissors;
            }
        } else if (opponentHand == Shape.Scissors) {
            if (hand.equals("X")) {
                return Shape.Paper;
            }
            if (hand.equals("Y")) {
                return Shape.Scissors;
            }
            if (hand.equals("Z")) {
                return Shape.Rock;
            }
        }
        throw new RuntimeException("Invalid your shape: " + hand);
    }

    public long getScore(final Shape shape) {
        if (shape == Shape.Rock) {
            return 1;
        } else if (shape == Shape.Paper) {
            return 2;
        } else if (shape == Shape.Scissors) {
            return 3;
        }
        throw new RuntimeException("Invalid shape: " + shape);
    }
}
