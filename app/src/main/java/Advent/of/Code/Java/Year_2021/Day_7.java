package Advent.of.Code.Java.Year_2021;

import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day_7 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2021/7/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        {
            final List<String> inputAsString = StringUtilities.splitStringIntoList(LoadUtilities.loadTextFileAsString(fileName), ",");
            final List<Long> input = new ArrayList<>();
            for (final String value : inputAsString) {
                input.add(Long.parseLong(value));
            }
            long minimum = input.get(0);
            long maximum = input.get(0);
            for (final long value : input) {
                minimum = Math.min(minimum, value);
                maximum = Math.max(maximum, value);
            }

            List<Long> fuelAmounts = new ArrayList<>();
            for (long i = minimum; i <= maximum; i++) {
                long fuel = 0;
                for (long value : input) {
                    fuel += Math.abs(value - i);
                }
                fuelAmounts.add(fuel);
            }
            //LogUtilities.logBlue("Fuel amounts: " + fuelAmounts);

            long minFuel = fuelAmounts.get(0);
            for (final long fuel : fuelAmounts) {
                minFuel = Math.min(minFuel, fuel);
            }


            LogUtilities.logGreen("Solution: " + minFuel);
        }

        {
            final List<String> inputAsString = StringUtilities.splitStringIntoList(LoadUtilities.loadTextFileAsString(fileName), ",");
            final List<Long> input = new ArrayList<>();
            for (final String value : inputAsString) {
                input.add(Long.parseLong(value));
            }
            long minimum = input.get(0);
            long maximum = input.get(0);
            for (final long value : input) {
                minimum = Math.min(minimum, value);
                maximum = Math.max(maximum, value);
            }

            List<Long> fuelAmounts = new ArrayList<>();
            Map<Long, Long> stepCosts = new HashMap<>();
            for (long i = minimum; i <= maximum; i++) {
                long fuel = 0;
                for (long value : input) {
                    long steps = Math.abs(value - i);
                    fuel += stepCosts.computeIfAbsent(steps, (s) -> {
                        long amount = 0;
                        long currentValue = 1;
                        for (int j = 0; j < steps; j++) {
                            amount += currentValue;
                            currentValue += 1;
                        }
                        return amount;
                    });
                }
                fuelAmounts.add(fuel);
            }
            //LogUtilities.logBlue("Fuel amounts: " + fuelAmounts);

            long minFuel = fuelAmounts.get(0);
            for (final long fuel : fuelAmounts) {
                minFuel = Math.min(minFuel, fuel);
            }

            LogUtilities.logGreen("Solution: " + minFuel);
        }
    }
}
