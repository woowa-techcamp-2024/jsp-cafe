package com.woowa.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

public class HttpMethodFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String method = httpRequest.getParameter("_method");
        if (method != null && (method.equalsIgnoreCase("put") || method.equalsIgnoreCase("delete"))) {
            httpRequest = new HttpMethodRequestWrapper(httpRequest, method);
        }
        chain.doFilter(httpRequest, response);
    }

    private static class HttpMethodRequestWrapper extends HttpServletRequestWrapper {

        private final String method;

        public HttpMethodRequestWrapper(HttpServletRequest request, String method) {
            super(request);
            this.method = method.toUpperCase();
        }

        @Override
        public String getMethod() {
            return this.method;
        }
    }
}
