import java.io.File;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class DirectorySize extends AbstractFileSize implements Iterable<FileSize> {
    private Set<FileSize> containedFiles;

    public DirectorySize(File file, FileSize parent) {
        this.file = file;
        this.parent = parent;
        containedFiles = new TreeSet<>();
        processSizeAndContained();
    }

    public DirectorySize(File file) {
        this(file, null);
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
                FileSize subFileSize = FSFactory.createFileSize(subFile, this);
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
            otherFile = (File) file;
        }
        return otherFile.toPath().startsWith(this.getPath());
    }

    public Iterator<FileSize> iterator() {
        return containedFiles.iterator();
    }

}
