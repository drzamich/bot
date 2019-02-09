package bot.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public final class FileHelper {
    private FileHelper() {}

    public static <T> void serializeObject(T o, String path) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(o);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (IOException e) {
            log.error("Could not serialize object to file", e);
        }
    }

    public static <T> T deserializeObject(String path) {
        T o;
        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            o = (T) objectInputStream.readObject();
            objectInputStream.close();
        } catch (Exception e) {
            log.error("Could not deserialize object from file", e);
            o = null;
        }
        return o;
    }

    public static boolean fileExists(String path) {
        File f = new File(path);
        return f.exists() && !f.isDirectory();
    }

    /**
     * @param path
     * Reads the file at given path and returns a List with the contents.
     * Each line is a list element, and lines starting with // are ignored
     */
    public static List<String> readFile(String path) {
        List<String> result = Collections.emptyList();
        Path pathToFile = Paths.get(path);
        try {
            List<String> l = Files.readAllLines(pathToFile);
            result = l.stream()
                    .filter(s -> s.indexOf("//") != 0)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("Unable to read the file: " + path, e);
        }
        return result;
    }
}
