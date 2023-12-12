package Advent.of.Code.Java.Year_2023;

import Advent.of.Code.Java.Utility.DataUtilities;
import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.NumberUtilities;
import Advent.of.Code.Java.Utility.SimpleParallelism;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;

import java.util.ArrayList;
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
            possibilities += countPossibilities(characterListToGroup(characters), numbers);
            count += 1;
            LogUtilities.logPurple("Progress: " + count + " / " + input.size());
        }

        LogUtilities.logGreen("Solution 1: " + possibilities);
    }

    private void runSolution2(final String fileName) throws Exception {
        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);

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
                possibilities.addAndGet(countPossibilities(expandedCharacterGroup, expandedNumbers));
                count.addAndGet(1);
                LogUtilities.logPurple("Progress: " + count.get() + " / " + input.size());
            });
        }
        simpleParallelism.waitForCompletion();

        LogUtilities.logGreen("Solution 2: " + possibilities);
    }

    private CharacterGroup characterListToGroup(final List<CharacterType> characters) {
        final CharacterGroup startingNode = new CharacterGroup();
        startingNode.type = characters.get(0);
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
        CharacterGroup startNode;

        public List<Integer> getGroupCountList(final CharacterType characterType) {
            CharacterGroup currentSearchGroup = startNode;
            final List<Integer> countList = new ArrayList<>();
            while (currentSearchGroup != null) {
                if (currentSearchGroup.type == characterType) {
                    countList.add(currentSearchGroup.count);
                }
                currentSearchGroup = currentSearchGroup.nextGroup;
            }
            return countList;
        }

        public long getNumberOfGroups(final CharacterType characterType) {
            CharacterGroup currentSearchGroup = startNode;
            long count = 0;
            while (currentSearchGroup != null) {
                if (currentSearchGroup.type == characterType) {
                    count += 1;
                }
                currentSearchGroup = currentSearchGroup.nextGroup;
            }
            return count;
        }

        public List<Integer> getNumberOfGroupCountsUntil(final CharacterType characterType, final CharacterType untilCharacterType) {
            CharacterGroup currentSearchGroup = startNode;
            List<Integer> counts = new ArrayList<>();
            while (currentSearchGroup != null) {
                if (currentSearchGroup.type == characterType) {
                    counts.add(currentSearchGroup.count);
                }
                if (currentSearchGroup.type == untilCharacterType) {
                    break;
                }
                currentSearchGroup = currentSearchGroup.nextGroup;
            }
            return counts;
        }

        public List<Integer> getNumberOfGroupCountsBeforeNeighbor(final CharacterType characterType, final CharacterType beforeCharacterType) {
            CharacterGroup currentSearchGroup = startNode;
            List<Integer> counts = new ArrayList<>();
            while (currentSearchGroup != null) {
                if (currentSearchGroup.type == characterType && (currentSearchGroup.nextGroup == null || currentSearchGroup.nextGroup.type != beforeCharacterType)) {
                    counts.add(currentSearchGroup.count);
                }
                if (currentSearchGroup.type == beforeCharacterType) {
                    break;
                }
                currentSearchGroup = currentSearchGroup.nextGroup;
            }
            return counts;
        }

        public long getNumberOfGroupsWithCountGreaterThan(final CharacterType characterType, long greaterThan) {
            CharacterGroup currentSearchGroup = startNode;
            long count = 0;
            while (currentSearchGroup != null) {
                if (currentSearchGroup.type == characterType && currentSearchGroup.count > greaterThan) {
                    count += 1;
                }
                currentSearchGroup = currentSearchGroup.nextGroup;
            }
            return count;
        }

        public long getTotalCount(final CharacterType characterType) {
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

        public boolean hasUnknowns() {
            CharacterGroup currentSearchGroup = startNode;
            while (currentSearchGroup != null) {
                if (currentSearchGroup.type == CharacterType.UNKNOWN) {
                    return true;
                }
                currentSearchGroup = currentSearchGroup.nextGroup;
            }
            return false;
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
            return newArrangement;
        }
    }

    private long countPossibilities(final CharacterGroup initialCharacterGroup, final List<Integer> expectedNumbers) {
        final long totalNumberCount = NumberUtilities.sumIntegers(expectedNumbers);
        final List<SpringArrangement> possibilities = new ArrayList<>();
        final SpringArrangement startingArrangement = new SpringArrangement();
        startingArrangement.startNode = initialCharacterGroup.copy();
        possibilities.add(startingArrangement);
        long validPossibilities = 0;
        while (!possibilities.isEmpty()) {
            final SpringArrangement arrangement = possibilities.remove(possibilities.size() - 1);
            final SpringArrangement newArrangement1 = arrangement.copy();
            newArrangement1.replaceNextUnknownWith(CharacterType.SEPARATOR);
            if (!newArrangement1.hasUnknowns()) {
                if (isValid(newArrangement1, expectedNumbers)) {
                    validPossibilities += 1;
                }
            } else if (isPartialValid(newArrangement1, expectedNumbers, totalNumberCount)) {
                possibilities.add(newArrangement1);
            }
            final SpringArrangement newArrangement2 = arrangement.copy();
            newArrangement2.replaceNextUnknownWith(CharacterType.NUMBER);
            if (!newArrangement2.hasUnknowns()) {
                if (isValid(newArrangement2, expectedNumbers)) {
                    validPossibilities += 1;
                }
            } else if (isPartialValid(newArrangement2, expectedNumbers, totalNumberCount)) {
                possibilities.add(newArrangement2);
            }
        }
        return validPossibilities;
    }

    private boolean isValid(final SpringArrangement arrangement, final List<Integer> numbers) {
        final List<Integer> numberGroupCounts = arrangement.getGroupCountList(CharacterType.NUMBER);
        if (numberGroupCounts.size() != numbers.size()) {
            return false;
        }
        for (int index = 0; index < numbers.size(); index++) {
            if (numbers.get(index) != numberGroupCounts.get(index)) {
                return false;
            }
        }
        return true;
    }

    private boolean isPartialValid(final SpringArrangement arrangement, final List<Integer> validNumbers, final long totalValidNumberCount) {
        // Check total count of valid numbers and see if we can get to that count with current numbers and unknowns
        final long possibleCount = arrangement.getTotalCount(CharacterType.NUMBER) + arrangement.getTotalCount(CharacterType.UNKNOWN);
        if (possibleCount < totalValidNumberCount) {
            return false;
        }

        // Remove impossible groupings
        // Need 3 groups - Invalid: ".??.###", Valid: ".???.###"
        final long numberGroupCount = arrangement.getNumberOfGroups(CharacterType.NUMBER);
        final List<Integer> unknownGroupList = arrangement.getGroupCountList(CharacterType.UNKNOWN);
        long numberOfPotentialUnknownGroups = 0;
        for (final int unknownGroupCount : unknownGroupList) {
            numberOfPotentialUnknownGroups += (long) Math.ceil((double) unknownGroupCount / 2);
        }
        if (numberGroupCount + numberOfPotentialUnknownGroups < validNumbers.size()) {
            return false;
        }

        // Remove where there is too many numbers to start with up until the first unknown
        // Need number 1, 1, 3 - Invalid: ##?.###, Valid: #.#?
        final List<Integer> numberCountsUntil = arrangement.getNumberOfGroupCountsUntil(CharacterType.NUMBER, CharacterType.UNKNOWN);
        if (numberCountsUntil.size() > validNumbers.size()) {
            return false;
        }
        for (int index = 0; index < numberCountsUntil.size(); index++) {
            if (numberCountsUntil.get(index) > validNumbers.get(index)) {
                return false;
            }
        }

        /*
        Need: 1, 3, 1, 6
        Invalid: .#.#.#?#?#?#?#?
        Invalid: .#.#.#.#?
         */
        final List<Integer> numberCountsBefore = arrangement.getNumberOfGroupCountsBeforeNeighbor(CharacterType.NUMBER, CharacterType.UNKNOWN);
        if (numberCountsBefore.size() > validNumbers.size()) {
            return false;
        }
        for (int index = 0; index < numberCountsBefore.size(); index++) {
            if (numberCountsBefore.get(index) < validNumbers.get(index)) {
                return false;
            }
        }

        // Example: Need 1, 1, 3
        // Invalid: .....??...?##.
        // Invalid: .....#?...?##.
        // Heuristic for this scenario?

        // Todo: Add heuristics to cut down on partial valid
        // Todo: Probably could add heuristics for counting total groups and if the number of number groups can be attained

        //final String prettyPrint = arrangement.prettyPrint();
        return true;
    }

    /*
    private boolean isValid(final SpringArrangement arrangement, final List<Integer> numbers) {
        if (arrangement.numberCounts.size() < numbers.size() || arrangement.numberCounts.size() > numbers.size() + 1) {
            return false;
        }
        if (arrangement.numberCounts.size() > numbers.size()) {
            if (arrangement.numberCounts.get(arrangement.numberCounts.size() - 1) != 0) {
                return false;
            }
        }
        for (int index = 0; index < numbers.size(); index++) {
            if (numbers.get(index) != arrangement.numberCounts.get(index)) {
                return false;
            }
        }
        return true;
    }

    private boolean isPartialValid(final SpringArrangement arrangement, final List<Integer> validNumbers, final long totalValidNumberCount) {
        final List<Integer> checkNumbers = arrangement.numberCounts;
        final int checkNumberSize = checkNumbers.size();
        final int validNumberSize = validNumbers.size();
        // Check numbers can have an end number of zero since we're still calculating totals
        boolean endNumberZero = checkNumbers.get(checkNumberSize - 1) == 0;
        if (checkNumberSize > validNumberSize + (endNumberZero ? 1 : 0)) {
            return false;
        }
        // Check total count of valid numbers and see if we can get to that count with current numbers and unknowns
        final long possibleCount = arrangement.totalNumberCount + arrangement.questionMarkIndexes.size();
        if (possibleCount < totalValidNumberCount) {
            return false;
        }
        // Combine question marks that are next to each other to see if we can filter out impossible groupings
        long possibleAdditionalGroups = 0;
        {
            int currentQuestionMarkIndex = arrangement.questionMarkIndexes.get(0);
            int currentGroupingCount = 1;
            for (int index = 1; index < arrangement.questionMarkIndexes.size(); index++) {
                final int nextQuestionMarkIndex = arrangement.questionMarkIndexes.get(index);
                if (nextQuestionMarkIndex != currentQuestionMarkIndex + 1) {
                    possibleAdditionalGroups += (long)Math.ceil((double) currentGroupingCount / 2);
                    currentGroupingCount = 0;
                }
                currentGroupingCount += 1;
                currentQuestionMarkIndex = nextQuestionMarkIndex;
            }
            if (currentGroupingCount > 0) {
                possibleAdditionalGroups += (long)Math.ceil((double) currentGroupingCount / 2);
            }
        }
        final long possibleTotalGroups = possibleAdditionalGroups + checkNumberSize - (endNumberZero ? 1 : 0);
        if (possibleTotalGroups < validNumberSize) {
            return false;
        }
        final int maxIndex = Math.min(validNumberSize, checkNumberSize - (endNumberZero ? 1 : 0));
        for (int index = 0; index < maxIndex; index++) {
            if (index == maxIndex - 1) {
                if (checkNumbers.get(index) > validNumbers.get(index)) {
                    return false;
                }
            } else {
                if (checkNumbers.get(index) != validNumbers.get(index)) {
                    return false;
                }
            }
        }

        // Todo: Add heuristics to cut down on partial valid
        // Todo: Probably could add heuristics for counting total groups and if the number of number groups can be attained


        return true;
    }

     */
}
