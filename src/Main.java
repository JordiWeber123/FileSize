import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;
import java.util.Map;

public class Main {
    private static final double BYTES_PER_KiB = 1024;

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Limit size: " + Long.MAX_VALUE + " bytes or "
                + Long.MAX_VALUE / BYTES_PER_KiB / BYTES_PER_KiB / BYTES_PER_KiB / BYTES_PER_KiB + " TiB");
        Scanner sc = new Scanner(System.in);
        FileSizeFactory FSFactory = new FileSizeFactory();
        HashMap<File, FileSize> files = new HashMap<>();
        do {
            String fileName = getFileName(sc);
            File file = new File(fileName);
            FileSize activeFile;
            Stack<FileSize> parents = new Stack<>();
            // Check if file has been processed already
            if (files.containsKey(file)) {
                activeFile = files.get(file);
            } else if (mapContainsSubFile(files, file)) {
                activeFile = getContainedSubFile(files, file);
            } else {
                // If it hasn't, create a FileSize object from it and add it to the map
                activeFile = FSFactory.createFileSize(file);
                files.put(file, activeFile);
            }
            do {
                // Display the information for a File
                if (activeFile.isDirectory()) {
                    activeFile = showMenuOptions(sc, activeFile, parents);
                } else {
                    System.out.println("Displaying information for: " + activeFile.getName());
                    displayBytes(activeFile.getSize());
                }
            } while (!askPick(sc, activeFile));
        } while (!askQuit(sc));
    }

    /**
     * Check if the map or any directories within it contains the given file
     * 
     * @param filesMap Map of Files and their corresponding FileSize objects
     * @param file     The File to search for
     * @return true if the file is contained within the map or any directories
     *         inside it, false otherwise
     */
    private static boolean mapContainsSubFile(Map<File, FileSize> filesMap, File file) {
        for (File key : filesMap.keySet()) {
            FileSize current = filesMap.get(key);
            if (current instanceof DirectorySize && ((DirectorySize) current).contains(file)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a FileSize object present inside a directory
     * 
     * @param filesMap Map of the current directory's Files to FileSize objects
     * @param file     The file to search for
     * @return FileSize object that is being searched. Null if not found
     */
    private static FileSize getContainedSubFile(Map<File, FileSize> filesMap, File file) {
        for (File key : filesMap.keySet()) {
            FileSize currentFile = filesMap.get(key);
            if (currentFile instanceof DirectorySize) {
                DirectorySize currentDir = (DirectorySize) currentFile;
                if (currentDir.contains(file)) {
                    return currentDir.getContained(file);
                }
            }
        }
        return null;
    }

    /**
     * Prompts the user to pick a file
     * 
     * @param sc   Scanner connected to System.in
     * @param file A FileSize object of the current file
     */
    private static boolean askPick(Scanner sc, FileSize file) {
        System.out.println("The current file is: " + file.getName());
        System.out.println("Do you wish to pick another file?");
        System.out.println("Type y for yes, anything else otherwise");
        return sc.nextLine().startsWith("y");
    }

    /**
     * Prompts the user to keep the program or exit
     * 
     * @param sc Scanner connected to System.in
     * @return false if user responds with q, false otherwise
     */
    private static boolean askQuit(Scanner sc) {
        System.out.println("Type q to quit, anything else to continue");
        return sc.nextLine().startsWith("q");
    }

    /**
     * Prompts the user to get the name of a file
     * 
     * @param sc Scanner connected to System.in
     * @return the name of a valid file
     * @throws FileNotFoundException
     */
    private static String getFileName(Scanner sc) throws FileNotFoundException {
        System.out.print("Path of file: ");
        String fileName = sc.nextLine();
        while (!(new File(fileName).exists())) {
            System.out.println("The file with path: " + fileName
                    + " does not exist");
            System.out.print("Path of file: ");
            fileName = sc.nextLine();
        }
        return fileName;
    }

    /**
     * Display the provided bytes in the correct format
     * 
     * @param bytes the bytes to display
     */
    private static void displayBytes(long bytes) {
        final String[] SUFFIXES = { "", "Ki", "Mi", "Gi", "Ti", "Pi" };
        int power = 0;
        double doubleBytes = (double) bytes;
        while (doubleBytes > BYTES_PER_KiB) {
            doubleBytes /= BYTES_PER_KiB;
            power++;
        }
        System.out.printf("%,.2f %sB", doubleBytes, SUFFIXES[power]);
        System.out.printf(" (%,d bytes)\n", bytes);
    }

    /**
     * Displays the directory's size and all of it's files' sizes
     * 
     * @param file the file to get the size of
     */
    private static void displayDirectory(FileSize file) {
        if (!(file instanceof DirectorySize)) {
            System.out.println("The current file is not a direcotry");
        }
        DirectorySize dir = (DirectorySize) file;
        System.out.print("Directory size: ");
        displayBytes(dir.getSize());
        for (FileSize subFile : dir) {
            System.out.printf("%-30s- ", subFile.getName());
            displayBytes(subFile.getSize());
        }
    }

    /**
     * Shows the options for an active file and allows the user to pick an action
     * 
     * @param sc      Scanner connected to System.in
     * @param file    The active file
     * @param parents A stack with the file's parents
     * @return file object depending on the action taken. If the show sub file
     *         option is picked, the returned file is the chosen sub file, if the
     *         previous option is picked the file is the file's parent directory, in
     *         any other case return the given active file
     */
    private static FileSize showMenuOptions(Scanner sc, FileSize file, Stack<FileSize> parents) {
        final String SHOW_SIZE = "1";
        final String SHOW_DIRECTORY = "2";
        final String SHOW_SUBFILE = "3";
        final String PREVIOUS = "4";
        final String RETURN = "5";
        System.out.println("Pick an option");
        System.out.println("1. Show the size of the current directory");
        System.out.println("2. Show the full directory");
        System.out.println("3. Show the size of a sub file");
        System.out.println("4. Go back to the previous file. (must be inside a sub-file)");
        System.out.println("5. Return");
        System.out.println("q to quit");
        String choice = sc.nextLine().substring(0, 1);
        switch (choice) {
            case SHOW_SIZE:
                displayBytes(file.getSize());
                break;
            case SHOW_DIRECTORY:
                displayDirectory(file);
                break;
            case SHOW_SUBFILE:
                file = pickSubFile(sc, file, parents);
                break;
            case PREVIOUS:
                if (!parents.isEmpty()) {
                    file = parents.pop();
                } else {
                    System.out.println("not inside a sub-file");
                }
            case RETURN:
                break;
            case "q":
                System.exit(0);
        }
        return file;
    }

    /**
     * Prompts the user to pick a sub file of a directory
     * 
     * 
     * @param sc      Scanner connected to System.in
     * @param file    FileSize object of the file to look for subfiles
     * @param parents A stack of the file's parents. Modified to add the given file
     *                in the case a sub file is found
     * @return a FileSize object of the sub file. If given file is not a directory,
     *         return file back
     */
    private static FileSize pickSubFile(Scanner sc, FileSize file, Stack<FileSize> parents) {
        if (!file.isDirectory()) {
            return file;
        }
        DirectorySize dir = (DirectorySize) file;
        System.out.println("Type the name of the subfile: ");
        String name = sc.nextLine();
        for (FileSize subFile : dir) {
            if (subFile.getName().equals(name)) {
                parents.add(file);
                return subFile;
            }
        }
        while (true) {
            System.out.println("The file: " + name + " was not found");
            System.out.println("Type the name of the subfile: ");
            name = sc.nextLine();
            for (FileSize subFile : dir) {
                if (subFile.getName().equals(name)) {
                    parents.add(file);
                    return subFile;
                }
            }
        }
    }
}