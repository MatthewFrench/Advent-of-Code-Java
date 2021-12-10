package Advent.of.Code.Java.Year_2021;

import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;
import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day_10 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2021/10/", this);
    }

    static final List<Operator> OPERATORS = ImmutableList.of(
            new Operator("(", ")", 3, 1),
            new Operator("[", "]", 57, 2),
            new Operator("{", "}", 1197, 3),
            new Operator("<", ">", 25137, 4)
    );
    static final Map<String, Operator> START_OPERATORS_MAP = OPERATORS.stream().collect(Collectors.toMap(Operator::getStart, Function.identity()));
    static final Map<String, Operator> END_OPERATORS_MAP = OPERATORS.stream().collect(Collectors.toMap(Operator::getEnd, Function.identity()));

    public void executeWithInput(final String fileName) throws Exception {
        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);

        int points = 0;
        List<Long> completePoints = new ArrayList<>();
        lineLoop:
        for (final String line : input) {
            Operator currentOperator = null;
            List<Operator> operatorStack = new ArrayList<>();
            for (int i = 0; i < line.length(); i++) {
                final String character = line.charAt(i) + "";
                final boolean isStartOperator = START_OPERATORS_MAP.containsKey(character);
                final Operator characterOperator = isStartOperator ? START_OPERATORS_MAP.get(character) : END_OPERATORS_MAP.get(character);
                if (isStartOperator) {
                    operatorStack.add(characterOperator);
                    currentOperator = characterOperator;
                } else {
                    if (characterOperator != currentOperator) {
                        points += characterOperator.getPoints();
                        continue lineLoop;
                    } else {
                        operatorStack.remove(operatorStack.size() - 1);
                        if (operatorStack.size() > 0) {
                            currentOperator = operatorStack.get(operatorStack.size() - 1);
                        } else {
                            currentOperator = null;
                        }
                    }
                }
            }
            Collections.reverse(operatorStack);
            // Now complete the lines
            long completePoint = 0;
            for (final Operator operator : operatorStack) {
                completePoint = completePoint * 5 + operator.completePoints;
            }
            completePoints.add(completePoint);
        }
        completePoints.sort(Long::compareTo);

        LogUtilities.logGreen("Solution: " + points);
        LogUtilities.logGreen("Complete points: " + completePoints.get(completePoints.size()/2));


    }
    @AllArgsConstructor
    @EqualsAndHashCode
    @Getter
    static class Operator {
        String start;
        String end;
        long points;
        long completePoints;
    }
}
