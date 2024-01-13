package Advent.of.Code.Java.Year_2023;

import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.Coordinate2D;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class Day_23 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2023/23/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        runSolution1(fileName);
        runSolution2(fileName);
    }
    enum LandType {
        EMPTY,
        BLOCKED,
        DOWN_SLOPE,
        UP_SLOPE,
        LEFT_SLOPE,
        RIGHT_SLOPE
    }
    class WalkPath {
        Coordinate2D<Integer> position;
        long totalSteps = 0;
        final HashSet<Coordinate2D<Integer>> pastPositions = new HashSet<>();
        public WalkPath(final Coordinate2D<Integer> position) {
            this.position = position;
            pastPositions.add(position);
        }
        public WalkPath copy() {
            final WalkPath newWalkPath = new WalkPath(new Coordinate2D<>(position.getX(), position.getY()));
            newWalkPath.totalSteps = totalSteps;
            newWalkPath.pastPositions.addAll(pastPositions);
            return newWalkPath;
        }
    }
    class GridMap {
        final LandType[][] grid;
        final int width;
        final int height;
        public GridMap(final String input) {
            final List<String> rows = StringUtilities.splitStringIntoList(input, "\n");
            width = rows.getFirst().length();
            height = rows.size();
            grid = new LandType[width][height];
            for (int y = 0; y < height; y++) {
                final String row = rows.get(y);
                for (int x = 0; x < width; x++) {
                    final char character = row.charAt(x);
                    LandType landType = LandType.EMPTY;
                    if (character == '#') {
                        landType = LandType.BLOCKED;
                    } else if (character == '>') {
                        landType = LandType.RIGHT_SLOPE;
                    } else if (character == '<') {
                        landType = LandType.LEFT_SLOPE;
                    } else if (character == '^') {
                        landType = LandType.UP_SLOPE;
                    } else if (character == 'v') {
                        landType = LandType.DOWN_SLOPE;
                    }
                    grid[x][y] = landType;
                }
            }
        }
        public long getLongestPath(final Coordinate2D<Integer> startingPosition, final Coordinate2D<Integer> endingPosition, boolean ignoreSlopes) {
            final PriorityQueue<WalkPath> walkingPaths = new PriorityQueue<>(Comparator.comparingLong(value -> -value.totalSteps));
            walkingPaths.add(new WalkPath(startingPosition));
            long longestPath = 0;
            walkingPathLoop:
            while (!walkingPaths.isEmpty()) {
                final WalkPath walkPath = walkingPaths.poll();
                boolean checkSinglePath = true;
                while (checkSinglePath) {
                    int x = walkPath.position.getX();
                    int y = walkPath.position.getY();
                    if (x == endingPosition.getX() && y == endingPosition.getY()) {
                        if (walkPath.totalSteps > longestPath) {
                            longestPath = walkPath.totalSteps;
                            LogUtilities.logPurple("Next longest path: " + longestPath);
                        }
                        continue walkingPathLoop;
                    }
                    int directions = 0;
                    boolean canWalkRight = canWalk(x + 1, y, walkPath, ignoreSlopes);
                    if (canWalkRight) {
                        directions += 1;
                    }
                    boolean canWalkLeft = canWalk(x - 1, y, walkPath, ignoreSlopes);
                    if (canWalkLeft) {
                        directions += 1;
                    }
                    boolean canWalkUp = canWalk(x, y - 1, walkPath, ignoreSlopes);
                    if (canWalkUp) {
                        directions += 1;
                    }
                    boolean canWalkDown = canWalk(x, y + 1, walkPath, ignoreSlopes);
                    if (canWalkDown) {
                        directions += 1;
                    }
                    if (directions == 0) {
                        continue walkingPathLoop;
                    }
                    if (directions == 1) {
                        walkPath.totalSteps += 1;
                        if (canWalkLeft) {
                            walkPath.position.setX(x - 1);
                        } else if (canWalkRight) {
                            walkPath.position.setX(x + 1);
                        } else if (canWalkUp) {
                            walkPath.position.setY(y - 1);
                        } else if (canWalkDown) {
                            walkPath.position.setY(y + 1);
                        }
                        walkPath.pastPositions.add(new Coordinate2D<>(walkPath.position.getX(), walkPath.position.getY()));
                        continue;
                    }
                    checkSinglePath = false;
                    if (canWalkLeft) {
                        final WalkPath newWalkPath = walkPath.copy();
                        newWalkPath.position = new Coordinate2D<>(x - 1, y);
                        newWalkPath.pastPositions.add(new Coordinate2D<>(newWalkPath.position.getX(), newWalkPath.position.getY()));
                        newWalkPath.totalSteps += 1;
                        walkingPaths.add(newWalkPath);
                    }
                    if (canWalkRight) {
                        final WalkPath newWalkPath = walkPath.copy();
                        newWalkPath.position = new Coordinate2D<>(x + 1, y);
                        newWalkPath.pastPositions.add(new Coordinate2D<>(newWalkPath.position.getX(), newWalkPath.position.getY()));
                        newWalkPath.totalSteps += 1;
                        walkingPaths.add(newWalkPath);
                    }
                    if (canWalkDown) {
                        final WalkPath newWalkPath = walkPath.copy();
                        newWalkPath.position = new Coordinate2D<>(x, y + 1);
                        newWalkPath.pastPositions.add(new Coordinate2D<>(newWalkPath.position.getX(), newWalkPath.position.getY()));
                        newWalkPath.totalSteps += 1;
                        walkingPaths.add(newWalkPath);
                    }
                    if (canWalkUp) {
                        final WalkPath newWalkPath = walkPath.copy();
                        newWalkPath.position = new Coordinate2D<>(x, y - 1);
                        newWalkPath.pastPositions.add(new Coordinate2D<>(newWalkPath.position.getX(), newWalkPath.position.getY()));
                        newWalkPath.totalSteps += 1;
                        walkingPaths.add(newWalkPath);
                    }
                }
            }
            return longestPath;
        }
        private boolean canWalk(final int x, final int y, final WalkPath walkPath, final boolean ignoreSlopes) {
            if (x < 0 || x > width - 1 || y < 0 || y > height - 1 || grid[x][y] == LandType.BLOCKED) {
                return false;
            }
            if (!ignoreSlopes) {
                final LandType currentLandType = grid[walkPath.position.getX()][walkPath.position.getY()];
                if (currentLandType != LandType.EMPTY) {
                    if (currentLandType == LandType.UP_SLOPE && y >= walkPath.position.getY()) {
                        return false;
                    }
                    if (currentLandType == LandType.DOWN_SLOPE && y <= walkPath.position.getY()) {
                        return false;
                    }
                    if (currentLandType == LandType.LEFT_SLOPE && x >= walkPath.position.getX()) {
                        return false;
                    }
                    if (currentLandType == LandType.RIGHT_SLOPE && x <= walkPath.position.getX()) {
                        return false;
                    }
                }
            }
            if (walkPath.pastPositions.contains(new Coordinate2D<>(x, y))) {
                return false;
            }
            return true;
        }
    }

    class GridEdge {
        List<GridNode> connectedNodes = new ArrayList<>();
        long distance;
    }
    class GridNode {
        List<GridEdge> edges = new ArrayList<>();
        int x;
        int y;
    }
    class GridNodeMap {
        final HashMap<Coordinate2D<Integer>, GridNode> nodes = new HashMap<>();
        final int width;
        final int height;
        public GridNodeMap(final String input) {
            final List<String> rows = StringUtilities.splitStringIntoList(input, "\n");
            width = rows.getFirst().length();
            height = rows.size();
            final HashMap<Coordinate2D<Integer>, GridNode> nodes = new HashMap<>();
            for (int y = 0; y < height; y++) {
                final String row = rows.get(y);
                for (int x = 0; x < width; x++) {
                    final char character = row.charAt(x);
                    if (character != '#') {
                        final GridNode gridNode = new GridNode();
                        gridNode.x = x;
                        gridNode.y = y;
                        nodes.put(new Coordinate2D<>(x, y), gridNode);
                    }
                }
            }
            // Connect all nodes
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    final Coordinate2D<Integer> position = new Coordinate2D<>(x, y);
                    if (nodes.containsKey(position)) {
                        final GridNode gridNode = nodes.get(position);
                        final Coordinate2D<Integer> rightPosition = new Coordinate2D<>(x + 1, y);
                        final Coordinate2D<Integer> downPosition = new Coordinate2D<>(x, y + 1);
                        if (nodes.containsKey(rightPosition)) {
                            final GridNode rightNode = nodes.get(rightPosition);
                            final GridEdge gridEdge = new GridEdge();
                            gridEdge.distance = 1;
                            gridEdge.connectedNodes.add(gridNode);
                            gridEdge.connectedNodes.add(rightNode);
                            gridNode.edges.add(gridEdge);
                            rightNode.edges.add(gridEdge);
                        }
                        if (nodes.containsKey(downPosition)) {
                            final GridNode downNode = nodes.get(downPosition);
                            final GridEdge gridEdge = new GridEdge();
                            gridEdge.distance = 1;
                            gridEdge.connectedNodes.add(gridNode);
                            gridEdge.connectedNodes.add(downNode);
                            gridNode.edges.add(gridEdge);
                            downNode.edges.add(gridEdge);
                        }
                    }
                }
            }

            // Collapse nodes
            List<GridNode> validNodes = new ArrayList<>(nodes.values());
            boolean collapsing = true;
            while (collapsing) {
                collapsing = false;
                for (int index = 0; index < validNodes.size(); index++) {
                    GridNode node = validNodes.get(index);
                    if (node.edges.size() == 2) {
                        collapsing = true;
                        validNodes.remove(node);
                        index--;
                        // Merge into the left edge, right right edge
                        final GridEdge leftEdge = node.edges.get(0);
                        final GridEdge rightEdge = node.edges.get(1);
                        leftEdge.distance += rightEdge.distance;
                        leftEdge.connectedNodes.remove(node);
                        rightEdge.connectedNodes.remove(node);
                        final GridNode rightNode = rightEdge.connectedNodes.get(0);
                        rightNode.edges.remove(rightEdge);
                        rightNode.edges.add(leftEdge);
                        leftEdge.connectedNodes.add(rightNode);
                    }
                }
            }
            for (final GridNode node : validNodes) {
                this.nodes.put(new Coordinate2D<>(node.x, node.y), node);
            }
        }
        public long getLongestPath(final Coordinate2D<Integer> startingPosition, final Coordinate2D<Integer> endingPosition) {
            long longestPath = 0;

            final GridNode startingNode = nodes.get(startingPosition);
            final GridNode endingNode = nodes.get(endingPosition);

            PriorityQueue<WalkingGridNode> walkingGridNodes = new PriorityQueue<>(Comparator.comparingLong(value -> -value.distance));
            {
                final WalkingGridNode newWalkingNode = new WalkingGridNode();
                newWalkingNode.walkedNodes.add(startingNode);
                newWalkingNode.currentNode = startingNode;
                walkingGridNodes.add(newWalkingNode);
            }
            while (!walkingGridNodes.isEmpty()) {
                final WalkingGridNode walkingNode = walkingGridNodes.poll();
                if (walkingNode.currentNode == endingNode) {
                    if (walkingNode.distance > longestPath) {
                        longestPath = walkingNode.distance;
                        LogUtilities.logPurple("Next longest: " + longestPath);
                    }
                    continue;
                }
                continueGridEdgeLoop:
                for (final GridEdge gridEdge : walkingNode.currentNode.edges) {
                    for (final GridNode node : gridEdge.connectedNodes) {
                        if (node != walkingNode.currentNode && !walkingNode.walkedNodes.contains(node)) {
                            final WalkingGridNode newWalkingNode = new WalkingGridNode();
                            newWalkingNode.walkedNodes.addAll(walkingNode.walkedNodes);
                            newWalkingNode.walkedNodes.add(node);
                            newWalkingNode.currentNode = node;
                            newWalkingNode.distance = walkingNode.distance + gridEdge.distance;
                            walkingGridNodes.add(newWalkingNode);
                            continue continueGridEdgeLoop;
                        }
                    }
                }
            }

            return longestPath;
        }
    }

    class WalkingGridNode {
        Set<GridNode> walkedNodes = new HashSet<>();
        long distance = 0;
        GridNode currentNode;
    }

    private void runSolution1(final String fileName) throws Exception {
        final GridMap gridMap = new GridMap(LoadUtilities.loadTextFileAsString(fileName));
        LogUtilities.logGreen("Solution 1: " + gridMap.getLongestPath(new Coordinate2D<>(1, 0), new Coordinate2D<>(gridMap.width - 2, gridMap.height - 1), false));
    }

    private void runSolution2(final String fileName) throws Exception {
        final GridNodeMap gridNodeMap = new GridNodeMap(LoadUtilities.loadTextFileAsString(fileName));
        LogUtilities.logGreen("Solution 2: " + gridNodeMap.getLongestPath(new Coordinate2D<>(1, 0), new Coordinate2D<>(gridNodeMap.width - 2, gridNodeMap.height - 1)));
    }
}
