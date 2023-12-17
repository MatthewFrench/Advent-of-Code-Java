package Advent.of.Code.Java.Utility.Structures;

import lombok.Data;

@Data
public class Quadruple<Object1, Object2, Object3, Object4> {
    private final Object1 object1;
    private final Object2 object2;
    private final Object3 object3;
    private final Object4 object4;

    public static <Object1, Object2, Object3, Object4> Quadruple<Object1, Object2, Object3, Object4> Create(Object1 object1, Object2 object2, Object3 object3, Object4 object4) {
        return new Quadruple<>(object1, object2, object3, object4);
    }
}