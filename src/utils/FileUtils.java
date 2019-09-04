package utils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileUtils {
    public static String getFileNameWithoutExtension(String name) {
        File file = new File(name);
        int index = file.getName().lastIndexOf(".");
        if (index > 0 && index <= file.getName().length() - 2) {
            return file.getName().substring(0, index);
        }
        return "noname";
    }

    public static String getFileExtension(File f) {
        String extension = null;
        String name = f.getName();
        int index = name.lastIndexOf(".");

        if (index > 0 && index < name.length() - 1) {
            extension = name.substring(index + 1).toLowerCase();
        }
        return extension;
    }

    public static void addFileFilter(JFileChooser fileChooser, FileFilter mp3FileFilter) {
        fileChooser.removeChoosableFileFilter(fileChooser.getFileFilter());
        fileChooser.setFileFilter(mp3FileFilter);
        fileChooser.setSelectedFile(new File(""));
    }

    public static void serialize(Object obj, String fileName) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(obj);
            objectOutputStream.flush();
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static Object deSerialize(String fileName) {
        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Object object = objectInputStream.readObject();
            fileInputStream.close();
            return object;
        } catch (IOException | ClassNotFoundException e) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }
}
