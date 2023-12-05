package Advent.of.Code.Java.Utility;

import Advent.of.Code.Java.Utility.Structures.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataUtilities {
    /**
     * Turn parameter pairs into a map.
     */
    @SafeVarargs
    public static <Key, Value> Map<Key, Value> Map(final Pair<Key, Value>... valueAndKeys) {
        return Arrays.stream(valueAndKeys).collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    /**
     * Create a list of pairs using the given values and dimension size.
     * Example:
     * Dimension: 2
     * Elements: A, B, C, D, E, F
     * Returns: [[A, B], [C, D], [E, F]]
     */
    @SafeVarargs
    public static <T> List<List<T>> ListPair(final int dimensions, final T... values) {
        final ArrayList<T> elementList = new ArrayList<>(Arrays.asList(values));
        List<List<T>> lists = new ArrayList<>();
        for (int i = 0; i < elementList.size(); i += dimensions) {
            int end = Math.min(elementList.size(), i + dimensions);
            lists.add(elementList.subList(i, end));
        }
        return lists;
    }

    /**
     * Turn parameters into an arraylist of those parameters.
     */
    @SafeVarargs
    public static <T> List<T> List(final T... a) {
        return new ArrayList<>(Arrays.asList(a));
    }

    /**
     * Get the elements common to the given collections.
     */
    @SafeVarargs
    public static <T> Set<T> getCommonElements(final Collection<T>... a) {
        return Arrays.stream(a)
                .map((items) ->
                        (Set<T>)new HashSet<>(items))
                .reduce((first, second) ->
                        first.stream().filter(second::contains).collect(Collectors.toSet()))
                .orElse(new HashSet<>());
    }

    /**
     * Searches a sorted list, looking for the matching item from the searchFunction.
     * @param sortedItems The sorted list, should be sorted to fit the search function.
     * @param searchFunction Return 0 if the item matches, return 1 if we should search higher, return -1 if we should search lower
     * @return The matching item to be returned.
     * @param <T> This allows binary searching any type.
     */
    public static <T> T binarySearchSortedList(final List<T> sortedItems, Function<T, Integer> searchFunction) {
        int low = 0;
        int high = sortedItems.size() - 1;
        int mid;
        while (low <= high) {
            mid = (low + high) / 2;
            final T itemToCompare = sortedItems.get(mid);
            final int comparatorValue = searchFunction.apply(itemToCompare);
            if (comparatorValue > 0) {
                high = mid - 1;
            } else if (comparatorValue < 0) {
                low = mid + 1;
            } else {
                return itemToCompare;
            }
        }
        return null;
    }
}