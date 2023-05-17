import java.io.File;

/**
 * Creates the appropiate FileSize object depending on the type of file passed
 * down
 */
public class FileSizeFactory {
    /**
     * Creates the appropiate FileSize object
     * 
     * @param file the file to make a FileSize from
     * @return a DirectorySize object if file is a directory, a FileFileSize object
     *         otherwise
     */
    public FileSize createFileSize(File file) {
        if (file.isDirectory()) {
            return new DirectorySize(file);
        }
        return new FileFileSize(file);
    }

    public FileSize createFileSize(File file, FileSize parent) {
        if (file.isDirectory()) {
            return new DirectorySize(file, parent);
        }
        return new FileFileSize(file, parent);
    }
}
