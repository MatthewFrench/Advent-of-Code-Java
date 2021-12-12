package Advent.of.Code.Java.Year_2021;

import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day_12 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2021/12/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);

        final Map<String, Cave> caveMap = new HashMap<>();
        for (final String line : input) {
            final List<String> split = StringUtilities.splitStringIntoList(line, "-");
            final String first = split.get(0);
            final String last = split.get(1);
            final Cave firstCave = caveMap.computeIfAbsent(first, (n) -> new Cave(n, n.equals(n.toLowerCase()), new HashSet<>()));
            final Cave lastCave = caveMap.computeIfAbsent(last, (n) -> new Cave(n, n.equals(n.toLowerCase()), new HashSet<>()));
            firstCave.connectedCaves.add(lastCave);
            lastCave.connectedCaves.add(firstCave);
        }

        final Cave startCave = caveMap.get("start");
        final CavePath initialPath = new CavePath(new ArrayList<>(), new HashMap<>());
        initialPath.pathList.add(startCave);
        initialPath.visited.put(startCave, 1);
        final List<CavePath> paths = createPaths(startCave, initialPath, false);

        int smallPathCount = 0;
        for (final CavePath path : paths) {
            for (final Cave cave : path.visited.keySet()) {
                if (cave.isSmallCave) {
                    smallPathCount += 1;
                    break;
                }
            }
        }

        LogUtilities.logGreen("Solution: " + smallPathCount);

        final List<CavePath> partTwoPaths = createPaths(startCave, initialPath, true);
        LogUtilities.logGreen("Solution: " + partTwoPaths.size());
    }
    public List<CavePath> createPaths(final Cave currentCave, final CavePath path, boolean partTwo) {
        List<CavePath> addPaths = new ArrayList<>();
        for (final Cave cave : currentCave.connectedCaves) {
            if (!partTwo) {
                if (!cave.isSmallCave || !path.visited.containsKey(cave)) {
                    final CavePath newCavePath = path.duplicate();
                    newCavePath.pathList.add(cave);
                    newCavePath.visited.put(cave, newCavePath.visited.getOrDefault(cave, 0)+1);
                    if (!cave.name.equals("end")) {
                        addPaths.addAll(createPaths(cave, newCavePath, partTwo));
                    } else {
                        addPaths.add(newCavePath);
                    }
                }
            } else {
                boolean containsSmallCaveTwice = false;
                for (final Cave visitedCave : path.visited.keySet()) {
                    if (visitedCave.isSmallCave && path.visited.get(visitedCave) > 1) {
                        containsSmallCaveTwice = true;
                        break;
                    }
                }
                if ((!cave.isSmallCave || !path.visited.containsKey(cave) || !containsSmallCaveTwice) && !cave.name.equals("start")) {
                    final CavePath newCavePath = path.duplicate();
                    newCavePath.pathList.add(cave);
                    newCavePath.visited.put(cave, newCavePath.visited.getOrDefault(cave, 0)+1);
                    if (!cave.name.equals("end")) {
                        addPaths.addAll(createPaths(cave, newCavePath, partTwo));
                    } else {
                        addPaths.add(newCavePath);
                    }
                }
            }
        }
        return addPaths;
    }

    @AllArgsConstructor
    class Cave {
        final String name;
        final boolean isSmallCave;
        final Set<Cave> connectedCaves;
    }

    @AllArgsConstructor
    class CavePath {
        final List<Cave> pathList;
        final Map<Cave, Integer> visited;
        public CavePath duplicate() {
            return new CavePath(new ArrayList<>(pathList), new HashMap<>(visited));
        }
    }
}
