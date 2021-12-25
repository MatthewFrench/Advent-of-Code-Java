package Advent.of.Code.Java.Year_2021;

import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;

public class Day_18 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2021/18/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);
        final List<Equation> equationsToAdd = new ArrayList<>();
        for (final String line : input) {
            equationsToAdd.add(createWith(line, 0, null));
        }

        // Add pairs together, fully reduce, repeat until we have a final reduced, then calculate magnitude

        /*
        To reduce a snailfish number, you must repeatedly do the first action in this list that applies to the snailfish number:

        If any pair is nested inside four pairs, the leftmost such pair explodes.
        If any regular number is 10 or greater, the leftmost such regular number splits.

        Once no action in the above list applies, the snailfish number is reduced.

        To explode a pair, the pair's left value is added to the first regular number to the left of the exploding pair (if any), and the pair's right value is added to the first regular number to the right of the exploding pair (if any). Exploding pairs will always consist of two regular numbers. Then, the entire exploding pair is replaced with the regular number 0.

        To split a regular number, replace it with a pair; the left element of the pair should be the regular number divided by two and rounded down, while the right element of the pair should be the regular number divided by two and rounded up. For example, 10 becomes [5,5], 11 becomes [5,6], 12 becomes [6,6], and so on.
         */


        LogUtilities.logGreen("Solution: " + equationsToAdd);
    }

    interface Equation {
    }
    static class EquationPair implements Equation {
        int level;
        EquationPair parent;
        Equation first;
        Equation second;
    }
    static class EquationValue implements Equation {
        int value;
        EquationPair parent;
    }

    static public EquationPair add(final Equation equation1, final Equation equation2) {
        // Create a new equation from these two equations, increment levels, reduce
        return null;
    }

    static public Equation createWith(final String input, int level, EquationPair parent) {
        if (NumberUtils.isDigits(input)) {
            final EquationValue equationValue = new EquationValue();
            equationValue.value = Integer.parseInt(input);
            equationValue.parent = parent;
            return equationValue;
        }
        final EquationPair equationPair = new EquationPair();
        equationPair.level = level;
        equationPair.parent = parent;
        final List<String> splitInput = new ArrayList<>(StringUtilities.splitStringIntoList(input, ""));
        splitInput.remove(0);
        splitInput.remove(splitInput.size() - 1);
        final List<String> leftSide = new ArrayList<>();
        final List<String> rightSide = new ArrayList<>();
        int pairCount = 0;
        boolean isLeftSide = true;
        for (final String character : splitInput) {
            if (character.equals("[")) {
                pairCount += 1;
            }
            if (character.equals("]")) {
                pairCount -= 1;
            }
            if (pairCount == 0 && character.equals(",")) {
                isLeftSide = false;
            } else {
                if (isLeftSide) {
                    leftSide.add(character);
                } else {
                    rightSide.add(character);
                }
            }
        }
        equationPair.first = createWith(String.join("", leftSide), level + 1, equationPair);
        equationPair.second = createWith(String.join("", rightSide), level + 1, equationPair);
        return equationPair;
    }
}
