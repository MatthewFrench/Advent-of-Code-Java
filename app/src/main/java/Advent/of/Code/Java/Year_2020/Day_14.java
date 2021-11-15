package Advent.of.Code.Java.Year_2020;

import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.Day;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day_14 implements Day {
    public void run() throws Exception {
        LogUtilities.log(Day_14.class.getName());
        final List<String> input = LoadUtilities.loadTextFileAsList("input/2020/14/input.txt");
        {
            var mask = "";
            Map<Integer, String> memory = new HashMap<>();
            for (var compute : input) {
                if (StringUtilities.chunkExistsAtStart(compute, "mask = ")) {
                    mask = StringUtilities.removeStartChunk(compute, "mask = ");
                    continue;
                }
                var split1 = StringUtilities.removeStartChunk(compute, "mem[");
                var memoryLocation = Integer.parseInt(split1.substring(0, split1.indexOf("]")));
                long decimalValue = Long.parseLong(StringUtilities.splitStringIntoList(compute, " = ").get(1));
                String bitRepresentation = Long.toBinaryString(decimalValue);
                LogUtilities.log("Bit rep: " + bitRepresentation);
                var finalString = "";
                for (var i = 0; i < mask.length(); i++) {
                    var character = StringUtilities.getStringChunk(mask, (mask.length() - 1) - i, 1);
                    if (character.equals("X")) {
                        if (i < bitRepresentation.length()) {
                            character = StringUtilities.getStringChunk(bitRepresentation, (bitRepresentation.length() - 1) - i, 1);
                        } else {
                            character = "0";
                        }
                    }
                    finalString = character + finalString;
                }
                memory.put(memoryLocation, finalString);
            }
            long total = 0;
            for (var key : memory.keySet()) {
                var value = memory.get(key);
                var test = binaryToInteger(value);
                total += test;
            }
            LogUtilities.log("Part 1: " + total);
        }

        var mask = "";
        Map<String, Long> memory = new HashMap<>();
        for (var compute : input) {
            if (StringUtilities.chunkExistsAtStart(compute, "mask = ")) {
                mask = StringUtilities.removeStartChunk(compute, "mask = ");
                continue;
            }
            var split1 = StringUtilities.removeStartChunk(compute, "mem[");
            var memoryLocation = Integer.parseInt(split1.substring(0, split1.indexOf("]")));

            var memoryLocationBitRepresentation = Long.toBinaryString(memoryLocation);
            var newMemoryLocationBitRepresentation = "";
            for (var i = 0; i < mask.length(); i++) {
                var character = StringUtilities.getStringChunk(mask, (mask.length() - 1) - i, 1);
                if (character.equals("0")) {
                    if (i < memoryLocationBitRepresentation.length()) {
                        character = StringUtilities.getStringChunk(memoryLocationBitRepresentation, (memoryLocationBitRepresentation.length() - 1) - i, 1);
                    }
                }
                newMemoryLocationBitRepresentation = character + newMemoryLocationBitRepresentation;
            }
            List<String> writeMemoryLocations = new ArrayList<>();
            writeMemoryLocations.add("");
            for (var i = 0; i < newMemoryLocationBitRepresentation.length(); i++) {
                var character = StringUtilities.getStringChunk(newMemoryLocationBitRepresentation, i, 1);
                if (character.equals("X")) {
                    // Add 1 for all
                    var size = writeMemoryLocations.size();
                    for (var memIndex = 0; memIndex < size; memIndex++) {
                        var mem = writeMemoryLocations.get(memIndex);
                        mem += "1";
                        writeMemoryLocations.add(mem);
                    }
                    // Add 0 for all
                    for (var memIndex = 0; memIndex < size; memIndex++) {
                        var mem = writeMemoryLocations.get(memIndex);
                        mem += "0";
                        writeMemoryLocations.set(memIndex, mem);
                    }
                } else {
                    for (var memIndex = 0; memIndex < writeMemoryLocations.size(); memIndex++) {
                        var mem = writeMemoryLocations.get(memIndex);
                        mem += character;
                        writeMemoryLocations.set(memIndex, mem);
                    }
                }
            }
            long decimalValue = Long.parseLong(StringUtilities.splitStringIntoList(compute, " = ").get(1));
            for (var location : writeMemoryLocations) {
                memory.put(location, decimalValue);
            }
        }
        long total = 0;
        for (var key : memory.keySet()) {
            var value = memory.get(key);
            total += value;
        }
        LogUtilities.log("Part 2: " + total);
    }
    // Pulled from stack overflow
    static public long binaryToInteger(String binary) {
        char[] numbers = binary.toCharArray();
        long result = 0;
        for(int i=numbers.length - 1; i>=0; i--)
            if(numbers[i]=='1')
                result += Math.pow(2, (numbers.length-i - 1));
        return result;
    }
}