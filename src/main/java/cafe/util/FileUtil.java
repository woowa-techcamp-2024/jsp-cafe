package cafe.util;

import java.io.File;
import java.io.IOException;

public class FileUtil {

    private FileUtil() {
    }

    public static void createFile(String path) {
        if (path == null || !path.contains(".")) {
            throw new IllegalArgumentException("Path is null");
        }
        File file = new File(path);
        File parentDir = file.getParentFile();

        try {
            if (parentDir != null && !parentDir.exists()) {
                boolean dirCreated = parentDir.mkdirs();
                if (!dirCreated) {
                    throw new IOException();
                }
            }

            if (!file.exists()) file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}