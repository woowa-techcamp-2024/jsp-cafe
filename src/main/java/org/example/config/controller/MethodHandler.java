package org.example.config.controller;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MethodHandler {
    private final Object instance;
    private final Method method;
    private Map<String, Class<?>> pathVariables = new HashMap<>();
    private Map<String, ParameterInfo> requestParams = new HashMap<>();

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

    public Map<String, Class<?>> getPathVariables() {
        return pathVariables;
    }

    public Map<String, ParameterInfo> getRequestParams() {
        return requestParams;
    }

    public void setPathVariables(Map<String, Class<?>> pathVariables) {
        this.pathVariables = pathVariables;
    }

    public void setRequestParams(Map<String, ParameterInfo> requestParams) {
        this.requestParams = requestParams;
    }

    @Override
    public String toString() {
        return "MethodHandler{" +
                "instance=" + instance +
                ", method=" + method +
                ", pathVariables=" + pathVariables +
                ", requestParams=" + requestParams +
                '}';
    }
}