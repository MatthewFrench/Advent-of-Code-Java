package Advent.of.Code.Java.Utility.Structures;

import lombok.Data;

/**
 * Pair data structure
 */
@Data
public class Pair<Key, Value> {
    private final Key key;
    private final Value value;

    public static <Key, Value> Pair<Key, Value> Create(Key key, Value value) {
        return new Pair<>(key, value);
    }
}

// Todo: Make a mutable version of Pair
// Todo: Make a DimensionPair that is X and Y, and additional dimensions as well
// Todo: Make Pair for triple, quadruple, and more