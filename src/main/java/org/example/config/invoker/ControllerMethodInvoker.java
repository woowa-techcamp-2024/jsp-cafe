package org.example.config.invoker;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import org.example.config.annotation.PathVariable;
import org.example.config.annotation.RequestParam;
import org.example.config.controller.MethodHandler;
import org.example.util.UrlMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerMethodInvoker {

    private static final Logger logger = LoggerFactory.getLogger(ControllerMethodInvoker.class);

    public Object invokeHandlerMethod(MethodHandler handler, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Method method = handler.getMethod();
        Object[] args = resolveHandlerArguments(method, request, response, request.getSession());
        return method.invoke(handler.getInstance(), args);
    }

    private Object[] resolveHandlerArguments(Method method, HttpServletRequest request, HttpServletResponse response,
                                             HttpSession session) {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            if (parameter.isAnnotationPresent(RequestParam.class)) {
                args[i] = resolveRequestParam(parameter, request);
            } else if (parameter.isAnnotationPresent(PathVariable.class)) {
                args[i] = resolvePathVariable(parameter, request);
            } else if (parameter.getType().equals(HttpServletRequest.class)) {
                args[i] = request;
            } else if (parameter.getType().equals(HttpServletResponse.class)) {
                args[i] = response;
            } else if (parameter.getType().equals(HttpSession.class)) {
                args[i] = session;
            }
        }
        return args;
    }

    private Object resolveRequestParam(Parameter parameter, HttpServletRequest request) {
        RequestParam annotation = parameter.getAnnotation(RequestParam.class);
        String paramName = annotation.value().isEmpty() ? parameter.getName() : annotation.value();
        String paramValue = request.getParameter(paramName);

        logger.info("paramName: {}, paramValue: {}", paramName, paramValue);

        if (paramValue == null) {
            if (!annotation.defaultValue().equals("\n\t\t\n\t\t\n\ue000\ue001\ue002\n\t\t\t\t\n")) {
                paramValue = annotation.defaultValue();
            } else if (annotation.required()) {
                throw new IllegalArgumentException("Required parameter '" + paramName + "' is not present");
            } else {
                return null;
            }
        }


        paramValue = URLDecoder.decode(paramValue, StandardCharsets.UTF_8);

        return convertValueToType(paramValue, parameter.getType());
    }

    private Object resolvePathVariable(Parameter parameter, HttpServletRequest request) {
        PathVariable annotation = parameter.getAnnotation(PathVariable.class);
        String paramName = annotation.value().isEmpty() ? parameter.getName() : annotation.value();
        String requestURI = request.getRequestURI();

        String urlPattern = (String) request.getAttribute("currentUrlPattern");
        Map<String, String> pathVariables = UrlMatcher.extractPathVariables(urlPattern, requestURI);

        String paramValue = URLDecoder.decode(pathVariables.get(paramName), StandardCharsets.UTF_8);

        if (paramValue == null && annotation.required()) {
            throw new IllegalArgumentException("Required path variable '" + paramName + "' is not present");
        }

        return convertValueToType(paramValue, parameter.getType());
    }

    private Object convertValueToType(String value, Class<?> targetType) {
        if (value == null) {
            return null;
        }

        if (targetType.equals(String.class)) {
            return value;
        } else if (targetType.equals(int.class) || targetType.equals(Integer.class)) {
            return Integer.parseInt(value);
        } else if (targetType.equals(long.class) || targetType.equals(Long.class)) {
            return Long.parseLong(value);
        } else if (targetType.equals(double.class) || targetType.equals(Double.class)) {
            return Double.parseDouble(value);
        } else if (targetType.equals(boolean.class) || targetType.equals(Boolean.class)) {
            return Boolean.parseBoolean(value);
        } else if (targetType.isEnum()) {
            return Enum.valueOf((Class<? extends Enum>) targetType, value);
        }

        throw new IllegalArgumentException("Unsupported type conversion for " + targetType.getName());
    }
}