import java.io.File;

public abstract class AbstractFileSize implements FileSize {
    protected FileSize parent;
    protected File file;
    protected long size;

    /**
     * Get the size of this file
     * 
     * @return the size of this file or directory in bytes
     */
    public long getSize() {
        return size;
    }

    /**
     * Get the path of this FileSize object
     * 
     * @return the path of the File or Folder
     */
    public String getPath() {
        return file.getPath();
    }

    /**
     * Get the file this object represents
     * 
     * @return the file of this representation
     */
    public File getFile() {
        return file;
    }

    /**
     * Check if the underlying file is a directory
     * 
     * @return true if the underlying file is a directory, false otherwise
     */
    public boolean isDirectory() {
        return file.isDirectory();
    }

    /**
     * Indicates wether this FileSize is equal to other
     * 
     * @return true if the FileSize object is equal to other, false otherwise
     */
    public boolean equals(Object other) {
        if (!(other instanceof FileSize)) {
            return false;
        }
        FileSize otherFileSize = (FileSize) other;
        if (this.getFile().equals(otherFileSize.getFile())) {
            return true;
        }
        return false;
    }

    /**
     * Get the name of the underlying file
     * 
     * @return the name of the underlying file. This is just the last name of the
     *         file's path
     */
    public String getName() {
        return file.getName();
    }

    /**
     * Returns a hashcode for this FileSize
     * 
     * @return this FileSize's hashcode
     */
    public int hashCode() {
        return file.hashCode();
    }

    /**
     * Compare this FileSize to another
     * 
     * @return -1 if this FileSize's size is bigger than other's size. 0 if they
     *         are equal and 1 this size is smaller
     */
    public int compareTo(FileSize other) {
        int sizeComp = Long.compare(other.getSize(), this.getSize());
        if(sizeComp != 0){
            return sizeComp;
        }
        return this.getPath().compareTo(other.getPath());

    }
}
