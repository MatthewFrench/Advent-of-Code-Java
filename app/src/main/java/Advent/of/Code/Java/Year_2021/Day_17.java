package Advent.of.Code.Java.Year_2021;

import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class Day_17 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2021/17/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        final String input = StringUtilities.removeStartChunk(LoadUtilities.loadTextFileAsString(fileName), "target area: x=");
        final List<String> targetRect = StringUtilities.splitStringIntoList(input, ", y=");
        final List<String> positionsX = StringUtilities.splitStringIntoList(targetRect.get(0), "..");
        final List<String> positionsY = StringUtilities.splitStringIntoList(targetRect.get(1), "..");
        int x1 = Integer.parseInt(positionsX.get(0));
        int y1 = Integer.parseInt(positionsY.get(0));
        int x2 = Integer.parseInt(positionsX.get(1));
        int y2 = Integer.parseInt(positionsY.get(1));
        int leftX = Math.min(x1, x2);
        int rightX = Math.max(x1, x2);
        int downY = Math.min(y1, y2);
        int upY = Math.max(y1, y2);
        int centerX = (rightX - leftX) / 2 + leftX;
        int centerY = (upY - downY) / 2 + downY;

        LogUtilities.logBlue("Target: " + centerX + ", " + centerY);
        LogUtilities.logBlue("Target Rect: " + leftX + ", " + downY + " to " + rightX + ", " + upY);

        int minXVelocity = Math.min(leftX, 0);
        int maxXVelocity = Math.max(rightX, 0)+1;
        int minYVelocity = Math.min(downY, 0);
        int maxYVelocity = Math.abs(centerY) * 100;

        Result[][] resultGrid = new Result[maxXVelocity - minXVelocity][maxYVelocity - minYVelocity];
        List<Result> hitResults = new ArrayList<>();

        Result bestResult = null;
        LogUtilities.logBlue("Simulating range X: " + minXVelocity + " to " + maxXVelocity);
        LogUtilities.logBlue("Simulating range Y: " + minYVelocity + " to " + maxYVelocity);
        int hits = 0;
        for (int xVelocity = minXVelocity; xVelocity < maxXVelocity; xVelocity++) {
            for (int yVelocity = minYVelocity; yVelocity < maxYVelocity; yVelocity++) {
                final Result newResult = runSimulation(xVelocity, yVelocity, leftX, rightX, upY, downY, centerX, centerY);
                resultGrid[xVelocity - minXVelocity][yVelocity - minYVelocity] = newResult;
                if (bestResult == null) {
                    bestResult = newResult;
                }
                if (!bestResult.hit && newResult.hit) {
                    bestResult = newResult;
                }
                if (!bestResult.hit && !newResult.hit) {
                    if (newResult.closestDistance < bestResult.closestDistance) {
                        bestResult = newResult;
                    }
                }
                if (bestResult.hit && newResult.hit) {
                    if (newResult.highestY > bestResult.highestY) {
                        bestResult = newResult;
                    }
                }
                if (newResult.hit) {
                    hits += 1;
                    hitResults.add(newResult);
                }
                // 3135 is too high
                // 2939 is too low
            }
        }

        LogUtilities.logGreen("Solution: " + StringUtilities.objectToString(bestResult));
        LogUtilities.logGreen("Hits: " + hits);
    }

    Result runSimulation(int xVelocity, int yVelocity, int leftX, int rightX, int upY, int downY, int centerX, int centerY) {
        final Probe probe = new Probe(0, 0, xVelocity, yVelocity);
        final Result result = new Result();
        result.startingVelocityX = probe.velocityX;
        result.startingVelocityY = probe.velocityY;
        while (true) {
            probe.x += probe.velocityX;
            probe.y += probe.velocityY;
            if (probe.velocityX > 0) {
                probe.velocityX -= 1;
            } else if (probe.velocityX < 0) {
                probe.velocityX += 1;
            }
            probe.velocityY -= 1;

            result.step += 1;
            double newPastDistance = Math.hypot(centerX - probe.x, centerY - probe.y);
            if (newPastDistance < result.closestDistance) {
                result.closestDistance = newPastDistance;
                result.closestX = probe.x;
                result.closestY = probe.y;
            }
            result.highestY = Math.max(probe.y, result.highestY);
            if (probe.x >= leftX && probe.x <= rightX && probe.y <= upY && probe.y >= downY) {
                result.hit = true;
                result.hitX = probe.x;
                result.hitY = probe.y;
                break;
            }
            if ((probe.velocityX > 0 && probe.x > rightX) || (probe.velocityX < 0 && probe.x < leftX)) {
                break;
            }
            if (probe.velocityY < 0 && probe.y < downY) {
                break;
            }
        }
        return result;
    }

    @AllArgsConstructor
    static class Probe {
        int x;
        int y;
        int velocityX;
        int velocityY;
    }

    @Data
    static class Result {
        int closestX = Integer.MAX_VALUE;
        int closestY = Integer.MAX_VALUE;
        double closestDistance = Double.MAX_VALUE;
        boolean hit = false;
        int hitX = 0;
        int hitY = 0;
        int step = 0;
        int startingVelocityX = 0;
        int startingVelocityY = 0;
        int highestY = Integer.MIN_VALUE;
    }
}
