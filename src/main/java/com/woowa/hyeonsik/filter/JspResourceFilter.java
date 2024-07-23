package com.woowa.hyeonsik.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebFilter(filterName = "JspResourceFilter", urlPatterns = {"*.jsp"})
public class JspResourceFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(JspResourceFilter.class);
    private static final String SERVER_URI_PREFIX = "/";
    private static final String STATIC_RESOURCE_PREFIX = "/template/";
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
        logger.debug("JspResourceFilter를 수행합니다. Path: {} ", path);

        String newPath = path.replaceFirst(SERVER_URI_PREFIX, STATIC_RESOURCE_PREFIX);
        context.getRequestDispatcher(newPath).forward(request, response);
    }
}
