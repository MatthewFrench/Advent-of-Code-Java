package Advent.of.Code.Java.Year_2022;

import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;
import lombok.Data;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Day_12 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2022/12/", this);
    }

    @Data
    class Tile {
        final int elevation;
        final int x;
        final int y;
        int fillValue = -1;
    }

    public void executeWithInput(final String fileName) throws Exception {
        final List<String> inputRows = StringUtilities.splitStringIntoList(LoadUtilities.loadTextFileAsString(fileName), "\n");
        final int width = inputRows.get(0).length();
        final int height = inputRows.size();
        final Tile[][] tiles = new Tile[width][height];
        int startingX=0;
        int startingY=0;
        int endingX=0;
        int endingY=0;
        List<Tile> tilesWithA = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            final List<String> row = StringUtilities.splitStringIntoList(inputRows.get(y), "");
            for (int x = 0; x < width; x++) {
                String tileValue = row.get(x);
                int fillValue = -1;
                if (tileValue.equals("S")) {
                    tileValue = "a";
                    startingX = x;
                    startingY = y;
                    fillValue = 0;
                } else if (tileValue.equals("E")) {
                    tileValue = "z";
                    endingX = x;
                    endingY = y;
                }
                final Tile tile = new Tile(tileValue.charAt(0) - 'a', x, y);
                if (tile.elevation == 0) {
                    tilesWithA.add(tile);
                }
                tile.fillValue = fillValue;
                tiles[x][y] = tile;
            }
        }

        LogUtilities.logGreen("Solution: " + getFewestSteps(tiles, startingX, startingY, endingX, endingY, width, height));

        int lowestStepA = -1;
        for (final Tile tile : tilesWithA) {
            int stepCount = getFewestSteps(tiles, tile.x, tile.y, endingX, endingY, width, height);
            if ((lowestStepA == -1 || stepCount < lowestStepA) && stepCount != -1) {
                lowestStepA = stepCount;
            }
        }

        LogUtilities.logGreen("Solution 2: " + lowestStepA);

    }

    public int getFewestSteps(final Tile[][] tiles, final int startingX, final int startingY, final int endingX, final int endingY, final int width, final int height) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y].fillValue = -1;
            }
        }
        tiles[startingX][startingY].fillValue = 0;
        // Clear the board
        List<Tile> emittingTiles = new ArrayList<>();
        emittingTiles.add(tiles[startingX][startingY]);
        // Flood fill the smallest path from start to end
        while (emittingTiles.size() > 0) {
            final Tile pullTile = emittingTiles.remove(0);
            final Tile topTile = getTile(pullTile.x, pullTile.y+1, width, height, tiles);
            final Tile bottomTile = getTile(pullTile.x, pullTile.y-1, width, height, tiles);
            final Tile leftTile = getTile(pullTile.x-1, pullTile.y, width, height, tiles);
            final Tile rightTile = getTile(pullTile.x+1, pullTile.y, width, height, tiles);
            fillTile(pullTile, topTile, emittingTiles);
            fillTile(pullTile, bottomTile, emittingTiles);
            fillTile(pullTile, leftTile, emittingTiles);
            fillTile(pullTile, rightTile, emittingTiles);
        }
        return tiles[endingX][endingY].fillValue;
    }

    public Tile getTile(final int x, final int y, final int width, final int height, final Tile[][] tiles) {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            return null;
        } else {
            return tiles[x][y];
        }
    }

    public void fillTile(final Tile pullTile, final Tile targetTile, final List<Tile> emittingTiles) {
        if (targetTile != null && (targetTile.fillValue == -1 || targetTile.fillValue > pullTile.fillValue+1) && targetTile.elevation <= pullTile.elevation+1) {
            targetTile.fillValue = pullTile.fillValue+1;
            emittingTiles.add(targetTile);
        }
    }
}
