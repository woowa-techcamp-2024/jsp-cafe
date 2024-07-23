package com.woowa;

import com.woowa.framework.BeanFactory;
import com.woowa.framework.argumentresovler.ArgumentResolverComposite;
import com.woowa.framework.web.HandlerMapping;
import com.woowa.framework.web.HandlerMethod;
import com.woowa.framework.web.ResponseEntity;
import com.woowa.framework.web.StaticResourceHandler;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Map.Entry;

public class DispatcherServlet extends HttpServlet {

    private final HandlerMapping handlerMapping;
    private ArgumentResolverComposite argumentResolvers = new ArgumentResolverComposite();

    public DispatcherServlet(HandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
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
        if(StaticResourceHandler.supports(req)) {
            byte[] body = StaticResourceHandler.getResource(req);
            resp.setContentLength(body.length);
            resp.setContentType("text/html");
            resp.getOutputStream().write(body);
            resp.getOutputStream().flush();
        }

        HandlerMethod handlerMethod = handlerMapping.getHandlerMethod(req);
        Parameter[] parameters = handlerMethod.getParameters();
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            args[i] = argumentResolvers.resolveArgument(parameter, req, resp);
        }

        ResponseEntity responseEntity = handlerMethod.invoke(args);

        resp.setStatus(responseEntity.getStatus());
        for (Entry<String, String> entry : responseEntity.getHeader().entrySet()) {
            resp.addHeader(entry.getKey(), entry.getValue());
        }
        resp.getWriter().print(Arrays.toString(responseEntity.getBody()));
    }
}
