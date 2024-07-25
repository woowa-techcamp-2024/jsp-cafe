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
    private Set<Class<?>> componentClasses = new HashSet<>();
    private static final Logger log = LoggerFactory.getLogger(SimpleDIContainer.class);

    public SimpleDIContainer(String basePackage) throws Exception {
        componentClasses = scan(basePackage);
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

        Class<?> originalClass = clazz;
        if (Modifier.isAbstract(clazz.getModifiers()) || clazz.isInterface()) {
            clazz = findImplementation(clazz);
        }

        log.info("Using implementation class: " + clazz.getName());

        Constructor<?> constructor = findSuitableConstructor(clazz);
        if (constructor == null) {
            throw new RuntimeException("No suitable constructor found for class: " + clazz.getName());
        }

        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object[] parameters = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            parameters[i] = getOrCreateInstance(parameterTypes[i]);
        }

        Object instance = constructor.newInstance(parameters);
        instances.put(originalClass, instance); // Save the instance with the original class
        if (!originalClass.equals(clazz)) {
            instances.put(clazz, instance); // Also save with the implementation class if different
        }
        currentlyCreating.remove(originalClass); // Remove from currentlyCreating using the original class
        return instance;
    }

    private Object getOrCreateInstance(Class<?> clazz) throws Exception {
        if (instances.containsKey(clazz)) {
            return instances.get(clazz);
        }
        if (currentlyCreating.contains(clazz)) {
            throw new RuntimeException("Circular dependency detected: " + clazz);
        }

        Class<?> implementationClass = clazz;
        if (Modifier.isAbstract(clazz.getModifiers()) || clazz.isInterface()) {
            implementationClass = findImplementation(clazz);
        }

        Object instance = createInstance(implementationClass);
        instances.put(clazz, instance); // Save the instance with the parent type
        instances.put(implementationClass, instance); // Save the instance with the implementation type
        currentlyCreating.remove(clazz);
        return instance;
    }

    private Constructor<?> findSuitableConstructor(Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getConstructors();
        for (Constructor<?> constructor : constructors) {
            if (isConstructorSuitable(constructor)) {
                return constructor;
            }
        }
        return null;
    }

    private boolean isConstructorSuitable(Constructor<?> constructor) {
        for (Class<?> parameterType : constructor.getParameterTypes()) {
            if (Modifier.isAbstract(parameterType.getModifiers()) || parameterType.isInterface()) {
                try {
                    findImplementation(parameterType);
                } catch (ClassNotFoundException e) {
                    return false;
                }
            }
        }
        return true;
    }

    private Class<?> findImplementation(Class<?> clazz) throws ClassNotFoundException {
        System.out.println("clazz = " + clazz);
        log.info("Finding implementation for: " + clazz.getName());

        for (Class<?> implClass : getAllClasses()) {
            log.info("implClass = " + implClass);
            System.out.println("implClass = " + implClass);
            if (clazz.isAssignableFrom(implClass) && !implClass.isInterface() && !Modifier.isAbstract(implClass.getModifiers())) {
                return implClass;
            }
        }
        for (Class<?> implClass : componentClasses) {
            if (clazz.isAssignableFrom(implClass) && !implClass.isInterface() && !Modifier.isAbstract(implClass.getModifiers())) {
                return implClass;
            }
        }
        throw new ClassNotFoundException("No implementation found for: " + clazz.getName());
    }

    private Set<Class<?>> getAllClasses() throws ClassNotFoundException {
        Set<Class<?>> classes = new HashSet<>();
        for (Class<?> clazz : instances.keySet()) {
            classes.add(clazz);
        }
        for (Class<?> clazz : currentlyCreating) {
            classes.add(clazz);
        }
        return classes;
    }

    public <T> T resolve(Class<T> clazz) {
        return clazz.cast(instances.get(clazz));
    }
}
