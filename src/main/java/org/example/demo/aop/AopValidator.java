package org.example.demo.aop;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo.RouteHandler;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

public class AopValidator {
    private List<AopHandler> handlers;

    public AopValidator() {
        this.handlers = List.of(new NeedLoginHandler());
    }

    public boolean validate(RouteHandler handler, HttpServletRequest request, HttpServletResponse response) throws IOException {
        for (AopHandler aopHandler : handlers) {
            if (aopHandler.matches(handler) && !aopHandler.validate(request, response, handler)) {
                return false;
            }
        }
        return true;
    }

    private boolean needsLogin(RouteHandler handler) {
        try {
            Method method = handler.getClass().getMethod("handle", HttpServletRequest.class, HttpServletResponse.class, List.class);
            return method.isAnnotationPresent(NeedLogin.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isUserLoggedIn(HttpServletRequest request) {
        return request.getSession().getAttribute("user") != null;
    }

    private void redirectToLogin(HttpServletResponse response) throws IOException {
        response.sendRedirect("/login");
    }
}
