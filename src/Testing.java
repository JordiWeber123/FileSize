import java.io.File;

public class Testing {
    public static void main(String[] args) {
        File file = new File(
                "C:/Users/miste/Programming/Java Prgramming/New folder/FolderView/bin/AbstractFileSize.class");
        File dir = new File("C:/Users/miste/Programming/Java Prgramming/New folder/FolderView");
        FileSizeFactory factory = new FileSizeFactory();
        FileSize FFS = factory.createFileSize(file);
        FileSize DS = factory.createFileSize(dir);
        DirectorySize ds = (DirectorySize) DS;
        System.out.println(ds.contains(FFS));
        System.out.println(FFS.hashCode());
    }
}
