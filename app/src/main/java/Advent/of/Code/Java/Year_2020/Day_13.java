package Advent.of.Code.Java.Year_2020;

import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.Day;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class Day_13 implements Day {
    final static NumberFormat myFormat = NumberFormat.getInstance();
    public void run() throws Exception {
        // Comment this to run the super long Day 13, takes around 6 minutes
        LogUtilities.log("Skip Day 13 due to taking too long");
        if (true) return;
        LogUtilities.log(Day_13.class.getName());
        final List<String> input = LoadUtilities.loadTextFileAsList("input/2020/13/input.txt");
        long time = Long.parseLong(input.get(0));
        final List<String> busIDs = StringUtilities.splitStringIntoList(input.get(1), ",");
        {
            long shortestTime = -1;
            long busId = -1;
            for (var checkBusId : busIDs) {
                if (checkBusId.equals("x")) {
                    continue;
                }
                var number = Long.parseLong(checkBusId);
                var newShortestTime = number - (time % number);
                if (shortestTime == -1 || newShortestTime < shortestTime) {
                    shortestTime = newShortestTime;
                    busId = number;
                }
            }
            LogUtilities.log("Part 1 " + (busId * shortestTime));
        }

        var minute = 0;
        List<Bus> buses = new ArrayList<>();
        for (var checkBusId : busIDs) {
            if (checkBusId.equals("x")) {
                minute += 1;
                continue;
            }
            var busNumber = Long.parseLong(checkBusId);
            LogUtilities.log("Bus Id: " + checkBusId + " at minute " + minute);
            var bus = new Bus();
            bus.busId = busNumber;
            bus.minute = minute;
            bus.currentPosition = busNumber;
            buses.add(bus);
            minute += 1;
        }

        var startingBus = buses.get(0);
        // Puzzle input said the earliest timestamp would likely be greater than 100 trillion
        long firstBusMultiply = 13_741_926_618L;
        var searching = true;
        //(7*19) for demo
        //(19*383) for main
        var multiplyAmount = (19*383);
        long start = System.currentTimeMillis();
        continueSearching:
        while (searching) {
            firstBusMultiply += 1;
            long amount = multiplyAmount * firstBusMultiply - startingBus.busId;
            long finish = System.currentTimeMillis();
            if (finish - start > 20_000) {
                LogUtilities.log("At amount: " + myFormat.format(amount));
                start = finish;
            }
            for (var i = 1; i < buses.size(); i++) {
                var bus = buses.get(i);
                if ((amount + bus.minute) % bus.busId != 0) {
                    continue continueSearching;
                }
            }
            searching = false;
        }
        LogUtilities.log("Part 2 timestamp: " + (multiplyAmount * firstBusMultiply - startingBus.busId));
    }
    static class Bus {
        long busId;
        long minute;
        long currentPosition;
    }
}