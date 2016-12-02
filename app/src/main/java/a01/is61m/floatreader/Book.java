package a01.is61m.floatreader;

/**
 * Created by admin on 27.11.2016.
 */
public class Book {

    private String path;
    private String name;

    public Book(String path, String name) {
        this.path = path;
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }
}
