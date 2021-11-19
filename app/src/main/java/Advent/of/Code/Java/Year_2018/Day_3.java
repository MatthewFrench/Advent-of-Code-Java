package Advent.of.Code.Java.Year_2018;

import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.Structures.Day;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Day_3 implements Day {
    public void run() throws Exception {
        //Part 1
        final String originalInput = LoadUtilities.loadTextFileAsString("input/2018/3/input.txt");
        String[] splitInput = originalInput.split("\n");

        //"#1258 @ 544,52: 19x29\n"

        Map<Integer, Map<Integer, HashSet<String>>> grid = new HashMap<Integer, Map<Integer, HashSet<String>>>();

        HashSet<String> ids = new HashSet<String>();

        for (String piece : splitInput) {
            String id = piece.substring(0, piece.indexOf(" "));
            ids.add(id);
            piece = piece.substring(id.length()+1+1+1);
            String xString = piece.substring(0, piece.indexOf(","));
            piece = piece.substring(xString.length()+1);
            String yString = piece.substring(0, piece.indexOf(":"));
            piece = piece.substring(yString.length()+1+1);
            String widthString = piece.substring(0, piece.indexOf("x"));
            piece = piece.substring(widthString.length()+1);
            String heightString = piece;

            int xCoord = Integer.parseInt(xString);
            int yCoord = Integer.parseInt(yString);
            int width = Integer.parseInt(widthString);
            int height = Integer.parseInt(heightString);
            for (int x = xCoord; x < xCoord + width; x++) {
                for (int y = yCoord; y < yCoord + height; y++) {
                    if (!grid.containsKey(x)) {
                        grid.put(x, new HashMap<>());
                    }
                    Map<Integer, HashSet<String>> yMap = grid.get(x);
                    if (!yMap.containsKey(y)) {
                        yMap.put(y, new HashSet<String>());
                    }
                    yMap.get(y).add(id);
                }
            }
        }

        int squaresWith2OrMore = 0;

        for (Map.Entry<Integer, Map<Integer, HashSet<String>>> e : grid.entrySet()) {
            int x = e.getKey();
            Map<Integer, HashSet<String>> yMap = e.getValue();
            for (Map.Entry<Integer, HashSet<String>> e2 : yMap.entrySet()) {
                int y = e2.getKey();
                int overlap = e2.getValue().size();
                if (overlap >= 2) {
                    squaresWith2OrMore += 1;
                    for (String id : e2.getValue()) {
                        ids.remove(id);
                    }
                }
            }
        }

        LogUtilities.log("Squares with 2 or more: " + squaresWith2OrMore);

        LogUtilities.log("Non-overlapping IDs: " + String.join(",", ids));
    }
}