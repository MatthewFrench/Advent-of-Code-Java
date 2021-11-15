package Advent.of.Code.Java.Year_2020;

import Advent.of.Code.Java.Utility.DataUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.Day;

import java.util.List;
import java.util.stream.IntStream;

public class Day_3 implements Day {
    public void run() throws Exception {
        LogUtilities.log("Day 3");
        final List<String> input = LoadUtilities.loadTextFileAsList("input/2020/3/input.txt");
        LogUtilities.log("Part 1: " + getTrees(input, DataUtilities.List(3, 1)));
        LogUtilities.log("Part 2: " + DataUtilities.ListPair(2, 1, 1, 3, 1, 5, 1, 7, 1, 1, 2).stream()
                .map(slope -> (long) getTrees(input, slope)).reduce(1L, Math::multiplyExact));
    }

    static int getTrees(final List<String> input, final List<Integer> slope) {
        final int mapWidth = input.get(0).length(), mapHeight = input.size();
        final int slopeX = slope.get(0), slopeY = slope.get(1);
        final int maxPossibleSteps = (int) Math.ceil((double) mapHeight / slopeY);
        return IntStream.range(1, maxPossibleSteps)
                .map(step -> StringUtilities.chunkExistsAtLocation(input.get(step * slopeY), "#", step * slopeX % mapWidth) ? 1 : 0).sum();
    }
}