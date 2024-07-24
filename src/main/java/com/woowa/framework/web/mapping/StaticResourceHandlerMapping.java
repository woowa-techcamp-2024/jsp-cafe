package com.woowa.framework.web.mapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StaticResourceHandlerMapping implements HandlerMapping {

    private final Map<String, HandlerMethod> cache = new ConcurrentHashMap<>();

    @Override
    public boolean isSupports(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.contains(".") && !uri.endsWith(".jsp");
    }

    @Override
    public HandlerMethod getHandlerMethod(HttpServletRequest request) {
        HandlerMethod handlerMethod = cache.get(request.getRequestURI());
        if (handlerMethod == null) {
            String realPath = request.getServletContext().getRealPath("/WEB-INF/classes/static");
            handlerMethod = new StaticResourceHandlerMethod(realPath, request.getRequestURI());
            cache.put(request.getRequestURI(), handlerMethod);
        }
        return handlerMethod;
    }
}
