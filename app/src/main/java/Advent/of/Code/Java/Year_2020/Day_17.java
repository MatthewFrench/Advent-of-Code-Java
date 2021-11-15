package Advent.of.Code.Java.Year_2020;

import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.Day;
import org.javatuples.Pair;
import org.javatuples.Quartet;
import org.javatuples.Triplet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day_17 implements Day {
    public void run() throws Exception {
        {
            LogUtilities.log(Day_17.class.getName());
            final List<String> input = LoadUtilities.loadTextFileAsList("input/2020/17/input.txt");

            int cycles = 6;
            // Inclusive bounds min, max
            Pair<Triplet<Long, Long, Long>, Triplet<Long, Long, Long>> bounds = Pair.with(Triplet.with(0L, 0L, 0L), Triplet.with(0L, 0L, 0L));
            // x, y, z on/off
            Map<Triplet<Long, Long, Long>, Boolean> grid = new HashMap<>();

            long x = 0, y = 0, z = 0;
            for (var line : input) {
                for (var piece : StringUtilities.splitStringIntoList(line, "")) {
                    bounds = setState(grid, x, y, z, piece.equals("#"), bounds);
                    x += 1;
                }
                x = 0;
                y += 1;
            }
            var originalGrid = cloneGrid(grid);
            for (var i = 0; i < cycles; i++) {
                var newGrid = cloneGrid(grid);
                for (var entry : grid.keySet()) {
                    var activeNeighbors = getActiveNeighbors(grid, entry.getValue0(), entry.getValue1(), entry.getValue2());
                    var isActive = grid.get(entry);
                    if (isActive) {
                        if (activeNeighbors == 2 || activeNeighbors == 3) {
                            isActive = true;
                        } else {
                            isActive = false;
                        }
                    } else {
                        if (activeNeighbors == 3) {
                            isActive = true;
                        } else {
                            isActive = false;
                        }
                    }
                    bounds = setState(newGrid, entry.getValue0(), entry.getValue1(), entry.getValue2(), isActive, bounds);
                }
                grid = newGrid;
                //LogUtilities.log("Round: " + i + ", active: " + countActive(grid));
            }

            LogUtilities.log("Part 1 active: " + countActive(grid));
        }

        {
            final List<String> input = LoadUtilities.loadTextFileAsList("input/2020/17/input.txt");

            int cycles = 6;
            // Inclusive bounds min, max
            Pair<Quartet<Long, Long, Long, Long>, Quartet<Long, Long, Long, Long>> bounds = Pair.with(Quartet.with(0L, 0L, 0L, 0L), Quartet.with(0L, 0L, 0L, 0L));
            // x, y, z on/off
            Map<Quartet<Long, Long, Long, Long>, Boolean> grid = new HashMap<>();

            long x = 0, y = 0, z = 0, w = 0;
            for (var line : input) {
                for (var piece : StringUtilities.splitStringIntoList(line, "")) {
                    bounds = setState2(grid, x, y, z, w, piece.equals("#"), bounds);
                    x += 1;
                }
                x = 0;
                y += 1;
            }
            var originalGrid = cloneGrid2(grid);
            for (var i = 0; i < cycles; i++) {
                var newGrid = cloneGrid2(grid);
                for (var entry : grid.keySet()) {
                    var activeNeighbors = getActiveNeighbors2(grid, entry.getValue0(), entry.getValue1(), entry.getValue2(), entry.getValue3());
                    var isActive = grid.get(entry);
                    if (isActive) {
                        if (activeNeighbors == 2 || activeNeighbors == 3) {
                            isActive = true;
                        } else {
                            isActive = false;
                        }
                    } else {
                        if (activeNeighbors == 3) {
                            isActive = true;
                        } else {
                            isActive = false;
                        }
                    }
                    bounds = setState2(newGrid, entry.getValue0(), entry.getValue1(), entry.getValue2(), entry.getValue3(), isActive, bounds);
                }
                grid = newGrid;
                //LogUtilities.log("Round: " + i + ", active: " + countActive2(grid));
            }

            LogUtilities.log("Part 2 active: " + countActive2(grid));
        }
    }

    public static int countActive(Map<Triplet<Long, Long, Long>, Boolean> grid) {
        int active = 0;
        for (var entry : grid.keySet()) {
            if (grid.get(entry)) {
                active += 1;
            }
        }
        return active;
    }
    public static int getActiveNeighbors(Map<Triplet<Long, Long, Long>, Boolean> grid, long x, long y, long z) {
        int active = 0;
        for (var x1 = x - 1; x1 <= x + 1; x1++) {
            for (var y1 = y - 1; y1 <= y + 1; y1++) {
                for (var z1 = z - 1; z1 <= z + 1; z1++) {
                    if (x1 == x && y1 == y && z1 == z) {
                        continue;
                    }
                    var position = Triplet.with(x1, y1, z1);
                    if (grid.containsKey(position) && grid.get(position)) {
                        active += 1;
                    }
                }
            }
        }
        return active;
    }
    public static Pair<Triplet<Long, Long, Long>, Triplet<Long, Long, Long>> setState(
            Map<Triplet<Long, Long, Long>, Boolean> grid,
            long x, long y, long z,
            boolean isActive,
            Pair<Triplet<Long, Long, Long>, Triplet<Long, Long, Long>> bounds) {
        grid.put(Triplet.with(x, y, z), isActive);
        // Activate nearby pieces
        for (var x1 = x - 1; x1 <= x + 1; x1++) {
            for (var y1 = y - 1; y1 <= y + 1; y1++) {
                for (var z1 = z - 1; z1 <= z + 1; z1++) {
                    var position = Triplet.with(x1, y1, z1);
                    if (!grid.containsKey(position)) {
                        grid.put(position, false);
                    }
                }
            }
        }
        return Pair.with(
                Triplet.with(Math.min(x-1, bounds.getValue0().getValue0()),
                        Math.min(y-1, bounds.getValue0().getValue1()),
                        Math.min(z-1, bounds.getValue0().getValue2())),
                Triplet.with(Math.max(x+1, bounds.getValue1().getValue0()),
                        Math.max(y+1, bounds.getValue1().getValue1()),
                        Math.max(z+1, bounds.getValue1().getValue2())));
    }
    public static Map<Triplet<Long, Long, Long>, Boolean> cloneGrid(Map<Triplet<Long, Long, Long>, Boolean> grid) {
        Map<Triplet<Long, Long, Long>, Boolean> newGrid = new HashMap<>();
        for (var entry : grid.keySet()) {
            newGrid.put(Triplet.with(entry.getValue0(), entry.getValue1(), entry.getValue2()), grid.get(entry));
        }
        return newGrid;
    }

    public static int countActive2(Map<Quartet<Long, Long, Long, Long>, Boolean> grid) {
        int active = 0;
        for (var entry : grid.keySet()) {
            if (grid.get(entry)) {
                active += 1;
            }
        }
        return active;
    }
    public static int getActiveNeighbors2(Map<Quartet<Long, Long, Long, Long>, Boolean> grid, long x, long y, long z, long w) {
        int active = 0;
        for (var x1 = x - 1; x1 <= x + 1; x1++) {
            for (var y1 = y - 1; y1 <= y + 1; y1++) {
                for (var z1 = z - 1; z1 <= z + 1; z1++) {
                    for (var w1 = w - 1; w1 <= w + 1; w1++) {
                        if (x1 == x && y1 == y && z1 == z && w1 == w) {
                            continue;
                        }
                        var position = Quartet.with(x1, y1, z1, w1);
                        if (grid.containsKey(position) && grid.get(position)) {
                            active += 1;
                        }
                    }
                }
            }
        }
        return active;
    }
    public static Pair<Quartet<Long, Long, Long, Long>, Quartet<Long, Long, Long, Long>> setState2(
            Map<Quartet<Long, Long, Long, Long>, Boolean> grid,
            long x, long y, long z, long w,
            boolean isActive,
            Pair<Quartet<Long, Long, Long, Long>, Quartet<Long, Long, Long, Long>> bounds) {
        grid.put(Quartet.with(x, y, z, w), isActive);
        // Activate nearby pieces
        for (var x1 = x - 1; x1 <= x + 1; x1++) {
            for (var y1 = y - 1; y1 <= y + 1; y1++) {
                for (var z1 = z - 1; z1 <= z + 1; z1++) {
                    for (var w1 = w - 1; w1 <= w + 1; w1++) {
                        var position = Quartet.with(x1, y1, z1, w1);
                        if (!grid.containsKey(position)) {
                            grid.put(position, false);
                        }
                    }
                }
            }
        }
        return Pair.with(
                Quartet.with(Math.min(x-1, bounds.getValue0().getValue0()),
                        Math.min(y-1, bounds.getValue0().getValue1()),
                        Math.min(z-1, bounds.getValue0().getValue2()),
                        Math.min(w-1, bounds.getValue0().getValue3())),
                Quartet.with(Math.max(x+1, bounds.getValue1().getValue0()),
                        Math.max(y+1, bounds.getValue1().getValue1()),
                        Math.max(z+1, bounds.getValue1().getValue2()),
                        Math.max(w+1, bounds.getValue1().getValue3())));
    }
    public static Map<Quartet<Long, Long, Long, Long>, Boolean> cloneGrid2(Map<Quartet<Long, Long, Long, Long>, Boolean> grid) {
        Map<Quartet<Long, Long, Long, Long>, Boolean> newGrid = new HashMap<>();
        for (var entry : grid.keySet()) {
            newGrid.put(Quartet.with(entry.getValue0(), entry.getValue1(), entry.getValue2(), entry.getValue3()), grid.get(entry));
        }
        return newGrid;
    }
}