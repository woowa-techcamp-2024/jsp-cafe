package woowa.frame.init;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 클래스를 스캔하는 유틸리티 클래스입니다.
 */
public class ClassUtils {

    public static List<Class<?>> scan(String basePackageName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = basePackageName.replace('.', '/');

        List<Class<?>> classes = new ArrayList<>();

        try {
            List<File> files = new ArrayList<>();
            Enumeration<URL> resources = classLoader.getResources(path);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                if (resource.getProtocol().equals("jar")) {
                    String jarPath = URLDecoder.decode(resource.getPath().substring(5, resource.getPath().indexOf("!")), StandardCharsets.UTF_8);
                    scanJar(basePackageName, jarPath, classes);
                } else {
                    files.add(new File(resource.getFile()));
                }
            }
            for (File file : files) {
                if (file.isDirectory()) {
                    classes.addAll(findClasses(file, basePackageName));
                }
            }

            return classes;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to scan classes");
        }
    }

    private static List<Class<?>> findClasses(File directory, String packageName) {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }

        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                try {
                    classes.add(Class.forName(className, false, classLoader));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        return classes;
    }

    private static void scanJar(String basePackageName, String jarPath, List<Class<?>> classes) throws IOException {
        try (JarFile jar = new JarFile(jarPath)) {
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String entryName = entry.getName();
                if (entryName.startsWith(basePackageName.replace('.', '/')) && entryName.endsWith(".class")) {
                    String className = entryName.substring(0, entryName.length() - 6).replace('/', '.');
                    Class<?> clazz = Class.forName(className);
                    classes.add(clazz);
                }
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
