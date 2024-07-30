package org.example.demo.aop;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo.RouteHandler;

public interface AopHandler {
    boolean matches(RouteHandler handler);
    boolean validate(HttpServletRequest request, HttpServletResponse response, RouteHandler handler);
}
