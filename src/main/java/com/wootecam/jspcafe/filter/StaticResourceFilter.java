package com.wootecam.jspcafe.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(filterName = "staticResourceFilter", urlPatterns = {"*.css", "*.js", "*.eot", "*.svg", "*.ttf", "*.woff",
        "*.woff2", "*.png"})
public class StaticResourceFilter implements Filter {

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse,
                         final FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        request.getRequestDispatcher("/static" + request.getRequestURI())
                .forward(servletRequest, servletResponse);
    }
}
