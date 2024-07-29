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
import org.slf4j.Logger;

/**
 * ENDPOINT_PATTERNS와 HTTP 메서드, URL이 모두 일치하는 경우, 세션에 사용자가 존재하지 않는다면 로그인 페이지로 리다이렉트한다.
 */
@WebFilter(urlPatterns = {"/*"})
public class AuthenticationFilter implements Filter {

    private static final Logger log = getLogger(AuthenticationFilter.class);
    private static final List<Endpoint> ENDPOINT_PATTERNS = List.of(
            new Endpoint("GET", "/questions"),
            new Endpoint("POST", "/questions"),
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
                httpResponse.sendRedirect("/login");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private boolean canMatch(HttpServletRequest request) {
        return ENDPOINT_PATTERNS.stream().anyMatch(pattern -> {
            if (pattern.path.endsWith("/") || pattern.path.endsWith("*")) {
                return request.getRequestURI().startsWith(pattern.path)
                        && request.getMethod().equals(pattern.method);
            }
            return request.getRequestURI().equals(pattern.path)
                    && request.getMethod().equals(pattern.method);
        });
    }

    private boolean isSessionValid(HttpSession session) {
        return session != null && session.getAttribute("userId") != null;
    }

    private record Endpoint(String method,
                            String path) {
    }
}
