package com.woowa;

import com.woowa.framework.BeanFactory;
import com.woowa.framework.argumentresovler.ArgumentResolverComposite;
import com.woowa.framework.web.ContentType;
import com.woowa.framework.web.mapping.HandlerMapping;
import com.woowa.framework.web.mapping.HandlerMethod;
import com.woowa.framework.web.ResponseEntity;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

public class DispatcherServlet extends HttpServlet {

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();
    private ArgumentResolverComposite argumentResolvers = new ArgumentResolverComposite();

    public void init(BeanFactory beanFactory) {
        handlerMappings.addAll(beanFactory.getBeans(HandlerMapping.class));
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
        if(req.getRequestURI().endsWith(".jsp")) {
            handleJspRequest(req, resp);
            return;
        }


        HandlerMapping handlerMapping = null;
        for (HandlerMapping mapping : handlerMappings) {
            if(mapping.isSupports(req)) {
                handlerMapping = mapping;
                break;
            }
        }
        if(handlerMapping == null) {
            throw new NoSuchElementException("지원하지 않는 요청 경로입니다.");
        }
        HandlerMethod handlerMethod = handlerMapping.getHandlerMethod(req);

        Parameter[] parameters = handlerMethod.getParameters();
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            args[i] = argumentResolvers.resolveArgument(parameter, req, resp);
        }

        ResponseEntity responseEntity = handlerMethod.invoke(args);

        if(responseEntity == null) {
            return;
        }
        String contentType = getContentType(req);
        resp.setContentType(contentType);
        resp.setContentLength(responseEntity.getBody().length);
        resp.setStatus(responseEntity.getStatus());
        if(responseEntity.getStatus() == 302) {
            resp.sendRedirect(responseEntity.getLocation());
            return;
        }
        for (Entry<String, String> entry : responseEntity.getHeader().entrySet()) {
            resp.addHeader(entry.getKey(), entry.getValue());
        }
        resp.getOutputStream().write(responseEntity.getBody());
        resp.getOutputStream().flush();
    }

    private void handleJspRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String jspPath = "/WEB-INF/classes/static" + req.getRequestURI();
        RequestDispatcher dispatcher = req.getRequestDispatcher(jspPath);
        dispatcher.forward(req, resp);
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
