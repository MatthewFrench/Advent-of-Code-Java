package Advent.of.Code.Java.Year_2022;

import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Day_1 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2022/1/", this);
    }

    static class Elf {
        private final List<Long> calories = new ArrayList<>();

        public void addItem(final long item) {
            calories.add(item);
        }

        public long getCount() {
            long total = 0;
            for (long calorie : calories) {
                total += calorie;
            }
            return total;
        }
    }

    public void executeWithInput(final String fileName) throws Exception {
        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);
        final List<Elf> elves = new ArrayList<>();
        Elf currentElf = new Elf();
        for (final String item : input) {
            if (item.length() == 0) {
                elves.add(currentElf);
                currentElf = new Elf();
            } else {
                currentElf.addItem(Long.parseLong(item));
            }
        }
        elves.add(currentElf);

        elves.sort(Comparator.comparingLong(Elf::getCount).reversed());

        LogUtilities.logGreen("Solution: " + elves.get(0).getCount());
        LogUtilities.logGreen("Solution 2: " + (elves.get(0).getCount() + elves.get(1).getCount() + elves.get(2).getCount()));
    }
}
