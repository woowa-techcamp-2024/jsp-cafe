package com.codesquad.cafe.filter;

import com.codesquad.cafe.exception.AuthenticationException;
import com.codesquad.cafe.servlet.LoginServlet;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class AuthenticationFilter implements Filter {

    private Map<Pattern, List<String>> targets;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        targets = new HashMap<>();
        targets.put(Pattern.compile("/users/edit"), List.of("GET", "POST"));
        targets.put(Pattern.compile("/posts/create"), List.of("GET", "POST"));
        targets.put(Pattern.compile("/users"), List.of("POST"));
        targets.put(Pattern.compile("/logout"), List.of("GET", "POST"));
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        if (!(servletRequest instanceof HttpServletRequest)) {
            servletResponse.getWriter().write("400 Bad Request");
            return;
        }

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (!isTarget(request)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(LoginServlet.SESSION_USER_PRINCIPAL_KEY) == null) {
            throw new AuthenticationException();
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean isTarget(HttpServletRequest request) {
        String method = request.getMethod();
        String url = request.getRequestURI();
        for (Pattern pattern : targets.keySet()) {
            if (pattern.matcher(url).matches() && targets.get(pattern).contains(method)) {
                return true;
            }
        }
        return false;
    }
}
