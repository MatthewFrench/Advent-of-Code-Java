package Advent.of.Code.Java.Utility;

import java.util.List;

public class NumberUtilities {
    public static long sum(final List<Long> valueList) {
        long sum = 0;
        for (final Long value : valueList) {
            sum += value;
        }
        return sum;
    }
    public static long numberFromBinaryString(final String value) {
        return Long.parseLong(value, 2);
    }
}
