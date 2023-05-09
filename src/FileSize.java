import java.io.File;

/**
 * Interface to represent a File with its associated size
 */
public interface FileSize extends Comparable<FileSize> {
    /**
     * Get the size of this FileSize object
     * 
     * @return the size in bytes of the File or Folder
     */
    abstract long getSize();

    /**
     * Get the path of this FileSize object
     * 
     * @return the path of the File or Folder
     */
    abstract String getPath();

    /**
     * Get the file this object represents
     * 
     * @return the file of this representation
     */
    abstract File getFile();

    /**
     * Check if the underlying file is a directory
     * 
     * @return true if the underlying file is a directory, false otherwise
     */
    abstract boolean isDirectory();

    /**
     * Get the name of the underlying file
     * 
     * @return the name of the underlying file. This is just the last name of the
     *         file's path
     */
    abstract String getName();
}
