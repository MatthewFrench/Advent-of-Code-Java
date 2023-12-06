package Advent.of.Code.Java.Utility;

import java.util.Collection;

public class NumberUtilities {
    public static long sum(final Collection<Long> valueList) {
        long sum = 0;
        for (final Long value : valueList) {
            sum += value;
        }
        return sum;
    }
    public static long multiply(final Collection<Long> valueList) {
        long sum = 1;
        for (final Long value : valueList) {
            sum *= value;
        }
        return sum;
    }
    public static long min(final Collection<Long> valueList) {
        long min = valueList.stream().findFirst().get();
        for (final Long value : valueList) {
            min = Math.min(min, value);
        }
        return min;
    }
    public static long max(final Collection<Long> valueList) {
        long max = valueList.stream().findFirst().get();
        for (final Long value : valueList) {
            max = Math.max(max, value);
        }
        return max;
    }
    public static long numberFromBinaryString(final String value) {
        return Long.parseLong(value, 2);
    }

    public static boolean isInteger(String value) {
        if (value == null) {
            return false;
        }
        try {
            int d = Integer.parseInt(value);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
