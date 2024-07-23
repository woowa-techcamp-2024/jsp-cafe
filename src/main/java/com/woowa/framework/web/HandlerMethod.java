package com.woowa.framework.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class HandlerMethod {
    private final Object bean;
    private final Method method;

    public HandlerMethod(Object bean, Method method) {
        this.bean = bean;
        this.method = method;
    }

    public ResponseEntity invoke(Object[] args) throws Throwable {
        try {
            return (ResponseEntity) method.invoke(bean, args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("지정된 메서드에 액세스 할 수 없습니다.");
        }
    }

    public Parameter[] getParameters() {
        return method.getParameters();
    }
}
