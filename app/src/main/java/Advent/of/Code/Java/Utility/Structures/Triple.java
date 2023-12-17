package Advent.of.Code.Java.Utility.Structures;

import lombok.Data;

@Data
public class Triple<Object1, Object2, Object3> {
    private final Object1 object1;
    private final Object2 object2;
    private final Object3 object3;

    public static <Object1, Object2, Object3> Triple<Object1, Object2, Object3> Create(Object1 object1, Object2 object2, Object3 object3) {
        return new Triple<>(object1, object2, object3);
    }
}