package com.woowa.cafe.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class LoginFilter implements Filter {

    private Set<String> permitAllUrls;

    @Override
    public void init(final FilterConfig filterConfig) {
        permitAllUrls = new HashSet<>();
        // Public URLs
        permitAllUrls.add("/");
        permitAllUrls.add("/user");
        permitAllUrls.add("/user/login");
        permitAllUrls.add("/user/logout");
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

        boolean loggedIn = session != null && session.getAttribute("memberId") != null;
        boolean isPublicUrl = permitAllUrls.contains(path) || path.startsWith("/static/");

        if (loggedIn || isPublicUrl) {
            chain.doFilter(request, response);
        } else {
            httpResponse.sendRedirect("/user/login");
        }
    }

}
