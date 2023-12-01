package Advent.of.Code.Java.Year_2022;

import Advent.of.Code.Java.Utility.DataUtilities;
import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Day_21 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2022/21/", this);
    }

    static class Monkey {
        public String monkeyName;
        public Monkey leftValueMonkey;
        public Monkey rightValueMonkey;
        public String operator;
        public Long value;
        public long calculate() {
            Long returnValue = null;
            if (value != null) {
                returnValue = value;
            } else
            if (operator.equals("+")) {
                returnValue =  leftValueMonkey.calculate() + rightValueMonkey.calculate();
            } else
            if (operator.equals("-")) {
                returnValue =  leftValueMonkey.calculate() - rightValueMonkey.calculate();
            } else
            if (operator.equals("*")) {
                returnValue =  leftValueMonkey.calculate() * rightValueMonkey.calculate();
            } else
            if (operator.equals("/")) {
                returnValue =  leftValueMonkey.calculate() / rightValueMonkey.calculate();
            }
            if (returnValue != null) {
                return returnValue;
            }
            throw new RuntimeException("Unknown operation");
        }

        public boolean containsMonkey(final String name) {
            if (Objects.equals(this.monkeyName, name)) {
                return true;
            }
            if (leftValueMonkey != null && leftValueMonkey.containsMonkey(name)) {
                return true;
            }
            if (rightValueMonkey != null && rightValueMonkey.containsMonkey(name)) {
                return true;
            }
            return false;
        }

        public Monkey(String monkeyName) {
            this.monkeyName = monkeyName;
        }
    }

    public void executeWithInput(final String fileName) throws Exception {
        {
            List<String> originalInput = StringUtilities.splitStringIntoList(LoadUtilities.loadTextFileAsString(fileName), "\n");
            Map<String, Monkey> monkeyNameMap = new HashMap<>();
            for (final String line : originalInput) {
                List<String> splitLine = StringUtilities.splitStringIntoList(line, ": ");
                final String name = splitLine.get(0);
                final Monkey monkey = monkeyNameMap.computeIfAbsent(name, Monkey::new);
                final List<String> secondHalfSplit = StringUtilities.splitStringIntoList(splitLine.get(1), " ");
                if (secondHalfSplit.size() == 1) {
                    monkey.value = Long.parseLong(secondHalfSplit.get(0));
                } else {
                    final String operation = secondHalfSplit.get(1);
                    final Monkey monkey1 = monkeyNameMap.computeIfAbsent(secondHalfSplit.get(0), (monkeyName) -> new Monkey(monkeyName));
                    final Monkey monkey2 = monkeyNameMap.computeIfAbsent(secondHalfSplit.get(2), (monkeyName) -> new Monkey(monkeyName));
                    monkey.operator = operation;
                    monkey.leftValueMonkey = monkey1;
                    monkey.rightValueMonkey = monkey2;
                }
            }

            LogUtilities.logGreen("Solution: " + monkeyNameMap.get("root").calculate());
        }

        {
            List<String> originalInput = StringUtilities.splitStringIntoList(LoadUtilities.loadTextFileAsString(fileName), "\n");
            Map<String, Monkey> monkeyNameMap = new HashMap<>();
            for (final String line : originalInput) {
                List<String> splitLine = StringUtilities.splitStringIntoList(line, ": ");
                final String name = splitLine.get(0);
                final Monkey monkey = monkeyNameMap.computeIfAbsent(name, Monkey::new);
                final List<String> secondHalfSplit = StringUtilities.splitStringIntoList(splitLine.get(1), " ");
                if (secondHalfSplit.size() == 1) {
                    monkey.value = Long.parseLong(secondHalfSplit.get(0));
                } else {
                    final String operation = secondHalfSplit.get(1);
                    final Monkey monkey1 = monkeyNameMap.computeIfAbsent(secondHalfSplit.get(0), (monkeyName) -> new Monkey(monkeyName));
                    final Monkey monkey2 = monkeyNameMap.computeIfAbsent(secondHalfSplit.get(2), (monkeyName) -> new Monkey(monkeyName));
                    monkey.operator = operation;
                    monkey.leftValueMonkey = monkey1;
                    monkey.rightValueMonkey = monkey2;
                }
            }

            Monkey rootMonkey = monkeyNameMap.get("root");
            Monkey leftMonkey = rootMonkey.leftValueMonkey;
            Monkey rightMonkey = rootMonkey.rightValueMonkey;
            if (rightMonkey.containsMonkey("humn")) {
                Monkey tempMonkey = leftMonkey;
                leftMonkey = rightMonkey;
                rightMonkey = tempMonkey;
            }
            // Now do all calculations repeatedly.
            long rightValue = rightMonkey.calculate();
            rootMonkey = leftMonkey;
            while (!Objects.equals(rootMonkey.monkeyName, "humn")) {
                if (rootMonkey.rightValueMonkey == null) {
                    LogUtilities.log("");
                }
                leftMonkey = rootMonkey.leftValueMonkey;
                rightMonkey = rootMonkey.rightValueMonkey;
                if (rightMonkey.containsMonkey("humn")) {
                    if (Objects.equals(rootMonkey.operator, "+")) {
                        rightValue -= leftMonkey.calculate();
                    } else if (Objects.equals(rootMonkey.operator, "-")) {
                        rightValue -= leftMonkey.calculate();
                        rightValue *= -1;
                    } else if (Objects.equals(rootMonkey.operator, "/")) {
                        rightValue = leftMonkey.calculate() / rightValue;

                    } else if (Objects.equals(rootMonkey.operator, "*")) {
                        rightValue /= leftMonkey.calculate();
                    }
                    rootMonkey = rightMonkey;
                } else {
                    if (Objects.equals(rootMonkey.operator, "+")) {
                        rightValue -= rightMonkey.calculate();
                    } else if (Objects.equals(rootMonkey.operator, "-")) {
                        rightValue += rightMonkey.calculate();
                    } else if (Objects.equals(rootMonkey.operator, "/")) {
                        rightValue *= rightMonkey.calculate();
                    } else if (Objects.equals(rootMonkey.operator, "*")) {
                        rightValue /= rightMonkey.calculate();
                    }
                    rootMonkey = leftMonkey;
                }
            }

            LogUtilities.logGreen("Solution 2: " + rightValue);
        }
    }
}
