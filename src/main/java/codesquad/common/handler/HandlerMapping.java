package codesquad.common.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.regex.Pattern;

public class HandlerMapping {
    private final Pattern pattern;
    private final RequestHandler handler;
    private final ReturnType returnType;

    public HandlerMapping(Pattern pattern, RequestHandler handler, ReturnType returnType) {
        this.pattern = pattern;
        this.handler = handler;
        this.returnType = returnType;
    }

    // headerName: accept, headerValue: */*
    // headerName: x-requested-with, headerValue: XMLHttpRequest
    // headerName: content-type, headerValue: application/x-www-form-urlencoded; charset=UTF-8
    public boolean matches(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String accept = request.getHeader("accept");
        String xRequestedWith = request.getHeader("x-requested-with");
        if ("application/json".equalsIgnoreCase(accept) || "XMLHttpRequest".equalsIgnoreCase(xRequestedWith)) {
            if (returnType == ReturnType.JSON) {
                return pattern.matcher(requestURI).matches();
            }
            return false;
        }
        return pattern.matcher(requestURI).matches();
    }

    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handler.handle(request, response);
    }

    public Pattern getPattern() {
        return pattern;
    }

    public RequestHandler getHandler() {
        return handler;
    }
}
