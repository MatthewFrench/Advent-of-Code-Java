package Advent.of.Code.Java.Year_2021;

import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.NumberUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;

import java.util.ArrayList;
import java.util.List;

public class Day_3 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2021/3/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        {
            final List<String> input = LoadUtilities.loadTextFileAsList(fileName);

            StringBuilder gamma = new StringBuilder();
            StringBuilder epsilon = new StringBuilder();

            int length = input.get(0).length();

            for (int i = 0; i < length; i++) {
                final char commonCharacter = mostCommonCharacter(input, i);
                gamma.append(commonCharacter);
                epsilon.append(commonCharacter == '0' ? '1' : '0');
            }

            long gammaValue = NumberUtilities.numberFromBinaryString(gamma.toString());
            long epsilonValue = NumberUtilities.numberFromBinaryString(epsilon.toString());

            LogUtilities.logGreen("Gamma: " + gammaValue);
            LogUtilities.logGreen("Epsilon: " + epsilonValue);
            LogUtilities.logGreen("Solution: " + (gammaValue * epsilonValue));
        }

        {
            final List<String> input = LoadUtilities.loadTextFileAsList(fileName);

            // Keep only numbers selected by the bit criteria for the rater value being searched, discard numbers that do not match
            // One number left is the rating
            // Otherwise repeat the process and consider the next bit ot the right

            //Bit criteria for oxygen generator rating is the most common value in the current bit position

            int length = input.get(0).length();
            final List<String> oxygenValues = new ArrayList<>(input);
            for (int i = 0; i < length; i++) {
                final char commonCharacter = mostCommonCharacter(oxygenValues, i);
                // Remove all not common characters
                final int position = i;
                oxygenValues.removeIf(o -> o.charAt(position) != commonCharacter);
                if (oxygenValues.size() == 1) {
                    break;
                }
            }
            final long oxygenGeneratorRating = NumberUtilities.numberFromBinaryString(oxygenValues.get(0));

            final List<String> scrubberValues = new ArrayList<>(input);
            for (int i = 0; i < length; i++) {
                final char mostCommonCharacter = mostCommonCharacter(scrubberValues, i);
                // Remove all not common characters
                final int position = i;
                scrubberValues.removeIf(o -> o.charAt(position) == mostCommonCharacter);
                if (scrubberValues.size() == 1) {
                    break;
                }
            }
            final long co2ScrubberRating = NumberUtilities.numberFromBinaryString(scrubberValues.get(0));

            LogUtilities.logGreen("oxygenGeneratorRating: " + oxygenGeneratorRating);
            LogUtilities.logGreen("co2ScrubberRating: " + co2ScrubberRating);
            LogUtilities.logGreen("Solution: " + (oxygenGeneratorRating * co2ScrubberRating));
        }
    }

    static char mostCommonCharacter(final List<String> input, int index) {
        int zero = 0;
        int one = 0;
        for (final String value : input) {
            if (value.charAt(index) == '0') {
                zero += 1;
            } else {
                one += 1;
            }
        }
        if (zero > one) {
            return '0';
        } else {
            return '1';
        }
    }
}
