package Advent.of.Code.Java.Year_2022;

import Advent.of.Code.Java.Utility.DataUtilities;
import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicLong;

public class Day_19 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2022/19/", this);
    }

    static int TASK_BUILD_ORE_ROBOT = 1;
    static int TASK_BUILD_CLAY_ROBOT = 2;
    static int TASK_BUILD_OBSIDIAN_ROBOT = 3;
    static int TASK_BUILD_GEODE_ROBOT = 4;

    static List<Integer> CAN_BUILD_CLAY_ROBOT = DataUtilities.List(TASK_BUILD_CLAY_ROBOT);
    static List<Integer> CAN_BUILD_OBSIDIAN_ROBOT = DataUtilities.List(TASK_BUILD_CLAY_ROBOT, TASK_BUILD_OBSIDIAN_ROBOT);
    static List<Integer> CAN_BUILD_GEODE_ROBOT = DataUtilities.List(TASK_BUILD_CLAY_ROBOT, TASK_BUILD_OBSIDIAN_ROBOT, TASK_BUILD_GEODE_ROBOT);

    @Data
    @AllArgsConstructor
    static class Scenario {
        int oreCount;
        int clayCount;
        int obsidianCount;
        int geodeCount;
        int oreRobotCount;
        int clayRobotCount;
        int obsidianRobotCount;
        int geodeRobotCount;
        int minutesLeft;
        Integer currentTask;

        List<Integer> getPossibleTasks() {
            if (obsidianRobotCount > 0) {
                return CAN_BUILD_GEODE_ROBOT;
            }
            if (clayRobotCount > 0) {
                return CAN_BUILD_OBSIDIAN_ROBOT;
            }
            return CAN_BUILD_CLAY_ROBOT;
        }
    }

    public void executeWithInput(final String fileName) throws Exception {
        List<String> trimInput = new ArrayList<>(StringUtilities.splitStringIntoList(LoadUtilities.loadTextFileAsString(fileName), "\n"));
        if (trimInput.size() > 3) {
            trimInput = DataUtilities.List(trimInput.get(0), trimInput.get(1), trimInput.get(2));
        }
        final List<String> input = trimInput;

        final List<Long> maxGeodeList = Collections.synchronizedList(new ArrayList<Long>());
        ForkJoinPool customThreadPool = new ForkJoinPool(input.size());
        customThreadPool.submit(() -> {
            input.parallelStream().forEach((blueprintRow) -> {
                int blueprintNumber = Integer.parseInt(StringUtilities.splitStringIntoList(StringUtilities.removeStartChunk(blueprintRow, "Blueprint "), ":").get(0));
                LogUtilities.logPurple("Processing blueprint " + blueprintNumber);
                int oreRobotOreCost = Integer.parseInt(StringUtilities.splitStringIntoList(StringUtilities.splitStringIntoList(blueprintRow, "Each ore robot costs ").get(1), " ore. ").get(0));
                int clayRobotOreCost = Integer.parseInt(StringUtilities.splitStringIntoList(StringUtilities.splitStringIntoList(blueprintRow, "Each clay robot costs ").get(1), " ore. ").get(0));
                int obsidianRobotOreCost = Integer.parseInt(StringUtilities.splitStringIntoList(StringUtilities.splitStringIntoList(blueprintRow, "Each obsidian robot costs ").get(1), " ore and ").get(0));
                int obsidianRobotClayCost = Integer.parseInt(
                        StringUtilities.splitStringIntoList(
                                StringUtilities.splitStringIntoList(
                                        StringUtilities.splitStringIntoList(blueprintRow, "Each obsidian robot costs ").get(1)
                                        , " clay."
                                ).get(0)
                                , " ore and ").get(1));
                int geodeRobotOreCost = Integer.parseInt(StringUtilities.splitStringIntoList(StringUtilities.splitStringIntoList(blueprintRow, "Each geode robot costs ").get(1), " ore and ").get(0));
                int geodeRobotObsidianCost = Integer.parseInt(
                        StringUtilities.splitStringIntoList(
                                StringUtilities.splitStringIntoList(
                                        StringUtilities.splitStringIntoList(blueprintRow, "Each geode robot costs ").get(1)
                                        , " obsidian"
                                ).get(0)
                                , " ore and ").get(1));
                int totalMinutes = 32;

                List<Scenario> scenarios = new ArrayList<>();
                scenarios.add(new Scenario(0, 0, 0, 0, 1, 0, 0, 0, totalMinutes, null));
                int maxGeodeCount = 0;
                while (scenarios.size() > 0) {
                    final Scenario scenario = scenarios.get(scenarios.size() - 1);
                    if (scenario.currentTask == null) {
                        final List<Integer> canDoTasks = scenario.getPossibleTasks();
                        for (final int canDoTask : canDoTasks) {
                            scenarios.add(new Scenario(
                                    scenario.oreCount, scenario.clayCount, scenario.obsidianCount, scenario.geodeCount,
                                    scenario.oreRobotCount, scenario.clayRobotCount, scenario.obsidianRobotCount, scenario.geodeRobotCount,
                                    scenario.minutesLeft, canDoTask));
                        }
                        scenario.currentTask = TASK_BUILD_ORE_ROBOT;
                    }
                    int addOreRobots = 0;
                    int addClayRobots = 0;
                    int addObsidianRobots = 0;
                    int addGeodeRobots = 0;
                    if (scenario.currentTask == TASK_BUILD_ORE_ROBOT) {
                        if (scenario.oreCount >= oreRobotOreCost) {
                            scenario.oreCount -= oreRobotOreCost;
                            addOreRobots += 1;
                            scenario.currentTask = null;
                        }
                    } else if (scenario.currentTask == TASK_BUILD_CLAY_ROBOT) {
                        if (scenario.oreCount >= clayRobotOreCost) {
                            scenario.oreCount -= clayRobotOreCost;
                            addClayRobots += 1;
                            scenario.currentTask = null;
                        }
                    } else if (scenario.currentTask == TASK_BUILD_OBSIDIAN_ROBOT) {
                        if (scenario.oreCount >= obsidianRobotOreCost && scenario.clayCount >= obsidianRobotClayCost) {
                            scenario.oreCount -= obsidianRobotOreCost;
                            scenario.clayCount -= obsidianRobotClayCost;
                            addObsidianRobots += 1;
                            scenario.currentTask = null;
                        }
                    } else if (scenario.currentTask == TASK_BUILD_GEODE_ROBOT) {
                        if (scenario.oreCount >= geodeRobotOreCost && scenario.obsidianCount >= geodeRobotObsidianCost) {
                            scenario.oreCount -= geodeRobotOreCost;
                            scenario.obsidianCount -= geodeRobotObsidianCost;
                            addGeodeRobots += 1;
                            scenario.currentTask = null;
                        }
                    }
                    scenario.minutesLeft -= 1;
                    scenario.oreCount += scenario.oreRobotCount;
                    scenario.clayCount += scenario.clayRobotCount;
                    scenario.obsidianCount += scenario.obsidianRobotCount;
                    scenario.geodeCount += scenario.geodeRobotCount;
                    scenario.oreRobotCount += addOreRobots;
                    scenario.clayRobotCount += addClayRobots;
                    scenario.obsidianRobotCount += addObsidianRobots;
                    scenario.geodeRobotCount += addGeodeRobots;
                    if (scenario.minutesLeft <= 0) {
                        if (scenario.geodeCount > maxGeodeCount) {
                            maxGeodeCount = scenario.geodeCount;
                            LogUtilities.logPurple(blueprintNumber + ": Found max geode count " + maxGeodeCount);
                        }
                        scenarios.remove(scenario);
                    } else {
                        // Check if it is possible to get more geodes  than the current max geodes in the time left
                        // If it is not possible, delete this scenario
                        int estimatedMaxGeodes = scenario.geodeCount;
                        int tempGeodeBots = scenario.geodeRobotCount;
                        for (int i = scenario.minutesLeft; i >= 0; i--) {
                            estimatedMaxGeodes += tempGeodeBots;
                            // We can at most make one geode bot per round
                            tempGeodeBots += 1;
                        }
                        if (estimatedMaxGeodes <= maxGeodeCount) {
                            scenarios.remove(scenario);
                        }
                        // We might also be able to trim scenarios by tracking when we have the most geode robots
                        // and removing any scenarios that have less geode robots by that marker
                    }
                }
                maxGeodeList.add((long)maxGeodeCount);
                LogUtilities.logPurple("Blueprint " + blueprintNumber + " max geode count is " + maxGeodeCount);
            });
        }).join();

        long geodeProduct = 1;
        for (final long geodeValue : maxGeodeList) {
            geodeProduct *= geodeValue;
        }
        LogUtilities.logGreen("Solution 2: " + geodeProduct);
    }
}

/*
Solution 1:
final List<String> input = StringUtilities.splitStringIntoList(LoadUtilities.loadTextFileAsString(fileName), "\n");

        AtomicLong qualitySum = new AtomicLong(0);
        ForkJoinPool customThreadPool = new ForkJoinPool(input.size());
        customThreadPool.submit(() -> {
            input.parallelStream().forEach((blueprintRow) -> {
                int blueprintNumber = Integer.parseInt(StringUtilities.splitStringIntoList(StringUtilities.removeStartChunk(blueprintRow, "Blueprint "), ":").get(0));
                LogUtilities.logPurple("Processing blueprint " + blueprintNumber);
                int oreRobotOreCost = Integer.parseInt(StringUtilities.splitStringIntoList(StringUtilities.splitStringIntoList(blueprintRow, "Each ore robot costs ").get(1), " ore. ").get(0));
                int clayRobotOreCost = Integer.parseInt(StringUtilities.splitStringIntoList(StringUtilities.splitStringIntoList(blueprintRow, "Each clay robot costs ").get(1), " ore. ").get(0));
                int obsidianRobotOreCost = Integer.parseInt(StringUtilities.splitStringIntoList(StringUtilities.splitStringIntoList(blueprintRow, "Each obsidian robot costs ").get(1), " ore and ").get(0));
                int obsidianRobotClayCost = Integer.parseInt(
                        StringUtilities.splitStringIntoList(
                                StringUtilities.splitStringIntoList(
                                        StringUtilities.splitStringIntoList(blueprintRow, "Each obsidian robot costs ").get(1)
                                        , " clay."
                                ).get(0)
                                , " ore and ").get(1));
                int geodeRobotOreCost = Integer.parseInt(StringUtilities.splitStringIntoList(StringUtilities.splitStringIntoList(blueprintRow, "Each geode robot costs ").get(1), " ore and ").get(0));
                int geodeRobotObsidianCost = Integer.parseInt(
                        StringUtilities.splitStringIntoList(
                                StringUtilities.splitStringIntoList(
                                        StringUtilities.splitStringIntoList(blueprintRow, "Each geode robot costs ").get(1)
                                        , " obsidian"
                                ).get(0)
                                , " ore and ").get(1));
                int totalMinutes = 24;

                List<Scenario> scenarios = new ArrayList<>();
                scenarios.add(new Scenario(0, 0, 0, 0, 1, 0, 0, 0, totalMinutes, null));
                int maxGeodeCount = 0;
                while (scenarios.size() > 0) {
                    final Scenario scenario = scenarios.get(scenarios.size() - 1);
                    if (scenario.currentTask == null) {
                        final List<Integer> canDoTasks = scenario.getPossibleTasks();
                        for (final int canDoTask : canDoTasks) {
                            scenarios.add(new Scenario(
                                    scenario.oreCount, scenario.clayCount, scenario.obsidianCount, scenario.geodeCount,
                                    scenario.oreRobotCount, scenario.clayRobotCount, scenario.obsidianRobotCount, scenario.geodeRobotCount,
                                    scenario.minutesLeft, canDoTask));
                        }
                        scenario.currentTask = TASK_BUILD_ORE_ROBOT;
                    }
                    int addOreRobots = 0;
                    int addClayRobots = 0;
                    int addObsidianRobots = 0;
                    int addGeodeRobots = 0;
                    if (scenario.currentTask == TASK_BUILD_ORE_ROBOT) {
                        if (scenario.oreCount >= oreRobotOreCost) {
                            scenario.oreCount -= oreRobotOreCost;
                            addOreRobots += 1;
                            scenario.currentTask = null;
                        }
                    } else if (scenario.currentTask == TASK_BUILD_CLAY_ROBOT) {
                        if (scenario.oreCount >= clayRobotOreCost) {
                            scenario.oreCount -= clayRobotOreCost;
                            addClayRobots += 1;
                            scenario.currentTask = null;
                        }
                    } else if (scenario.currentTask == TASK_BUILD_OBSIDIAN_ROBOT) {
                        if (scenario.oreCount >= obsidianRobotOreCost && scenario.clayCount >= obsidianRobotClayCost) {
                            scenario.oreCount -= obsidianRobotOreCost;
                            scenario.clayCount -= obsidianRobotClayCost;
                            addObsidianRobots += 1;
                            scenario.currentTask = null;
                        }
                    } else if (scenario.currentTask == TASK_BUILD_GEODE_ROBOT) {
                        if (scenario.oreCount >= geodeRobotOreCost && scenario.obsidianCount >= geodeRobotObsidianCost) {
                            scenario.oreCount -= geodeRobotOreCost;
                            scenario.obsidianCount -= geodeRobotObsidianCost;
                            addGeodeRobots += 1;
                            scenario.currentTask = null;
                        }
                    }
                    scenario.minutesLeft -= 1;
                    scenario.oreCount += scenario.oreRobotCount;
                    scenario.clayCount += scenario.clayRobotCount;
                    scenario.obsidianCount += scenario.obsidianRobotCount;
                    scenario.geodeCount += scenario.geodeRobotCount;
                    scenario.oreRobotCount += addOreRobots;
                    scenario.clayRobotCount += addClayRobots;
                    scenario.obsidianRobotCount += addObsidianRobots;
                    scenario.geodeRobotCount += addGeodeRobots;
                    if (scenario.minutesLeft <= 0) {
                        if (scenario.geodeCount > maxGeodeCount) {
                            maxGeodeCount = scenario.geodeCount;
                            LogUtilities.logPurple(blueprintNumber + ": Found max geode count " + maxGeodeCount);
                        }
                        scenarios.remove(scenario);
                    } else {
                        // Check if it is possible to get more geodes  than the current max geodes in the time left
                        // If it is not possible, delete this scenario
                    }
                }
                qualitySum.addAndGet(blueprintNumber * (long) maxGeodeCount);
                LogUtilities.logPurple("Blueprint " + blueprintNumber + " quality level is " + (blueprintNumber * (long) maxGeodeCount));
            });
        }).join();

        LogUtilities.logGreen("Solution: " + qualitySum);
 */