package Advent.of.Code.Java.Year_2023;

import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day_20 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2023/20/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        runSolution1(fileName);
        runSolution2(fileName);
    }

    enum ModuleType {
        BROADCASTER,
        FLIPFLOP,
        CONJUNCTION
    }

    class Module {
        String name;
        List<String> outputs = new ArrayList<>();
        ModuleType type;
        List<String> inputs = new ArrayList<>();
        // Flip flop specific
        boolean flipFlopState = false;
        // Conjunction specific
        Map<String, Boolean> conjunctionState = new HashMap<>();
        long sentLow = 0;
        long sentHigh = 0;
        public void initialize() {
            flipFlopState = false;
            for (final String input : inputs) {
                conjunctionState.put(input, false);
            }
        }
        @SneakyThrows
        public void activate(final boolean state, final String fromInput, final Map<String, Module> moduleMap, final long index) {
            if (type == ModuleType.BROADCASTER) {
                for (final String output : outputs) {
                    if (state) {
                        sentHigh += 1;
                    } else {
                        sentLow += 1;
                    }
                    if (moduleMap.containsKey(output)) {
                        moduleMap.get(output).activate(state, name, moduleMap, index);
                    } else {
                        //LogUtilities.logPurple(state + " to " + output);
                        if (!state) {
                            throw new Exception("RX");
                        }
                    }
                }
            } else if (type == ModuleType.FLIPFLOP) {
                if (!state) {
                    flipFlopState = !flipFlopState;
                    for (final String output : outputs) {
                        if (flipFlopState) {
                            sentHigh += 1;
                        } else {
                            sentLow += 1;
                        }
                        if (moduleMap.containsKey(output)) {
                            moduleMap.get(output).activate(flipFlopState, name, moduleMap, index);
                        } else {
                            //LogUtilities.logPurple(flipFlopState + " to " + output);
                            if (!flipFlopState) {
                                throw new Exception("RX");
                            }
                        }
                    }
                }
            } else if (type == ModuleType.CONJUNCTION) {
                conjunctionState.put(fromInput, state);
                boolean allHigh = true;
                for (final Boolean value : conjunctionState.values()) {
                    if (!value) {
                        allHigh = false;
                        break;
                    }
                }
                for (final String output : outputs) {
                    boolean toSend = !allHigh;
                    if (toSend) {
                        sentHigh += 1;
                    } else {
                        sentLow += 1;
                    }
                    if (moduleMap.containsKey(output)) {
                        if (toSend && output.equals("kc")) {
                            LogUtilities.logPurple(name + " to " + output + " -> " + index);
                        }
                        moduleMap.get(output).activate(toSend, name, moduleMap, index);
                    } else {
                        //LogUtilities.logPurple(toSend + " to " + output);
                        if (!toSend) {
                            throw new Exception("RX");
                        }
                    }
                }
            }
        }
    }

    private void runSolution1(final String fileName) throws Exception {
        if (fileName.contains("sample")) {
            return;
        }
        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);
        final Map<String, Module> moduleMap = new HashMap<>();
        for (final String line : input) {
            final List<String> halves = StringUtilities.splitStringIntoList(line, " -> ");
            final List<String> outputs = StringUtilities.splitStringIntoList(halves.get(1), ", ");
            final Module module = new Module();
            module.outputs.addAll(outputs);
            if (StringUtilities.chunkExistsAtStart(halves.get(0), "%")) {
                module.type = ModuleType.FLIPFLOP;
                module.name = StringUtilities.removeStartChunk(halves.get(0), "%");
            } else if (StringUtilities.chunkExistsAtStart(halves.get(0), "&")) {
                module.type = ModuleType.CONJUNCTION;
                module.name = StringUtilities.removeStartChunk(halves.get(0), "&");
            } else {
                module.type = ModuleType.BROADCASTER;
                module.name = halves.get(0);
            }
            moduleMap.put(module.name, module);
        }
        for (final Module module : moduleMap.values()) {
            for (final String output : module.outputs) {
                if (moduleMap.containsKey(output)) {
                    moduleMap.get(output).inputs.add(module.name);
                }
            }
        }
        for (final Module module : moduleMap.values()) {
            module.initialize();
        }

        int count = 1000;
        long lowCount = 0;
        long highCount = 0;
        long index = 0;
        while (index > -1) {
            lowCount += 1;
            index += 1;
            moduleMap.get("broadcaster").activate(false, "button", moduleMap, index);
        }
        for (final Module module : moduleMap.values()) {
            lowCount += module.sentLow;
            highCount += module.sentHigh;
        }
        long multiple = lowCount * highCount;
        LogUtilities.logPurple("Low count: " + lowCount);
        LogUtilities.logPurple("High count: " + highCount);
        LogUtilities.logGreen("Solution 1: " + multiple);
    }

    private void runSolution2(final String fileName) throws Exception {
    }
}
