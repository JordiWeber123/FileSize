import java.io.File;

public class FileFileSize extends AbstractFileSize {
    /**
     * Create a FileSize of a file
     * It is preferred to use a File object where isDirectory() is false, in which
     * case DirectorySize should be used
     * 
     * @param file the file
     */
    public FileFileSize(File file) {
        this.file = file;
        this.size = file.length();
    }
}
