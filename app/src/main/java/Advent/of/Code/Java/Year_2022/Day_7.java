package Advent.of.Code.Java.Year_2022;

import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;
import Advent.of.Code.Java.Utility.Structures.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Day_7 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2022/7/", this);
    }

    interface FileSystemItem {
        long getSize();
    }
    static class Folder implements FileSystemItem {
        final String name;
        List<File> files = new ArrayList<>();
        List<Folder> folders = new ArrayList<>();
        public Folder(final String name) {
            this.name = name;
        }
        @Override
        public long getSize() {
            long totalSize = 0;
            for (final File file : files) {
                totalSize += file.getSize();
            }
            for (final Folder folder : folders) {
                totalSize += folder.getSize();
            }
            return totalSize;
        }
        public void addFile(final File file) {
            files.add(file);
        }
        public void addFolder(final Folder folder) {
            folders.add(folder);
        }
        public Folder getFolder(final String name) {
            // Warning, this can cause a null pointer exception if the name doesn't exist
            return folders.stream().filter(folder -> folder.getName().equals(name)).findFirst().orElse(null);
        }
        public File getFile(final String name) {
            // Warning, this can cause a null pointer exception if the name doesn't exist
            return files.stream().filter(file -> file.getName().equals(name)).findFirst().orElse(null);
        }
        public String getName() {
            return this.name;
        }
        public List<Folder> getFolders() {
            return this.folders;
        }
        public List<File> getFiles() {
            return this.files;
        }
        public boolean containsFolder(final Folder folder) {
            if (folders.contains(folder)) {
                return true;
            }
            for (final Folder checkFolder : folders) {
                if (checkFolder.containsFolder(folder)) {
                    return true;
                }
            }
            return false;
        }
    }
    static class File implements FileSystemItem {
        final String name;
        final long size;
        public File(final long size, final String name) {
            this.size = size;
            this.name = name;
        }
        @Override
        public long getSize() {
            return size;
        }
        public String getName() {
            return name;
        }
    }
    public void executeWithInput(final String fileName) throws Exception {
        {
            // Find all of the directories with a total size of at most 100,000. What is the sum of the total sizes of those directories?
            final List<String> input = StringUtilities.splitStringIntoList(LoadUtilities.loadTextFileAsString(fileName), "\n");
            final Folder rootFolder = new Folder("/");
            final List<Folder> priorFolders = new ArrayList<>();
            Folder currentFolder = rootFolder;
            for (final String command : input) {
                if (StringUtilities.chunkExistsAtStart(command, "$")) {
                    // Execute commands
                    if (StringUtilities.chunkExistsAtStart(command, "$ cd ")) {
                        final String folderName = StringUtilities.removeStartChunk(command, "$ cd ");
                        // Go back to the root folder if we cd to root
                        if (folderName.equals("/")) {
                            currentFolder = rootFolder;
                            priorFolders.clear();
                        } else if (folderName.equals("..")) {
                            currentFolder = priorFolders.remove(priorFolders.size() - 1);
                        } else {
                            priorFolders.add(currentFolder);
                            Folder gettingFolder = currentFolder.getFolder(folderName);
                            if (gettingFolder == null) {
                                gettingFolder = new Folder(folderName);
                                currentFolder.addFolder(gettingFolder);
                            }
                            currentFolder = gettingFolder;
                        }
                    } else if (StringUtilities.chunkExistsAtStart(command, "$ ls")) {
                        // Don't do anything, the next loop will catch file names or do another command
                    }
                } else {
                    // Check and create folder
                    if (StringUtilities.chunkExistsAtStart(command, "dir ")) {
                        final String folderName = StringUtilities.removeStartChunk(command, "dir ");
                        Folder gettingFolder = currentFolder.getFolder(folderName);
                        if (gettingFolder == null) {
                            gettingFolder = new Folder(folderName);
                            currentFolder.addFolder(gettingFolder);
                        }
                    } else {
                        // Create file
                        final List<String> fileDetails = StringUtilities.splitStringIntoList(command, " ");
                        File gettingFile = currentFolder.getFile(fileDetails.get(1));
                        if (gettingFile == null) {
                            gettingFile = new File(Long.parseLong(fileDetails.get(0)), fileDetails.get(1));
                            currentFolder.addFile(gettingFile);
                        }
                    }
                }
            }
            {
                // Now search all folders and make a list with all folders and add their sums of they are at most 100_000
                List<Folder> searchFolders = new ArrayList<>();
                searchFolders.add(rootFolder);
                long totalCount = 0;
                while (searchFolders.size() > 0) {
                    final Folder currentSearchFolder = searchFolders.remove(0);
                    for (final Folder folder : currentSearchFolder.getFolders()) {
                        searchFolders.add(folder);
                        if (folder.getSize() <= 100_000) {
                            totalCount += folder.getSize();
                        }
                    }
                }


                LogUtilities.logGreen("Folder Structure: ");

                // Print out folder structure for debugging purposes
                printFolderStructure(rootFolder, 0);

                LogUtilities.logGreen("Solution: " + totalCount);
            }

            // Find the smallest directory that is at least 30,000,000
            final long needToFreeUpSpace = 30000000 - (70000000 - rootFolder.getSize());
            List<Folder> searchFolders = new ArrayList<>();
            searchFolders.add(rootFolder);
            Folder smallestDirectory = rootFolder;
            while (searchFolders.size() > 0) {
                final Folder currentSearchFolder = searchFolders.remove(0);
                for (final Folder folder : currentSearchFolder.getFolders()) {
                    if (folder.getSize() >= needToFreeUpSpace) {
                        searchFolders.add(folder);
                        if (folder.getSize() < smallestDirectory.getSize()) {
                            smallestDirectory = folder;
                        }
                    }
                }
            }
            LogUtilities.logGreen("Solution 2: " + smallestDirectory.getSize());
        }
    }

    public static void printFolderStructure(final Folder printFolder, int currentIndent) {
        LogUtilities.logPurple(LogUtilities.getIndentedText(currentIndent, " ") + "- " + printFolder.getName() + " (dir)");
        for (final File file : printFolder.getFiles()) {
            LogUtilities.logBlue(LogUtilities.getIndentedText(currentIndent + 1, " ") + "- " + file.getName() + " (file, size=" + file.getSize() + ")");
        }
        for (final Folder folder : printFolder.getFolders()) {
            printFolderStructure(folder, currentIndent + 1);
        }
    }
}
