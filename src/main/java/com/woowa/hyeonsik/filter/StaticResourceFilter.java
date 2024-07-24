package com.woowa.hyeonsik.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebFilter(filterName = "StaticResourceFilter", urlPatterns = {"*.js", "*.css", "*.html", "*.png", "*.eot", "*.svg", "*.ttf", "*.woff", "*.woff2"})
public class StaticResourceFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(StaticResourceFilter.class);
    private static final String STATIC_RESOURCE_PREFIX = "/static";
    private ServletContext context;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.debug("StaticResourceFilter 초기화");
        this.context = filterConfig.getServletContext();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String serverUriPrefix = request.getServletContext().getContextPath();
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String path = httpRequest.getRequestURI();
        logger.debug("StaticResourceFilter를 수행합니다. Path: {}, Content-Type: {}", path, request.getContentType());

        String newPath = path.replaceFirst(serverUriPrefix, STATIC_RESOURCE_PREFIX);
        logger.debug("프로젝트 기준 내부 Path: {}", newPath);
        context.getRequestDispatcher(newPath).forward(request, response);
    }
}
