package Advent.of.Code.Java.Year_2023;

import Advent.of.Code.Java.Utility.DataUtilities;
import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.Coordinate3D;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day_22 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2023/22/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        runSolution1(fileName);
        runSolution2(fileName);
    }

    class BrickPiece {
        Brick parentBrick;
        Coordinate3D<Long> position;
        public BrickPiece(final Brick parentBrick, final Coordinate3D<Long> position) {
            this.parentBrick = parentBrick;
            this.position = new Coordinate3D<>(position.getX(), position.getY(), position.getZ());
        }
    }

    class Brick {
        String name;
        List<BrickPiece> pieces = new ArrayList<>();

        public BrickPiece addPiece(final Coordinate3D<Long> coordinate) {
            final BrickPiece brickPiece = new BrickPiece(this, coordinate);
            pieces.add(brickPiece);
            return brickPiece;
        }
        public Brick copy() {
            final Brick newBrick = new Brick();
            newBrick.name = name;
            for (final BrickPiece brickPiece : pieces) {
                newBrick.addPiece(brickPiece.position);
            }
            return newBrick;
        }
    }

    class WorldSimulation {
        static long Z_FLOOR = 0;
        List<Brick> bricks = new ArrayList<>();
        Map<Long, Set<BrickPiece>> brickZPieces = new HashMap<>();

        public void addBrick(final String name, final Coordinate3D<Long> brickStart, final Coordinate3D<Long> brickEnd) {
            final Brick brick = new Brick();
            brick.name = name;
            for (long x = brickStart.getX(); x <= brickEnd.getX(); x++) {
                for (long y = brickStart.getY(); y <= brickEnd.getY(); y++) {
                    for (long z = brickStart.getZ(); z <= brickEnd.getZ(); z++) {
                        final BrickPiece brickPiece = brick.addPiece(new Coordinate3D<>(x, y, z));
                        addBrickPiece(brickPiece);
                    }
                }
            }
            bricks.add(brick);
        }
        private void addBrick(final Brick brick) {
            bricks.add(brick);
            for (final BrickPiece brickPiece : brick.pieces) {
                addBrickPiece(brickPiece);
            }
        }
        private void removeBrick(final Brick brick) {
            bricks.remove(brick);
            for (final BrickPiece brickPiece : brick.pieces) {
                removeBrickPiece(brickPiece);
            }
        }
        private void removeBrickPiece(final BrickPiece brickPiece) {
            if (brickZPieces.containsKey(brickPiece.position.getZ())) {
                final Set<BrickPiece> brickPieceSet = brickZPieces.get(brickPiece.position.getZ());
                brickPieceSet.remove(brickPiece);
                if (brickPieceSet.isEmpty()) {
                    brickZPieces.remove(brickPiece.position.getZ());
                }
            }
        }
        private void addBrickPiece(final BrickPiece brickPiece) {
            brickZPieces.computeIfAbsent(brickPiece.position.getZ(), aLong -> new HashSet<>()).add(brickPiece);
        }

        public long settleBricks() {
            boolean movingPieces = true;
            final Set<Brick> movedBricks = new HashSet<>();
            while (movingPieces) {
                movingPieces = false;
                for (final Brick brick : bricks) {
                    if (!isBrickResting(brick)) {
                        moveBrickDown(brick);
                        movingPieces = true;
                        movedBricks.add(brick);
                    }
                }
            }
            return movedBricks.size();
        }
        private boolean areBricksSettled() {
            for (final Brick brick : bricks) {
                if (!isBrickResting(brick)) {
                    return false;
                }
            }
            return true;
        }
        private boolean isBrickResting(final Brick brick) {
            for (final BrickPiece brickPiece : brick.pieces) {
                final Coordinate3D<Long> targetPosition = new Coordinate3D<>(brickPiece.position.getX(), brickPiece.position.getY(), brickPiece.position.getZ() - 1);
                if (targetPosition.getZ() <= Z_FLOOR) {
                    return true;
                }
                if (brickZPieces.containsKey(targetPosition.getZ())) {
                    for (final BrickPiece potentialTouchingBrickPiece : brickZPieces.get(targetPosition.getZ())) {
                        if (potentialTouchingBrickPiece.parentBrick != brick && potentialTouchingBrickPiece.position.equals(targetPosition)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        private void moveBrickDown(final Brick brick) {
            for (final BrickPiece brickPiece : brick.pieces) {
                removeBrickPiece(brickPiece);
                brickPiece.position.setZ(brickPiece.position.getZ() - 1);
                addBrickPiece(brickPiece);
            }
        }
        public long getNumberOfRemovableBricks() {
            final List<Brick> originalBrickList = new ArrayList<>(bricks);
            long numberOfRemovableBricks = 0;
            for (final Brick brick : originalBrickList) {
                removeBrick(brick);
                if (areBricksSettled()) {
                    numberOfRemovableBricks += 1;
                }
                addBrick(brick);
            }
            return numberOfRemovableBricks;
        }
        public WorldSimulation copy() {
            final WorldSimulation newWorldSimulation = new WorldSimulation();
            for (final Brick brick : bricks) {
                newWorldSimulation.addBrick(brick.copy());
            }
            return newWorldSimulation;
        }
    }

    private void runSolution1(final String fileName) throws Exception {
        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);
        final WorldSimulation worldSimulation = new WorldSimulation();
        for (final String line : input) {
            final List<String> split = StringUtilities.splitStringIntoList(line, "~");
            final List<Long> startNumbers = DataUtilities.transformData(StringUtilities.splitStringIntoList(split.get(0), ","), Long::parseLong);
            final List<Long> endNumbers = DataUtilities.transformData(StringUtilities.splitStringIntoList(split.get(1), ","), Long::parseLong);
            final Coordinate3D<Long> startCoordinate = new Coordinate3D<>(startNumbers.get(0), startNumbers.get(1), startNumbers.get(2));
            final Coordinate3D<Long> endCoordinate = new Coordinate3D<>(endNumbers.get(0), endNumbers.get(1), endNumbers.get(2));
            worldSimulation.addBrick(Character.toString(worldSimulation.bricks.size() + 'A'), startCoordinate, endCoordinate);
        }
        worldSimulation.settleBricks();
        LogUtilities.logGreen("Solution 1: " + worldSimulation.getNumberOfRemovableBricks());
    }

    private void runSolution2(final String fileName) throws Exception {
        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);
        final WorldSimulation worldSimulation = new WorldSimulation();
        for (final String line : input) {
            final List<String> split = StringUtilities.splitStringIntoList(line, "~");
            final List<Long> startNumbers = DataUtilities.transformData(StringUtilities.splitStringIntoList(split.get(0), ","), Long::parseLong);
            final List<Long> endNumbers = DataUtilities.transformData(StringUtilities.splitStringIntoList(split.get(1), ","), Long::parseLong);
            final Coordinate3D<Long> startCoordinate = new Coordinate3D<>(startNumbers.get(0), startNumbers.get(1), startNumbers.get(2));
            final Coordinate3D<Long> endCoordinate = new Coordinate3D<>(endNumbers.get(0), endNumbers.get(1), endNumbers.get(2));
            worldSimulation.addBrick(Character.toString(worldSimulation.bricks.size() + 'A'), startCoordinate, endCoordinate);
        }
        worldSimulation.settleBricks();
        long totalFelledBricks = 0;
        for (int index = 0; index < worldSimulation.bricks.size(); index++) {
            final WorldSimulation newWorldSimulation = worldSimulation.copy();
            newWorldSimulation.removeBrick(newWorldSimulation.bricks.get(index));
            totalFelledBricks += newWorldSimulation.settleBricks();
        }
        LogUtilities.logGreen("Solution 2: " + totalFelledBricks);
    }
}
