package Advent.of.Code.Java.Year_2023;

import Advent.of.Code.Java.Utility.DataUtilities;
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
        public List<Action> activate(final boolean state, final String fromInput, final Map<String, Module> moduleMap, final long index) {
            final List<Action> newActions = new ArrayList<>();
            if (type == ModuleType.BROADCASTER) {
                for (final String output : outputs) {
                    if (state) {
                        sentHigh += 1;
                    } else {
                        sentLow += 1;
                    }
                    if (moduleMap.containsKey(output)) {
                        final Action action = new Action();
                        action.index = index;
                        action.signal = state;
                        action.fromModule = name;
                        action.toModule = output;
                        newActions.add(action);
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
                            final Action action = new Action();
                            action.index = index;
                            action.signal = flipFlopState;
                            action.fromModule = name;
                            action.toModule = output;
                            newActions.add(action);
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
                        final Action action = new Action();
                        action.index = index;
                        action.signal = toSend;
                        action.fromModule = name;
                        action.toModule = output;
                        newActions.add(action);
                    }
                }
            }
            return newActions;
        }
    }

    class Action {
        String fromModule;
        String toModule;
        boolean signal;
        long index;
    }

    private Map<String, Module> getModuleMap(final List<String> input) {
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
        return moduleMap;
    }

    private void runSolution1(final String fileName) throws Exception {
        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);
        final Map<String, Module> moduleMap = getModuleMap(input);
        for (final Module module : moduleMap.values()) {
            module.initialize();
        }

        final ArrayList<Action> actions = new ArrayList<>();
        int count = 1000;
        long lowCount = 0;
        long highCount = 0;
        for (int index = 1; index <= count; index++) {
            lowCount += 1;
            final Action startingAction = new Action();
            startingAction.fromModule = "button";
            startingAction.toModule = "broadcaster";
            startingAction.index = index;
            startingAction.signal = false;
            actions.add(startingAction);
            while (!actions.isEmpty()) {
                final Action action = actions.removeFirst();
                actions.addAll(moduleMap.get(action.toModule).activate(action.signal, action.fromModule, moduleMap, action.index));
            }
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
        if (fileName.contains("sample")) {
            return;
        }
        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);
        final Map<String, Module> moduleMap = getModuleMap(input);
        for (final Module module : moduleMap.values()) {
            module.initialize();
        }

        final ArrayList<Action> actions = new ArrayList<>();
        int count = 1_000_000;
        Long vn = null;
        Long ph = null;
        Long hn = null;
        Long kt = null;
        for (int index = 1; index <= count; index++) {
            final Action startingAction = new Action();
            startingAction.fromModule = "button";
            startingAction.toModule = "broadcaster";
            startingAction.index = index;
            startingAction.signal = false;
            actions.add(startingAction);
            while (!actions.isEmpty()) {
                final Action action = actions.removeFirst();
                if (action.toModule.equals("kc") && action.signal) {
                    if (action.fromModule.equals("vn") && vn == null) {
                        vn = action.index;
                    }
                    if (action.fromModule.equals("ph") && ph == null) {
                        ph = action.index;
                    }
                    if (action.fromModule.equals("hn") && hn == null) {
                        hn = action.index;
                    }
                    if (action.fromModule.equals("kt") && kt == null) {
                        kt = action.index;
                    }
                }
                actions.addAll(moduleMap.get(action.toModule).activate(action.signal, action.fromModule, moduleMap, action.index));
            }
        }
        LogUtilities.logGreen("Solution 2: " + NumberUtilities.getLowestCommonMultiple(DataUtilities.List(vn, ph, hn, kt)));
    }
}
