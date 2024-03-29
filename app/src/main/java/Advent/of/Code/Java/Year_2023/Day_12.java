package Advent.of.Code.Java.Year_2023;

import Advent.of.Code.Java.Utility.DataUtilities;
import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.NumberUtilities;
import Advent.of.Code.Java.Utility.SimpleParallelism;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;
import Advent.of.Code.Java.Utility.TimeUtilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Day_12 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2023/12/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        runSolution1(fileName);
        runSolution2(fileName);
    }

    private void runSolution1(final String fileName) throws Exception {
        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);

        long possibilities = 0;
        long count = 0;
        for (final String line : input) {
            final List<String> sides = StringUtilities.splitStringIntoList(line, " ");
            final List<Integer> numbers = DataUtilities.transformData(StringUtilities.splitStringIntoList(sides.get(1), ","), Integer::parseInt);
            final List<CharacterType> characters = DataUtilities.transformData(StringUtilities.splitStringIntoList(sides.get(0), ""), s -> {
                if (s.equals("?")) {
                    return CharacterType.UNKNOWN;
                }
                if (s.equals("#")) {
                    return CharacterType.NUMBER;
                }
                if (s.equals(".")) {
                    return CharacterType.SEPARATOR;
                }
                return null;
            });
            final long startMilliseconds = TimeUtilities.getCurrentMilliseconds();


            long calculatedPossibilities = countPossibilitiesChopped(characterListToGroup(characters), numbers);
            possibilities += calculatedPossibilities;
            count += 1;
            LogUtilities.logPurple("Progress: " + count + " / " + input.size());

            final long totalMilliseconds = TimeUtilities.getCurrentMilliseconds() - startMilliseconds;
            LogUtilities.logPurple(TimeUtilities.getTimeAsString()
                    + "  Progress: " + count + " / " + input.size()
                    + " - Calculated: " + NumberUtilities.formatNumber(calculatedPossibilities)
                    + " - " + TimeUtilities.getMillisecondTimeAsString(totalMilliseconds)
                    + " - " + line
            );
        }

        LogUtilities.logGreen("Solution 1: " + possibilities);
    }

    private void runSolution2(final String fileName) throws Exception {
        final long initialMilliseconds = TimeUtilities.getCurrentMilliseconds();
        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);
        // Sort the input randomly so that we get a better estimate on time as we do calculations
        Collections.shuffle(input);

        AtomicLong possibilities = new AtomicLong();
        AtomicLong count = new AtomicLong();

        final SimpleParallelism simpleParallelism = new SimpleParallelism(input.size());
        for (final String line : input) {
            final List<String> sides = StringUtilities.splitStringIntoList(line, " ");
            final List<Integer> numbers = DataUtilities.transformData(StringUtilities.splitStringIntoList(sides.get(1), ","), Integer::parseInt);
            final List<CharacterType> characters = DataUtilities.transformData(StringUtilities.splitStringIntoList(sides.get(0), ""), s -> {
                if (s.equals("?")) {
                    return CharacterType.UNKNOWN;
                }
                if (s.equals("#")) {
                    return CharacterType.NUMBER;
                }
                if (s.equals(".")) {
                    return CharacterType.SEPARATOR;
                }
                return null;
            });
            final List<Integer> expandedNumbers = new ArrayList<>();
            final List<CharacterType> expandedCharacters = new ArrayList<>();
            for (int index = 0; index < 5; index++) {
                expandedNumbers.addAll(numbers);
                if (!expandedCharacters.isEmpty()) {
                    expandedCharacters.add(CharacterType.UNKNOWN);
                }
                expandedCharacters.addAll(characters);
            }
            final CharacterGroup expandedCharacterGroup = characterListToGroup(expandedCharacters);

            simpleParallelism.add(() -> {
                final long startMilliseconds = TimeUtilities.getCurrentMilliseconds();
                final long calculatedPossibilities = countPossibilitiesChopped(expandedCharacterGroup, expandedNumbers);
                possibilities.addAndGet(calculatedPossibilities);
                long completedCount = count.addAndGet(1);
                final long totalMilliseconds = TimeUtilities.getCurrentMilliseconds() - startMilliseconds;
                long totalExecutionMilliseconds = TimeUtilities.getCurrentMilliseconds() - initialMilliseconds;
                double millisecondsPerCompletedItem = (double) totalExecutionMilliseconds / completedCount;
                long itemsLeftToComplete = input.size() - completedCount;
                double remainingMilliseconds = millisecondsPerCompletedItem * itemsLeftToComplete;
                LogUtilities.logPurple(TimeUtilities.getTimeAsString()
                        + "  Progress: " + completedCount + " / " + input.size()
                        + " - Calculated: " + NumberUtilities.formatNumber(calculatedPossibilities)
                        + " - " + TimeUtilities.getMillisecondTimeAsString(totalMilliseconds)
                        + " - Estimated remaining time: " + TimeUtilities.getMillisecondTimeAsString((long) remainingMilliseconds)
                        + " - " + line
                );
            });
        }
        simpleParallelism.waitForCompletion();

        LogUtilities.logGreen("Solution 2: " + possibilities);
    }

    private CharacterGroup characterListToGroup(final List<CharacterType> characters) {
        final CharacterGroup startingNode = new CharacterGroup();
        startingNode.type = characters.getFirst();
        startingNode.count = 0;
        CharacterGroup currentNode = startingNode;
        for (final CharacterType type : characters) {
            if (currentNode.type != type) {
                final CharacterGroup newNode = new CharacterGroup();
                newNode.type = type;
                newNode.count = 0;
                currentNode.nextGroup = newNode;
                newNode.priorGroup = currentNode;
                currentNode = newNode;
            }
            currentNode.count += 1;
        }
        return startingNode;
    }
    private List<CharacterType> characterGroupToList(final CharacterGroup characterGroup) {
        final List<CharacterType> characterList = new ArrayList<>();
        CharacterGroup currentNode = characterGroup;
        while (currentNode != null) {
            for (int index = 0; index < currentNode.count; index++) {
                characterList.add(currentNode.type);
            }
            currentNode = currentNode.nextGroup;
        }
        return characterList;
    }

    enum CharacterType {
        UNKNOWN,
        SEPARATOR,
        NUMBER
    }

    class CharacterGroup {
        CharacterGroup priorGroup = null;
        CharacterGroup nextGroup = null;
        CharacterType type;
        // The number of characters in this group
        int count = 0;
        public CharacterGroup copy() {
            final CharacterGroup newCharacterGroup = new CharacterGroup();
            newCharacterGroup.type = type;
            newCharacterGroup.count = count;
            if (nextGroup != null) {
                newCharacterGroup.nextGroup = nextGroup.copy();
                newCharacterGroup.nextGroup.priorGroup = newCharacterGroup;
            }
            return newCharacterGroup;
        }
    }

    class SpringArrangement {
        List<Integer> validNumbers;
        Long totalValidNumberCount;
        // Total number of characters in the arrangement
        long totalCharacterCount;
        // Total number of number characters in the arrangement
        long totalNumberCharacterCount;
        long totalUnknownCharacterCount;
        CharacterGroup startNode;

        public boolean numberGroupsMatch(final List<Integer> validNumbers) {
            CharacterGroup currentSearchGroup = startNode;
            int index = 0;
            while (currentSearchGroup != null) {
                if (currentSearchGroup.type == CharacterType.NUMBER) {
                    if (index >= validNumbers.size()) {
                        return false;
                    }
                    if (validNumbers.get(index) != currentSearchGroup.count) {
                        return false;
                    }
                    index += 1;
                }
                currentSearchGroup = currentSearchGroup.nextGroup;
            }
            if (index < validNumbers.size()) {
                return false;
            }
            return true;
        }

        public boolean numberGroupsValidUntilUnknown(final List<Integer> validNumbers) {
            CharacterGroup currentSearchGroup = startNode;
            int index = 0;
            while (currentSearchGroup != null) {
                if (currentSearchGroup.type == CharacterType.NUMBER) {
                    if (index >= validNumbers.size()) {
                        return false;
                    }
                    if (currentSearchGroup.nextGroup != null && currentSearchGroup.nextGroup.type == CharacterType.UNKNOWN) {
                        if (currentSearchGroup.count > validNumbers.get(index)) {
                            return false;
                        }
                        break;
                    } else {
                        if (validNumbers.get(index) != currentSearchGroup.count) {
                            return false;
                        }
                    }
                    index += 1;
                } else if (currentSearchGroup.type == CharacterType.UNKNOWN) {
                    break;
                }
                currentSearchGroup = currentSearchGroup.nextGroup;
            }
            return true;
        }

        // Example: .....??...?##.
        //    Calculate as two groups
        // Example: .....#?...?##.
        //    Calculate as two groups
        // Example: .....??...???.
        //    Calculate as three groups
        // Example: .....??...???#.
        //    Calculate as three groups
        // Example:   ....#??...?##.
        //    Calculate as three groups
        // Example:   ....#??#...?##.
        //    Calculate as two groups
        // Example:   ....#???#...?##.
        //    Calculate as three groups
        public boolean hasValidMaxPossibleNumberGroupCount(long validNumberGroupCount) {
            // We're hard-coding the usage of UNKNOWN and NUMBER
            CharacterGroup currentSearchGroup = startNode;
            long maxPossibleGroupCount = 0;
            while (currentSearchGroup != null) {
                if (currentSearchGroup.type == CharacterType.NUMBER) {
                    maxPossibleGroupCount += 1;
                    if (maxPossibleGroupCount >= validNumberGroupCount) {
                        return true;
                    }
                } else if (currentSearchGroup.type == CharacterType.UNKNOWN) {
                    boolean numberTouchingLeft = currentSearchGroup.priorGroup != null && currentSearchGroup.priorGroup.type == CharacterType.NUMBER;
                    boolean numberTouchingRight = currentSearchGroup.nextGroup != null && currentSearchGroup.nextGroup.type == CharacterType.NUMBER;
                    int unknownCount = currentSearchGroup.count;
                    if (numberTouchingLeft) {
                        unknownCount -= 1;
                    }
                    if (numberTouchingRight) {
                        unknownCount -= 1;
                    }
                    maxPossibleGroupCount += (long) Math.ceil(((double) Math.max(unknownCount, 0)) / 2);
                    if (maxPossibleGroupCount >= validNumberGroupCount) {
                        return true;
                    }
                }
                currentSearchGroup = currentSearchGroup.nextGroup;
            }
            return false;
        }

        public boolean validPossibleCount(final long totalValidNumberCount) {
            CharacterGroup currentSearchGroup = startNode;
            long count = 0;
            while (currentSearchGroup != null) {
                if (currentSearchGroup.type == CharacterType.NUMBER || currentSearchGroup.type == CharacterType.UNKNOWN) {
                    count += currentSearchGroup.count;
                    if (count >= totalValidNumberCount) {
                        return true;
                    }
                }
                currentSearchGroup = currentSearchGroup.nextGroup;
            }
            return count >= totalValidNumberCount;
        }

        public long calculateCharacterCount(final CharacterType characterType) {
            CharacterGroup currentSearchGroup = startNode;
            long count = 0;
            while (currentSearchGroup != null) {
                if (currentSearchGroup.type == characterType) {
                    count += currentSearchGroup.count;
                }
                currentSearchGroup = currentSearchGroup.nextGroup;
            }
            return count;
        }

        public long calculateCharacterCount() {
            CharacterGroup currentSearchGroup = startNode;
            long count = 0;
            while (currentSearchGroup != null) {
                count += currentSearchGroup.count;
                currentSearchGroup = currentSearchGroup.nextGroup;
            }
            return count;
        }

        public void trimUnneededNodes() {
            // Example: 1, 1, 3 - #??.### goes to 1, 3 - ?.###
            CharacterGroup currentSearchGroup = startNode;
            while (currentSearchGroup != null) {
                if (currentSearchGroup.type == CharacterType.NUMBER) {
                    if (validNumbers.size() > 1 && currentSearchGroup.count == validNumbers.getFirst()) {
                        if (currentSearchGroup.nextGroup != null && currentSearchGroup.nextGroup.type == CharacterType.SEPARATOR) {
                            validNumbers.removeFirst();
                            totalValidNumberCount -= currentSearchGroup.count;
                            removeNode(currentSearchGroup);
                            totalNumberCharacterCount -= currentSearchGroup.count;
                            totalCharacterCount -= currentSearchGroup.count;
                        } else if (currentSearchGroup.nextGroup != null && currentSearchGroup.nextGroup.type == CharacterType.UNKNOWN) {
                            // Pull an unknown to be a separator, it is the only choice here
                            validNumbers.removeFirst();
                            totalValidNumberCount -= currentSearchGroup.count;
                            totalNumberCharacterCount -= currentSearchGroup.count;
                            totalCharacterCount -= currentSearchGroup.count;
                            if (currentSearchGroup.nextGroup.count == 1) {
                                currentSearchGroup.nextGroup.type = CharacterType.SEPARATOR;
                                totalUnknownCharacterCount -= 1;
                                removeNode(currentSearchGroup);
                            } else {
                                totalUnknownCharacterCount -= 1;
                                currentSearchGroup.nextGroup.count -= 1;
                                currentSearchGroup.count = 1;
                                currentSearchGroup.type = CharacterType.SEPARATOR;
                            }
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                } else if (currentSearchGroup.type == CharacterType.SEPARATOR) {
                    totalCharacterCount -= currentSearchGroup.count;
                    removeNode(currentSearchGroup);
                } else {
                    return;
                }
                currentSearchGroup = currentSearchGroup.nextGroup;
            }
        }

        private void removeNode(final CharacterGroup node) {
            final CharacterGroup priorNode = node.priorGroup;
            final CharacterGroup nextNode = node.nextGroup;
            if (nextNode != null) {
                nextNode.priorGroup = priorNode;
            }
            if (priorNode != null) {
                priorNode.nextGroup = nextNode;
            }
            if (node == startNode) {
                startNode = nextNode;
            }
        }

        public void replaceNextUnknownWith(final CharacterType characterType) {
            CharacterGroup currentSearchGroup = startNode;
            while (currentSearchGroup != null) {
                if (currentSearchGroup.type == CharacterType.UNKNOWN) {
                    // Shift and merge groups left
                    if (currentSearchGroup.count > 1) {
                        currentSearchGroup.count -= 1;
                        if (currentSearchGroup.priorGroup == null) {
                            // Replace start node
                            currentSearchGroup.priorGroup = new CharacterGroup();
                            currentSearchGroup.priorGroup.type = characterType;
                            currentSearchGroup.priorGroup.nextGroup = currentSearchGroup;
                            currentSearchGroup.priorGroup.count = 1;
                            startNode = currentSearchGroup.priorGroup;
                        } else {
                            if (currentSearchGroup.priorGroup.type == characterType) {
                                currentSearchGroup.priorGroup.count += 1;
                            } else {
                                // Splice in a new node to the left
                                final CharacterGroup farLeftNode = currentSearchGroup.priorGroup;
                                final CharacterGroup leftNode = new CharacterGroup();
                                leftNode.type = characterType;
                                leftNode.count = 1;
                                farLeftNode.nextGroup = leftNode;
                                leftNode.priorGroup = farLeftNode;
                                leftNode.nextGroup = currentSearchGroup;
                                currentSearchGroup.priorGroup = leftNode;
                            }
                        }
                    } else {
                        // Delete this group and either splice in a new group or merge groups
                        currentSearchGroup.type = characterType;
                        // Check left group to see if it needs merged with current group
                        if (currentSearchGroup.priorGroup != null) {
                            final CharacterGroup leftNode = currentSearchGroup.priorGroup;
                            final CharacterGroup rightNode = currentSearchGroup.nextGroup;
                            if (leftNode.type == characterType) {
                                leftNode.count += 1;
                                if (rightNode != null) {
                                    rightNode.priorGroup = leftNode;
                                }
                                leftNode.nextGroup = rightNode;
                                // Collapse current search group into left node
                                currentSearchGroup = leftNode;
                                if (leftNode.priorGroup == null) {
                                    startNode = leftNode;
                                }
                            }
                        }
                        // Merge the right group into the current group if the right group is the same type
                        if (currentSearchGroup.nextGroup != null) {
                            final CharacterGroup rightNode = currentSearchGroup.nextGroup;
                            if (rightNode.type == currentSearchGroup.type) {
                                currentSearchGroup.count += rightNode.count;
                                currentSearchGroup.nextGroup = rightNode.nextGroup;
                                if (currentSearchGroup.nextGroup != null) {
                                    currentSearchGroup.nextGroup.priorGroup = currentSearchGroup;
                                }
                            }
                        }
                    }
                    totalUnknownCharacterCount -= 1;
                    if (characterType == CharacterType.NUMBER) {
                        totalNumberCharacterCount += 1;
                    }
                    return;
                }
                currentSearchGroup = currentSearchGroup.nextGroup;
            }
        }

        public String prettyPrint() {
            String result = "";
            CharacterGroup currentSearchGroup = startNode;
            while (currentSearchGroup != null) {
                for (int index = 0; index < currentSearchGroup.count; index++) {
                    if (currentSearchGroup.type == CharacterType.UNKNOWN) {
                        result += "?";
                    }
                    if (currentSearchGroup.type == CharacterType.NUMBER) {
                        result += "#";
                    }
                    if (currentSearchGroup.type == CharacterType.SEPARATOR) {
                        result += ".";
                    }
                }
                currentSearchGroup = currentSearchGroup.nextGroup;
            }
            return result;
        }

        public SpringArrangement copy() {
            final SpringArrangement newArrangement = new SpringArrangement();
            newArrangement.startNode = startNode.copy();
            newArrangement.totalValidNumberCount = totalValidNumberCount;
            newArrangement.validNumbers = new ArrayList<>(validNumbers);
            newArrangement.totalCharacterCount = totalCharacterCount;
            newArrangement.totalNumberCharacterCount = totalNumberCharacterCount;
            newArrangement.totalUnknownCharacterCount = totalUnknownCharacterCount;
            return newArrangement;
        }
    }


    // Optimization: I could separate the line on a separator with an even number of question marks on both sides
    // then I could do every combination of shifting the numbers to the left of the line and to the right of the line. Multiply
    // the left half and right half solutions and then add the different combinations of shifting. That may product the same number,
    // but the fact that we're multiplying instead of adding to get the solution count would drastically reduce the number of
    // computation cycles.
        /*
        Example: ?#???#?.?.#?#?#?#??? 2,2,1,7,1
        Split into: ?#???#?.? and #?#?#?#???
        Add calculations:
            [] * [2,2,1,7,1] +
            [2] * [2,1,7,1] +
            [2,2] * [1,7,1] +
            [2,2,1] * [7,1] +
            [2,2,1,7,1] * []
            Because this is multiplication, shouldn't this be substantially faster than testing each combo?
            Instead of calculating 4,000,000,000 combinations, I can calculate 2,000 by 50 times. Insanely faster.
         */
    private long countPossibilitiesChopped(final CharacterGroup initialCharacterGroup, final List<Integer> originalExpectedNumbers) {
        {
            final SpringArrangement initialSpringArrangement = new SpringArrangement();
            initialSpringArrangement.startNode = initialCharacterGroup;
            final CharacterGroup initialGroupCopy = initialCharacterGroup.copy();
            // First get the number of unknown groups
            long totalNumberOfUnknownGroups = 0;
            CharacterGroup currentCharacterGroup = initialGroupCopy;
            while (currentCharacterGroup != null) {
                if (currentCharacterGroup.type == CharacterType.UNKNOWN) {
                    totalNumberOfUnknownGroups += 1;
                }
                currentCharacterGroup = currentCharacterGroup.nextGroup;
            }
            // Next find the most center separator group
            long passedUnknownGroups = 0;
            CharacterGroup bestSeparatorGroup = null;
            long distanceFromCenter = 0;
            currentCharacterGroup = initialGroupCopy;
            while (currentCharacterGroup != null) {
                if (currentCharacterGroup.type == CharacterType.UNKNOWN) {
                    passedUnknownGroups += 1;
                } else if (currentCharacterGroup.type == CharacterType.SEPARATOR && currentCharacterGroup.priorGroup != null && currentCharacterGroup.nextGroup != null) {
                    long currentDistanceFromCenter = Math.abs(totalNumberOfUnknownGroups / 2 - passedUnknownGroups);
                    if (bestSeparatorGroup == null || currentDistanceFromCenter < distanceFromCenter) {
                        bestSeparatorGroup = currentCharacterGroup;
                        distanceFromCenter = currentDistanceFromCenter;
                    }
                }
                currentCharacterGroup = currentCharacterGroup.nextGroup;
            }
            if (bestSeparatorGroup != null) {
                // Now split the character groups along the separator group
                final CharacterGroup firstHalfStart = initialGroupCopy;
                final CharacterGroup secondHalfStart = bestSeparatorGroup.nextGroup;
                bestSeparatorGroup.priorGroup.nextGroup = null;
                secondHalfStart.priorGroup = null;
                final List<Integer> firstHalfNumbers = new ArrayList<>();
                final List<Integer> secondHalfNumbers = new ArrayList<>(originalExpectedNumbers);
                // Todo: Another optimization idea, could start at the center and go left and right on the number groups so I do less count calls
                // Don't need to continue going left and right when they return 0 results.
                long totalPossibilities = 0;
                // Todo: Another potential optimization, I could recursively have this function call itself if the number array is big enough.
                // Then we can split big items way down as much as needed.
                totalPossibilities += countPossibilities(firstHalfStart, firstHalfNumbers) * countPossibilities(secondHalfStart, secondHalfNumbers);
                while (!secondHalfNumbers.isEmpty()) {
                    firstHalfNumbers.add(secondHalfNumbers.removeFirst());
                    totalPossibilities += countPossibilities(firstHalfStart, firstHalfNumbers) * countPossibilities(secondHalfStart, secondHalfNumbers);
                }
                return totalPossibilities;
            }
        }
        // Split ?? into .. and #. and .# and ##
        List<CharacterType> characters = characterGroupToList(initialCharacterGroup);
        if (characters.size() > Math.max(5, NumberUtilities.maxOfIntegers(originalExpectedNumbers))) {
            int bestIndex = -1;
            int bestIndexFromCenter = 0;
            int midPoint = characters.size() / 2;
            for (int index = 0; index < characters.size() - 1; index++) {
                if (characters.get(index) == CharacterType.UNKNOWN && characters.get(index + 1) == CharacterType.UNKNOWN) {
                    int distanceFromCenter = Math.abs(midPoint - index);
                    if (bestIndex == -1 || distanceFromCenter < bestIndexFromCenter) {
                        bestIndex = index;
                        bestIndexFromCenter = distanceFromCenter;
                    }
                }
            }
            if (bestIndex != -1) {
                List<List<CharacterType>> listsToCount = new ArrayList<>();
                final List<CharacterType> doubleSeparator = new ArrayList<>(characters);
                doubleSeparator.set(bestIndex, CharacterType.SEPARATOR);
                doubleSeparator.set(bestIndex + 1, CharacterType.SEPARATOR);
                listsToCount.add(doubleSeparator);
                final List<CharacterType> leftNumber = new ArrayList<>(characters);
                leftNumber.set(bestIndex, CharacterType.NUMBER);
                leftNumber.set(bestIndex + 1, CharacterType.SEPARATOR);
                listsToCount.add(leftNumber);
                final List<CharacterType> rightNumber = new ArrayList<>(characters);
                rightNumber.set(bestIndex, CharacterType.SEPARATOR);
                rightNumber.set(bestIndex + 1, CharacterType.NUMBER);
                listsToCount.add(rightNumber);
                final List<CharacterType> doubleNumber = new ArrayList<>(characters);
                doubleNumber.set(bestIndex, CharacterType.NUMBER);
                doubleNumber.set(bestIndex + 1, CharacterType.NUMBER);
                listsToCount.add(doubleNumber);
                long totalPossibilities = 0;
                for (final List<CharacterType> characterList : listsToCount) {
                    totalPossibilities += countPossibilitiesChopped(characterListToGroup(characterList), originalExpectedNumbers);
                }
                return totalPossibilities;
            }
        }
        return countPossibilities(initialCharacterGroup, originalExpectedNumbers);
    }

    private long countPossibilities(final CharacterGroup initialCharacterGroup, final List<Integer> originalExpectedNumbers) {
        final long originalTotalNumberCount = NumberUtilities.sumIntegers(originalExpectedNumbers);
        final List<SpringArrangement> possibilities = new ArrayList<>();
        final SpringArrangement startingArrangement = new SpringArrangement();
        startingArrangement.validNumbers = new ArrayList<>(originalExpectedNumbers);
        startingArrangement.totalValidNumberCount = originalTotalNumberCount;
        startingArrangement.startNode = initialCharacterGroup.copy();
        startingArrangement.totalCharacterCount = startingArrangement.calculateCharacterCount();
        startingArrangement.totalNumberCharacterCount = startingArrangement.calculateCharacterCount(CharacterType.NUMBER);
        startingArrangement.totalUnknownCharacterCount = startingArrangement.calculateCharacterCount(CharacterType.UNKNOWN);
        possibilities.add(startingArrangement);
        //final String startingArrangementString = startingArrangement.prettyPrint();
        long validPossibilities = 0;
        long currentMilliseconds = TimeUtilities.getCurrentMilliseconds();
        while (!possibilities.isEmpty()) {
            long nowMilliseconds = TimeUtilities.getCurrentMilliseconds();
            if (nowMilliseconds - currentMilliseconds > 20000) {
                LogUtilities.logBlue("Probabilities so far: " + NumberUtilities.formatNumber(validPossibilities) + ", in array: " + possibilities.size());
                currentMilliseconds = nowMilliseconds;
            }
            final SpringArrangement arrangement = possibilities.removeLast();
            // Temporarily removing optimization to see full results
            arrangement.trimUnneededNodes();
            if (arrangement.totalCharacterCount < arrangement.totalValidNumberCount + (arrangement.validNumbers.size() - 1)) {
                continue;
            }
            boolean shouldAddNumber = arrangement.totalNumberCharacterCount < arrangement.totalValidNumberCount;

            final SpringArrangement newArrangement1 = !shouldAddNumber ? arrangement : arrangement.copy();
            newArrangement1.replaceNextUnknownWith(CharacterType.SEPARATOR);
            if (newArrangement1.totalUnknownCharacterCount == 0) {
                if (isValid(newArrangement1)) {
                    validPossibilities += 1;
                }
            } else if (isPartialValid(newArrangement1)) {
                // If we maxed out on the valid numbers even with unknowns, there are no more possibilities
                // Valid numbers: 1, 6, 5 - #???.######..#####.
                if (newArrangement1.numberGroupsMatch(newArrangement1.validNumbers)) {
                    validPossibilities += 1;
                } else {
                    possibilities.add(newArrangement1);
                }
            }
            if (shouldAddNumber) {
                // Avoid a copy by re-using the prior dead arrangement
                final SpringArrangement newArrangement2 = arrangement;
                newArrangement2.replaceNextUnknownWith(CharacterType.NUMBER);
                if (newArrangement2.totalUnknownCharacterCount == 0) {
                    if (isValid(newArrangement2)) {
                        validPossibilities += 1;
                    }
                } else if (isPartialValid(newArrangement2)) {
                    // If we maxed out on the valid numbers even with unknowns, there are no more possibilities
                    // Valid numbers: 1, 6, 5 - #???.######..#####.
                    if (newArrangement2.numberGroupsMatch(newArrangement2.validNumbers)) {
                        validPossibilities += 1;
                    } else {
                        possibilities.add(newArrangement2);
                    }
                }
            }
        }
        return validPossibilities;
    }

    private boolean isValid(final SpringArrangement arrangement) {
        return arrangement.numberGroupsMatch(arrangement.validNumbers);
    }

    private boolean isPartialValid(final SpringArrangement arrangement) {
        final List<Integer> validNumbers = arrangement.validNumbers;
        final long totalValidNumberCount = arrangement.totalValidNumberCount;
        /*
        Need: 1, 3, 1, 6
        Invalid: .#.#.#?#?#?#?#?
        Invalid: .#.#.#.#?
         */
        if (!arrangement.numberGroupsValidUntilUnknown(validNumbers)) {
            return false;
        }

        // Check total count of valid numbers and see if we can get to that count with current numbers and unknowns
        if (!arrangement.validPossibleCount(totalValidNumberCount)) {
            return false;
        }

        // Example: Need 1, 1, 3
        // Invalid: .....??...?##.
        //    Calculate as two groups, not enough groups
        // Invalid: .....#?...?##.
        //    Calculate as two groups, not enough groups
        // Valid:   ....#??...?##.
        //    Calculate as three groups, valid
        if (!arrangement.hasValidMaxPossibleNumberGroupCount(validNumbers.size())) {
            return false;
        }

        return true;
    }
}
