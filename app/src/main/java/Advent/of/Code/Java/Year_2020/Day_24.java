package Advent.of.Code.Java.Year_2020;

import Advent.of.Code.Java.Utility.DataUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.Day;
import org.javatuples.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day_24 implements Day {
    public void run() throws Exception {
        final List<String> input = LoadUtilities.loadTextFileAsList("input/2020/24/input.txt");
        Map<Pair<Double, Double>, Boolean> tiles = new HashMap<>();
        for (var line : input) {
            double x = 0;
            double y = 0;
            while (line.length() > 0) {
                if (StringUtilities.chunkExistsAtStart(line, "e")) {
                    x += 1;
                    line = StringUtilities.removeStartChunk(line, "e");
                } else if (StringUtilities.chunkExistsAtStart(line, "w")) {
                    x -= 1;
                    line = StringUtilities.removeStartChunk(line, "w");
                } else if (StringUtilities.chunkExistsAtStart(line, "se")) {
                    x += 0.5;
                    y -= 0.5;
                    line = StringUtilities.removeStartChunk(line, "se");
                } else if (StringUtilities.chunkExistsAtStart(line, "sw")) {
                    x -= 0.5;
                    y -= 0.5;
                    line = StringUtilities.removeStartChunk(line, "sw");
                } else if (StringUtilities.chunkExistsAtStart(line, "ne")) {
                    x += 0.5;
                    y += 0.5;
                    line = StringUtilities.removeStartChunk(line, "ne");
                } else if (StringUtilities.chunkExistsAtStart(line, "nw")) {
                    x -= 0.5;
                    y += 0.5;
                    line = StringUtilities.removeStartChunk(line, "nw");
                }
            }
            // Now set this tile
            var key = Pair.with(x, y);
            tiles.put(key, !tiles.getOrDefault(key, false));
        }
        // Count true tiles
        var total = 0;
        for (var key : tiles.keySet()) {
            if (tiles.get(key)) {
                total += 1;
            }
        }
        LogUtilities.log("Part 1: " + total);

        var maxRound = 100;
        for (var round = 0; round < maxRound; round++) {
            Map<Pair<Double, Double>, Boolean> newTiles = new HashMap<>();
            var range = getTileRange(tiles);
            for (var x = range.getValue0().getValue0() - 1; x <= range.getValue0().getValue1() + 1; x+= 0.5) {
                for (var y = range.getValue1().getValue0() - 1; y <= range.getValue1().getValue1() + 1; y+= 0.5) {
                    var position = Pair.with(x, y);
                    var isBlack = tiles.getOrDefault(position, false);
                    var adjacentTiles = DataUtilities.List(
                            Pair.with(x-1, y),
                            Pair.with(x+1, y),
                            Pair.with(x-0.5, y-0.5),
                            Pair.with(x+0.5, y+0.5),
                            Pair.with(x-0.5, y+0.5),
                            Pair.with(x+0.5, y-0.5)
                    );
                    var adjacentBlackTiles = 0;
                    for (var newPosition : adjacentTiles) {
                        if (tiles.getOrDefault(newPosition, false)) {
                            adjacentBlackTiles += 1;
                        }
                    }
                    if (isBlack) {
                        if (adjacentBlackTiles == 0 || adjacentBlackTiles > 2) {
                            isBlack = false;
                        }
                    } else {
                        if (adjacentBlackTiles == 2) {
                            isBlack = true;
                        }
                    }
                    if (isBlack) {
                        newTiles.put(Pair.with(x, y), isBlack);
                    }
                }
            }
            tiles = newTiles;
        }

        var total2 = 0;
        for (var key : tiles.keySet()) {
            if (tiles.get(key)) {
                total2 += 1;
            }
        }
        LogUtilities.log("Part 2: " + total2);
    }
    static public Pair<Pair<Double, Double>, Pair<Double, Double>> getTileRange(Map<Pair<Double, Double>, Boolean> tiles) {
        double minXTile = 0;
        double maxXTile = 0;
        double minYTile = 0;
        double maxYTile = 0;
        for (var tile : tiles.keySet()) {
            var x = tile.getValue0();
            var y = tile.getValue1();
            minXTile = Math.min(minXTile, x);
            maxXTile = Math.max(maxXTile, x);
            minYTile = Math.min(minYTile, y);
            maxYTile = Math.max(maxYTile, y);
        }
        return Pair.with(Pair.with(minXTile, maxXTile), Pair.with(minYTile, maxYTile));
    }
}