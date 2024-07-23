package com.woowa;

import com.woowa.framework.BeanFactory;
import com.woowa.framework.argumentresovler.ArgumentResolverComposite;
import com.woowa.framework.web.DynamicHandlerMapping;
import com.woowa.framework.web.ContentType;
import com.woowa.framework.web.HandlerMethod;
import com.woowa.framework.web.ResponseEntity;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Map.Entry;

public class DispatcherServlet extends HttpServlet {

    private final DynamicHandlerMapping dynamicHandlerMapping;
    private ArgumentResolverComposite argumentResolvers = new ArgumentResolverComposite();

    public DispatcherServlet(DynamicHandlerMapping dynamicHandlerMapping) {
        this.dynamicHandlerMapping = dynamicHandlerMapping;
    }

    public void init(BeanFactory beanFactory) {
        argumentResolvers = beanFactory.getBean(ArgumentResolverComposite.class);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        try {
            dispatch(req, resp);
        } catch (Throwable e) {
            //todo 에러핸들링
        }
    }

    private void dispatch(HttpServletRequest req, HttpServletResponse resp) throws Throwable {
        HandlerMethod handlerMethod = dynamicHandlerMapping.getHandlerMethod(req);
        Parameter[] parameters = handlerMethod.getParameters();
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            args[i] = argumentResolvers.resolveArgument(parameter, req, resp);
        }

        ResponseEntity responseEntity = handlerMethod.invoke(args);

        String contentType = getContentType(req);
        resp.setContentType(contentType);
        resp.setContentLength(responseEntity.getBody().length);
        resp.setStatus(responseEntity.getStatus());
        for (Entry<String, String> entry : responseEntity.getHeader().entrySet()) {
            resp.addHeader(entry.getKey(), entry.getValue());
        }
        resp.getOutputStream().write(responseEntity.getBody());
        resp.getOutputStream().flush();
    }

    private String getContentType(HttpServletRequest request) {
        if(request.getContentType() != null) {
            return request.getContentType();
        }

        int lastIndexOfDot = request.getRequestURI().lastIndexOf(".");
        if(lastIndexOfDot == -1) {
            return ContentType.TEXT_HTML.getContentType();
        }
        return ContentType.getContentType(request.getRequestURI().substring(lastIndexOfDot + 1));
    }
}
