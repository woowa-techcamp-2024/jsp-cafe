package org.example.cafe.common.auth;


import static org.example.cafe.utils.LoggerFactory.getLogger;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import org.example.cafe.common.endpoint.Endpoint;
import org.example.cafe.common.endpoint.PathEndPoint;
import org.slf4j.Logger;

/**
 * ENDPOINT_PATTERNS와 HTTP 메서드, URL이 모두 일치하는 경우, 세션에 사용자가 존재하지 않는다면 로그인 페이지로 리다이렉트한다.
 */
@WebFilter(urlPatterns = {"/*"})
public class AuthenticationFilter implements Filter {

    private static final Logger log = getLogger(AuthenticationFilter.class);
    private static final List<Endpoint> ENDPOINT_PATTERNS = List.of(
            new Endpoint("POST", "/replies"),
            new Endpoint("GET", "/questions"),
            new PathEndPoint("GET", "/questions/*", "edit=true"),
            new Endpoint("POST", "/questions"),
            new Endpoint("PUT", "/questions/*"),
            new Endpoint("DELETE", "/questions/*"),
            new Endpoint("POST", "/users/*"),
            new Endpoint("GET", "/logout"));

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

        log.debug("{} {}", httpRequest.getMethod(), httpRequest.getRequestURI());
        if (canMatch(httpRequest)) {
            HttpSession session = httpRequest.getSession(false);
            if (!isSessionValid(session)) {
                if ("GET".equals(httpRequest.getMethod()) || "POST".equals(httpRequest.getMethod())) {
                    httpResponse.sendRedirect("/login");
                    return;
                } else {
                    httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인이 필요합니다.");
                    return;
                }
            }
        }

        chain.doFilter(request, response);
    }

    private boolean canMatch(HttpServletRequest request) {
        return ENDPOINT_PATTERNS.stream()
                .anyMatch(pattern -> pattern.match(request));
    }

    private boolean isSessionValid(HttpSession session) {
        return session != null && session.getAttribute("userId") != null;
    }
}
