package com.woowa.framework.web;

import com.woowa.framework.BeanFactory;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class HandlerMapping {

    private final Map<String, Map<HttpMethod, HandlerMethod>> handlers = new ConcurrentHashMap<>();
    private final BeanFactory container;

    public HandlerMapping(BeanFactory container) {
        this.container = container;
    }

    public void init() {
        for (Object bean : container.getBeans()) {
            for (Method method : bean.getClass().getMethods()) {
                registerDynamicHandler(bean, method);}
        }
    }

    private void registerDynamicHandler(Object bean, Method method) {
        if(method.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping annotation = method.getAnnotation(RequestMapping.class);
            String path = annotation.path();
            HttpMethod httpMethod = annotation.method();
            Map<HttpMethod, HandlerMethod> httpMethodHandlerMethodMap = handlers.computeIfAbsent(path,
                    key -> new ConcurrentHashMap<>());
            httpMethodHandlerMethodMap.put(httpMethod, new HandlerMethod(bean, method));
        }
    }

    public HandlerMethod getHandlerMethod(HttpServletRequest request) {
        Optional<Map<HttpMethod, HandlerMethod>> optionalHandlerMethods = Optional.ofNullable(
                handlers.get(request.getRequestURI()));
        if(optionalHandlerMethods.isEmpty()) {
            throw new IllegalArgumentException("지원하지 않는 경로입니다.");
        }
        Map<HttpMethod, HandlerMethod> requestHandlerMethods = optionalHandlerMethods.get();
        return Optional.ofNullable(requestHandlerMethods.get(HttpMethod.from(request.getMethod())))
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 메서드입니다."));
    }
}
