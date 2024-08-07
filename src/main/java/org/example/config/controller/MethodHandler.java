package org.example.config.controller;

import java.lang.reflect.Method;

public class MethodHandler {
    private final Object instance;
    private final Method method;

    public MethodHandler(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
    }

    public Object getInstance() {
        return instance;
    }

    public Method getMethod() {
        return method;
    }

    @Override
    public String toString() {
        return "MethodHandler{" +
                "instance=" + instance +
                ", method=" + method +
                '}';
    }
}