package com.woowa.cafe.filter;

import com.woowa.cafe.exception.HttpException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static com.woowa.cafe.config.GsonConfig.gson;
import static org.slf4j.LoggerFactory.getLogger;

public class ExceptionFilter implements Filter {

    private static final Logger log = getLogger(ExceptionFilter.class);

    @Override
    public void doFilter(final ServletRequest req, final ServletResponse resp, final FilterChain chain) throws ServletException, IOException {
        try {
            chain.doFilter(req, resp);
        } catch (HttpException e) {
            log.error("HttpException occurred: {}, {}", e.getStatus(), e.getMessage());
            HttpServletResponse httpResp = (HttpServletResponse) resp;
            String encodedStatus = URLEncoder.encode(String.valueOf(e.getStatus()), StandardCharsets.UTF_8);
            String encodedMessage = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            HttpServletRequest request = (HttpServletRequest) req;
            if (request.getMethod().equals("GET")) {
                httpResp.sendRedirect("/error?status=" + encodedStatus + "&message=" + encodedMessage);
                return;
            }

            httpResp.setStatus(e.getStatus());
            httpResp.getWriter().write(gson.toJson(e));
        } catch (Exception e) {
            log.error("Exception occurred", e);
            HttpServletResponse httpResp = (HttpServletResponse) resp;
            String encodedMessage = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            httpResp.sendRedirect("/error?status=500&message=" + encodedMessage);
        }
    }
}
