package objects;

import utils.FileUtils;

import java.io.Serializable;

public class MP3 implements Serializable {
    private String name;
    private String path;

    @Override
    public String toString() {
        return FileUtils.getFileNameWithoutExtension(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public MP3(String name, String path) {
        this.name = name;
        this.path = path;
    }
}
