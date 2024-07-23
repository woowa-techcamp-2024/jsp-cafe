package com.woowa.framework.argumentresovler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class ArgumentResolverComposite implements ArgumentResolver {
    private final List<ArgumentResolver> argumentResolvers = new ArrayList<>();

    @Override
    public boolean supportsParameter(Parameter parameter) {
        return false;
    }

    @Override
    public Object resolveArgument(Parameter parameter, HttpServletRequest request, HttpServletResponse response) {
        for (ArgumentResolver argumentResolver : argumentResolvers) {
            if(argumentResolver.supportsParameter(parameter)) {
                return argumentResolver.resolveArgument(parameter, request, response);
            }
        }
        return null;
    }

    public void addArgumentResolver(ArgumentResolver argumentResolver) {
        argumentResolvers.add(argumentResolver);
    }
}
