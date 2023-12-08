package Advent.of.Code.Java.Year_2023;

import Advent.of.Code.Java.Utility.DataUtilities;
import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.NumberUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;
import Advent.of.Code.Java.Utility.Structures.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Day_8 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2023/8/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        // Commenting because part 2 has a different sample than part 1.
        //runSolution1(fileName);
        runSolution2(fileName);
    }

    class InstructionNode {
        String name;
        String leftName;
        String rightName;
        InstructionNode left;
        InstructionNode right;
    }

    private void runSolution1(final String fileName) throws Exception {
        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);

        List<String> instructions = StringUtilities.splitStringIntoList(input.get(0), "");
        Map<String, InstructionNode> instructionNodeMap = new HashMap<>();

        input.remove(0);
        input.remove(0);

        for (final String line : input) {
            final InstructionNode instructionNode = new InstructionNode();
            instructionNode.name = StringUtilities.splitStringIntoList(line, " = ").get(0);
            final String rightSide = StringUtilities.splitStringIntoList(line, " = ").get(1);
            instructionNode.leftName = StringUtilities.removeStartChunk(StringUtilities.splitStringIntoList(rightSide, ", ").get(0), "(");
            instructionNode.rightName = StringUtilities.removeEndChunk(StringUtilities.splitStringIntoList(rightSide, ", ").get(1), ")");
            instructionNodeMap.put(instructionNode.name, instructionNode);
        }

        int currentIndex = 0;
        int totalSteps = 0;
        String currentNode = "AAA";
        String targetNode = "ZZZ";
        while (!Objects.equals(currentNode, targetNode)) {
            final InstructionNode instructionNode = instructionNodeMap.get(currentNode);
            final String currentInstruction = instructions.get(currentIndex);
            if (currentInstruction.equals("L")) {
                currentNode = instructionNode.leftName;
            } else if (currentInstruction.equals("R")) {
                currentNode = instructionNode.rightName;
            }

            totalSteps += 1;
            currentIndex += 1;
            if (currentIndex >= instructions.size()) {
                currentIndex = 0;
            }
        }

        LogUtilities.logGreen("Solution 1: " + totalSteps);
    }

    class CurrentNodeLocation {
        InstructionNode currentNode;
        List<Pair<Long, String>> Zhits = new ArrayList<>();
    }

    private void runSolution2(final String fileName) throws Exception {
        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);

        List<String> instructions = StringUtilities.splitStringIntoList(input.get(0), "");
        Map<String, InstructionNode> instructionNodeMap = new HashMap<>();

        input.remove(0);
        input.remove(0);

        List<CurrentNodeLocation> currentNodeLocations = new ArrayList<>();
        Set<InstructionNode> endNodeLocations = new HashSet<>();

        for (final String line : input) {
            final InstructionNode instructionNode = new InstructionNode();
            instructionNode.name = StringUtilities.splitStringIntoList(line, " = ").get(0);
            final String rightSide = StringUtilities.splitStringIntoList(line, " = ").get(1);
            instructionNode.leftName = StringUtilities.removeStartChunk(StringUtilities.splitStringIntoList(rightSide, ", ").get(0), "(");
            instructionNode.rightName = StringUtilities.removeEndChunk(StringUtilities.splitStringIntoList(rightSide, ", ").get(1), ")");
            instructionNodeMap.put(instructionNode.name, instructionNode);
            if (StringUtilities.chunkExistsAtEnd(instructionNode.name, "A")) {
                final CurrentNodeLocation currentNodeLocation = new CurrentNodeLocation();
                currentNodeLocation.currentNode = instructionNode;
                currentNodeLocations.add(currentNodeLocation);
            }
            if (StringUtilities.chunkExistsAtEnd(instructionNode.name, "Z")) {
                endNodeLocations.add(instructionNode);
            }
        }

        for (final InstructionNode instructionNode : instructionNodeMap.values()) {
            instructionNode.left = instructionNodeMap.get(instructionNode.leftName);
            instructionNode.right = instructionNodeMap.get(instructionNode.rightName);
        }

        int currentIndex = 0;
        long totalSteps = 0;
        while (!allNodesOnZ(currentNodeLocations, endNodeLocations)) {
            for (final CurrentNodeLocation currentNodeLocation : currentNodeLocations) {
                if (currentNodeLocation.Zhits.isEmpty()) {
                    final String currentInstruction = instructions.get(currentIndex);
                    if (currentInstruction.equals("L")) {
                        currentNodeLocation.currentNode = currentNodeLocation.currentNode.left;
                    } else if (currentInstruction.equals("R")) {
                        currentNodeLocation.currentNode = currentNodeLocation.currentNode.right;
                    }
                    if (endNodeLocations.contains(currentNodeLocation.currentNode)) {
                        currentNodeLocation.Zhits.add(new Pair<>(totalSteps + 1, currentNodeLocation.currentNode.name));
                    }
                }
            }

            totalSteps += 1;
            currentIndex += 1;
            if (currentIndex >= instructions.size()) {
                currentIndex = 0;
            }
        }

        // Turns out that the node paths follow a pattern that is steadily consistent
        // We just need to find the point that they all hit the same step count
        // That just happens to be the LCM of the list of first hits
        LogUtilities.logGreen("Solution 2: "
                + NumberUtilities.getLowestCommonMultiple(
                        DataUtilities.transformData(currentNodeLocations,
                                currentNodeLocation -> currentNodeLocation.Zhits.get(0).getKey()
                        )
                )
        );
    }
    private boolean allNodesOnZ(final List<CurrentNodeLocation> currentNodeLocations, final Set<InstructionNode> endNodes) {
        for (final CurrentNodeLocation currentNodeLocation : currentNodeLocations) {
            if (!endNodes.contains(currentNodeLocation.currentNode)) {
                return false;
            }
        }
        return true;
    }
}
