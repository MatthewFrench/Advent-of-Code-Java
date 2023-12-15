package Advent.of.Code.Java.Year_2023;

import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.NumberUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Day_15 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2023/15/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        runSolution1(fileName);
        runSolution2(fileName);
    }

    private void runSolution1(final String fileName) throws Exception {
        final List<String> input = StringUtilities.splitStringIntoList(LoadUtilities.loadTextFileAsString(fileName), ",");

        long totalSum = 0;
        for (final String item : input) {
            long current = 0;
            for (int index = 0; index < item.length(); index++) {
                current += item.charAt(index);
                current *= 17;
                current = current % 256;
            }
            totalSum += current;
        }

        LogUtilities.logGreen("Solution 1: " + totalSum);
    }
    class Lens {
        String name;
        long focalLength;
    }
    private void runSolution2(final String fileName) throws Exception {
        final List<String> input = StringUtilities.splitStringIntoList(LoadUtilities.loadTextFileAsString(fileName), ",");

        final Map<Long, List<Lens>> boxes = new HashMap<>();
        for (final String item : input) {
            final String label = StringUtilities.removeEndChunkIfExists(StringUtilities.splitStringIntoList(item, "=").getFirst(), "-");
            long box = 0;
            for (int index = 0; index < label.length(); index++) {
                box += item.charAt(index);
                box *= 17;
                box = box % 256;
            }
            if (StringUtilities.chunkExistsAtEnd(item, "-")) {
                if (boxes.containsKey(box)) {
                    final List<Lens> lenses = boxes.get(box);
                    lenses.removeIf(lens -> lens.name.equals(label));
                }
            } else {
                final List<String> splitItem = StringUtilities.splitStringIntoList(item, "=");
                final long focalLength = Long.parseLong(splitItem.get(1));
                if (!boxes.containsKey(box)) {
                    boxes.put(box, new ArrayList<>());
                }
                final List<Lens> lenses = boxes.get(box);
                final Optional<Lens> lensOptional = lenses.stream().filter(lens -> lens.name.equals(label)).findFirst();
                if (lensOptional.isPresent()) {
                    lensOptional.get().focalLength = focalLength;
                } else {
                    final Lens lens = new Lens();
                    lens.name = label;
                    lens.focalLength = focalLength;
                    lenses.add(lens);
                }
            }
        }

        long totalSum = 0;
        for (final long box : boxes.keySet()) {
            final List<Lens> lenses = boxes.get(box);
            int slot = 1;
            for (final Lens lens : lenses) {
                totalSum += (box + 1) * slot * lens.focalLength;
                slot+=1;
            }
        }

        LogUtilities.logGreen("Solution 2: " + totalSum);
    }
}
