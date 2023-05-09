import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class DirectorySize extends AbstractFileSize implements Iterable<FileSize> {
    private List<FileSize> containedFiles;

    public DirectorySize(File file) {
        this.file = file;
        containedFiles = new ArrayList<>();
        processSizeAndContained();
        Collections.sort(containedFiles);
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
            otherFile = (File) file;
        }
        return otherFile.toPath().startsWith(this.getPath());
    }

    public Iterator<FileSize> iterator() {
        return containedFiles.iterator();
    }

}
