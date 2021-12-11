package Advent.of.Code.Java.Year_2021;

import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

public class Day_11 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2021/11/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);

        final int width = input.get(0).length();
        final int height = input.size();

        final Octopus[][] octopi = new Octopus[width][height];
        int currentY = 0;
        for (final String line : input) {
            for (int x = 0; x < line.length(); x++) {
                final int power = Integer.parseInt(line.charAt(x) + "");
                octopi[x][currentY] = new Octopus(power, x, currentY);
            }
            currentY += 1;
        }

        int flashes = 0;
        boolean isSync = false;
        stepLoop:
        for (int step = 0; !isSync; step++) {
            List<Octopus> incrementOctopuses = new ArrayList<>();
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    final Octopus octopus = octopi[x][y];
                    if (incrementOctopus(octopus, incrementOctopuses, octopi, width, height, true)) {
                        flashes += 1;
                    }
                }
            }
            while (incrementOctopuses.size() > 0) {
                if (incrementOctopus(incrementOctopuses.remove(0), incrementOctopuses, octopi, width, height, false)) {
                    flashes += 1;
                }
            }
            if (step == 99) {
                LogUtilities.logGreen("Solution: " + flashes);
            }

            int value = octopi[0][0].power;
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (octopi[x][y].power != value) {
                        continue stepLoop;
                    }
                }
            }
            LogUtilities.logGreen("Sync step: " + (step + 1));
            isSync = true;
        }
    }

    static boolean incrementOctopus(final Octopus octopus, List<Octopus> incrementOctopuses, Octopus[][] octopi, int width, int height, boolean incrementZero) {
        if (!incrementZero && octopus.power == 0) {
            return false;
        }
        final int x = octopus.x;
        final int y = octopus.y;
        octopus.power += 1;
        if (octopus.power > 9) {
            octopus.power = 0;
            // Flash
            if (x > 0) {
                incrementOctopuses.add(octopi[x-1][y]);
                if (y > 0) {
                    incrementOctopuses.add(octopi[x-1][y-1]);
                }
                if (y < height - 1) {
                    incrementOctopuses.add(octopi[x-1][y+1]);
                }
            }
            if (x < width - 1) {
                incrementOctopuses.add(octopi[x+1][y]);
                if (y > 0) {
                    incrementOctopuses.add(octopi[x+1][y-1]);
                }
                if (y < height - 1) {
                    incrementOctopuses.add(octopi[x+1][y+1]);
                }
            }
            if (y > 0) {
                incrementOctopuses.add(octopi[x][y-1]);
            }
            if (y < height - 1) {
                incrementOctopuses.add(octopi[x][y+1]);
            }
            return true;
        }
        return false;
    }

    @AllArgsConstructor
    @EqualsAndHashCode
    class Octopus {
        public int power;
        int x;
        int y;
    }
}
