import java.io.File;
import java.util.HashSet;

public class FolderSize {
    final int NOT_PROCESSED = -1;
    private File file;
    private long size;
    private HashSet<File> containedFiles;

    public FolderSize(File file) {
        if (!file.isDirectory()) {
            throw new IllegalArgumentException("file is not a directory");
        }
        this.file = file;
        this.size = NOT_PROCESSED;
        this.containedFiles = new HashSet<>();
    }

    public FolderSize(String pathName) {
        this(new File(pathName));
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
