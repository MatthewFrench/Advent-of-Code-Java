package Advent.of.Code.Java.Utility;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LoadUtilities {
    final static Map<String, byte[]> CACHED_FILES = new HashMap<>();

    public static void preloadFiles() throws Exception {
        final List<String> files = getFilesInFolder("input");
        for (final String file : files) {
            CACHED_FILES.put(file, loadFileAsBytes(file));
        }
        LogUtilities.log("Files loaded: " + CACHED_FILES.size());
    }

    public static List<String> getFilesInFolder(final String folder) throws Exception {
        List<String> filenames = new ArrayList<>();
        InputStream stream = LoadUtilities.class.getClassLoader().getResourceAsStream(folder);
        if (stream != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    final String name = folder + "/" + line;
                    final URL resource = LoadUtilities.class.getClassLoader().getResource(name);
                    if (resource != null) {
                        final String path = resource.getPath();
                        final File file = new File(path);
                        if (file.isFile()) {
                            filenames.add(name);
                        } else {
                            filenames.addAll(getFilesInFolder(name));
                        }
                    }
                }
            }
        }
        return filenames;
    }

    public static byte[] loadFileAsBytes(final String name) throws Exception {
        final InputStream stream = LoadUtilities.class.getClassLoader().getResourceAsStream(name);
        if (stream == null) {
            throw new Exception("Invalid file: " + name);
        }
        return stream.readAllBytes();
    }

    public static Stream<String> loadTextFileStream(final String name) throws Exception {
        final byte[] bytes = CACHED_FILES.get(name);
        if (bytes == null) {
            throw new Exception("Invalid text file: " + name);
        }
        final BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes), StandardCharsets.UTF_8));
        return reader.lines();
    }

    public static String loadTextFileAsString(final String name) throws Exception {
        return loadTextFileStream(name).collect(Collectors.joining("\n"));
    }

    public static List<String> loadTextFileAsList(final String name) throws Exception {
        return loadTextFileStream(name).collect(Collectors.toList());
    }

    public static <R> List<R> loadTextFileAsTypeList(final String name, Function<? super String, ? extends R> mapperFunction) throws Exception {
        return loadTextFileStream(name).map(mapperFunction).collect(Collectors.toList());
    }
}
