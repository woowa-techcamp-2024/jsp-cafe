package com.woowa.cafe.filter;

import com.woowa.cafe.exception.HttpException;
import jakarta.servlet.*;
import org.slf4j.Logger;

import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class ExceptionFilter implements Filter {

    private static final Logger log = getLogger(ExceptionFilter.class);

    @Override
    public void doFilter(final ServletRequest req, final ServletResponse resp, final FilterChain chain) throws ServletException, IOException {
        try {
            chain.doFilter(req, resp);
        } catch (HttpException e) {
            log.error("HttpException occurred: {}, {}", e.getStatus(), e.getMessage());
            req.setAttribute("status", e.getStatus());
            req.setAttribute("message", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(req, resp);
        } catch (Exception e) {
            log.error("Exception occurred", e);
            req.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(req, resp);
        }
    }
}
