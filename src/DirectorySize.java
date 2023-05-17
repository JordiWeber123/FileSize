import java.io.File;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class DirectorySize extends AbstractFileSize implements Iterable<FileSize> {
    private Set<FileSize> containedFiles;

    public DirectorySize(File file) {
        this.file = file;
        containedFiles = new TreeSet<>();
        processSizeAndContained();
    }

    /**
     * Recursively calculates this Directory's size in bytes and adds its contained
     * files to the List
     */
    private void processSizeAndContained() {
        if (file == null) {
            throw new IllegalStateException("file is null");
        }
        size = 0;
        File[] subFiles = file.listFiles();
        if (subFiles != null) {
            FileSizeFactory FSFactory = new FileSizeFactory();
            for (File subFile : subFiles) {
                FileSize subFileSize = FSFactory.createFileSize(subFile);
                size += subFileSize.getSize();
                containedFiles.add(subFileSize);
            }
        }
    }

    /**
     * Check if this directory contains another file
     * 
     * @param other the object to check
     * @return true if this directory contains the file, false otherwise
     */
    public boolean contains(Object other) {
        if (!(other instanceof FileSize || other instanceof File)) {
            return false;
        }
        File otherFile;
        if (other instanceof FileSize) {
            otherFile = ((FileSize) other).getFile();
        } else {
            otherFile = (File) other;
        }
        return otherFile.getPath().startsWith(this.getPath());
    }

    /**
     * Get a FileSize contained by this DirectorySize
     * 
     * @param file the File that matches the FileSize contained in this
     *             Directory
     * @return the FileSize that matches the File contained in this Directory
     */
    public FileSize getContained(File file) {
        if (!this.contains(file)) {
            throw new IllegalArgumentException("file is not contained in this directory");
        }
        for (FileSize sub : this) {
            if (sub.getFile().equals(file)) {
                return sub;
            } else if (sub instanceof DirectorySize) {
                DirectorySize subDir = (DirectorySize) sub;
                if (subDir.contains(file)) {
                    return subDir.getContained(file);
                }
            }
        }
        return null;
    }

    /**
     * Create an Iterator over this Directory's sub files
     * 
     * @return Iterator over the files contained in this directory
     */
    public Iterator<FileSize> iterator() {
        return containedFiles.iterator();
    }

}
