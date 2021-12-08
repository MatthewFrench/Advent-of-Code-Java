package Advent.of.Code.Java.Year_2021;

import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day_8 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2021/8/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        {
            final List<String> input = LoadUtilities.loadTextFileAsList(fileName);

            // For part 1, just count instances of 2 segments, 4 segments, 3 segments, and 7 segments
            long totalCount = 0;
            for (final String line : input) {
                List<String> sides = StringUtilities.splitStringIntoList(line, " | ");
                List<String> leftValues = StringUtilities.splitStringIntoList(sides.get(0), " ");
                List<String> rightValues = StringUtilities.splitStringIntoList(sides.get(1), " ");
                for (final String value : rightValues) {
                    if (value.length() == 2 || value.length() == 4 || value.length() == 3 || value.length() == 7) {
                        totalCount += 1;
                    }
                }
            }

            LogUtilities.logGreen("Solution: " + totalCount);
        }
        {
            final List<String> input = LoadUtilities.loadTextFileAsList(fileName);

            /*
                How many times each letter shows up across each number
                a = 8
                b = 6 - Unique
                c = 8
                d = 7
                e = 4 - Unique
                f = 9 - Unique
                g = 7

                How many letters each number has
                0 = 6
                1 = 2 - Unique
                2 = 5
                3 = 5
                4 = 4 - Unique
                5 = 5
                6 = 6
                7 = 3 - Unique
                8 = 7 - Unique
                9 = 6
                 */

            long totalCount = 0;
            for (final String line : input) {
                List<String> sides = StringUtilities.splitStringIntoList(line, " | ");
                List<String> leftValues = new ArrayList<>(StringUtilities.splitStringIntoList(sides.get(0), " "));
                List<String> rightValues = StringUtilities.splitStringIntoList(sides.get(1), " ");

                // badf gadfec bgcad ad dbcfg gcaeb fecdgab gad bgcadf efcdgb | gcadb ad agd deacfg
                final Map<String, Segment> solvedLetters = new HashMap<>();
                final Map<Segment, String> solvedSegments = new HashMap<>();
                for (final String letter : LETTERS) {
                    if (getNumberOfShowing(letter, leftValues) == 6) { // Must be SEGMENT_B_TOP_LEFT
                        solvedLetters.put(letter, Segment.SEGMENT_B_LEFT_TOP);
                        solvedSegments.put(Segment.SEGMENT_B_LEFT_TOP, letter);
                    } else if (getNumberOfShowing(letter, leftValues) == 4) { // Must be SEGMENT_E_LEFT_BOTTOM
                        solvedLetters.put(letter, Segment.SEGMENT_E_LEFT_BOTTOM);
                        solvedSegments.put(Segment.SEGMENT_E_LEFT_BOTTOM, letter);
                    } else if (getNumberOfShowing(letter, leftValues) == 9) { // Must be SEGMENT_F_RIGHT_BOTTOM
                        solvedLetters.put(letter, Segment.SEGMENT_F_RIGHT_BOTTOM);
                        solvedSegments.put(Segment.SEGMENT_F_RIGHT_BOTTOM, letter);
                    }
                }
                // Unsolved at this moment are A, C, D, G
                // This will solve C, get display for 1, remove F because we already have it solved
                final String letterForC = removeLetter(getWordWithLength(leftValues, 2), solvedSegments.get(Segment.SEGMENT_F_RIGHT_BOTTOM));
                solvedLetters.put(letterForC, Segment.SEGMENT_C_RIGHT_TOP);
                solvedSegments.put(Segment.SEGMENT_C_RIGHT_TOP, letterForC);
                // Unsolved at this moment are A, D, G
                // This will solve A, get display for 7, remove C and F since they are solved
                final String letterForA = removeLetter(removeLetter(getWordWithLength(leftValues, 3), solvedSegments.get(Segment.SEGMENT_F_RIGHT_BOTTOM)), solvedSegments.get(Segment.SEGMENT_C_RIGHT_TOP));
                solvedLetters.put(letterForA, Segment.SEGMENT_A_TOP);
                solvedSegments.put(Segment.SEGMENT_A_TOP, letterForA);
                // Unsolved at this moment are D, G
                // This will solve for D, get display for 4, remove B, C, and F
                final String letterForD = removeLetter(removeLetter(removeLetter(getWordWithLength(leftValues, 4), solvedSegments.get(Segment.SEGMENT_F_RIGHT_BOTTOM)), solvedSegments.get(Segment.SEGMENT_C_RIGHT_TOP)), solvedSegments.get(Segment.SEGMENT_B_LEFT_TOP));
                solvedLetters.put(letterForD, Segment.SEGMENT_D_MIDDLE);
                solvedSegments.put(Segment.SEGMENT_D_MIDDLE, letterForD);
                // G is the only letter left
                for (final String letter : LETTERS) {
                    if (!solvedLetters.containsKey(letter)) {
                        solvedLetters.put(letter, Segment.SEGMENT_G_BOTTOM);
                        solvedSegments.put(Segment.SEGMENT_G_BOTTOM, letter);
                        break;
                    }
                }

                // Now convert right values into their numbers
                String rightNumber = "";
                for (final String rightLetters : rightValues) {
                    final Set<Segment> litSegments = new HashSet<>();
                    for (int i = 0; i < rightLetters.length(); i++){
                        String letter = rightLetters.charAt(i) + "";
                        litSegments.add(solvedLetters.get(letter));
                    }
                    rightNumber += NUMBERS_MAP.get(litSegments);
                }
                totalCount += Integer.parseInt(rightNumber);

                /*
                focus on the uniques
                 */

                /*
                a and d can be c or f
                 */

                /*
                How many times each letter shows up across each number
                a = 8
                b = 6 - Unique
                c = 8
                d = 7
                e = 4 - Unique
                f = 9 - Unique
                g = 7

                How many letters each number has
                0 = 6
                1 = 2 - Unique
                2 = 5
                3 = 5
                4 = 4 - Unique
                5 = 5
                6 = 6
                7 = 3 - Unique
                8 = 7 - Unique
                9 = 6
                 */

                /*
                List<String, >

                Map<Integer, SegmentNumber> segments = new HashMap();
                segments.put(0, new SegmentNumber(0, NUMBER_ZERO, new HashMap<>()));
                segments.put(1, new SegmentNumber(1, NUMBER_ONE, new HashMap<>()));
                segments.put(2, new SegmentNumber(2, NUMBER_TWO, new HashMap<>()));
                segments.put(3, new SegmentNumber(3, NUMBER_THREE, new HashMap<>()));
                segments.put(4, new SegmentNumber(4, NUMBER_FOUR, new HashMap<>()));
                segments.put(5, new SegmentNumber(5, NUMBER_FIVE, new HashMap<>()));
                segments.put(6, new SegmentNumber(6, NUMBER_SIX, new HashMap<>()));
                segments.put(7, new SegmentNumber(7, NUMBER_SEVEN, new HashMap<>()));
                segments.put(8, new SegmentNumber(8, NUMBER_EIGHT, new HashMap<>()));
                segments.put(9, new SegmentNumber(9, NUMBER_NINE, new HashMap<>()));

                Map<String, Integer> solvedValues = new HashMap<>();
                // Maps from letter to SEGMENT_POSITION
                Map<String, List<Integer>> unsolvedLetters = new HashMap<>();
                Map<String, Integer> solvedLetters = new HashMap<>();
                final List<String> letters = ImmutableList.of("a", "b", "c", "d", "e", "f", "g");

                for (final String letter : letters) {
                    List<Integer> segmentList = new ArrayList<>(ImmutableList.of(SEGMENT_A_TOP, SEGMENT_D_MIDDLE, SEGMENT_G_BOTTOM, SEGMENT_E_LEFT_BOTTOM, SEGMENT_B_LEFT_TOP, SEGMENT_F_RIGHT_BOTTOM, SEGMENT_C_RIGHT_TOP));
                    unsolvedLetters.put(letter, segmentList);
                }
                for (int i = 0; i < unsolvedValues.size(); i++) {
                    final String value = unsolvedValues.get(i);
                    if (value.length() == 2) {
                        solvedValues.put(value, 1);
                        unsolvedValues.remove(i);
                        i -= 1;
                    }
                    if (value.length() == 4) {
                        solvedValues.put(value, 4);
                        unsolvedValues.remove(i);
                        i -= 1;
                    }
                    if (value.length() == 3) {
                        solvedValues.put(value, 7);
                        unsolvedValues.remove(i);
                        i -= 1;
                    }
                    if (value.length() == 7) {
                        solvedValues.put(value, 8);
                        unsolvedValues.remove(i);
                        i -= 1;
                    }
                }
                while (unsolvedValues.size() > 0) {

                }

                for (final String value : rightValues) {
                    if (value.length() == 2 || value.length() == 4 || value.length() == 3 || value.length() == 7 ) {
                        totalCount += 1;
                    }
                }

                 */
            }

            LogUtilities.logGreen("Solution: " + totalCount);
        }
    }

    //badf gadfec bgcad ad dbcfg gcaeb fecdgab gad bgcadf efcdgb
    // Get number of times a letter shows up in each word
    static int getNumberOfShowing(final String targetLetter, final List<String> values) {
        int count = 0;
        for (final String value : values) {
            if (value.contains(targetLetter)) {
                count += 1;
            }
        }
        return count;
    }

    static String getWordWithLength(final List<String> values, int length) {
        for (final String value : values) {
            if (value.length() == length) {
                return value;
            }
        }
        return null;
    }

    static String removeLetter(final String value, final String letterToRemove) {
        return value.replace(letterToRemove, "");
    }

    /*
    @AllArgsConstructor
    @EqualsAndHashCode
    class SegmentNumber {
        int number;
        Set<Integer> segments;
        Map<Integer, List<String>> segmentToStringPossibleMatches;
    }

     */

    static final Set<String> LETTERS = ImmutableSet.of("a", "b", "c", "d", "e", "f", "g");

    enum Segment {
        SEGMENT_A_TOP, SEGMENT_D_MIDDLE, SEGMENT_G_BOTTOM, SEGMENT_B_LEFT_TOP, SEGMENT_E_LEFT_BOTTOM, SEGMENT_C_RIGHT_TOP, SEGMENT_F_RIGHT_BOTTOM;
    }

    static final Set<Segment> NUMBER_ZERO = ImmutableSet.of(Segment.SEGMENT_A_TOP, Segment.SEGMENT_G_BOTTOM, Segment.SEGMENT_E_LEFT_BOTTOM, Segment.SEGMENT_B_LEFT_TOP, Segment.SEGMENT_F_RIGHT_BOTTOM, Segment.SEGMENT_C_RIGHT_TOP);
    static final Set<Segment> NUMBER_ONE = ImmutableSet.of(Segment.SEGMENT_F_RIGHT_BOTTOM, Segment.SEGMENT_C_RIGHT_TOP);
    static final Set<Segment> NUMBER_TWO = ImmutableSet.of(Segment.SEGMENT_A_TOP, Segment.SEGMENT_D_MIDDLE, Segment.SEGMENT_G_BOTTOM, Segment.SEGMENT_C_RIGHT_TOP, Segment.SEGMENT_E_LEFT_BOTTOM);
    static final Set<Segment> NUMBER_THREE = ImmutableSet.of(Segment.SEGMENT_A_TOP, Segment.SEGMENT_D_MIDDLE, Segment.SEGMENT_G_BOTTOM, Segment.SEGMENT_C_RIGHT_TOP, Segment.SEGMENT_F_RIGHT_BOTTOM);
    static final Set<Segment> NUMBER_FOUR = ImmutableSet.of(Segment.SEGMENT_D_MIDDLE, Segment.SEGMENT_B_LEFT_TOP, Segment.SEGMENT_C_RIGHT_TOP, Segment.SEGMENT_F_RIGHT_BOTTOM);
    static final Set<Segment> NUMBER_FIVE = ImmutableSet.of(Segment.SEGMENT_A_TOP, Segment.SEGMENT_D_MIDDLE, Segment.SEGMENT_G_BOTTOM, Segment.SEGMENT_B_LEFT_TOP, Segment.SEGMENT_F_RIGHT_BOTTOM);
    static final Set<Segment> NUMBER_SIX = ImmutableSet.of(Segment.SEGMENT_A_TOP, Segment.SEGMENT_D_MIDDLE, Segment.SEGMENT_G_BOTTOM, Segment.SEGMENT_F_RIGHT_BOTTOM, Segment.SEGMENT_B_LEFT_TOP, Segment.SEGMENT_E_LEFT_BOTTOM);
    static final Set<Segment> NUMBER_SEVEN = ImmutableSet.of(Segment.SEGMENT_A_TOP, Segment.SEGMENT_C_RIGHT_TOP, Segment.SEGMENT_F_RIGHT_BOTTOM);
    static final Set<Segment> NUMBER_EIGHT = ImmutableSet.of(Segment.SEGMENT_A_TOP, Segment.SEGMENT_D_MIDDLE, Segment.SEGMENT_G_BOTTOM, Segment.SEGMENT_E_LEFT_BOTTOM, Segment.SEGMENT_B_LEFT_TOP, Segment.SEGMENT_F_RIGHT_BOTTOM, Segment.SEGMENT_C_RIGHT_TOP);
    static final Set<Segment> NUMBER_NINE = ImmutableSet.of(Segment.SEGMENT_A_TOP, Segment.SEGMENT_D_MIDDLE, Segment.SEGMENT_G_BOTTOM, Segment.SEGMENT_B_LEFT_TOP, Segment.SEGMENT_F_RIGHT_BOTTOM, Segment.SEGMENT_C_RIGHT_TOP);

    static final List<Set<Segment>> ALL_NUMBERS = ImmutableList.of(
            NUMBER_ZERO, NUMBER_ONE, NUMBER_TWO, NUMBER_THREE, NUMBER_FOUR, NUMBER_FIVE, NUMBER_SIX, NUMBER_SEVEN, NUMBER_EIGHT, NUMBER_NINE
    );

    static final Map<Set<Segment>, Integer> NUMBERS_MAP = ImmutableMap.<Set<Segment>, Integer>builder()
            .put(NUMBER_ZERO, 0)
            .put(NUMBER_ONE, 1)
            .put(NUMBER_TWO, 2)
            .put(NUMBER_THREE, 3)
            .put(NUMBER_FOUR, 4)
            .put(NUMBER_FIVE, 5)
            .put(NUMBER_SIX, 6)
            .put(NUMBER_SEVEN, 7)
            .put(NUMBER_EIGHT, 8)
            .put(NUMBER_NINE, 9)
            .build();

    static final Map<Segment, Integer> SEGMENTS_SHOW_NUMBERS = ImmutableMap.<Segment, Integer>builder()
            .put(Segment.SEGMENT_A_TOP, countSegmentInAllNumbers(Segment.SEGMENT_A_TOP))
            .put(Segment.SEGMENT_D_MIDDLE, countSegmentInAllNumbers(Segment.SEGMENT_D_MIDDLE))
            .put(Segment.SEGMENT_G_BOTTOM, countSegmentInAllNumbers(Segment.SEGMENT_G_BOTTOM))
            .put(Segment.SEGMENT_B_LEFT_TOP, countSegmentInAllNumbers(Segment.SEGMENT_B_LEFT_TOP))
            .put(Segment.SEGMENT_E_LEFT_BOTTOM, countSegmentInAllNumbers(Segment.SEGMENT_E_LEFT_BOTTOM))
            .put(Segment.SEGMENT_C_RIGHT_TOP, countSegmentInAllNumbers(Segment.SEGMENT_C_RIGHT_TOP))
            .put(Segment.SEGMENT_F_RIGHT_BOTTOM, countSegmentInAllNumbers(Segment.SEGMENT_F_RIGHT_BOTTOM)).build();

    static int countSegmentInAllNumbers(final Segment segment) {
        int count = 0;
        for (final Set<Segment> segments : ALL_NUMBERS) {
            if (segments.contains(segment)) {
                count += 1;
            }
        }
        return count;
    }
}
