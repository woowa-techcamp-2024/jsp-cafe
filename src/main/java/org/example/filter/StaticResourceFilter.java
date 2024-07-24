package org.example.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.example.util.LoggerUtil;
import org.slf4j.Logger;

public class StaticResourceFilter implements Filter {

    private final Logger logger = LoggerUtil.getLogger();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        logger.info("Request URI: {}", req.getRequestURI());
        // /user/form.html 요청을 /static/user/form.html로 리디렉션
        if (req.getRequestURI().equals("/user/form.html")) {
            res.sendRedirect("/static/user/form.html");
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
    }
}