package com.woowa.framework.argumentresovler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Parameter;

public interface ArgumentResolver {

    boolean supportsParameter(Parameter parameter);

    Object resolveArgument(Parameter parameter, HttpServletRequest request, HttpServletResponse response);
}
