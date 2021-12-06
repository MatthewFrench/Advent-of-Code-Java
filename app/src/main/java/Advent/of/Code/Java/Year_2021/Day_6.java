package Advent.of.Code.Java.Year_2021;

import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day_6 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2021/6/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);
        final List<String> inputList = StringUtilities.splitStringIntoList(input.get(0), ",");

        final List<Long> values = new ArrayList<>();

        for (final String inputLine : inputList) {
            long value = Long.parseLong(inputLine);
            values.add(value);
        }

        LogUtilities.logGreen("Solution: " + getDaysForInput(values, 80));
        LogUtilities.logGreen("Solution: " + getDaysForInput(values, 256));
    }
    @AllArgsConstructor
    class FishGroup {
        long numberOfFish;
        long daysLeft;
    }

    long getDaysForInput(final List<Long> values, final int days) {
        final List<FishGroup> fishes = new ArrayList<>();
        for (final long value : values) {
            final FishGroup fishGroup = getFishGroup(fishes, value);
            fishGroup.numberOfFish += 1;
        }

        for (int i = 0; i < days; i++) {

            long addNew = 0;
            for (final FishGroup fishGroup : fishes) {
                fishGroup.daysLeft -= 1;
                if (fishGroup.daysLeft < 0) {
                    fishGroup.daysLeft = 6;
                    addNew += fishGroup.numberOfFish;
                }
            }
            final FishGroup fishGroup = getFishGroup(fishes, 8);
            fishGroup.numberOfFish += addNew;
        }
        long total = 0;
        for (final FishGroup fishGroup : fishes) {
            total += fishGroup.numberOfFish;
        }
        return total;
    }

    FishGroup getFishGroup(List<FishGroup> fishGroupList, long daysLeft) {
        for (final FishGroup group : fishGroupList) {
            if (group.daysLeft == daysLeft) {
                return group;
            }
        }
        final FishGroup fishGroup = new FishGroup(0, daysLeft);
        fishGroupList.add(fishGroup);
        return fishGroup;
    }
}
