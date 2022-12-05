package Advent.of.Code.Java.Year_2022;

import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;

import java.util.ArrayList;
import java.util.List;

public class Day_5 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2022/5/", this);
    }

    static class BoxArrangement {
        public List<BoxStack> stacks = new ArrayList<>();
        public BoxArrangement(final String input) {
            final List<String> inputLines = StringUtilities.splitStringIntoList(input, "\n");
            for (int columnLocation = 1; columnLocation < inputLines.get(inputLines.size()-1).length(); columnLocation += 4) {
                stacks.add(new BoxStack(inputLines, columnLocation));
            }
        }

        public void move(final int fromStackLocation, final int toStackLocation, final int numberToMove) {
            final BoxStack fromStack = stacks.get(fromStackLocation);
            final BoxStack toStack = stacks.get(toStackLocation);
            for (int i = 0; i < numberToMove; i++) {
                toStack.addToTop(fromStack.popTop());
            }
        }
        public void multiMove(final int fromStackLocation, final int toStackLocation, final int numberToMove) {
            final BoxStack fromStack = stacks.get(fromStackLocation);
            final BoxStack toStack = stacks.get(toStackLocation);
            toStack.addMultipleToTop(fromStack.popMultipleFromTop(numberToMove));
        }
        public String getTopReadout() {
            StringBuilder output = new StringBuilder();
            for (final BoxStack stack : stacks) {
                output.append(stack.getTop());
            }
            return output.toString();
        }
    }
    static class BoxStack {
        public List<String> boxes = new ArrayList<>();
        public BoxStack(final List<String> inputColumns, final int columnLocation) {
            for (int row = inputColumns.size() - 2; row >= 0; row -= 1) {
                final String inputRow = inputColumns.get(row);
                // Get the letter at this location
                final String letter = inputRow.length() >= columnLocation ? inputRow.substring(columnLocation, columnLocation + 1) : " ";
                if (!letter.equals(" ")) {
                    boxes.add(letter);
                } else {
                    break;
                }
            }
        }
        public String getTop() {
            return boxes.get(boxes.size() - 1);
        }
        public String popTop() {
            return boxes.remove(boxes.size()-1);
        }
        public void addToTop(final String box) {
            boxes.add(box);
        }
        public List<String> popMultipleFromTop(final int numberToRemove) {
            final List<String> removingBoxes = new ArrayList<>();
            final int indexToChop = boxes.size() - numberToRemove;
            for (int removeCount = 0; removeCount < numberToRemove; removeCount += 1) {
                removingBoxes.add(boxes.remove(indexToChop));
            }
            return removingBoxes;
        }
        public void addMultipleToTop(final List<String> box) {
            boxes.addAll(box);
        }
    }

    public void executeWithInput(final String fileName) throws Exception {
        {
            final String input = LoadUtilities.loadTextFileAsString(fileName);
            final List<String> inputHalves = StringUtilities.splitStringIntoList(input, "\n\n");
            final BoxArrangement boxArrangement = new BoxArrangement(inputHalves.get(0));
            final List<String> inputInstructions = StringUtilities.splitStringIntoList(inputHalves.get(1), "\n");
            for (final String instruction : inputInstructions) {
                final List<String> instructionHalves = StringUtilities.splitStringIntoList(instruction, " from ");
                final int numberToMove = Integer.parseInt(StringUtilities.removeStartChunk(instructionHalves.get(0), "move "));
                final List<String> toMoveInstructions = StringUtilities.splitStringIntoList(instructionHalves.get(1), " to ");
                final int moveFrom = Integer.parseInt(toMoveInstructions.get(0));
                final int moveTo = Integer.parseInt(toMoveInstructions.get(1));
                boxArrangement.move(moveFrom - 1, moveTo - 1, numberToMove);
            }

            LogUtilities.logGreen("Solution: " + boxArrangement.getTopReadout());
        }
        {
            final String input = LoadUtilities.loadTextFileAsString(fileName);
            final List<String> inputHalves = StringUtilities.splitStringIntoList(input, "\n\n");
            final BoxArrangement boxArrangement = new BoxArrangement(inputHalves.get(0));
            final List<String> inputInstructions = StringUtilities.splitStringIntoList(inputHalves.get(1), "\n");
            for (final String instruction : inputInstructions) {
                final List<String> instructionHalves = StringUtilities.splitStringIntoList(instruction, " from ");
                final int numberToMove = Integer.parseInt(StringUtilities.removeStartChunk(instructionHalves.get(0), "move "));
                final List<String> toMoveInstructions = StringUtilities.splitStringIntoList(instructionHalves.get(1), " to ");
                final int moveFrom = Integer.parseInt(toMoveInstructions.get(0));
                final int moveTo = Integer.parseInt(toMoveInstructions.get(1));
                boxArrangement.multiMove(moveFrom - 1, moveTo - 1, numberToMove);
            }

            LogUtilities.logGreen("Solution 2: " + boxArrangement.getTopReadout());
        }
    }
}
