package Advent.of.Code.Java.Year_2020;

import Advent.of.Code.Java.Utility.DataUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.Day;
import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day_20 implements Day {
    public void run() throws Exception {
        LogUtilities.log(Day_20.class.getName());
        final String input = LoadUtilities.loadTextFileAsString("input/2020/20/input.txt");
        final List<String> rawTiles = StringUtilities.splitStringIntoList(input, "\n\n");
        final List<Tile> tiles = new ArrayList<>();
        for (var rawTile : rawTiles) {
            var tile = new Tile(rawTile);
            tiles.add(tile);
        }
        List<Tile> tilesLeft = new ArrayList<>(tiles);
        Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> bounds = new Pair<>(Pair.with(0, 0), Pair.with(0, 0));
        // X, Y .. Tile, Index of Image
        final Map<Pair<Integer, Integer>, Pair<Tile, Integer>> tilePlacement = new HashMap<>();
        var checkPositions = new ArrayList<Pair<Integer, Integer>>();

        bounds = placeTile(tilesLeft, bounds, tilePlacement, checkPositions, tilesLeft.get(0), 0, 0, 0);

        overallLoop:
        while (tilesLeft.size() > 0 && checkPositions.size() > 0) {
            var position = checkPositions.remove(0);
            // Compare every tile left to see if it will match with this position
            var nearest = getTileNearPosition(tilePlacement, position.getValue0(), position.getValue1());
            var comparingTilePair = nearest.getValue0();
            var comparingTile = comparingTilePair.getValue0();
            var comparingTileImageIndex = comparingTilePair.getValue1();
            var comparingTileImage = comparingTile.comparisonImages.get(comparingTileImageIndex);
            var deltaX = nearest.getValue1();
            var deltaY = nearest.getValue2();
            for (var tile : tilesLeft) {
                // check if a tile matches this position compared to the comparingTile
                nextTileImage:
                for (var tileImageIndex = 0; tileImageIndex < tile.comparisonImages.size(); tileImageIndex++) {
                    var tileImage = tile.comparisonImages.get(tileImageIndex);
                    if (deltaX == -1) {
                        // Check right side of the comparing tile and the left side of the new tile
                        for (var i = 0; i < TILE_WIDTH; i++) {
                            if (comparingTileImage[TILE_WIDTH - 1][i] != tileImage[0][i]) {
                                continue nextTileImage;
                            }
                        }
                    } else if (deltaX == 1) {
                        // Check left side of the comparing tile and the right side of the new tile
                        for (var i = 0; i < TILE_WIDTH; i++) {
                            if (comparingTileImage[0][i] != tileImage[TILE_WIDTH - 1][i]) {
                                continue nextTileImage;
                            }
                        }
                    } else if (deltaY == -1) {
                        // Check bottom side of the comparing tile and the top side of the new tile
                        for (var i = 0; i < TILE_WIDTH; i++) {
                            if (comparingTileImage[i][TILE_HEIGHT - 1] != tileImage[i][0]) {
                                continue nextTileImage;
                            }
                        }
                    } else if (deltaY == 1) {
                        // Check top side of the comparing tile and the bottom side of the new tile
                        for (var i = 0; i < TILE_WIDTH; i++) {
                            if (comparingTileImage[i][0] != tileImage[i][TILE_HEIGHT - 1]) {
                                continue nextTileImage;
                            }
                        }
                    }
                    // This tile image is correct
                    bounds = placeTile(tilesLeft, bounds, tilePlacement, checkPositions, tile, tileImageIndex, position.getValue0(), position.getValue1());
                    continue overallLoop;
                }
            }
        }

        LogUtilities.log("Bounds- x: " + bounds.getValue0().getValue0() + " to " + bounds.getValue0().getValue1() + ", y: "
                + bounds.getValue1().getValue0() + " to " + bounds.getValue1().getValue1());
        var edgeTiles = DataUtilities.List(
                tilePlacement.get(Pair.with(bounds.getValue0().getValue0(), bounds.getValue1().getValue0())),
                tilePlacement.get(Pair.with(bounds.getValue0().getValue0(), bounds.getValue1().getValue1())),
                tilePlacement.get(Pair.with(bounds.getValue0().getValue1(), bounds.getValue1().getValue0())),
                tilePlacement.get(Pair.with(bounds.getValue0().getValue1(), bounds.getValue1().getValue1()))
        );
        long product = 1;
        for (var tile : edgeTiles) {
            product *= tile.getValue0().index;
        }
        LogUtilities.log("Part 1: " + product);

        // In this exercise, we need to combine the images into a single image and look for the sea monster
        var rawSeaMonster = DataUtilities.List("                  # ", "#    ##    ##    ###", " #  #  #  #  #  #   ");
        var seaCreatureWidth = rawSeaMonster.get(0).length();
        var seaCreatueHeight = rawSeaMonster.size();
        var seaMonsterImage = new Boolean[seaCreatureWidth][seaCreatueHeight];
        for (var y = 0; y < seaCreatueHeight; y++) {
            for (var x = 0; x < seaCreatureWidth; x++) {
                seaMonsterImage[x][y] = rawSeaMonster.get(y).charAt(x) == '#';
            }
        }

        // Now create the final image
        var boundsMinX = bounds.getValue0().getValue0();
        var boundsMaxX = bounds.getValue0().getValue1();
        var boundsMinY = bounds.getValue1().getValue0();
        var boundsMaxY = bounds.getValue1().getValue1();
        var tilesWidth = boundsMaxX - boundsMinX + 1;
        var tilesHeight = boundsMaxY - boundsMinY + 1;
        // Calculate the width/height of the new image
        var combinedImageWidth = tilesWidth * (TILE_WIDTH - 2);
        var combinedImageHeight = tilesHeight * (TILE_HEIGHT - 2);
        var combinedImage = new Boolean[combinedImageWidth][combinedImageHeight];
        var debugImageWidth = tilesWidth * (TILE_WIDTH);
        var debugImageHeight = tilesHeight * (TILE_HEIGHT);
        var debugImage = new Boolean[debugImageWidth][debugImageHeight];

        for (var x = 0; x < combinedImageWidth; x++) {
            for (var y = 0; y < combinedImageHeight; y++) {
                combinedImage[x][y] = false;
            }
        }
        for (var x = 0; x < debugImageWidth; x++) {
            for (var y = 0; y < debugImageHeight; y++) {
                debugImage[x][y] = false;
            }
        }

        // print tile order
        // LogUtilities.log("Tile order: ");
        for (var tileY = boundsMinY; tileY <= boundsMaxY; tileY++) {
            var line = "";
            for (var tileX = boundsMinX; tileX <= boundsMaxX; tileX++) {
                var tilePair = tilePlacement.get(Pair.with(tileX, tileY));
                var tile = tilePair.getValue0();
                line += tile.index + "     ";
            }
            // LogUtilities.log(line);
        }

        // Create new combined image
        var countX = 0;
        for (var tileX = boundsMinX; tileX <= boundsMaxX; tileX++) {
            var countY = 0;
            for (var tileY = boundsMinY; tileY <= boundsMaxY; tileY++) {
                var tilePair = tilePlacement.get(Pair.with(tileX, tileY));
                var tile = tilePair.getValue0();
                var imageIndex = tilePair.getValue1();
                var image = tile.comparisonImages.get(imageIndex);
                // LogUtilities.log("Putting in image index: " + tile.index);
                for (var x = 1; x < TILE_WIDTH - 1; x++) {
                    var newX = countX * (TILE_WIDTH - 2) + (x - 1);
                    for (var y = 1; y < TILE_HEIGHT - 1; y++) {
                        var newY = countY * (TILE_HEIGHT - 2) + (y - 1);
                        combinedImage[newX][newY] = image[x][y];
                    }
                }
                for (var x = 0; x < TILE_WIDTH; x++) {
                    var newX = countX * (TILE_WIDTH) + x;
                    for (var y = 0; y < TILE_HEIGHT; y++) {
                        var newY = countY * (TILE_HEIGHT) + y;
                        debugImage[newX][newY] = image[x][y];
                    }
                }
                countY += 1;
            }
            countX += 1;
        }

        var combinedComparisonImages = new ArrayList<Boolean[][]>();
        var currentOriginal = combinedImage;
        var flippedHorizontal = flipImageHorizontal(currentOriginal, combinedImageWidth, combinedImageHeight);
        combinedComparisonImages.add(currentOriginal);
        combinedComparisonImages.add(flippedHorizontal);
        for (var i = 0; i < 3; i++) {
            currentOriginal = rotateImage90(currentOriginal, combinedImageWidth, combinedImageHeight);
            flippedHorizontal = rotateImage90(flippedHorizontal, combinedImageWidth, combinedImageHeight);
            combinedComparisonImages.add(currentOriginal);
            combinedComparisonImages.add(flippedHorizontal);
        }

        // LogUtilities.log("Debug image: ");
        // printImage(debugImage, debugImageWidth, debugImageHeight);

        // LogUtilities.log("Sea monster image: ");
        // printImage(seaMonsterImage, seaCreatureWidth, seaCreatueHeight);
        // Look for sea creatures and count their parts that are not sea creature
        for (var comparisonImage : combinedComparisonImages) {
            var seaMonsterParts = 0;
            // Loop over every part of the image the sea creature can be in
            for (var x = 0; x < combinedImageWidth - seaCreatureWidth; x++) {
                yLoop:
                for (var y = 0; y < combinedImageHeight - seaCreatueHeight; y++) {
                    var parts = 0;
                    for (var x2 = 0; x2 < seaCreatureWidth; x2++) {
                        for (var y2 = 0; y2 < seaCreatueHeight; y2++) {
                            if (seaMonsterImage[x2][y2]) {
                                parts += 1;
                            }
                            if (!comparisonImage[x + x2][y + y2] && seaMonsterImage[x2][y2]) {
                                continue yLoop;
                            }
                        }
                    }
                    // Found sea creature
                    seaMonsterParts += parts;
                }
            }
            if (seaMonsterParts > 0) {
                LogUtilities.log("Found sea monster parts: " + seaMonsterParts);
                var totalTrue = countTrue(comparisonImage,  combinedImageWidth, combinedImageHeight);
                LogUtilities.log("Rough waters that are not sea monster: " + (totalTrue - seaMonsterParts));
            }
        }
    }

    static int countTrue(Boolean[][] image, int width, int height) {
        var count = 0;
        for (var y = 0; y < height; y++) {
            for (var x = 0; x < width; x++) {
                if (image[x][y]) {
                    count += 1;
                }
            }
        }
        return count;
    }

    static void printImage(Boolean[][] image, int width, int height) {
        LogUtilities.log("");
        LogUtilities.log("Printing Image: ");
        for (var y = 0; y < height; y++) {
            var line = "";
            for (var x = 0; x < width; x++) {
                line += image[x][y] ? "#" : ".";
            }
            LogUtilities.log(line);
        }
    }

    // Tile, Delta, Delta
    static Triplet<Pair<Tile, Integer>, Integer, Integer> getTileNearPosition(Map<Pair<Integer, Integer>, Pair<Tile, Integer>> tilePlacement, int x, int y) {
        var positions = DataUtilities.List(Pair.with(x - 1, y), Pair.with(x + 1, y), Pair.with(x, y - 1), Pair.with(x, y + 1));
        for (var position : positions) {
            if (tilePlacement.containsKey(position)) {
                return Triplet.with(tilePlacement.get(position), position.getValue0() - x, position.getValue1() - y);
            }
        }
        return null; // This will cause an exception
    }

    // Returns bounds
    static Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> placeTile(
            List<Tile> tilesLeft,
            Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> bounds,
            Map<Pair<Integer, Integer>, Pair<Tile, Integer>> tilePlacement,
            ArrayList<Pair<Integer, Integer>> checkPositions,
            Tile tileToPlace,
            Integer tileImageIndex,
            int x,
            int y) {
        tilesLeft.remove(tileToPlace);
        tilePlacement.put(Pair.with(x, y), Pair.with(tileToPlace, tileImageIndex));
        checkPositions.remove(Pair.with(x, y));
        // Add new positions to check
        var positions = DataUtilities.List(Pair.with(x - 1, y), Pair.with(x + 1, y), Pair.with(x, y - 1), Pair.with(x, y + 1));
        for (var position : positions) {
            if (!tilePlacement.containsKey(position)) {
                checkPositions.add(position);
            }
        }
        return Pair.with(
                Pair.with(Math.min(bounds.getValue0().getValue0(), x), Math.max(bounds.getValue0().getValue1(), x)),
                Pair.with(Math.min(bounds.getValue1().getValue0(), y), Math.max(bounds.getValue1().getValue1(), y)));
    }

    public static int TILE_WIDTH = 10;
    public static int TILE_HEIGHT = 10;

    static class Tile {
        public long index;
        public Boolean[][] image;
        public List<Boolean[][]> comparisonImages = new ArrayList<>();

        Tile(String rawTile) {
            var split = StringUtilities.splitStringIntoList(rawTile, ":\n");
            index = Integer.parseInt(StringUtilities.removeStartChunk(split.get(0), "Tile "));
            var split2 = StringUtilities.splitStringIntoList(split.get(1), "\n");
            image = new Boolean[TILE_WIDTH][TILE_HEIGHT];
            var y = 0;
            for (var line : split2) {
                var x = 0;
                for (var character : line.toCharArray()) {
                    if (character == '#') {
                        image[x][y] = true;
                    } else {
                        image[x][y] = false;
                    }
                    x += 1;
                }
                y += 1;
            }
            // Add original, rotate, rotate, rotate, flip horizontally, rotate, rotate, rotate
            var currentOriginal = image;
            var flippedHorizontal = flipImageHorizontal(currentOriginal, TILE_WIDTH, TILE_HEIGHT);
            comparisonImages.add(currentOriginal);
            comparisonImages.add(flippedHorizontal);
            for (var i = 0; i < 3; i++) {
                currentOriginal = rotateImage90(currentOriginal, TILE_WIDTH, TILE_HEIGHT);
                flippedHorizontal = rotateImage90(flippedHorizontal, TILE_WIDTH, TILE_HEIGHT);
                comparisonImages.add(currentOriginal);
                comparisonImages.add(flippedHorizontal);
            }
        }
    }

    static Boolean[][] rotateImage90(Boolean[][] image, int width, int height) {
        var newImage = new Boolean[width][height];
        for (var x = 0; x < width; x++) {
            for (var y = 0; y < height; y++) {
                newImage[x][y] = image[y][height - 1 - x];
            }
        }
        return newImage;
    }

    static Boolean[][] flipImageHorizontal(Boolean[][] image, int width, int height) {
        var newImage = new Boolean[width][height];
        for (var x = 0; x < width; x++) {
            for (var y = 0; y < height; y++) {
                newImage[x][y] = image[width - 1 - x][y];
            }
        }
        return newImage;
    }
}