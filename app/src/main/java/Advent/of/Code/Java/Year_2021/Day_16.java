package Advent.of.Code.Java.Year_2021;

import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.NumberUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Day_16 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2021/16/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        final String input = LoadUtilities.loadTextFileAsString(fileName);
        final List<String> inputSeparated = StringUtilities.splitStringIntoList(input, "");
        final List<Integer> bits = new ArrayList<>();
        for (final String hex : inputSeparated) {
            bits.addAll(HEXADECIMAL_TO_BITS.get(hex));
        }
        // First 3 bits are package version
        // Next three bits are packet type id
        //
        // type id 4 is literal value
        // For literal:
            // Start with 1, then 4 bits
            // Start with 1, then 4 bits
            // Start with 0, then 4 bits
            // Put all the 4 bits together to make a number
        //
        // type id other is an operator, contains 1 or more packets
        // 1 bit for length type ID, 0 means next 15 bits are a number that is the total length in bits of the sub packets
            // 1 means the next 11 bits are a number that is the number of sub-packets contained inside the packet

        final List<Packet> packets = new ArrayList<>();
        packets.add(parseNextPacket(bits));

        long totalSum = 0;
        for (final Packet packet : packets) {
            totalSum += packet.getSumOfVersionNumbers();
        }

        LogUtilities.logGreen("Solution: " + totalSum);


        LogUtilities.logGreen("Solution value: " + packets.get(0).getValue());
    }

    static Packet parseNextPacket(final List<Integer> bits) {
        final long packetVersion = NumberUtilities.numberFromBinaryString(removeNumberOfBits(bits, 3));
        final long packetId = NumberUtilities.numberFromBinaryString(removeNumberOfBits(bits, 3));
        if (packetId == 4) {
            String literalString = "";
            boolean keepBuilding = true;
            while (keepBuilding) {
                final long buildFlag = NumberUtilities.numberFromBinaryString(removeNumberOfBits(bits, 1));
                if (buildFlag == 0) {
                    keepBuilding = false;
                }
                literalString += removeNumberOfBits(bits, 4);
            }
            final long literal = NumberUtilities.numberFromBinaryString(literalString);
            return new LiteralPacket(packetVersion, packetId, literal);
        } else {
            final long lengthTypeID = NumberUtilities.numberFromBinaryString(removeNumberOfBits(bits, 1));
            final List<Packet> packets = new ArrayList<>();
            if (lengthTypeID == 0) {
                final long lengthOfBits = NumberUtilities.numberFromBinaryString(removeNumberOfBits(bits, 15));
                final List<Integer> newBits = removeNumberOfBitsAsList(bits, (int) lengthOfBits);
                while (newBits.size() > 0) {
                    packets.add(parseNextPacket(newBits));
                }
            } else if (lengthTypeID == 1) {
                final long numberOfPackets = NumberUtilities.numberFromBinaryString(removeNumberOfBits(bits, 11));
                for (int i = 0; i < numberOfPackets; i++) {
                    packets.add(parseNextPacket(bits));
                }
            }
            return new OperatorPacket(packetVersion, packetId, lengthTypeID, packets);
        }
    }

    static String removeNumberOfBits(final List<Integer> bits, int count) {
        String bitString = "";
        for (int i = 0; i < count; i++) {
            bitString += bits.remove(0);
        }
        return bitString;
    }

    static List<Integer> removeNumberOfBitsAsList(final List<Integer> bits, int count) {
        List<Integer> newBits = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            newBits.add(bits.remove(0));
        }
        return newBits;
    }


    final Map<String, List<Integer>> HEXADECIMAL_TO_BITS = ImmutableMap.<String, List<Integer>>builder()
            .put("0", ImmutableList.of(0, 0, 0, 0))
            .put("1", ImmutableList.of(0, 0, 0, 1))
            .put("2", ImmutableList.of(0, 0, 1, 0))
            .put("3", ImmutableList.of(0, 0, 1, 1))
            .put("4", ImmutableList.of(0, 1, 0, 0))
            .put("5", ImmutableList.of(0, 1, 0, 1))
            .put("6", ImmutableList.of(0, 1, 1, 0))
            .put("7", ImmutableList.of(0, 1, 1, 1))
            .put("8", ImmutableList.of(1, 0, 0, 0))
            .put("9", ImmutableList.of(1, 0, 0, 1))
            .put("A", ImmutableList.of(1, 0, 1, 0))
            .put("B", ImmutableList.of(1, 0, 1, 1))
            .put("C", ImmutableList.of(1, 1, 0, 0))
            .put("D", ImmutableList.of(1, 1, 0, 1))
            .put("E", ImmutableList.of(1, 1, 1, 0))
            .put("F", ImmutableList.of(1, 1, 1, 1))
            .build();

    interface Packet {
        long getSumOfVersionNumbers();
        long getValue();
    }
    @AllArgsConstructor
    static class LiteralPacket implements Packet {
        final long version;
        final long id;
        final long value;
        // Has version
        public long getSumOfVersionNumbers() {
            return version;
        }
        public long getValue() {
            return value;
        }
    }
    @AllArgsConstructor
    static class OperatorPacket implements Packet {
        final long version;
        final long id;
        final long lengthTypeID;
        final List<Packet> packets;
        // Has Packets
        public long getSumOfVersionNumbers() {
            long sum = version;
            for (final Packet packet : packets) {
                sum += packet.getSumOfVersionNumbers();
            }
            return sum;
        }
        public long getValue() {
            if (id == 0) {
                long sum = 0;
                for (final Packet packet : packets) {
                    sum += packet.getValue();
                }
                return sum;
            } else if (id == 1) {
                long product = 1;
                for (final Packet packet : packets) {
                    product *= packet.getValue();
                }
                return product;
            } else if (id == 2) {
                long minimum = packets.get(0).getValue();
                for (final Packet packet : packets) {
                    minimum = Math.min(packet.getValue(), minimum);
                }
                return minimum;
            } else if (id == 3) {
                long maximum = packets.get(0).getValue();
                for (final Packet packet : packets) {
                    maximum = Math.max(packet.getValue(), maximum);
                }
                return maximum;
            } else if (id == 5) {
                if (packets.get(0).getValue() > packets.get(1).getValue()) {
                    return 1;
                }
                return 0;
            } else if (id == 6) {
                if (packets.get(0).getValue() < packets.get(1).getValue()) {
                    return 1;
                }
                return 0;
            } else if (id == 7) {
                if (packets.get(0).getValue() == packets.get(1).getValue()) {
                    return 1;
                }
                return 0;
            }
            return 0;
        }
    }
}
