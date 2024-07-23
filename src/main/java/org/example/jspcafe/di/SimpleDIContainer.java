package org.example.jspcafe.di;

import org.example.jspcafe.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class SimpleDIContainer {

    private final Map<Class<?>, Object> instances = new HashMap<>();
    private final Set<Class<?>> currentlyCreating = new HashSet<>();
    private static final Logger log = LoggerFactory.getLogger(SimpleDIContainer.class);

    public SimpleDIContainer(String basePackage) throws Exception {
        Set<Class<?>> componentClasses = scan(basePackage);
        createInstances(componentClasses);
    }

    private Set<Class<?>> scan(String basePackage) throws IOException, ClassNotFoundException {
        Set<Class<?>> classes = new HashSet<>();
        String path = basePackage.replace('.', '/');
        Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(path);
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            classes.addAll(findClasses(resource, basePackage));
        }
        return filterComponents(classes);
    }

    private Set<Class<?>> findClasses(URL resource, String packageName) throws ClassNotFoundException, IOException {
        Set<Class<?>> classes = new HashSet<>();
        if (resource.getProtocol().equals("jar")) {
            // Scan classes from JAR
            String jarPath = resource.getPath().substring(5, resource.getPath().indexOf("!"));
            try (JarFile jarFile = new JarFile(URLDecoder.decode(jarPath, "UTF-8"))) {
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    if (entry.getName().startsWith(packageName.replace('.', '/')) && entry.getName().endsWith(".class") && !entry.isDirectory()) {
                        String className = entry.getName().replace('/', '.').substring(0, entry.getName().length() - 6);
                        classes.add(Class.forName(className));
                    }
                }
            }
        } else {
            // Scan classes from filesystem
            File directory = new File(URLDecoder.decode(resource.getFile(), "UTF-8"));
            if (directory.exists()) {
                for (File file : directory.listFiles()) {
                    if (file.isDirectory()) {
                        classes.addAll(findClasses(file.toURI().toURL(), packageName + "." + file.getName()));
                    } else if (file.getName().endsWith(".class")) {
                        classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
                    }
                }
            }
        }
        return classes;
    }

    private Set<Class<?>> filterComponents(Set<Class<?>> classes) {
        Set<Class<?>> components = new HashSet<>();
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Component.class)) {
                components.add(clazz);
            }
        }
        return components;
    }

    private void createInstances(Set<Class<?>> componentClasses) throws Exception {
        for (Class<?> clazz : componentClasses) {
            createInstance(clazz);
        }
    }

    private Object createInstance(Class<?> clazz) throws Exception {
        if (instances.containsKey(clazz)) {
            return instances.get(clazz);
        }
        if (currentlyCreating.contains(clazz)) {
            throw new RuntimeException("Circular dependency detected: " + clazz);
        }
        currentlyCreating.add(clazz);

        log.info("Creating instance of class: " + clazz.getName());

        if (Modifier.isAbstract(clazz.getModifiers()) || clazz.isInterface()) {
            clazz = findImplementation(clazz);
        }

        Constructor<?>[] constructors = clazz.getConstructors();
        if (constructors.length == 0) {
            throw new RuntimeException("No public constructor found for class: " + clazz.getName());
        }

        Constructor<?> constructor = constructors[0];
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object[] parameters = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            parameters[i] = createInstance(parameterTypes[i]);
        }

        Object instance = constructor.newInstance(parameters);
        instances.put(clazz, instance);
        currentlyCreating.remove(clazz);
        return instance;
    }

    private Class<?> findImplementation(Class<?> clazz) throws ClassNotFoundException {
        for (Class<?> implClass : instances.keySet()) {
            if (clazz.isAssignableFrom(implClass) && !implClass.isInterface() && !Modifier.isAbstract(implClass.getModifiers())) {
                return implClass;
            }
        }
        throw new ClassNotFoundException("No implementation found for: " + clazz.getName());
    }

    public <T> T resolve(Class<T> clazz) {
        return clazz.cast(instances.get(clazz));
    }
}
