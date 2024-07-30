package org.example.demo.aop;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo.RouteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;

public class NeedLoginHandler implements AopHandler{
    private static final Logger logger = LoggerFactory.getLogger(NeedLoginHandler.class);

    @Override
    public boolean matches(RouteHandler handler) {
        logger.info("handler: {}", handler.getClass().getSimpleName());
        logger.info("annotations: {}", handler.getClass().getAnnotations().length);

        Annotation[] annotations = handler.getClass().getAnnotations();
        for (Annotation annotation : annotations) {
            logger.info("annotation: {}", annotation.annotationType().getSimpleName());
            if (annotation.annotationType().getSimpleName().equals("NeedLogin")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean validate(HttpServletRequest request, HttpServletResponse response, RouteHandler handler) {
        return false;
    }
}
