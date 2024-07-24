package com.woowa.framework.argumentresovler;

import com.woowa.framework.web.RequestParameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Parameter;

public class RequestParameterResolver implements ArgumentResolver {
    @Override
    public boolean supportsParameter(Parameter parameter) {
        return parameter.isAnnotationPresent(RequestParameter.class);
    }

    @Override
    public Object resolveArgument(Parameter parameter, HttpServletRequest request, HttpServletResponse response) {
        RequestParameter annotation = parameter.getAnnotation(RequestParameter.class);
        String name = annotation.value();
        return request.getParameter(name);
    }
}
