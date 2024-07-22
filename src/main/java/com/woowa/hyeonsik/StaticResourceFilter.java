package com.woowa.hyeonsik;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

@WebFilter(filterName = "StaticResourceFilter")
public class StaticResourceFilter implements Filter {
    private static final String SERVER_URI_PREFIX = "/cafe";
    private static final String STATIC_RESOURCE_PREFIX = "/static/";
    private ServletContext context;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.context = filterConfig.getServletContext();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String path = httpRequest.getRequestURI();

        if (path.startsWith(SERVER_URI_PREFIX) && (path.endsWith(".js") || path.endsWith(".css") || path.endsWith(".html"))) {
            String newPath = path.replaceFirst(SERVER_URI_PREFIX, STATIC_RESOURCE_PREFIX);  // FIXME 경로 정보 하드코딩
            context.getRequestDispatcher(newPath).forward(request, response);
        } else {
            chain.doFilter(request, response);
        }
    }
}
