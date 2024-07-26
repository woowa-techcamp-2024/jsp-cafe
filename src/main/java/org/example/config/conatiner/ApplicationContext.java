package org.example.config.conatiner;

public interface ApplicationContext {

    void initialize(String basePackage) throws Exception;

    void initialize(Class<?> mainClass) throws Exception;

    <T> T getBean(String name);

    <T> T getBean(Class<T> clazz);
}
