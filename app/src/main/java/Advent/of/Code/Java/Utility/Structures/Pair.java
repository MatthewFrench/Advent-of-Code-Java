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
