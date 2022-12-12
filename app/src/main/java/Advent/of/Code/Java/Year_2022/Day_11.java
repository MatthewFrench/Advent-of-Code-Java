package Advent.of.Code.Java.Year_2022;

import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;
import lombok.Data;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Day_11 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2022/11/", this);
    }

    class Item {
        BigInteger originalValue;
        BigInteger value;
        Integer originalMonkey;
        List<Integer> sentTo = new ArrayList<>();
    }

    @Data
    static
    class Monkey {
        List<Item> items;
        final String operationLeft;
        BigInteger operationLeftNumber = null;
        final String operationOperator;
        final String operationRight;
        BigInteger operationRightNumber;
        final BigInteger testDivisibleBy;
        final Integer targetMonkeyTrue;
        final Integer targetMonkeyFalse;
        int inspectedCount = 0;

        public Monkey(final List<Item> items, final String operationLeft, final String operationOperator, final String operationRight, final BigInteger testDivisibleBy, final Integer targetMonkeyTrue, final Integer targetMonkeyFalse) {
            this.items = items;
            this.operationLeft = operationLeft;
            this.operationRight = operationRight;
            this.testDivisibleBy = testDivisibleBy;
            this.targetMonkeyTrue = targetMonkeyTrue;
            this.targetMonkeyFalse = targetMonkeyFalse;
            this.operationOperator = operationOperator;
            if (!this.operationLeft.equals("old")) {
                this.operationLeftNumber = BigInteger.valueOf(Long.parseLong(this.operationLeft));
            }
            if (!this.operationRight.equals("old")) {
                this.operationRightNumber = BigInteger.valueOf(Long.parseLong(this.operationRight));
            }
        }

        public void doRound(final List<Monkey> monkeys, final BigInteger divisibleTotal) {
            while (items.size() > 0) {
                final Item itemObject = items.remove(0);
                final BigInteger itemWorryLevel = itemObject.value;
                final BigInteger valueLeft = operationLeftNumber == null ? itemWorryLevel : operationLeftNumber;
                final BigInteger valueRight = operationRightNumber == null ? itemWorryLevel : operationRightNumber;
                BigInteger newValue;
                final Integer goesToMonkey;
                if (operationOperator.equals("+")) {
                    newValue = valueLeft.add(valueRight);
                } else if (operationOperator.equals("*")) {
                    newValue = valueLeft.multiply(valueRight);
                } else {
                    throw new RuntimeException("Invalid operation: " + operationOperator);
                }
                newValue = newValue.mod(divisibleTotal);
                if (newValue.mod(testDivisibleBy).equals(BigInteger.ZERO)) {
                    goesToMonkey = targetMonkeyTrue;
                } else {
                    goesToMonkey = targetMonkeyFalse;
                }
                itemObject.value = newValue;
                itemObject.sentTo.add(goesToMonkey);
                monkeys.get(goesToMonkey).items.add(itemObject);
                inspectedCount += 1;
            }
        }
    }

    public void executeWithInput(final String fileName) throws Exception {
        final List<String> inputRows = StringUtilities.splitStringIntoList(LoadUtilities.loadTextFileAsString(fileName), "\n\n");
        final List<Item> allItems = new ArrayList<>();

        final List<BigInteger> divisibleByList = new ArrayList<>();
        final List<Monkey> monkeys = new ArrayList<>();
        for (final String monkeyDefinition : inputRows) {
            final List<String> definitionLines = StringUtilities.splitStringIntoList(monkeyDefinition, "\n");
            //  Starting items: 79, 98
            final List<String> itemStrings = StringUtilities.splitStringIntoList(StringUtilities.removeStartChunk(definitionLines.get(1), "  Starting items: "), ", ");
            //  Operation: new = old * 19
            final List<String> operation = StringUtilities.splitStringIntoList(StringUtilities.removeStartChunk(definitionLines.get(2), "  Operation: new = "), " ");
            //  Test: divisible by 23
            final long divisibleBy = Long.parseLong(StringUtilities.removeStartChunk(definitionLines.get(3), "  Test: divisible by "));
            //    If true: throw to monkey 2
            final int trueMonkey = Integer.parseInt(StringUtilities.removeStartChunk(definitionLines.get(4), "    If true: throw to monkey "));
            //    If false: throw to monkey 3
            final int falseMonkey = Integer.parseInt(StringUtilities.removeStartChunk(definitionLines.get(5), "    If false: throw to monkey "));
            final List<Item> items = new ArrayList<>();
            for (final String item : itemStrings) {
                final Item itemObject = new Item();
                itemObject.value = BigInteger.valueOf(Long.parseLong(item));
                itemObject.sentTo.add(monkeys.size());
                itemObject.originalValue = itemObject.value;
                itemObject.originalMonkey = monkeys.size();
                items.add(itemObject);
                allItems.add(itemObject);
            }
            divisibleByList.add(BigInteger.valueOf(divisibleBy));
            monkeys.add(new Monkey(items, operation.get(0), operation.get(1), operation.get(2), BigInteger.valueOf(divisibleBy), trueMonkey, falseMonkey));
        }
        BigInteger divisibleTotal = BigInteger.valueOf(1);
        for (final BigInteger divisible : divisibleByList) {
            divisibleTotal = divisibleTotal.multiply(divisible);
        }
        /*
        final BigInteger divisibleByAll;
        {
            BigInteger currentNumber = BigInteger.valueOf(1);
            whileLoop:
            while (true) {
                for (final BigInteger checkDivisible : divisibleByList) {
                    if (!currentNumber.mod(checkDivisible).equals(BigInteger.ZERO)) {
                        currentNumber = currentNumber.add(BigInteger.ONE);
                        continue whileLoop;
                    }
                }
                divisibleByAll = currentNumber;
                break;
            }
        }
         */
        //LogUtilities.logPurple("Divisible by all: " + divisibleByAll);
        for (int round = 0; round < 10000; round++) {
            for (final Monkey monkey : monkeys) {
                monkey.doRound(monkeys, divisibleTotal);
            }
            //LogUtilities.logPurple("Round: " + round);
        }

        monkeys.sort(Comparator.comparingLong(Monkey::getInspectedCount).reversed());

        LogUtilities.logGreen("Solution: " + (BigInteger.valueOf(monkeys.get(0).inspectedCount).multiply(BigInteger.valueOf(monkeys.get(1).inspectedCount))));
    }
}
