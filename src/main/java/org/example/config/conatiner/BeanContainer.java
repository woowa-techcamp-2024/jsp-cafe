package org.example.config.conatiner;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.example.config.annotation.Autowired;
import org.example.config.exception.BeanCreationException;
import org.example.config.exception.BeanNotFoundException;
import org.example.config.exception.CircularDependencyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeanContainer {
    private static final Logger logger = LoggerFactory.getLogger(BeanContainer.class);
    private final Map<String, Object> beans = new HashMap<>();
    private final Map<String, Class<?>> beanClasses = new HashMap<>();
    private final Map<Class<?>, String> typeToNameMap = new HashMap<>();
    private final Set<String> beansInCreation = new HashSet<>();

    public void registerBeanClass(String name, Class<?> beanClass) {
        beanClasses.put(name, beanClass);
        registerType(beanClass, name);
        for (Class<?> iface : beanClass.getInterfaces()) {
            registerType(iface, name);
        }
    }

    public void instantiateAndRegister() {
        List<String> remainingBeans = new ArrayList<>(beanClasses.keySet());

        while (!remainingBeans.isEmpty()) {
            boolean progress = false;
            Iterator<String> iterator = remainingBeans.iterator();
            while (iterator.hasNext()) {
                String beanName = iterator.next();
                if (canCreateBean(beanName)) {
                    createAndRegisterBean(beanName);
                    iterator.remove();
                    progress = true;
                }
            }
            if (!progress) {
                throw new BeanCreationException(
                        "Unable to create beans due to unresolved dependencies: " + remainingBeans);
            }
        }
    }

    private boolean canCreateBean(String beanName) {
        Class<?> beanClass = beanClasses.get(beanName);
        Constructor<?> constructor = findAutowiredConstructor(beanClass);
        if (constructor == null) {
            return true; // 의존성이 없는 빈
        }
        for (Class<?> paramType : constructor.getParameterTypes()) {
            String dependencyName = typeToNameMap.get(paramType);
            if (dependencyName != null && !beans.containsKey(dependencyName)) {
                return false; // 의존성이 아직 생성되지 않음
            }
        }
        return true;
    }

    private void createAndRegisterBean(String beanName) {
        if (beansInCreation.contains(beanName)) {
            throw new CircularDependencyException("Circular dependency detected for bean: " + beanName);
        }

        beansInCreation.add(beanName);
        try {
            Class<?> beanClass = beanClasses.get(beanName);
            Constructor<?> constructor = findAutowiredConstructor(beanClass);
            Object bean;
            if (constructor != null) {
                Object[] params = getConstructorParams(constructor);
                bean = constructor.newInstance(params);
            } else {
                bean = beanClass.getDeclaredConstructor().newInstance();
            }
            beans.put(beanName, bean);
            logger.debug("Created and registered bean '{}': {}", beanName, bean);
        } catch (CircularDependencyException e) {
            throw e; // 순환 의존성 예외를 그대로 전파
        } catch (Exception e) {
            throw new BeanCreationException("Failed to create bean: " + beanName, e);
        } finally {
            beansInCreation.remove(beanName);
        }
    }

    public Object getBean(String name) {
        Object bean = beans.get(name);
        if (bean == null) {
            throw new BeanNotFoundException("No bean found with name: " + name);
        }
        return bean;
    }

    public <T> T getBean(Class<T> type) {
        String beanName = typeToNameMap.get(type);
        if (beanName == null) {
            throw new BeanNotFoundException("No bean found for type: " + type.getName());
        }
        return (T) getBean(beanName);
    }

    public Map<String, Object> getBeans() {
        return beans;
    }

    private void registerType(Class<?> type, String name) {
        typeToNameMap.putIfAbsent(type, name);
    }

    private Constructor<?> findAutowiredConstructor(Class<?> beanClass) {
        return Arrays.stream(beanClass.getDeclaredConstructors())
                .filter(c -> c.isAnnotationPresent(Autowired.class))
                .findFirst()
                .orElse(null);
    }

    private Object[] getConstructorParams(Constructor<?> constructor) {
        return Arrays.stream(constructor.getParameterTypes())
                .map(paramType -> {
                    String dependencyName = typeToNameMap.get(paramType);
                    if (dependencyName == null) {
                        throw new BeanNotFoundException("No bean found for type: " + paramType.getName());
                    }
                    if (beansInCreation.contains(dependencyName)) {
                        throw new CircularDependencyException(
                                "Circular dependency detected for bean: " + dependencyName);
                    }
                    return getBean(dependencyName);
                })
                .toArray();
    }

    private Object resolveParameter(Class<?> paramType) {
        String beanName = typeToNameMap.get(paramType);
        if (beanName == null) {
            throw new BeanNotFoundException("No bean found for type: " + paramType.getName());
        }
        return getBean(beanName);
    }
}