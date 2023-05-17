import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    private static final double BYTES_PER_KiB = 1024;

    // TODO: make user experience better
    // TODO: efficiency
    // TODO: check efficiency, maybe use recursive backtracking to work the way I
    // TODO: want it to?
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
            if (files.containsKey(file)) {
                activeFile = files.get(file);
            } else if (mapContainsSubFile(files, file)) {
                // TODO: finish this
                activeFile = files.get(file);
            } else {
                activeFile = FSFactory.createFileSize(file);
                files.put(file, activeFile);
            }
            do {
                if (activeFile.isDirectory()) {
                    activeFile = showMenuOptions(sc, activeFile);
                } else {
                    System.out.println("Displaying information for: " + activeFile.getName());
                    displayBytes(activeFile.getSize());
                }
            } while (!askPick(sc, activeFile));
        } while (!askQuit(sc));
    }

    private static boolean mapContainsSubFile(HashMap<File, FileSize> filesMap, File file) {
        return false;
    }

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

    private static FileSize showMenuOptions(Scanner sc, FileSize file) {
        final String SHOW_SIZE = "1";
        final String SHOW_DIRECTORY = "2";
        final String SHOW_SUBFILE = "3";
        final String RETURN = "4";
        System.out.println("Pick an option");
        System.out.println("1. Show the size of the current directory");
        System.out.println("2. Show the full directory");
        System.out.println("3. Show the size of a sub file");
        System.out.println("4. Pick return");
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
                file = pickSubFile(sc, file);
                break;
            case RETURN:
                break;
            case "q":
                System.exit(0);
        }
        return file;
    }

    private static FileSize pickSubFile(Scanner sc, FileSize file) {
        DirectorySize dir = (DirectorySize) file;
        System.out.println("Type the name of the subfile: ");
        String name = sc.nextLine();
        for (FileSize subFile : dir) {
            if (subFile.getName().equals(name)) {
                return subFile;
            }
        }
        while (true) {
            System.out.println("The file: " + name + " was not found");
            System.out.println("Type the name of the subfile: ");
            name = sc.nextLine();
            for (FileSize subFile : dir) {
                if (subFile.getName().equals(name)) {
                    return subFile;
                }
            }
        }
    }

}