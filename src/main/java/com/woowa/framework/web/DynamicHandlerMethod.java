package com.woowa.framework.web;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class DynamicHandlerMethod implements HandlerMethod {
    private final Object bean;
    private final Method method;

    public DynamicHandlerMethod(Object bean, Method method) {
        this.bean = bean;
        this.method = method;
    }

    @Override
    public ResponseEntity invoke(Object[] args) throws Throwable {
        try {
            return (ResponseEntity) method.invoke(bean, args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("지정된 메서드에 액세스 할 수 없습니다.");
        }
    }

    @Override
    public Parameter[] getParameters() {
        return method.getParameters();
    }
}
