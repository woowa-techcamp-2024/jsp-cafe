package com.woowa.framework.argumentresovler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Parameter;

public class HttpServletResponseResolver implements ArgumentResolver {
    @Override
    public boolean supportsParameter(Parameter parameter) {
        Class<?> type = parameter.getType();
        return HttpServletResponse.class.isAssignableFrom(type);
    }

    @Override
    public Object resolveArgument(Parameter parameter, HttpServletRequest request, HttpServletResponse response) {
        return response;
    }
}
