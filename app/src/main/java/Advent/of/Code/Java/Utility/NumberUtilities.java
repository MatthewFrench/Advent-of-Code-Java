package Advent.of.Code.Java.Utility;

import Advent.of.Code.Java.Utility.Structures.Pair;

import java.text.NumberFormat;
import java.util.Collection;
import java.util.List;

public class NumberUtilities {
    final static NumberFormat formatter = NumberFormat.getInstance();
    public static String formatNumber(final Long number) {
        return formatter.format(number);
    }
    public static long sum(final Collection<Long> valueList) {
        long sum = 0;
        for (final Long value : valueList) {
            sum += value;
        }
        return sum;
    }
    public static long sumIntegers(final Collection<Integer> valueList) {
        long sum = 0;
        for (final int value : valueList) {
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
    public static int maxOfIntegers(final Collection<Integer> valueList) {
        int max = valueList.stream().findFirst().get();
        for (final int value : valueList) {
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

    public static long getLowestCommonMultiple(final List<Long> valueList) {
        long result = valueList.get(0);
        for(int index = 1; index < valueList.size(); index++) {
            result = lowestCommonMultiple(result, valueList.get(index));
        }
        return result;
    }

    // Note: I have no idea if this one is correct with how I'm looping
    public static long getGreatestCommonFactor(final List<Long> valueList) {
        long result = valueList.get(0);
        for(int index = 1; index < valueList.size(); index++) {
            result = greatestCommonFactor(result, valueList.get(index));
        }
        return result;
    }

    private static long greatestCommonFactor(long a, long b)
    {
        while (a != b) // while the two numbers are not equal...
        {
            // ...subtract the smaller one from the larger one
            if (a > b) a -= b; // if a is larger than b, subtract b from a
            else b -= a; // if b is larger than a, subtract a from b
        }

        return a; // or return b, a will be equal to b either way
    }

    private static long lowestCommonMultiple(long a, long b)
    {
        // the lcm is simply (a * b) divided by the gcf of the two
        return (a * b) / greatestCommonFactor(a, b);
    }

    // This was pulled from https://stackoverflow.com/questions/25987465/computing-area-of-a-polygon-in-java
    public double areaOfPolygon(final List<Pair<Long, Long>> vertices)
    {
        double sum = 0;
        for (int i = 0; i < vertices.size() ; i++)
        {
            if (i == 0)
            {
                //System.out.println(vertices.get(i).getKey() + "x" + (vertices.get(i + 1).y + "-" + vertices.get(vertices.size() - 1).y));
                sum += vertices.get(i).getKey() * (vertices.get(i + 1).getValue() - vertices.get(vertices.size() - 1).getValue());
            }
            else if (i == vertices.size() - 1)
            {
                //System.out.println(vertices.get(i).getKey() + "x" + (vertices.get(0).y + "-" + vertices.get(i - 1).y));
                sum += vertices.get(i).getKey() * (vertices.get(0).getValue() - vertices.get(i - 1).getValue());
            }
            else
            {
                //System.out.println(vertices.get(i).x + "x" + (vertices.get(i + 1).y + "-" + vertices.get(i - 1).y));
                sum += vertices.get(i).getKey() * (vertices.get(i + 1).getValue() - vertices.get(i - 1).getValue());
            }
        }

        double area = 0.5 * Math.abs(sum);
        return area;
    }
}
