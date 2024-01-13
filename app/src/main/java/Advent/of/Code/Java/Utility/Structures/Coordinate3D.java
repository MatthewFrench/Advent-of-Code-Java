package Advent.of.Code.Java.Utility.Structures;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Coordinate3D<T> {
    T x;
    T y;
    T z;
}
