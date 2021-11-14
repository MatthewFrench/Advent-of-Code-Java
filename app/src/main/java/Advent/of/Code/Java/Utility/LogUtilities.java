package Advent.of.Code.Java.Utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Stopwatch;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LogUtilities {
    final static ExecutorService logExecutorService = Executors.newFixedThreadPool(1);
    final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    final static Map<String, Stopwatch> STOPWATCHES = new HashMap<>();
    static int INDENT = 0;
    public static void log(final String text) {
        final int currentIndent = INDENT;
        logExecutorService.submit(()-> System.out.println(getIndentedText(currentIndent, text)));
    }
    public static void error(final String text) {
        final int currentIndent = INDENT;
        logExecutorService.submit(()-> System.err.println(getIndentedText(currentIndent, text)));
    }
    private static String getIndentedText(final int indent, final String text) {
        return "\t".repeat(Math.max(0, indent)) + text;
    }
    public static void error(final String text, final Exception e) {
        error(text);
        error(e.toString());
    }
    public static void indent() {
        INDENT += 1;
    }
    public static void unIndent() {
        INDENT -= 1;
    }
    public static void logSeparator() {
        log("-".repeat(Math.max(0, 10)));
    }
    public static void log(final String text, final Object object) {
        try {
            log(text + OBJECT_MAPPER.writeValueAsString(object));
        } catch (final JsonProcessingException e) {
            log("Failed to turn object into JSON: " + object.toString());
        }
    }
    public static void logObject(final Object object) {
        try {
            log(OBJECT_MAPPER.writeValueAsString(object));
        } catch (final JsonProcessingException e) {
            log("Failed to turn object into JSON: " + object.toString());
        }
    }
    public static void startTiming(final String identifier) {
        log("Timing started: " + identifier);
        final Stopwatch stopwatch = Stopwatch.createStarted();
        STOPWATCHES.put(identifier, stopwatch);
    }
    public static void endTiming(final String identifier) {
        final Stopwatch stopwatch = STOPWATCHES.remove(identifier);
        log("Timing ended: " + identifier + ", " + stopwatch.toString());
    }
    public static void shutdown() {
        logExecutorService.shutdown();
    }
}
