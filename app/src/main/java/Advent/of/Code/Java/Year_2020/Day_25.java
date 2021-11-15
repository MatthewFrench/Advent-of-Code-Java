package Advent.of.Code.Java.Year_2020;

import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.Structures.Day;

public class Day_25 implements Day {
    public void run() throws Exception {
        // Card uses a specific secret loop size
        // Door uses a different secret loop size.
        // Card public key = transformSubjectNumber based on the loop size with subject number 7
        // Door public key = transformSubjectNumber based on door secret loop size with subject number 7
        // You have public keys but neither device's loop size
        // Door public key gets transformed by subject Number to the card's loop size. Creating the encryption key
        // Card public key gets transformed by subject number to the door's loop size. Creating the encryption key

        // Use the 2 public keys to find each loop size. Then calculate the encryption keys
        long initialSubjectNumber = 7;

        long cardPublicKey = 15628416; //5764801;
        LogUtilities.log("Calculating card loop size");
        // find card loop size
        long cardLoopSize = findLoopSize(cardPublicKey, initialSubjectNumber);

        long doorPublicKey = 11161639; //17807724;
        LogUtilities.log("Calculating door loop size");
        //find door loop size
        long doorLoopSize = findLoopSize(doorPublicKey, initialSubjectNumber);

        LogUtilities.log("Calculating encryption key 1");
        long encryptionKeyOne = transformSubjectNumber(cardLoopSize, doorPublicKey); // Should produce 14897079
        LogUtilities.log("Calculating encryption key 2");
        long encryptionKeyTwo = transformSubjectNumber(doorLoopSize, cardPublicKey); // Should produce 14897079

        LogUtilities.log("Encryption keys: " + encryptionKeyOne + ", " + encryptionKeyTwo);
    }
    public static long transformSubjectNumber(long loopSize, long subjectNumber) {
        return transformSubjectNumber(loopSize, subjectNumber, 0, 1);
    }
    public static long transformSubjectNumber(long loopSize, long subjectNumber, long startAtLoopSize, long startValue) {
        long value = startValue;
        for (var i = startAtLoopSize; i < loopSize; i++) {
            value = value * subjectNumber;
            value = value % 20201227;
        }
        return value;
    }
    public static long findLoopSize(long publicKey, long subjectNumber) {
        long loopSize = 0;
        long lastCheckedValue = 1;
        while (true) {
            loopSize += 1;
            var value = transformSubjectNumber(loopSize, subjectNumber, loopSize - 1, lastCheckedValue);
            lastCheckedValue = value;
            if (value == publicKey) {
                return loopSize;
            }
        }
    }
}