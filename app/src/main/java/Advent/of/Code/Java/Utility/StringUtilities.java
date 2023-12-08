package Advent.of.Code.Java.Utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

// Todo: Turn this into a lombok extension so I can reference these functions directly on the relevant types: https://stackoverflow.com/a/50412907
// Example: "test  test".removeWhitespaceFromString();
//@ExtensionMethod({java.lang.String, Extensions.class})
// Note: Giving up on extensions for now, IntelliJ doesn't support autocompleting extension functions: https://youtrack.jetbrains.com/issue/IDEA-261477/Lombok-ExtensionMethod-autocomplete-feature
public class StringUtilities {
    final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    // Todo: Add a function to remove everything before a first instance of a character including that character
    // Example: "Distance: 1", removeAllBeforeFirstInstanceOfCharacterInclusive() -> " 1"

    public static String removeCharactersFromString(final String target, final List<String> charactersToRemove) {
        String updatedString = target;
        for (final String characterToRemove : charactersToRemove) {
            updatedString = updatedString.replace(characterToRemove, "");
        }
        return updatedString;
    }
    public static String removeDuplicateCharactersFromString(final String target, final List<String> charactersToDeduplicate) {
        String updatedString = target;
        for (final String characterToDeduplicate : charactersToDeduplicate) {
            boolean searchForString = true;
            while (searchForString) {
                searchForString = false;
                int currentLength = updatedString.length();
                // This is unoptimized
                updatedString = updatedString.replace(characterToDeduplicate + characterToDeduplicate, characterToDeduplicate);
                if (currentLength != updatedString.length()) {
                    searchForString = true;
                }
            }
        }
        return updatedString;
    }
    public static String removeDuplicateWhitespaceFromString(final String target) {
        return StringUtils.normalizeSpace(target);
    }
    public static String removeWhitespaceFromString(final String target) {
        return StringUtils.deleteWhitespace(target);
    }

    public static List<String> splitStringByWhitespace(final String target) {
        return new ArrayList<>(Arrays.asList(target.split("\\s+").clone()));
    }
    public static List<String> splitStringByWhitespaceDeduplicated(final String target) {
        return new ArrayList<>(Arrays.asList(removeDuplicateWhitespaceFromString(target).split("\\s+").clone()));
    }
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
