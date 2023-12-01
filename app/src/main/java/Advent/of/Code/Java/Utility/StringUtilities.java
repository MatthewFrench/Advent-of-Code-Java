package Advent.of.Code.Java.Utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class StringUtilities {
    final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public static List<String> splitStringIntoList(final String target, final String splitBy) {
        return new ArrayList<>(Arrays.asList(target.split(Pattern.quote(splitBy)).clone()));
    }
    public static String getStringChunk(final String target, final int index, final int length) {
        return target.substring(index, index + length);
    }
    public static int countStringInstanceInString(final String target, final String count) {
        return target.length() - target.replaceAll(Pattern.quote(count), "").length();
    }
    public static boolean chunkExistsAtLocation(final String target, final String chunk, final int location) {
        if (target.length() >= location + chunk.length()) {
            return getStringChunk(target, location, chunk.length()).equals(chunk);
        }
        return false;
    }
    public static boolean chunkExistsAtEnd(final String target, final String chunk) {
        return chunkExistsAtLocation(target, chunk, target.length() - chunk.length());
    }
    public static boolean chunkExistsAtStart(final String target, final String chunk) {
        return chunkExistsAtLocation(target, chunk, 0);
    }
    public static String removeEndChunk(final String target, final String chunk) {
        return getStringChunk(target, 0, target.length() - chunk.length());
    }
    public static String removeStartChunk(final String target, final String chunk) {
        return getStringChunk(target, chunk.length(), target.length() - chunk.length());
    }
    public static String objectToString(final Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getFirstMatchingValue(final String searchString, final List<String> valuesToSearchFor) {
        String foundValue = null;
        Integer foundLocation = null;
        for (final String value : valuesToSearchFor) {
            final int indexOfValue = searchString.indexOf(value);
            if (indexOfValue != -1 && (foundLocation == null || indexOfValue < foundLocation)) {
                foundLocation = indexOfValue;
                foundValue = value;
            }
        }
        return foundValue;
    }

    public static String getLastMatchingValue(final String searchString, final List<String> valuesToSearchFor) {
        String foundValue = null;
        Integer foundLocation = null;
        for (final String value : valuesToSearchFor) {
            final int indexOfValue = searchString.lastIndexOf(value);
            if (indexOfValue != -1 && (foundLocation == null || indexOfValue > foundLocation)) {
                foundLocation = indexOfValue;
                foundValue = value;
            }
        }
        return foundValue;
    }
}
