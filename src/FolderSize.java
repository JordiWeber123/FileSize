import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class FolderSize {
    final int NOT_PROCESSED = -1;
    private File file;
    private long size;
    private HashMap<FolderSize, Long> containedFolders;
    private ArrayList<File> containedFiles;

    public FolderSize(File file) {
        if (!file.isDirectory()) {
            throw new IllegalArgumentException("file is not a directory");
        }
        this.file = file;
        this.size = NOT_PROCESSED;
        this.containedFolders = new HashMap<>();
        this.containedFiles = new ArrayList<>();

    }

    public FolderSize(String pathName) {
        File fileFromPath = new File(pathName);
        if (!fileFromPath.isDirectory()) {
            throw new IllegalArgumentException("file from pathName is not a directory");
        }
        this.file = fileFromPath;
        this.size = NOT_PROCESSED;
        this.containedFolders = new HashMap<>();
        this.containedFiles = new ArrayList<>();
    }

    public String getPath() {
        return file.getPath();
    }

    /**
     * Get the size of a folder
     * pre: file must be a directory
     * 
     * @param file the directory to get the size of
     * @return size of folder(including all )
     */
    public long getSize() {
        if (size == NOT_PROCESSED) {
            size = 0;
            File[] filesList = file.listFiles();
            if (filesList != null) {
                for (File subFile : filesList) {
                    if (subFile.isDirectory()) {
                        FolderSize subFolder = new FolderSize(subFile);
                        size += subFolder.getSize();
                        containedFolders.put(subFolder, subFolder.getSize());
                    } else {
                        long subLength = subFile.length();
                        size += subLength;
                        containedFiles.add(subFile);
                    }
                }
            }
        }
        return size;
    }
}
