package org.example.cafe.common.auth;


import static org.example.cafe.utils.LoggerFactory.getLogger;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.slf4j.Logger;

public class AuthenticationFilter implements Filter {

    private static final Logger log = getLogger(AuthenticationFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        log.debug("Init filter: {}", this.getClass().getSimpleName());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (httpRequest.getRequestURI().equals("/users") || (httpRequest.getMethod().equals("GET"))) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = httpRequest.getSession();
        if (session == null || session.getAttribute("userId") == null) {
            httpResponse.sendRedirect("/login");
            return;
        }

        chain.doFilter(request, response);
    }
}
